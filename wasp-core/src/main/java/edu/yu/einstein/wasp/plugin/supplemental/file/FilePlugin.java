package edu.yu.einstein.wasp.plugin.supplemental.file;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfaces.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.service.FileService;

public class FilePlugin extends WaspPlugin implements InitializingBean, DisposableBean, ClientMessageI {

	private static Logger logger = LoggerFactory.getLogger(FilePlugin.class);

	@Autowired
	private FileService fileService;

	@Autowired
	private GridHostResolver hostResolver;

	@Autowired
	private MessageChannelRegistry messageChannelRegistry;

	public FilePlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3772956358774490373L;

	public Message<String> add(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help") || m.getPayload().toString().equals(""))
			return addHelp();
		try {
			Set<JSONObject> in = parseInput(m);
			checkObjects(in, "uri");
			if (in.size() == 0) {
				return noFileFound();
			}
			Set<File> files = doAdd(in);
			return returnResult(files);
		} catch (InvalidParameterException e) {
			return badParameter(e);
		} catch (JSONException e) {
			return badJSON(e);
		} catch (URISyntaxException e) {
			return badJSON(e);
		}
	}

	public Message<String> register(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help") || m.getPayload().toString().equals(""))
			return registerHelp();
		try {
			Set<JSONObject> in = parseInput(m);
			checkObjects(in, "id");
			Set<File> files = getFiles(in);
			files = doRegister(files);
			return returnResult(files);
		} catch (InvalidParameterException e) {
			return badParameter(e);
		} catch (JSONException e) {
			return badJSON(e);
		}
	}

	public Message<String> addAndRegister(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help") || m.getPayload().toString().equals(""))
			return addAndRegisterHelp();
		try {
			Set<JSONObject> in = parseInput(m);
			checkObjects(in, "uri");
			Set<File> files = doAdd(in);
			files = doRegister(files);
			return returnResult(files);
		} catch (InvalidParameterException e) {
			return badParameter(e);
		} catch (JSONException e) {
			return badJSON(e);
		} catch (URISyntaxException e) {
			return badJSON(e);
		}
	}

	private Set<JSONObject> parseInput(Message<String> m) throws JSONException {
		Set<JSONObject> in = new LinkedHashSet<JSONObject>();
		JSONObject jo = null;
		try {
			jo = new JSONObject(m.getPayload().toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (jo == null)
			throw new JSONException("JSON evaluated to null");
		if (jo.has("set") || ((jo.has("uri") && jo.has("fileType")) || (jo.has("id")))) {
			String keystring = "";
			Iterator<?> i = jo.keys();
			while (i.hasNext()) {
				keystring += i.next().toString() + " ";
			}
			logger.debug("request has keys: " + keystring);
		} else {
			throw new JSONException("Unexpected JSON content.");
		}

		if ((jo.has("uri") && jo.has("fileType")) || jo.has("id")) {
			// singleton
			in.add(jo);
			return in;
		}
		if (jo.has("set")) {
			JSONArray ja = null;
			try {
				ja = (JSONArray) jo.get("set");
				if (ja == null)
					throw new JSONException("");
			} catch (JSONException e) {
				if (!jo.has("uri") || !jo.has("fileType"))
					throw new JSONException("Unexpected JSON content.");
			}

			for (int x = 0; x < ja.length(); x++) {
				JSONObject o = (JSONObject) ja.get(x);
				if ((!o.has("uri") && !o.has("fileType")) || !o.has("id"))
					throw new JSONException("Unexpected JSON content");
				in.add(o);
			}
		}
		return in;

	}

	private Set<File> doAdd(Set<JSONObject> in) throws JSONException, URISyntaxException {
		LinkedHashSet<File> files = new LinkedHashSet<File>();
		for (JSONObject o : in) {
			File f = new File();
			String t = o.get("fileType").toString();
			FileType ft = fileService.getFileType(t);
			f.setFileType(ft);
			f.setFileURI(new URI(o.get("uri").toString()));
			f.setIsActive(0);
			f.setIsArchived(0);
			fileService.addFile(f);
			files.add(f);
		}
		return files;
	}

	private Set<File> getFiles(Set<JSONObject> in) throws JSONException {

		Set<File> files = new LinkedHashSet<File>();

		for (JSONObject o : in) {
			if (!o.has("id"))
				throw new InvalidParameterException("required \"id\" missing");
			File f = fileService.getFileByFileId(o.getInt("id"));
			if (f == null) {
				throw new InvalidParameterException("File id=" + o.getInt("id") + " is not known");
			}
			logger.debug("got file: " + f.getFileId());
			files.add(f);
		}
		return files;

	}

	private Set<File> doRegister(Set<File> files) {

		ExecutorService exec = Executors.newCachedThreadPool();

		for (File f : files) {
			if (f.getIsActive().equals(1)) {
				logger.warn("file " + f.getFileId() + " already registered, skipping");
				continue;
			}
			Runnable reg = new RegThread(f);
			exec.execute(reg);
		}

		exec.shutdown();

		while (!exec.isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		return files;

	}

	private class RegThread implements Runnable {
		File file;

		public RegThread(File f) {
			this.file = f;
		}

		@Override
		public void run() {
			try {
				fileService.registerFile(file);
			} catch (FileNotFoundException e) {
				logger.error("File not found: " + e.getLocalizedMessage());
				e.printStackTrace();
			} catch (GridException e) {
				logger.error("Problem registering file: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	private JSONObject returnFiles(Set<File> files) throws InvalidParameterException, JSONException {
		if (files.size() == 0) {
			throw new InvalidParameterException("returned 0 files");
		}
		JSONObject jo = new JSONObject();
		if (files.size() == 1) {
			File f = files.iterator().next();
			f = fileService.getFileByFileId(f.getFileId());
			jo.put("id", f.getFileId());
			jo.put("uri", f.getFileURI().toString());
			jo.put("md5", f.getMd5hash() == null ? "" : f.getMd5hash());
			jo.put("fileType", f.getFileType().getIName());
			jo.put("isActive", f.getIsActive());
			return jo;
		}
		Set<JSONObject> jos = new LinkedHashSet<JSONObject>();
		for (File f : files) {
			f = fileService.getFileByFileId(f.getFileId());
			JSONObject j = new JSONObject();
			jo.put("id", f.getFileId());
			jo.put("uri", f.getFileURI().toString());
			jo.put("md5", f.getMd5hash() == null ? "" : f.getMd5hash());
			jo.put("fileType", f.getFileType().getIName());
			jo.put("isActive", f.getIsActive());
			jos.add(jo);
		}
		JSONArray ja = new JSONArray(jos);
		jo.put("set", ja);
		return jo;
	}

	private Message<String> returnResult(Set<File> files) throws InvalidParameterException, JSONException {
		JSONObject jo = returnFiles(files);
		return MessageBuilder.withPayload(jo.toString()).build();
	}

	private void checkObjects(Set<JSONObject> in, String type) throws InvalidParameterException, JSONException {
		for (JSONObject o : in) {
			if (type.equals("uri") && !o.has("uri")) {
				throw new InvalidParameterException("parameter \"uri\" is required");
			}
			if (type.equals("id") && !o.has("id")) {
				throw new InvalidParameterException("parameter \"uri\" is required");
			}
			if ((type.equals("id") && o.has("uri")) || (type.equals("uri") && o.has("id"))) {
				throw new InvalidParameterException("sepcify either \"uri\" (when adding) or \"id\" (when registering)");
			}
			if (type.equals("uri") && !o.has("fileType")) {
				throw new InvalidParameterException("parameter \"fileType\" is required");
			}
			FileType ft;
			if (o.has("fileType")) {
				ft = fileService.getFileType(o.get("fileType").toString());
				if (ft == null) {
					throw new InvalidParameterException("unknown \"fileType\"");
				}
			}
			String u = null;
			if (o.has("uri")) {
				try {
					u = o.get("uri").toString();
					URI uri = new URI(u);
				} catch (URISyntaxException e) {
					throw new InvalidParameterException(u + " is not a valid URI");
				}
			}
			if (o.has("id")) {
				try {
					o.getInt("id");
				} catch (JSONException e) {
					throw new InvalidParameterException("bad id " + e.getLocalizedMessage());
				}
			}
		}
	}

	private Message<String> noFileFound() {
		return MessageBuilder.withPayload("No file found in message").build();
	}

	private Message<String> badParameter(Exception e) {
		return MessageBuilder.withPayload("Bad parameter: " + e.getLocalizedMessage()).build();
	}

	private Message<String> badJSON(Exception e) {
		return MessageBuilder.withPayload("Malformed JSON: " + e.getLocalizedMessage()).build();
	}

	public Message<String> listFileTypes(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return listFileTypesHelp();
		Set<FileType> ftypes = fileService.getFileTypes();
		String retval = "\n";
		for (FileType ft : ftypes) {
			retval += ft.getIName() + "\n";
		}

		return MessageBuilder.withPayload(retval).build();
	}

	public Message<String> associateSample(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return associateSampleHelp();

		return null;
	}

	public Message<String> addHelp() {
		String mstr = "\nFile Plugin: add file into wasp database (not required to be physically present)\n" +
				"wasp -T file -t register -m \'{uri:\"file://example.com/example/example.txt\", type:fileType}\'\n" +
				"wasp -T file -t register -m \'{set:[{uri:\"file://e.com/example/e1.txt\", type:fileType},uri:\"file://e.com/example/e2.txt\", type:fileType}]}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> registerHelp() {
		String mstr = "\nFile Plugin: register file; check for presence and MD5sum\n" +
				"wasp -T file -t register -m \'{uri:\"file://example.com/example/example.txt\", type:fileType}\'\n" +
				"wasp -T file -t register -m \'{set:[{uri:\"file://e.com/example/e1.txt\", type:fileType},uri:\"file://e.com/example/e2.txt\", type:fileType}]}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> addAndRegisterHelp() {
		String mstr = "\nFile Plugin: add and register file\n" +
				"wasp -T file -t register -m \'{uri:\"file://example.com/example/example.txt\", type:fileType}\'\n" +
				"wasp -T file -t register -m \'{set:[{uri:\"file://e.com/example/e1.txt\", type:fileType},uri:\"file://e.com/example/e2.txt\", type:fileType}]}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> listFileTypesHelp() {
		String mstr = "\nFile Plugin: list available file types\n" +
				"wasp -T file -t listFileTypes ";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> associateSampleHelp() {
		String mstr = "\nFile Plugin: associate a file with a sample\n" +
				"wasp -T file -t associateSample -m {id:\"1\", sampleId:\"1\"} ";
		return MessageBuilder.withPayload(mstr).build();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

}

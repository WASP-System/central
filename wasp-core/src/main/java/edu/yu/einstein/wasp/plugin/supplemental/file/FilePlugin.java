package edu.yu.einstein.wasp.plugin.supplemental.file;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityNotFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
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

	public FilePlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3772956358774490373L;

	public Message<String> add(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help") || m.getPayload().toString().equals(""))
			return addHelp();
		try {
			JSONObject in = parse(m);
			validateAdd(in);
			FileGroup files = doAdd(in);
			return returnResult(files);
		} catch (EntityNotFoundException e) {
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
			JSONObject in = parse(m);
			validateRegister(in);
			FileGroup files = getFiles(in);
			files = doRegister(files);
			return returnResult(files);
		} catch (EntityNotFoundException e) {
			return badParameter(e);
		} catch (JSONException e) {
			return badJSON(e);
		}
	}

	public Message<String> addAndRegister(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help") || m.getPayload().toString().equals(""))
			return addAndRegisterHelp();
		try {
			JSONObject in = parse(m);
			validateAdd(in);
			FileGroup files = doAdd(in);
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

	private JSONObject parse(Message<String> m) throws JSONException {
		JSONObject jo = new JSONObject(m.getPayload().toString());
		return jo;
	}
		
	private void validateAdd(JSONObject add) throws JSONException {
		String t = add.get("fileType").toString();
		FileType ft = fileService.getFileType(t);
		if (ft.getId() == null)
			throw new EntityNotFoundException("filetype " + t + " not found");
		JSONArray ja = add.getJSONArray("files");
		if (ja.length() == 0) 
			throw new JSONException("files required for add");
		for (int x = 0; x <= ja.length(); x++) {
			JSONObject f = ja.getJSONObject(x);
			if (!f.has("uri")) 
				throw new JSONException("URI is a required parameter");
			try {
				URI uri = new URI(f.getString("uri"));
			} catch (URISyntaxException e) {
				throw new JSONException(e.getLocalizedMessage());
			}
		}
		
	}
	
	private void validateRegister(JSONObject register) throws JSONException {
		if (!register.has("fileGroup"))
			throw new JSONException("fileGroup is a required parameter");
		Integer fg = register.getInt("fileGroup");
		FileGroup group = fileService.getFileGroupById(fg);
		if (group == null || group.getId() == null)
			throw new EntityNotFoundException("file group " + fg + "was not found");
	}

	private FileGroup doAdd(JSONObject in) throws EntityNotFoundException, JSONException, URISyntaxException {
		FileGroup group = new FileGroup();
		String t = in.get("fileType").toString();
		FileType ft = fileService.getFileType(t);
		if (ft.getId() == null)
			throw new EntityNotFoundException("filetype " + t + " not found");
		group.setFileType(ft);
		String description = in.get("description").toString();
		if (description != null) {
			group.setDescription(description);
		}
		JSONArray ja = in.getJSONArray("files");
		if (ja.length() == 0) {
			throw new JSONException("files are required");
		}
		group.setIsActive(0);
		group.setIsArchived(0);
		for (int x=0; x<= ja.length(); x++) {
			JSONObject o = ja.getJSONObject(x);
			FileHandle fh = new FileHandle();
			fh.setFileURI(new URI(o.get("uri").toString()));
			fileService.addFile(fh);
			group.addFileHandle(fh);
		}
		return group;
	}

	private FileGroup getFiles(JSONObject in) throws EntityNotFoundException, JSONException {
		FileGroup fg = fileService.getFileGroupById(in.getInt("fileGroup"));
		return fg;
	}

	private FileGroup doRegister(FileGroup group) {

		ExecutorService exec = Executors.newCachedThreadPool();

		for (FileHandle f : group.getFileHandles()) {
			if (f.getMd5hash() != null && !f.getMd5hash().equals("")) {
				logger.warn("file " + f.getId() + " appears to already be registered, skipping");
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

		return group;

	}

	private class RegThread implements Runnable {
		FileHandle file;
		
		private RegThread(FileHandle f) {
			this.file = f;
		}

		@Override
		public void run() {
			try {
				fileService.register(file);
			} catch (FileNotFoundException e) {
				logger.error("FileHandle not found: " + e.getLocalizedMessage());
				e.printStackTrace();
			} catch (GridException e) {
				logger.error("Problem registering file: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	private JSONObject returnFiles(FileGroup group) throws InvalidParameterException, JSONException {
		if (group == null || group.getFileHandles().size() == 0) {
			throw new InvalidParameterException("returned 0 files");
		}
		JSONObject jo = new JSONObject();
		if (group.getFileHandles().size() == 1) {
			FileHandle f = group.getFileHandles().iterator().next();
			f = fileService.getFileHandleById(f.getId());
			jo.put("id", f.getId());
			jo.put("uri", f.getFileURI().toString());
			jo.put("md5", f.getMd5hash() == null ? "" : f.getMd5hash());
			jo.put("fileType", group.getFileType().getIName());
			jo.put("isActive", group.getIsActive());
			jo.put("isArchived", group.getIsArchived());
			return jo;
		}
		Set<JSONObject> jos = new LinkedHashSet<JSONObject>();
		JSONObject data = new JSONObject();
		data.put("fileType", group.getFileType().getIName());
		data.put("isActive", group.getIsActive());
		data.put("isArchived", group.getIsArchived());
		for (FileHandle f : group.getFileHandles()) {
			f = fileService.getFileHandleById(f.getId());
			JSONObject j = new JSONObject();
			j.put("id", f.getId());
			j.put("uri", f.getFileURI().toString());
			j.put("md5", f.getMd5hash() == null ? "" : f.getMd5hash());
			jos.add(j);
		}
		JSONArray ja = new JSONArray(jos);
		jo.put("groupInfo", data);
		jo.put("set", ja);
		return jo;
	}

	private Message<String> returnResult(FileGroup group) throws InvalidParameterException, JSONException {
		JSONObject jo = returnFiles(group);
		return MessageBuilder.withPayload(jo.toString()).build();
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
		String mstr = "\nFile Plugin: add file group into wasp database (not required to be physically present)\n" +
				"wasp -T file -t add -m \'{description:\"File group text description\", type:fileType, files:[{uri:\"file://e.com/example/e1.txt\"},{uri:\"file://e.com/example/e2.txt\"]}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> registerHelp() {
		String mstr = "\nFile Plugin: register file group; check for presence and MD5 checksum\n" +
				"wasp -T file -t register -m \'{fileGroup:\"1\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> addAndRegisterHelp() {
		String mstr = "\nFile Plugin: add and register a file group\n" +
				"wasp -T file -t addAndRegister -m \'{description:\"File group text description\", type:fileType, files:[{uri:\"file://e.com/example/e1.txt\"},{uri:\"file://e.com/example/e2.txt\"]}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> listFileTypesHelp() {
		String mstr = "\nFile Plugin: list available file types\n" +
				"wasp -T file -t listFileTypes ";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> associateSampleHelp() {
		String mstr = "\nFile Plugin: associate a file with a sample\n" +
				"wasp -T file -t associateSample -m {fileGroupId:\"1\", sampleId:\"1\"} ";
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

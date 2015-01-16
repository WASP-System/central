package edu.yu.einstein.wasp.file.web.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.file.web.service.WebFileService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;

@Service
@Transactional("entityManager")
public class WebFileServiceImpl implements WebFileService, InitializingBean {

	@Autowired
	private FileService fileService;

	@Autowired
	private JobService jobService;

	@Autowired
	private Properties waspSiteProperties;

	private Map<String, String> hosts = new HashMap<String, String>();

	private Map<String, String> roots = new HashMap<String, String>();

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// Constants ----------------------------------------------------------------------------------
	private static final int DEFAULT_BUFFER_SIZE = 20480; // ..bytes = 20KB.
	private static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.
	private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

	// Inner classes ------------------------------------------------------------------------------
	/**
	 * This class represents a byte range.
	 */
	protected class Range {
		long start;
		long end;
		long length;
		long total;

		/**
		 * Construct a byte range.
		 * 
		 * @param start
		 *            Start of the byte range.
		 * @param end
		 *            End of the byte range.
		 * @param total
		 *            Total length of the byte source.
		 */
		public Range(long start, long end, long total) {
			this.start = start;
			this.end = end;
			this.length = end - start + 1;
			this.total = total;
		}
	}

	/**
	 * Returns a substring of the given string value from the given begin index to the given end index as a long. If the substring is empty, then -1 will be
	 * returned
	 * 
	 * @param value
	 *            The string value to return a substring as long for.
	 * @param beginIndex
	 *            The begin index of the substring to be returned as long.
	 * @param endIndex
	 *            The end index of the substring to be returned as long.
	 * @return A substring of the given string value as long or -1 if substring is empty.
	 */
	public static long sublong(String value, int beginIndex, int endIndex) {
		String substring = value.substring(beginIndex, endIndex);
		return (substring.length() > 0) ? Long.parseLong(substring) : -1;
	}

	/**
	 * Returns true if the given accept header accepts the given value.
	 * 
	 * @param acceptHeader
	 *            The accept header.
	 * @param toAccept
	 *            The value to be accepted.
	 * @return True if the given accept header accepts the given value.
	 */
	public static boolean accepts(String acceptHeader, String toAccept) {
		String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
		Arrays.sort(acceptValues);
		return Arrays.binarySearch(acceptValues, toAccept) > -1 || Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
			   || Arrays.binarySearch(acceptValues, "*/*") > -1;
	}

	/**
	 * Close the given resource.
	 * 
	 * @param resource
	 *            The resource to be closed.
	 */
	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException ignore) {
				// Ignore IOException. If you want to handle this anyway, it might be useful to know
				// that this will generally only be thrown when the client aborted the request.
			}
		}
	}

	/**
	 * Copy the given byte range of the given input to the given output.
	 * 
	 * @param input
	 *            The input to copy the given range to the given output for.
	 * @param output
	 *            The output to copy the given range from the given input for.
	 * @param start
	 *            Start of the byte range.
	 * @param length
	 *            Length of the byte range.
	 * @throws IOException
	 *             If something fails at I/O level.
	 */
	private static void copy(InputStream input, OutputStream output, long inputSize, long start, long length) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int read;

		if (inputSize == length) {
			// Write full range.
			while ((read = input.read(buffer)) > 0) {
				output.write(buffer, 0, read);
				output.flush();
			}
		} else {
			input.skip(start);
			long toRead = length;

			while ((read = input.read(buffer)) > 0) {
				if ((toRead -= read) > 0) {
					output.write(buffer, 0, read);
					output.flush();
				} else {
					output.write(buffer, 0, (int) toRead + read);
					output.flush();
					break;
				}
			}
		}
	}

	/**
	 * @param uuid
	 * @param adjext
	 *            The filename extension for adjacent file, used by UCSC genome browser, e.g. {uuid} -> XXXX.bam, {uuid}.bai -> XXXX.bam.bai
	 * @return
	 * @throws WaspException
	 */
	private java.io.File getLocalFileFromUUID(String uuid, String reqFileName) throws WaspException {
		UUID uu;
		try {
			uu = UUID.fromString(uuid);
		} catch (NumberFormatException e) {
			throw new WaspException("unable to search for record " + uuid);
		}

		FileHandle wf;
		try {
			wf = fileService.getFileHandle(uu);
			logger.debug("FileURI: " + wf.getFileURI().toString());
		} catch (FileNotFoundException e1) {
			logger.debug(uuid.toString() + " not in db");
			throw new WaspException("FileHandle not in database");
		}

		if (wf.getId() == null) {
			logger.debug("empty object");
			throw new WaspException("FileHandle is not known");
		}

		// TODO: non file URL and URN resolution
		if (!wf.getFileURI().toString().startsWith("file://")) {
			logger.warn("This implementation only handles file URIs. URI: " + wf.getFileURI());
			throw new WaspException("unable to resolve URI: " + wf.getId());
		}

		Matcher filem = Pattern.compile("^file://(.*?)/(.*)$").matcher(wf.getFileURI().normalize().toString());

		if (!filem.find()) {
			logger.warn("unable to parse URI: " + wf.getFileURI().toString());
			throw new WaspException("unable to parse URI: " + wf.getId());
		}

		String prefix = "";
		String host = filem.group(1);
		String location = filem.group(2);
		String folder;
		String filename;

		if (roots.get(hosts.get(host)) != null && roots.get(hosts.get(host)).equals("true")) {
			prefix = System.getProperty("user.home");
		}

		if (location.contains("/")) {
			Matcher locm = Pattern.compile("^(.*/)(.*)$").matcher(location);
			if (!locm.find()) {
				logger.warn("unable to parse file location: " + location);
				throw new WaspException("unable to parse location");
			}
			folder = locm.group(1);
			filename = locm.group(2);
		} else {
			folder = "";
			filename = location;
		}

		if (!reqFileName.isEmpty() && filename.compareTo(reqFileName) != 0) {
			if (reqFileName.startsWith(filename)) {
				logger.warn("Requesting adj file '" + reqFileName + "' for file '" + filename + "'");
				filename = reqFileName;
			} else {
				throw new WaspException("Req filename '" + reqFileName + "' doesn't match the file for uuid '" + uuid + "'");
			}
		}

		// if (!filename.endsWith(adjext)) {
		// String ext = filename.substring(filename.lastIndexOf("."));
		// if (!ext.equals(adjext))
		// filename += adjext;
		// }
		// filename += adjext.substring(OverlappedStringLength(filename, adjext));

		String localFilePath = prefix + "/" + folder + filename;
		logger.debug("Local file: " + localFilePath);

		java.io.File download = new java.io.File(localFilePath);
		if (!download.exists()) {
			if (download.getName().endsWith(".bam.bai")) {
				download = new java.io.File(download.getPath().replaceAll("\\.bam\\.bai", ".bai"));
			}
			if (!download.exists()) {
				throw new WaspException("File not exists: " + download.getPath());
			}
		}

		return download;
	}

	@Override
	public void processFileRequest(String uuid, String reqFileName, HttpServletRequest request, HttpServletResponse response) throws IOException, WaspException {

		java.io.File download;
		try {
			download = getLocalFileFromUUID(uuid, reqFileName);
		} catch (WaspException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			throw e;
		}

		String filename = download.getName();

		ConfigurableMimeFileTypeMap mimeMap = new ConfigurableMimeFileTypeMap();
		String contentType = mimeMap.getContentType(filename);
		logger.debug("File name: " + filename);
		logger.debug("Content type: " + contentType);

		// Added by AJ: to enable Cross-site HTTP requests for CORS support
		response.setHeader("Access-Control-Allow-Origin", "*");

		String range = request.getHeader("Range");
		if (range == null) {
			logger.debug("No range specified in http request header.");
			InputStream is = new FileInputStream(download);

			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			response.setHeader("Content-Length", String.valueOf(download.length()));

			// set 'max-age' to cache files for up to 1h (3600s) since most files shouldn't change on the server anyway.
			// We use 'must-revalidate' to force browser to always use this rule.
			response.setHeader("Cache-Control", "max-age=3600, must-revalidate");

			// write a cookie to indicate that a file download has been initiated properly
			// for the jquery file download plugin (http://johnculviner.com/jquery-file-download-plugin-for-ajax-like-feature-rich-file-downloads/)
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");

			// copy it to response's OutputStream
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();

		} else {
			logger.debug("Range specified in http request header: " + range);
			long length = download.length();
			// Prepare some variables. The full Range represents the complete file.
			Range full = new Range(0, length - 1, length);
			List<Range> ranges = new ArrayList<Range>();

			if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
				response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
				response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
				return;
			}

			String ifRange = request.getHeader("If-Range");
			if (ifRange != null && !ifRange.equals(uuid)) {
				try {
					long ifRangeTime = request.getDateHeader("If-Range"); // Throws IAE if invalid.
					if (ifRangeTime != -1) {
						ranges.add(full);
					}
				} catch (IllegalArgumentException ignore) {
					ranges.add(full);
				}
			}

			// If any valid If-Range header, then process each part of byte range.
			if (ranges.isEmpty()) {
				for (String part : range.substring(6).split(",")) {
					// Assuming a file with length of 100, the following examples returns bytes at:
					// 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
					long start = sublong(part, 0, part.indexOf("-"));
					long end = sublong(part, part.indexOf("-") + 1, part.length());

					if (start == -1) {
						start = length - end;
						end = length - 1;
					} else if (end == -1 || end > length - 1) {
						end = length - 1;
					}

					// Check if Range is syntactically valid. If not, then return 416.
					if (start > end) {
						response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
						response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
						return;
					}

					// Add range.
					ranges.add(new Range(start, end, length));
				}
			}

			// Prepare and initialize response --------------------------------------------------------

			// Get content type by file name and set content disposition.
			String disposition = "inline";

			// If content type is unknown, then set the default value.
			// For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
			// To add new content types, add new mime-mapping entry in web.xml.
			if (contentType == null) {
				contentType = "application/octet-stream";
			} else if (!contentType.startsWith("image")) {
				// Else, expect for images, determine content disposition. If content type is supported by
				// the browser, then set to inline, else attachment which will pop a 'save as' dialogue.
				String accept = request.getHeader("Accept");
				disposition = accept != null && accepts(accept, contentType) ? "inline" : "attachment";
			}

			// Initialize response.
			response.reset();
			response.setBufferSize(DEFAULT_BUFFER_SIZE);
			response.setHeader("Content-Disposition", disposition + ";filename=\"" + filename + "\"");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("ETag", filename);
			response.setDateHeader("Last-Modified", download.lastModified());
			response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);

			// Send requested file (part(s)) to client ------------------------------------------------

			// Prepare streams.
			// Open streams.
			InputStream input = new BufferedInputStream(new FileInputStream(download));
			ServletOutputStream sos = response.getOutputStream();

			if (ranges.isEmpty() || ranges.get(0) == full) {

				// Return full file.
				Range r = full;
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
				response.setHeader("Content-Length", String.valueOf(r.length));
				copy(input, new BufferedOutputStream(sos), length, r.start, r.length);

			} else if (ranges.size() == 1) {

				// Return single part of file.
				Range r = ranges.get(0);
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
				response.setHeader("Content-Length", String.valueOf(r.length));
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

				// Copy single part range.
				copy(input, new BufferedOutputStream(sos), length, r.start, r.length);

			} else {

				// Return multiple parts of file.
				response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

				// Copy multi part range.
				for (Range r : ranges) {
					// Add multipart boundary and header fields for every range.
					sos.println();
					sos.println("--" + MULTIPART_BOUNDARY);
					sos.println("Content-Type: " + contentType);
					sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);

					// Copy single part range of multi part range.
					copy(input, new BufferedOutputStream(sos), length, r.start, r.length);
				}

				// End with multipart boundary.
				sos.println();
				sos.println("--" + MULTIPART_BOUNDARY + "--");
			}

			// Gently close streams.
			close(input);
			sos.flush();
			close(sos);
		}
	}

	@Override
	public void processMultipleFileDownloadRequest(String uuids, String reqFileName, boolean fileOrGroup, HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, WaspException {
		// parse the comma delimited string into a list of filegroup uuids
		List<String> uuidList = Arrays.asList(uuids.split("\\s*,\\s*"));

		// Create list for local files
		List<java.io.File> localFilesList = new ArrayList<java.io.File>();

		String filename = "FileGroup.zip";

		UUID uu;
		for (String uuid : uuidList) {
			try {
				uu = UUID.fromString(uuid);
			} catch (NumberFormatException e) {
				throw new WaspException("unable to search for record " + uuid);
			}

			if (fileOrGroup) {
				localFilesList.add(getLocalFileFromUUID(uuid, ""));
			} else {
				FileGroup fg = fileService.getFileGroup(uu);
				Set<FileHandle> fhSet = fg.getFileHandles();
				for (FileHandle fh : fhSet) {
					localFilesList.add(getLocalFileFromUUID(fh.getUUID().toString(), ""));
				}
			}
		}

		if (uuidList.toArray().length == 1) {
			if (fileOrGroup) {
				filename = getLocalFileFromUUID(uuidList.get(0), "").getName() + ".zip";
			} else {
				filename = fileService.getFileGroup(UUID.fromString(uuidList.get(0))).getDescription().replaceAll("\\W", "") + ".zip";
			}

			if (!reqFileName.isEmpty() && !reqFileName.equals(filename)) {
				throw new WaspException("Req filename '" + reqFileName + "' doesn't match the file for uuid list '" + uuidList.toString() + "'");
			}
		}

		// ..code to add URLs to the list
		byte[] buf = new byte[2048];

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		// set 'max-age' to cache files for up to 1h (3600s) since most files shouldn't change on the server anyway.
		// We use 'must-revalidate' to force browser to always use this rule.
		response.setHeader("Cache-Control", "max-age=3600, must-revalidate");

		// Create the ZIP file
		ServletOutputStream sos = response.getOutputStream();
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(sos));

		// Compress the files
		for (java.io.File file : localFilesList) {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

			// Add ZIP entry to output stream.
			String entryname = file.getName();
			out.putNextEntry(new ZipEntry(entryname));

			int bytesRead;
			while ((bytesRead = bis.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}
			out.closeEntry();
			bis.close();
		}
		out.flush();
		close(out);
		sos.flush();
		close(sos);
	}

	@Override
	public void processLinksFileRequest(String uuids, HttpServletRequest request, HttpServletResponse response) throws IOException, WaspException {

		// parse the comma delimited string into a list of filegroup uuids
		List<String> uuidList = Arrays.asList(uuids.split("\\s*,\\s*"));
		String pathURL = request.getRequestURL().toString();
		pathURL = pathURL.substring(0, pathURL.lastIndexOf('/') + 1);

		String linksFileStr = "";
		for (String uuid : uuidList) {
			try {
				java.io.File download = getLocalFileFromUUID(uuid, "");
			} catch (WaspException e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				throw e;
			}

			linksFileStr += pathURL + uuid + "\n";
		}

		// Added by AJ: to enable Cross-site HTTP requests for CORS support
		response.setHeader("Access-Control-Allow-Origin", "*");

		InputStream is = new ByteArrayInputStream(linksFileStr.getBytes());

		response.setContentType("text/html");
		response.setHeader("Content-Disposition", "attachment;filename=multi-tracks.txt");
		response.setHeader("Content-Length", String.valueOf(linksFileStr.length()));

		// set 'max-age' to cache files for up to 1h (3600s) since most files shouldn't change on the server anyway.
		// We use 'must-revalidate' to force browser to always use this rule.
		response.setHeader("Cache-Control", "max-age=3600, must-revalidate");

		// write a cookie to indicate that a file download has been initiated properly
		// for the jquery file download plugin (http://johnculviner.com/jquery-file-download-plugin-for-ajax-like-feature-rich-file-downloads/)
		response.setHeader("Set-Cookie", "fileDownload=true; path=/");

		// copy it to response's OutputStream
		IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
		is.close();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (Object k : waspSiteProperties.keySet()) {
			String key = (String) k;
			Matcher hostm = Pattern.compile("^(.+?)\\.settings\\.host$").matcher(key);
			if (hostm.find()) {
				String host = hostm.group(1);
				hosts.put((String) waspSiteProperties.get(k), host);
				continue;
			}
			Matcher rootm = Pattern.compile("^(.+?)\\.settings\\.userDirIsRoot$").matcher(key);
			if (rootm.find()) {
				String root = rootm.group(1);
				roots.put(root, (String) waspSiteProperties.get(k));
			}
		}
	}

}

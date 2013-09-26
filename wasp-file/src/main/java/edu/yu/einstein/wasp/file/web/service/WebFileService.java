package edu.yu.einstein.wasp.file.web.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.FileGroup;

public interface WebFileService {

	void processFileRequest(String uuid, String adjExtension, HttpServletRequest request,
			HttpServletResponse response) throws IOException, WaspException;

	void processMultipleFileDownloadRequest(String uuids, boolean fileOrGroup,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, WaspException;

}

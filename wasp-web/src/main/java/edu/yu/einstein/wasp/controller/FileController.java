package edu.yu.einstein.wasp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.exception.FileDownloadException;
import edu.yu.einstein.wasp.model.File;

@Controller
@Transactional
@RequestMapping("/file")
public class FileController extends WaspController{
	
	@Value("${wasp.host.baseurl}")
	protected String baseurl;

	@Autowired
	protected FileDao fileDao;
	
	/**
	 * Download file
	 * @param fileId
	 * @param response
	 * @return
	 * @throws IOException
	 * 
	 * TODO: This needs to be refactored to use wasp-file.
	 * 
	 */
	@RequestMapping(value = "/downloadFile.do", method = RequestMethod.GET)	
	@Deprecated
	public String downloadSampleDraftFile(@RequestParam("id") Integer fileId,HttpServletResponse response) throws FileDownloadException {
		
		logger.error("THIS METHOD IS DEPRECATED AND DOES NOT DO WHAT YOU THINK IT DOES.");
		
		File file=fileDao.findById(fileId);
		
		if (file==null) {
				waspErrorMessage("file.not_found.error");
				String referrer = request.getHeader("Referer");
				referrer = referrer.replace(baseurl, "");
				referrer.replaceAll("\\/$", "");
				return "redirect:/"+referrer;
		}
//		ServletOutputStream out = null;
//		InputStream in = null;
//		try {
//			out = response.getOutputStream();
//			
//			java.io.File diskFile=new java.io.File(file.getAbsolutePath());
//			in = new FileInputStream(diskFile);
//			
//			String mimeType = file.getContentType();
//			byte[] bytes = new byte[FILEBUFFERSIZE];
//			int bytesRead;
//
//			response.setContentType(mimeType);
//			
//			response.setContentLength( (int)diskFile.length() );
//			
//			String fileName=diskFile.getName();
//				
//			response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\"" );
//
//			while ((bytesRead = in.read(bytes)) != -1) {
//				out.write(bytes, 0, bytesRead);
//			}
//			
//		} catch (Throwable e) {
//			throw new IllegalStateException("Cant download file id "+fileId+": "+e.getMessage());
//		} finally {
//			try {
//				if (in != null)
//					in.close();
//				if (out != null)
//					out.close();
//			} catch (IOException e) {
//				throw new FileDownloadException(e.getMessage());
//			}
//		}
		return null;
	}
	
}

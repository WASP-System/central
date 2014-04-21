package edu.yu.einstein.wasp.controller;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.dao.FileHandleDao;
import edu.yu.einstein.wasp.exception.FileDownloadException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;

@Controller
@Transactional
@RequestMapping("/file")
public class FileController extends WaspController{
	
	@Value("${wasp.host.fullServletPath}")
	protected String fullServletPath;

	@Autowired
	protected FileHandleDao fileDao;

	@Autowired
	protected FileService fileService;

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
		
		FileHandle file=fileDao.findById(fileId);
		
		if (file==null) {
				waspErrorMessage("file.not_found.error");
				String referrer = request.getHeader("Referer");
				referrer = referrer.replace(fullServletPath, "");
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
	
  	@RequestMapping(value = "/fileHandle/{fileHandleId}/download", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('su') or hasRole('ft')")
	public void downloadFileHandle(@PathVariable("fileHandleId") Integer fileHandleId, ModelMap m, HttpServletResponse response)  {        

  		String referer = request.getHeader("Referer");
  		
  		//these were very helpful
  		//http://www.mkyong.com/java/how-to-download-file-from-website-java-jsp/
  		//http://stackoverflow.com/questions/5673260/downloading-a-file-from-spring-controllers
  		FileHandle fileHandle = fileService.getFileHandleById(fileHandleId);
  		if(fileHandle==null || fileHandle.getId()==null){
  			String mess = "FileHandle with id = "+fileHandleId+" not found in database";
  			logger.debug(mess);
  			waspErrorMessage("file.not_found.error");
  			try{response.sendRedirect(referer);}catch(Exception e){}
			return;
  		}
  		//found this helpful hint on web: Once you start writing data to the output stream, you can no longer set any headers, so it's possible that's why your filename is getting lost
  		try{
  			response.setContentType("application/octet-stream"); 
  			response.setHeader("Content-Disposition","attachment; filename="+fileHandle.getFileName());
 			fileService.copyFileHandleToOutputStream(fileHandle, response.getOutputStream());
 			response.flushBuffer();
 		}catch(Exception e){
 			logger.debug("Error downloading fileHandleId = " + fileHandleId + ". "+ e.getLocalizedMessage());
 			waspErrorMessage("file.unable_to_download.error");
  			try{response.sendRedirect(referer);}catch(Exception e2){}
			return;
 		}
  	}
	
	@RequestMapping(value = "/fileGroup/{fileGroupId}/download", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('su') or hasRole('ft')")
	public void downloadFileGroup(@PathVariable("fileGroupId") Integer fileGroupId, ModelMap m, HttpServletResponse response)  {        

		String referer = request.getHeader("Referer");
 		
  		FileGroup fileGroup = fileService.getFileGroupById(fileGroupId);
  		if(fileGroup==null || fileGroup.getId()==null){
  			String mess = "FileGroup with Id = "+fileGroupId+" not found in database";
  			logger.debug(mess);
  			waspErrorMessage("file.not_found.error");
  			try{response.sendRedirect(referer);}catch(Exception e){}
  			return;
  		}
  		Set<FileHandle> fileHandleSet = fileGroup.getFileHandles();
  		if(fileHandleSet.size()==0){
  			waspErrorMessage("file.not_found.error");
  			try{response.sendRedirect(referer);}catch(Exception e){}
  			return;
  		}
  		else if(fileHandleSet.size()==1){
  			for(FileHandle fileHandle : fileHandleSet){
  				try{
  	  	  			response.setContentType("application/octet-stream"); 
  	  	  			response.setHeader("Content-Disposition","attachment; filename="+fileHandle.getFileName());
  	  	 			fileService.copyFileHandlesInFileGroupToOutputStream(fileGroup, response.getOutputStream());
  	  	 			response.flushBuffer();
  	  	 			return;
  	  	 		}catch(Exception e){
  	  	 			logger.debug("Error downloading the single file in fileGroupId = " + fileGroupId + ". "+ e.getLocalizedMessage());
  	  	 			waspErrorMessage("file.unable_to_download.error");
  	  	 			try{response.sendRedirect(referer);}catch(Exception e2){}
  	  	 		} 
  			}
  		}
  		else{
  			try{
  	  			response.setContentType("application/zip"); //http://en.wikipedia.org/wiki/Zip_(file_format)
  	  			response.setHeader("Content-Disposition","attachment; filename="+fileGroup.getDescription().replaceAll("\\s+", "_")+".zip");
  	 			fileService.copyFileHandlesInFileGroupToOutputStream(fileGroup, response.getOutputStream());
  	 			response.flushBuffer();
  	 		}catch(Exception e){
  	 			logger.debug("Error zipping up and downloading fileGroupId = " + fileGroupId + ". "+ e.getLocalizedMessage());
  	 			waspErrorMessage("file.unable_to_download.error");
  	  			try{response.sendRedirect(referer);}catch(Exception e2){}
  	 		} 
  		}
   	}
	
 	@RequestMapping(value = "/fileHandle/{fileHandleId}/view", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('su') or hasRole('ft')")
	public void viewFileHandle(@PathVariable("fileHandleId") Integer fileHandleId, ModelMap m, HttpServletResponse response)  {        

  		//////////String referer = request.getHeader("Referer");
  		
  		FileHandle fileHandle = fileService.getFileHandleById(fileHandleId);
  		if(fileHandle==null || fileHandle.getId()==null){
  			String mess = "FileHandle with id = "+fileHandleId+" not found in database";
  			logger.debug(mess);
  			////waspErrorMessage("file.not_found.error");
  			try{response.setContentType("text/html"); response.getOutputStream().print(mess);}catch(Exception e){}
			return;
  		}
  		try{
   			String fileName = fileHandle.getFileName();
  			String mimeType = fileService.getMimeType(fileName);//TODO this service method can be updated with Java7
 
  			if(mimeType==null || mimeType.isEmpty() || !mimeType.contains("/")){
  				String mess = "Unable to download file: mime type unknown";
  				logger.debug(mess);
  				response.setContentType("text/html");  				
  				response.getOutputStream().print(mess);
  				return;
  			}
  			response.setContentType(mimeType); 
  			fileService.copyFileHandleToOutputStream(fileHandle, response.getOutputStream());
 			//to view the file, do not flush, it's not needed and it screws things up   ///NO NO NO NO NO response.flushBuffer();
 		}catch(Exception e){
 			String mess = "Error downloading fileHandleId = " + fileHandleId + ". "+ e.getLocalizedMessage();
 			logger.debug(mess);
 			////waspErrorMessage("file.unable_to_download.error");
 			try{response.setContentType("text/html"); response.getOutputStream().print(mess);}catch(Exception e2){}
			return;
 		}
  	}
}

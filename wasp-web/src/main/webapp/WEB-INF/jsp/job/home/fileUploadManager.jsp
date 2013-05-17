<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<head>
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/reset.css" />
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
  <link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
</head>
<%-- <form action="<c:url value="/sampleDnaToLibrary/uploadJobFile/${job.getId()}.do" />" method="POST"  enctype="multipart/form-data" onsubmit="return validateFileUploadForm(this);">--%>
<form name="uploadFileForm" id="uploadFileForm" action="" method="POST"  enctype="multipart/form-data" onsubmit="return validateFileUploadForm(this);">
<table class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_name.label"/></td>
		<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_description.label"/></td>
		<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
	</tr>
	<c:forEach items="${fileGroups}" var="fileGroup">
	 	<c:set value="${fileGroupFileHandlesMap.get(fileGroup)}" var="fileHandles"/>
	 	<c:choose>
	 		<c:when test="${fn:length(fileHandles)==1}">
	 		  	<c:forEach items="${fileHandles}" var="fileHandle" >
	 		  		<tr>
	 		  			<td class="DataTD value-centered"><c:out value="${fileHandle.getFileName()}" /></td>
	 		  			<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /></td>
	 		  			<!--  <a href="<wasp:url fileAccessor="${fileHandle}" />" > -->
	 		  			<td class="DataTD value-centered"><a href="<c:url value="/file/fileHandle/${fileHandle.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a> 
	 		  				<c:if test="${fileHandlesThatCanBeViewedList.contains(fileHandle)}">
 		  					| <a href="javascript:void(0);" onclick='parent.showModalessDialog("<c:url value="/file/fileHandle/${fileHandle.getId()}/view.do" />");' >View</a>
 		  				</c:if>
	 		  			</td>
	 		  		</tr>
	 			</c:forEach>
	 		</c:when>			 		  
	 		<c:otherwise>
	 			<tr>
	 		  		<td class="DataTD value-centered"><c:out value="${fn:length(fileHandles)}" /> <fmt:message key="listJobSamples.file_download_grouped_files.label"/></td>
	 		  		<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /></td>			 		  			
	 		  		<td class="DataTD value-centered"><a href="<c:url value="/file/fileGroup/${fileGroup.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a></td>
	 		  	</tr>
	 		</c:otherwise>			 		
	 	</c:choose>
	</c:forEach>
	<tr>
		<%-- <td class="DataTD value-centered"><input type="file" name="file_upload" /></td><td class="DataTD value-centered" ><input type="text" maxlength="30" name="file_description" /></td><td align="center"><input type="submit" name="file_upload_submit_button" value="<fmt:message key="listJobSamples.file_upload.label"/>" /></td>--%>
		<td class="DataTD value-centered"><input type="file" name="file_upload" /></td><td class="DataTD value-centered" ><input type="text" maxlength="30" name="file_description" /></td><td align="center"><input name="bu" id="bu" class="button" type="button" value="Upload File" onclick='postMultipartForm("uploadFileForm","<c:url value="/job/${job.getId()}/uploadFileManager.do" />");' /></td>
	</tr>
</table>
</form>

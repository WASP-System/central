<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" />

<%--
see here for explanation of the 2 ways to upload file via ajax: http://stackoverflow.com/questions/1686099/file-upload-via-ajax-within-jquery 
method2,  (the easier of the two): targeting to a hidden iframe to enable the file post to work see: http://blog.manki.in/2011/08/ajax-fie-upload.html worked well but Andy didn't want it this way 
method1 using flash swf, can be implemented with this jquery plugin http://www.uploadify.com/
In the end we used the jquery form plug (http://malsup.com/jquery/form/). Its use is described well in http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/
(Note that we did NOT use the Html5 FormObject method, as some browsers do not yet support it).
--%>

<%-- example using the target = hidden iFrame worked nicely but Andy didn't like it. It only requires the target attribute to be set in the form tag, as is here, and to have a hidden iframe
<form action="<c:url value="/job/${job.getId()}/fileUploadManager.do" />" method="POST"  enctype="multipart/form-data" target="hiddenIFrame">
--%>

<%--need onsubmit='return false' to suppress hitting the event when the ENTER key is pressed with the cursor in the description input box --%>
<form id="fileUploadFormId" action="<c:url value="/job/${job.getId()}/fileUploadManager.do" />" method="POST"  enctype="multipart/form-data" onsubmit='return false;' >
	<table class="data" style="margin: 0px 0px">
		<tr class="FormData">
			<td colspan="3" class="label-centered" style="background-color:#FAF2D6">Upload A New File</td>
		</tr>
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6">Select File To Upload</td>
			<td class="label-centered" style="background-color:#FAF2D6">Provide A Brief Description</td>
			<td class="label-centered" style="background-color:#FAF2D6">Action</td>
		</tr>
		<tr>
			<td class="DataTD value-centered"><input type="file" name="file_upload" /></td>
			<td class="DataTD value-centered" ><input type="text" maxlength="30" name="file_description" /></td>
			<td align="center">
				<input type="reset" name="reset" value="Reset" />
				<%--I don't know why, but having uploadJqueryForm("fileUploadFormId") wired through onsubmit via a regular submit button makes for Major problems!!!! use the button below --%>
				<a class="button" href="javascript:void(0);"  onclick='uploadJqueryForm("fileUploadFormId")' ><fmt:message key="listJobSamples.file_upload.label" /></a>
			</td>
		</tr>
		<c:if test="${fn:length(errorMessage)>0}">
				<tr><td colspan="3" align="center" style="color:red;font-weight:bold"><c:out value="${errorMessage}" /></td></tr>
		</c:if>
		<c:forEach items="${fileGroups}" var="fileGroup" varStatus="fileGroupCounter">
			<c:if test="${fileGroupCounter.first}">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_name.label"/></td>
					<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_description.label"/></td>
					<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
				</tr>		
			</c:if>
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
	</table>
</form>
<br /><br />

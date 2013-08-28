<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" />
<%-- What was used was: from http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/ --%>
<%--Apparently need onsubmit='return false' to suppress hitting the event when the ENTER key is pressed with the cursor in the description input box --%>

<sec:authorize access="hasRole('su') or hasRole('ft') or hasRole('da')">
	<br /><a class="button" href="javascript:void(0);" onclick='loadNewPageWithAjax("<c:url value="/job/${job.getId()}/createQuoteOrInvoice.do" />");' >Create Quote / Invoice</a><br />
</sec:authorize>
<c:if test="${not empty mostRecentQuote }">
	<br /><h2>Most Recent Quote: <c:out value="${localCurrencyIcon}" /> <c:out value="${mostRecentQuote}" /></h2>
</c:if>
<br />
<a href="javascript:void(0);" onclick='$("#createNewQuote").css("display", "block");'>New Quote</a>
 | <a href="javascript:void(0);" onclick='$("#updateCurrentQuote").css("display", "block");'>Update Current Quote</a>
 

<br />
<form id="fileUploadFormId" action="<c:url value="/job/${job.getId()}/fileUploadManager.do" />" method="POST"  enctype="multipart/form-data" onsubmit='return false;' >
	<table class="data" style="margin: 0px 0px">
		<%-- 
		<tr class="FormData">
			<td colspan="3" class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.fileUploadUploadNewFile.label"/></td>
		</tr>
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.fileUploadSelectFileToUpload.label"/></td>
			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.fileUploadProvideBriefDescription.label"/></td>
			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
		</tr>
		<tr>
			<td class="DataTD value-centered"><input type="file" name="file_upload" /></td>
			<td class="DataTD value-centered" ><input type="text" maxlength="30" name="file_description" /></td>
			<td align="center">
				<input type="reset" name="reset" value="<fmt:message key="listJobSamples.file_reset.label" />" />
				--%>
				<%--I don't know why, but having uploadJqueryForm("fileUploadFormId") wired through onsubmit via a regular submit button makes for Major problems!!!! use the button below --%>
				<%--
				<a class="button" href="javascript:void(0);"  onclick='uploadJqueryForm("fileUploadFormId")' ><fmt:message key="listJobSamples.file_upload.label" /></a>
			</td>
		</tr>
		<c:if test="${fn:length(errorMessage)>0}">
				<tr><td colspan="3" align="center" style="color:red;font-weight:bold"><c:out value="${errorMessage}" /></td></tr>
		</c:if>
		--%>
		<c:choose>
			<c:when test="${empty fileGroups }">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6">Job Quotes / Invoices</td>
					<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_description.label"/></td>
					<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
				</tr>
				<tr class="FormData">
					<td colspan="3" class="DataTD value-centered">No Quotes Or Invoices Have Been Generated</td>
				</tr>			
			</c:when>
			<c:otherwise>
				<c:forEach items="${fileGroups}" var="fileGroup" varStatus="fileGroupCounter">
					<c:if test="${fileGroupCounter.first}">
						<tr class="FormData">
							<td class="label-centered" style="background-color:#FAF2D6">Job Quotes / Invoices</td>
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
			 		  					| <a href="javascript:void(0);" onclick='parent.showModalessDialog("<c:url value="/file/fileHandle/${fileHandle.getId()}/view.do" />");' ><fmt:message key="listJobSamples.file_view.label"/></a>
			 		  				<!-- no longer wanted	| <a href="javascript:void(0);" onclick='doGetWithAjax("<c:url value="/job/${job.getId()}/createQuote.do" />");' >CreateQuote</a> -->
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
			</c:otherwise>
		</c:choose>		
	</table>
</form>
<br />
<div id="createNewQuote" style="display:none">
	<span style='font-weight:bold'>1. Library Constructions Expected For This Job: <c:out value="${numberOfLibrariesExpectedToBeConstructed}" />
		<c:if test="${numberOfLibrariesExpectedToBeConstructed > 0}">
			&nbsp;&nbsp;[if no charge for a library, please set its cost to 0]
		</c:if>
	</span><br /><br />
	<table class="data" style="margin: 0px 0px">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6">Number</td>
			<td class="label-centered" style="background-color:#FAF2D6">Submitted Sample</td>
			<td class="label-centered" style="background-color:#FAF2D6">Material</td>
			<td class="label-centered" style="background-color:#FAF2D6">Library Cost</td>
		</tr>
		
		<c:forEach items="${submittedSamples}" var="submittedSample" >
			<input type='hidden' name="submittedSampleId" value="${submittedSample.getSampleId()}"/>
			<tr>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${submittedSample.getNumber()}" />
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${submittedSample.getSampleName()}" />
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${submittedSample.getMaterial()}"/>
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
				
					<c:choose>
						<c:when test='${submittedSample.getCost()=="N/A"}'>
							<c:out value="${submittedSample.getCost()}" />
							<input type='hidden' name="libraryCost_${submittedSample.getSampleId()}" value="${submittedSample.getCost()}"/>
						</c:when>
						<c:otherwise>
							<c:out value="${localCurrencyIcon}" /><input style="text-align:right;" name="libraryCost_${submittedObject.getId()}" type="text" maxlength="4" size="4" value="<c:out value="${submittedSample.getCost()}" />"/>.00
						</c:otherwise>
					</c:choose>
					
					
				</td>
			</tr>
		</c:forEach>
			
	</table>
	<br /><br />

</div>
<div id="updateCurrentQuote" style="display:none">
 b<br />b<br />b<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />
</div>
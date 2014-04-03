<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<div >
<table>
<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobId.label" />:</td><td class="DataTD">J<c:out value="${job.jobId}" /></td></tr>
<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobName.label" />:</td><td class="DataTD"><c:out value="${job.name}" /></td></tr>
<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmitter.label" />:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobPI.label" />:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmissionDate.label" />:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobWorkflow.label" />:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
<c:forEach items="${extraJobDetailsMap.keySet()}" var="detailKey">
	<tr><td class="CaptionTD"><fmt:message key="${detailKey}" />:</td><td class="DataTD"><c:out value="${extraJobDetailsMap.get(detailKey)}" /></td></tr>
</c:forEach>

<c:if test="${not empty jobStatus}"> 
	<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobStatus.label" />:</td><td class="DataTD"><c:out value="${jobStatus}" /></td></tr>
</c:if>
<c:forEach items="${jobApprovalsMap.keySet()}" var="jobApproveCode">
	<tr>
		<td class="CaptionTD"><fmt:message key="status.${jobApproveCode}.label" />:</td>
		<td class="DataTD"><fmt:message key="status.${jobApprovalsMap.get(jobApproveCode)}.label" />		
		  <c:if test="${not empty jobApprovalsCommentsMap.get(jobApproveCode)}"> 
		    <fmt:formatDate value="${jobApprovalsCommentsMap.get(jobApproveCode).getDate()}" pattern="yyyy-MM-dd" var="date" />
		  	<wasp:comment value="${jobApprovalsCommentsMap.get(jobApproveCode).getValue()} (${jobApprovalsCommentsMap.get(jobApproveCode).getUser().getNameFstLst()}; ${date})" />
		  </c:if>
		</td>
	</tr>
</c:forEach> 
</table>

<c:if test="${not empty fileGroups}">
	<br />
	<table class="data" style="margin: 0px 0px">
		<tr class="FormData">
			<td class="value-centered" style="background-color:#FAF2D6; font-weight: bold;"><fmt:message key="listJobSamples.file_name.label"/></td>
			<td class="value-centered" style="background-color:#FAF2D6; font-weight: bold;"><fmt:message key="listJobSamples.file_description.label"/></td>
			<td class="value-centered" style="background-color:#FAF2D6; font-weight: bold;"><fmt:message key="listJobSamples.file_action.label"/></td>
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
		 		  			<td class="DataTD value-centered">
		 		  				<a href="<wasp:relativeUrl value="/file/fileHandle/${fileHandle.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a> 
		 		  				<c:if test="${fileHandlesThatCanBeViewedList.contains(fileHandle)}">
		 		  				| <a href="javascript:void(0);" onclick='parent.showModalessDialog("<wasp:relativeUrl value="/file/fileHandle/${fileHandle.getId()}/view.do" />");' >View</a>
		 		  				</c:if>
		 		  			</td>
		 		  		</tr>
		 			</c:forEach>
		 		</c:when>			 		  
		 		<c:otherwise>
		 			<tr>
		 		  		<td class="DataTD value-centered"><c:out value="${fn:length(fileHandles)}" /> <fmt:message key="listJobSamples.file_download_grouped_files.label"/></td>
		 		  		<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /></td>			 		  			
		 		  		<td class="DataTD value-centered"><a href="<wasp:relativeUrl value="/file/fileGroup/${fileGroup.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a></td>
		 		  	</tr>
		 		</c:otherwise>			 		
		 	</c:choose>
		</c:forEach>
	</table>
</c:if>

</div>



<%-- see this for inheriting javascript from parent. basically precede with parent.    http://stackoverflow.com/questions/4612374/iframe-inherit-from-parent --%>
<%--do not remove with speaking to rob 
<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
These are here for demo purpose. Will eventually remove. Please do not remove now.
<a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">Right Frame: View Fastqc report from /results/production_wiki</a>
<br />
<a href="<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">Right Frame: Wasp job 87's home page</a>
<br />
<a href="<wasp:relativeUrl value="/datadisplay/showplay.do" />" target="myIframe">Right Frame: view a FILE stored on Chiam</a>
<br />	
<a href="javascript:void(0);" title="popup"  onclick="parent.showPopupWindow('http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html');">Aligned Popup WINDOW: fastqc</a>
<br />	
<a href="javascript:void(0);" title="popup"  onclick="parent.showPopupWindow('<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />');">Aligned Popup WINDOW: Wasp job 87's home page</a>
<br />	
<a href="javascript:void(0);" title="popup"  onclick="window.open('<wasp:relativeUrl value="/datadisplay/showplay.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">Popup WINDOW (left): view a FILE stored on Chiam</a>
<br />	
<a href="javascript:void(0);" title="popup"  onclick="parent.showPopupWindow('<wasp:relativeUrl value="/datadisplay/showplay.do" />');">Aligned Popup WINDOW: File on Chiam</a>
<br />
<a href="javascript:void(0);" onclick='parent.showModalDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modal Dialog: fastqc</a>
<br />
<a href="javascript:void(0);" onclick='parent.showModalDialog("<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modal Dialog: Wasp job 87's home page</a>
<br />
<a href="javascript:void(0);" onclick='parent.showModalDialog("<wasp:relativeUrl value="/datadisplay/showplay.do" />");' >Modal Dialog: view a FILE stored on Chiam</a>
<br />
<a href="javascript:void(0);" onclick='parent.showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modaless Dialog: fastqc</a>
<br />
<a href="javascript:void(0);" onclick='parent.showModalessDialog("<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modaless Dialog: Wasp job 87's home page</a>
<br />
<a href="javascript:void(0);" onclick='parent.showModalessDialog("<wasp:relativeUrl value="/datadisplay/showplay.do" />");' >Modaless Dialog: view a FILE stored on Chiam</a>
--%>

<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<h1><fmt:message key="pageTitle.task/aggregationAnalysis/list.label" /></h1>
<c:choose>
<c:when test="${jobs.size()==0}">
	<h2><fmt:message key="task.aggregateAnalysis_nojobs.label" /></h2>
</c:when>
<c:otherwise> 
	<br />
	<c:set var="currentJobId" value="-1" scope="page" />
	<table class="EditTable ui-widget ui-widget-content">
	<c:forEach items="${jobs}" var="job">
		<c:if test='${currentJobId != "-1"}'>
	 		<tr><td colspan="8" style='background-color:black'></td></tr>
	 	</c:if>
	 	<form  id="theForm${job.getJobId()}" method="POST" onsubmit="openWaitDialog();"  >
	 	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.getJobId()}"> 
	 	<tr class="FormData">
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_jobId.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_jobName.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_submitter.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_sample.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_library.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_pu_seqrun.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_analysisStatus.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_status.label" /></th>
		</tr>
		
		<c:set value="${jobCellLibraryMap.get(job)}" var="cellLibraryList" />
		<c:forEach items="${cellLibraryList}" var="cellLibrary" varStatus="status">
			<tr class="FormData">
				<c:choose>
					<c:when test="${currentJobId !=  job.getJobId()}">
						<td style='text-align:center;vertical-align:middle;'><a style="color: #801A00;" href="<wasp:relativeUrl value="job/${job.getJobId()}/homepage.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
						<td style='text-align:center;vertical-align:middle;'><c:out value="${job.getName()}" /></td>
						<td style='text-align:center;vertical-align:middle;'><c:out value="${job.getUser().getFirstName()}" /> <c:out value="${job.getUser().getLastName()}" /></td>
					</c:when>
					<c:otherwise>
						<td style='text-align:center'>&nbsp;</td>
						<td style='text-align:center'>&nbsp;</td>
						<td style='text-align:center'>&nbsp;</td>
					</c:otherwise>
				</c:choose>
				<c:set value="${cellLibraryMacromoleculeMap.get(cellLibrary)}" var="macromolecule" />
				<c:set value="${cellLibraryLibraryMap.get(cellLibrary)}" var="library" />
				<c:set value="${cellLibraryPUMap.get(cellLibrary)}" var="pu" />
				<c:set value="${cellLibraryRunMap.get(cellLibrary)}" var="run" />
				<td style='text-align:center;vertical-align:middle;'><c:out value="${macromolecule.getName()}" /></td>
				<td style='text-align:center;vertical-align:middle;'><c:out value="${library.getName()}" /></td>
				<td style='text-align:center;vertical-align:middle;'><c:out value="${pu.getName()}" /> (Cell: <c:out value="${cellLibrary.getIndex().toString()}" />) --&gt;<br /><c:out value="${run.getName()}" /></td>
				<td style='text-align:center;vertical-align:middle;'>
				<c:set value="${cellLibraryWithPreprocessingStatusMap.get(cellLibrary)}" var = "processingStatus" />
				<c:if test="${processingStatus != null && processingStatus == 'COMPLETED' }">
						<wasp:successIcon />
				</c:if>
				<c:if test="${processingStatus != null && processingStatus == 'FAILED' }">
					<wasp:failureIcon />
				</c:if>
				<c:if test="${processingStatus != null && processingStatus == 'STOPPED' }">
					<wasp:warningIcon key="task.aggregateAnalysis_processingStopped.label" />
				</c:if>
				</td>
				<td style='text-align:center;vertical-align:middle;'>
					<c:set value="${cellLibraryQcStatusMap.get(cellLibrary)}" var="cellLibraryQcStatus" />
					<c:set value="${cellLibraryQcStatusCommentMap.get(cellLibrary)}" var="cellLibraryQcStatusComment" />
					<c:if test="${cellLibraryQcStatus != null && cellLibraryQcStatus == true }">
						<wasp:successIcon  value="${cellLibraryQcStatusComment}"/>
					</c:if>
					<c:if test="${cellLibraryQcStatus != null && cellLibraryQcStatus == false }">
						<div>
							<wasp:failureIcon value="${cellLibraryQcStatusComment}"/>
						</div>
					</c:if>
					<c:if test="${cellLibraryQcStatus == null }">
						<fmt:message key="task.aggregateAnalysis_noQCData.label" />
					</c:if>
	 			</td>
			</tr>	
				
			<c:if test="${!status.last}">
				<tr><td >&nbsp;</td>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				<td colspan="5" style='border-top: 2px solid gray;'>&nbsp;</td></tr>
			</c:if>
			
			<c:if test="${status.last}">
				<tr style='text-align:center'>
					<td colspan="7" >&nbsp;</td>
					<td>
					<select class="FormElement ui-widget-content ui-corner-all" name="startAnalysis" id = "startAnalysis${cellLibrary.getSampleSourceId()}" size="1" >
						<option value=""><fmt:message key="task.aggregateAnalysis_startAnalysisQuestion.label" /></option>
						<option value="Now"><fmt:message key="task.aggregateAnalysis_startAnalysisNow.label" /></option>
						<option value="Later"><fmt:message key="task.aggregateAnalysis_startAnalysisLater.label" /></option>
						<option value="Never"><fmt:message key="task.aggregateAnalysis_startAnalysisNever.label" /></option>
					</select>				
					<br />
					<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.aggregateAnalysis_reset.label" />">
					<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.aggregateAnalysis_submit.label" />">
					</td>
				</tr>
			</c:if>
				
			<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />
				
			</c:forEach>
			</form>
		
	</c:forEach>
	</table>
</c:otherwise>
</c:choose>
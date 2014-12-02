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
	
	<div id="accordion">    
	<c:forEach items="${jobs}" var="job">
		<h4><a href="#">Job J<c:out value="${job.jobId}" /> : <c:out value="${job.getName()}" /> (<fmt:message key="jobapprovetask.submitter.label" />: <c:out value="${job.getUser().getNameFstLst()}" />; <fmt:message key="jobapprovetask.pi.label" />: <c:out value="${job.getLab().getUser().getNameFstLst()}" />)</a></h4>
		<div>
			<table class="EditTable ui-widget ui-widget-content">
			<form  id="theForm${job.getJobId()}" method="POST" onsubmit="return validate(this);"  >
		 	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.getJobId()}"> 
		 	<tr class="FormData">
				<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_sample.label" /></th>
				<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_library.label" /></th>
				<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_pu_seqrun.label" /></th>
				<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_analysisStatus.label" /></th>
				<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_status.label" /></th>
			</tr>
			
			<c:set value="${jobCellLibraryMap.get(job)}" var="cellLibraryList" />
			<c:forEach items="${cellLibraryList}" var="cellLibrary" varStatus="status">
				<tr class="FormData">
					<c:set value="${cellLibraryMacromoleculeMap.get(cellLibrary)}" var="macromolecule" />
					<c:set value="${cellLibraryLibraryMap.get(cellLibrary)}" var="library" />
					<c:set value="${cellLibraryPUMap.get(cellLibrary)}" var="pu" />
					<c:set value="${cellLibraryRunMap.get(cellLibrary)}" var="run" />
					<c:set value="${cellLibraryLaneMap.get(cellLibrary)}" var="laneNumber" />
					<td style='text-align:center;vertical-align:middle;'><c:out value="${macromolecule.getName()}" /></td>
					<td style='text-align:center;vertical-align:middle;'><c:out value="${library.getName()}" /></td>
					<td style='text-align:center;vertical-align:middle;'><c:out value="${pu.getName()}" /> (Cell: <c:out value="${laneNumber.toString()}" />) --&gt;<br /><c:out value="${run.getName()}" /></td>
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
						<c:if test="${processingStatus == null || processingStatus == 'UNKNOWN' }">
							<wasp:warningIcon key="task.aggregateAnalysis_processingUnknown.label" />
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
							<wasp:warningIcon key="task.aggregateAnalysis_noQCData.label" />
						</c:if>
		 			</td>
				</tr>	
					
				<c:if test="${!status.last}">
					<td colspan="5" style='border-top: 2px solid gray;'>&nbsp;</td></tr>
				</c:if>
				
				<c:if test="${status.last}">
					<tr style='text-align:center'>
						<td colspan="4" style='border-top: 2px solid gray;'>&nbsp;</td>
						<td  style='border-top: 2px solid gray;'>
						<select class="FormElement ui-widget-content ui-corner-all" name="startAnalysis" id = "startAnalysis${cellLibrary.getSampleSourceId()}" size="1" >
							<option value=""><fmt:message key="task.aggregateAnalysis_startAnalysisQuestion.label" /></option>
							<option value="Now"><fmt:message key="task.aggregateAnalysis_startAnalysisNow.label" /></option>
							<option value="Later"><fmt:message key="task.aggregateAnalysis_startAnalysisLater.label" /></option>
							<option value="Never"><fmt:message key="task.aggregateAnalysis_startAnalysisNever.label" /></option>
						</select>
						<p style="color:red"><fmt:message key="task.aggregateAnalysis_startAnalysisNote.label" /></p>				
						<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.aggregateAnalysis_reset.label" />">
						<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.aggregateAnalysis_submit.label" />">
						</td>
					</tr>
				</c:if>
				
					
				</c:forEach>
				</form>
			</table>
		</div>
	</c:forEach>
	</div>
</c:otherwise>
</c:choose>
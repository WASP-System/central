<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<h1><fmt:message key="pageTitle.task/cellLibraryQC/list.label" /></h1>
<%-- 
<c:forEach items="${jobs}" var="job">
	<br />
	Job: <c:out value="${job.getName()}" />
	<c:set value="${jobCellLibraryMap.get(job)}" var="cellLibraryList" />
	<c:forEach items="${cellLibraryList}" var="cellLibrary">
		<c:set value="${cellLibraryMacromoleculeMap.get(cellLibrary)}" var="macromolecule" />
		<c:set value="${cellLibraryLibraryMap.get(cellLibrary)}" var="library" />
		<c:set value="${cellLibraryPUMap.get(cellLibrary)}" var="pu" />
		<c:set value="${cellLibraryRunMap.get(cellLibrary)}" var="run" />
		<br />-<c:out value="${macromolecule.getName()}" />		
		<br />--<c:out value="${library.getName()}" />
		<br />---<c:out value="${pu.getName()}" /> (Lane <c:out value="${cellLibrary.getIndex().toString()}" />)
		<br />----<c:out value="${run.getName()}" />
	</c:forEach>
</c:forEach>
--%>
<br />
<c:set var="currentJobId" value="-1" scope="page" />
<table class="EditTable ui-widget ui-widget-content">
<c:forEach items="${jobs}" var="job">
	<c:if test='${currentJobId != "-1"}'>
 		<tr><td colspan="7" style='background-color:black'></td></tr>
 	</c:if>
 	<tr class="FormData">
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_jobId.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_jobName.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_submitter.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_sample.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_library.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_pu_seqrun.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_forAggregateAnalysis.label" /></th>
	</tr>
	
	<c:set value="${jobCellLibraryMap.get(job)}" var="cellLibraryList" />
	<c:forEach items="${cellLibraryList}" var="cellLibrary" varStatus="status">
		<tr class="FormData">
			<c:choose>
				<c:when test="${currentJobId !=  job.getJobId()}">
					<td style='text-align:center;'><a style="color: #801A00;" href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
					<td style='text-align:center'><c:out value="${job.getName()}" /></td>
					<td style='text-align:center'><c:out value="${job.getUser().getFirstName()}" /> <c:out value="${job.getUser().getLastName()}" /></td>
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
			<td style='text-align:center'><c:out value="${macromolecule.getName()}" /></td>
			<td style='text-align:center'><c:out value="${library.getName()}" /></td>
			<td style='text-align:center'><c:out value="${pu.getName()}" /> (Lane: <c:out value="${cellLibrary.getIndex().toString()}" />) --&gt;<br /><c:out value="${run.getName()}" /></td>
			<td style='text-align:center'>
			
				<form action="<c:url value="/task/cellLibraryQC/qc.do"/>" id="theForm<c:out value="${cellLibrary.getSampleSourceId()}" />" method="POST" onsubmit="return validate(this);"  >
 				<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleSourceId" value="${cellLibrary.getSampleSourceId()}"> 
 				<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "qcStatus" name = "qcStatus" value = "INCLUDE"><fmt:message key="task.cellLibraryqc_include.label" /> &nbsp;
 				<input class="FormElement ui-widget-content ui-corner-all" onclick='selectedExclude("theForm<c:out value="${cellLibrary.getSampleSourceId()}" />");' type="radio" id = "qcStatus" name = "qcStatus" value = "EXCLUDE"><fmt:message key="task.cellLibraryqc_exclude.label" /><br />
 				<textarea id="comment" name="comment" cols="25" rows="2"></textarea><br />
					<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.cellLibraryqc_reset.label" />">
				<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.cellLibraryqc_submit.label" />">
				</form>
			
			</td>
		</tr>		
		<c:if test="${!status.last}">
			<tr><td >&nbsp;</td>
			<td >&nbsp;</td>
			<td >&nbsp;</td>
			<td colspan="4" style='border-top: 2px solid gray;'>&nbsp;</td></tr>
		</c:if>
			
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />
			
		</c:forEach>
</c:forEach>
</table>
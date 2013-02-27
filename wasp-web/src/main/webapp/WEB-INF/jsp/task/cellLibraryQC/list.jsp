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
 	<form action="<c:url value="/task/cellLibraryQC/qc.do"/>" id="theForm${job.getJobId()}" method="POST" onsubmit="return validate(this);"  >
 	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.getJobId()}"> 
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
					<td style='text-align:center;vertical-align:middle;'><a style="color: #801A00;" href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
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
			<td style='text-align:center;vertical-align:middle;'><c:out value="${pu.getName()}" /> (Lane: <c:out value="${cellLibrary.getIndex().toString()}" />) --&gt;<br /><c:out value="${run.getName()}" /></td>
			<td style='text-align:center;vertical-align:middle;'>
			
				<c:if test="${status.first}"><br /></c:if>
 				<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleSourceId" value="${cellLibrary.getSampleSourceId()}"> 
 				<input class="FormElement ui-widget-content ui-corner-all" onclick='selected("${cellLibrary.getSampleSourceId()}");' type="radio" id = "qcStatus${cellLibrary.getSampleSourceId()}A" name = "qcStatus${cellLibrary.getSampleSourceId()}" value = "INCLUDE"><fmt:message key="task.cellLibraryqc_include.label" /> &nbsp;
 				<input class="FormElement ui-widget-content ui-corner-all" onclick='selected("${cellLibrary.getSampleSourceId()}");' type="radio" id = "qcStatus${cellLibrary.getSampleSourceId()}B" name = "qcStatus${cellLibrary.getSampleSourceId()}" value = "EXCLUDE"><fmt:message key="task.cellLibraryqc_exclude.label" /> 
				<br />
 				 <textarea id="comment${cellLibrary.getSampleSourceId()}" name="comment${cellLibrary.getSampleSourceId()}" cols="25" rows="2" onclick='changeTextColor(this, "black");'></textarea><br /><c:if test="${!status.last}"><br /></c:if>
			</td>
		</tr>	
			
		<c:if test="${!status.last}">
			<tr><td >&nbsp;</td>
			<td >&nbsp;</td>
			<td >&nbsp;</td>
			<td colspan="4" style='border-top: 2px solid gray;'>&nbsp;</td></tr>
		</c:if>
		
		<c:if test="${status.last}">
			<tr style='text-align:center'>
				<td colspan="6" >&nbsp;</td>
				<td>
				<span style="font-size:10px"><a href="javascript:void(0)" onclick='setAll("theForm${job.getJobId()}", "INCLUDE");'><fmt:message key="task.cellLibraryqc_setAllInclude.label" /></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick='setAll("theForm${job.getJobId()}", "EXCLUDE");'><fmt:message key="task.cellLibraryqc_setAllExclude.label" /></a><br /></span>
				<br /><input type="checkbox" name="startAnalysis"  value="true">Start Analysis<br />
				<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.cellLibraryqc_reset.label" />">
				<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.cellLibraryqc_submit.label" />">
				</td>
			</tr>
		</c:if>
			
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />
			
		</c:forEach>
		<%-- 
		<tr class="FormData"><td colspan='7' style='text-align:right'>
		<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.cellLibraryqc_reset.label" />">
		<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.cellLibraryqc_submit.label" />">
		</td></tr>--%>
		</form>
		
</c:forEach>
</table>
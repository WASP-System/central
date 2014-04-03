<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<h1><fmt:message key="pageTitle.task/cellLibraryQC/list.label" /></h1>
<c:choose>
<c:when test="${jobs.size()==0}">
	<h2><fmt:message key="task.cellLibraryqc_nojobs.label" /></h2>
</c:when>
<c:otherwise> 
	<br />
	<c:set var="currentJobId" value="-1" scope="page" />
	<table class="EditTable ui-widget ui-widget-content">
	<c:forEach items="${jobs}" var="job">
		<c:if test='${currentJobId != "-1"}'>
	 		<tr><td colspan="7" style='background-color:black'></td></tr>
	 	</c:if>
	 	<form  id="theForm${job.getJobId()}" method="POST" onsubmit="return validate(this);"  >
	 	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.getJobId()}"> 
	 	<tr class="FormData">
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_jobId.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_jobName.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_submitter.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_sample.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_library.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_pu_seqrun.label" /></th>
			<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_status.label" /></th>
		</tr>
		
		<c:set value="${jobCellLibraryMap.get(job)}" var="cellLibraryList" />
		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="maxNumCellLibrariesThatCanBeRecorded" value="${cellLibraryList.size()}"> 
		<c:forEach items="${cellLibraryList}" var="cellLibrary" varStatus="status">
			<tr class="FormData">
				<c:choose>
					<c:when test="${currentJobId !=  job.getJobId()}">
						<td style='text-align:center;vertical-align:middle;'><a style="color: #801A00;" href="<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
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
					<c:set value="" var="passChecked" />
					<c:set value="" var="failChecked" />
					<c:if test="${status.first}"><br /></c:if>
					<c:set value="${cellLibraryQcStatusMap.get(cellLibrary)}" var="cellLibraryQcStatus" />
					<c:if test="${cellLibraryQcStatus != null && cellLibraryQcStatus == true }">
						<c:set value="checked='CHECKED'" var="passChecked" />
					</c:if>
					<c:if test="${cellLibraryQcStatus != null && cellLibraryQcStatus == false }">
						<c:set value="checked='CHECKED'" var="failChecked" />
					</c:if>
	 				<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleSourceId" value="${cellLibrary.getSampleSourceId()}"> 
	 				<input class="FormElement ui-widget-content ui-corner-all" onclick='selected("${cellLibrary.getSampleSourceId()}");' type="radio" ${passChecked} id = "qcStatus${cellLibrary.getSampleSourceId()}A" name = "qcStatus${cellLibrary.getSampleSourceId()}" value = "PASSED"><fmt:message key="task.cellLibraryqc_pass.label" /> &nbsp;
	 				<input class="FormElement ui-widget-content ui-corner-all" onclick='selected("${cellLibrary.getSampleSourceId()}");' type="radio" ${failChecked} id = "qcStatus${cellLibrary.getSampleSourceId()}B" name = "qcStatus${cellLibrary.getSampleSourceId()}" value = "FAILED"><fmt:message key="task.cellLibraryqc_fail.label" /> 
					<br />
					<c:set value="${cellLibraryQcStatusCommentMap.get(cellLibrary)}" var="cellLibraryQcStatusComment" />
	 				 <textarea id="comment${cellLibrary.getSampleSourceId()}" name="comment${cellLibrary.getSampleSourceId()}" cols="25" rows="2" onclick='changeTextColor(this, "black");'>${cellLibraryQcStatusComment}</textarea><br /><c:if test="${!status.last}"><br /></c:if>
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
					<span style="font-size:10px"><a href="javascript:void(0)" onclick='setAll("theForm${job.getJobId()}", "PASSED");'><fmt:message key="task.cellLibraryqc_setAllPass.label" /></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick='setAll("theForm${job.getJobId()}", "FAILED");'><fmt:message key="task.cellLibraryqc_setAllFail.label" /></a><br /></span>
					<%-- <br /><input type="checkbox" name="startAnalysis"  value="true">Start Analysis<br />--%>
					<br />
					<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.cellLibraryqc_reset.label" />">
					<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.cellLibraryqc_submit.label" />">
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
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<h1><fmt:message key="pageTitle.task/cellLibraryQC/list.label" /></h1>
<c:choose>
<c:when test="${jobs.size()==0}">
	<h2><fmt:message key="task.cellLibraryqc_nojobs.label" /></h2>
</c:when>
<c:otherwise> 
	<div id="accordion">
	<c:forEach items="${jobs}" var="job">
		<h4><a href="#">Job J<c:out value="${job.jobId}" /> : <c:out value="${job.getName()}" /> (<fmt:message key="jobapprovetask.submitter.label" />: <c:out value="${job.getUser().getNameFstLst()}" />; <fmt:message key="jobapprovetask.pi.label" />: <c:out value="${job.getLab().getUser().getNameFstLst()}" />)</a></h4>
		<div>
			<table class="EditTable ui-widget ui-widget-content">
			<form id="theForm${job.getJobId()}" method="POST" onsubmit="return validate(this);"  >
		 	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.getJobId()}"> 
		 	<tr class="FormData">
				<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_sample.label" /></th>
				<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_library.label" /></th>
				<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_pu_seqrun.label" /></th>
				<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.cellLibraryqc_status.label" /></th>
			</tr>
			
			<c:set value="${jobCellLibraryMap.get(job)}" var="cellLibraryList" />
			<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="maxNumCellLibrariesThatCanBeRecorded" value="${cellLibraryList.size()}"> 
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
					<td colspan="4" style='border-top: 2px solid gray;'>&nbsp;</td></tr>
				</c:if>
				
				<c:if test="${status.last}">
					<tr style='text-align:center'>
						<td colspan="3" >&nbsp;</td>
						<td>
						<span style="font-size:10px"><a href="javascript:void(0)" onclick='setAll("theForm${job.getJobId()}", "PASSED");'><fmt:message key="task.cellLibraryqc_setAllPass.label" /></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick='setAll("theForm${job.getJobId()}", "FAILED");'><fmt:message key="task.cellLibraryqc_setAllFail.label" /></a><br /></span>
						<%-- <br /><input type="checkbox" name="startAnalysis"  value="true">Start Analysis<br />--%>
						<br />
						<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.cellLibraryqc_reset.label" />">
						<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.cellLibraryqc_submit.label" />">
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
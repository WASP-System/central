<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.job/analysisParameters.label"/></title>
<h1><fmt:message key="pageTitle.job/analysisParameters.label"/> - J<c:out value="${job.jobId}" /></h1>

<div>
	<table class="EditTable ui-widget ui-widget-content">
	    <c:forEach items="${softwareList}" var="software">
	   	  <tr class="FormData"><td colspan="2" class="label-centered" style="font-weight:bold;text-decoration:underline"><c:out value="${software.getResourceType().getName()}" />: <c:out value="${software.name}" /></td></tr>
	   	  <c:set var="_area" value = "${parentArea}" scope="request"/>
	   	  <c:set value="${softwareMap.get(software)}" var="_metaList" scope="request" />
		  <c:import url="/WEB-INF/jsp/meta_ro.jsp" />
	  	</c:forEach> 
	</table>
</div>
<br />
<div>
  <c:set var="submittedSamplesList" value="${submittedSamplesList}" />
  <c:set var="samplePairsMap" value="${samplePairsMap}" />
  <c:set var="columns" value="${fn:length(submittedSamplesList) + 1}" />
  <table>
  <tr><th colspan = "${columns}">Analysis Pairs</th></tr>
  <tr>
    <th>&nbsp;</th>
  <c:forEach var="sample" items="${submittedSamplesList}">
  <th><c:out value="${sample.name}" /></th>
  </c:forEach>
  </tr>
  
  <c:forEach var="sample" items="${submittedSamplesList}">
   <c:set var="truefalseList" value="${samplePairsMap.get(sample)}" />
   <c:if test="${truefalseList != null}">
    <tr>
     <th><c:out value="${sample.name}" /></th>
     	<c:forEach var="isPaired" items="${truefalseList}">
  			<c:choose>
  				<c:when test="${isPaired == 'f'}">
  					<td>&nbsp;</td>
  				</c:when>
  				<c:otherwise>
  					<td>X</td>
  				</c:otherwise>
  			</c:choose>
		</c:forEach>
  	</tr>
  	</c:if>
  </c:forEach>
 
  </table>
</div>


<%-- absolutely not ready for display!!!!!
<c:set var="workflowIName" value="${job.getWorkflow().getIName()}" />
<c:set var="sampleNumber" value="${fn:length(samples)}" />
<form method="POST" class="chipseqPairingform">
<table class="data">
	<tr class="row">
		<td class="label">&nbsp;</td>
		<td colspan="${sampleNumber + 1 }" align="center" class="label"><fmt:message key="${workflowIName}.test.label"/></td>
		<td class="noBorder" rowspan="2">&nbsp;</td>
	</tr>
	<tr class="row">
		<td rowspan="${sampleNumber + 1 }" valign="middle" class="label"><fmt:message key="${workflowIName}.control.label"/></td>
		<td class="label">&nbsp;</td>
		<c:forEach var="s" items="${samples}">
			<td class="label"><c:out value="${s.name}" /></td>
		</c:forEach>
	</tr>
	<c:forEach var="sControl" items="${samples}" varStatus="statusControl">
		<tr class="row">
			<td class="label"><c:out value="${sControl.name}" /></td>
			<c:forEach var="sTest" items="${samples}" varStatus="statusTest">
				<c:choose>
					<c:when test="${statusControl.count == statusTest.count }" >
						<td class="input-centered" >&nbsp;</td>
					</c:when>
					<c:otherwise>
						<td name="rowcolumn_${statusControl.count}_${statusTest.count}" class="input-centered" >
					      <c:set var="key" value="testVsControl_${sTest.sampleDraftId}_${sControl.sampleDraftId}" />
					      <c:set var="checked" value="" />
					      <c:if test="${fn:contains(selectedSamplePairs, key)}">
					        <c:set var="checked" value="CHECKED" />
					      </c:if>
					      <input type="checkbox" value="1" ${checked} name="${key}" id="rowcolumn_${statusControl.count}_${statusTest.count}">
					    </td>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<td class="noBorder"><input id="row_${statusControl.count}_select_all" type="button" value="select all" onclick="toggleRow(${statusControl.count}, ${sampleNumber})"></td>
		</tr>
	</c:forEach>
	<tr>
		<td class="noBorder">&nbsp;</td>
		<td class="noBorder">&nbsp;</td>
		<c:forEach begin="1" end="${sampleNumber}" step="1" varStatus="status">
			<td><input id="col_${status.count}_select_all" type="button" value="select all" onclick="toggleCol(${status.count}, ${sampleNumber})"></td>
		</c:forEach>
		<td class="noBorder">&nbsp;</td>
	</tr>
</table>

<div class="submit">
  <input type="submit" value="<fmt:message key="jobDraft.submit.label" />" />
</div>
</form>
--%>
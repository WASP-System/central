<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<c:set var="workflowIName" value="${jobDraft.getWorkflow().getIName()}" />
<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${workflowIName}.jobsubmit/helptag/pair.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>


<div class="instructions">
   <fmt:message key="${workflowIName}.pairing_instructions.label"/>
</div>

<c:set var="m_sampleNumber" value="${fn:length(m_samples)}" />
<c:set var="h_sampleNumber" value="${fn:length(h_samples)}" />

<form method="POST" class="helptagPairingform">
<table class="data">
	<tr class="row">
		<td class="label">&nbsp;</td>
		<td colspan="${h_sampleNumber + 1 }" align="center" class="label"><fmt:message key="${workflowIName}.test.label"/></td>
		<td class="noBorder" rowspan="2">&nbsp;</td>
	</tr>
	<tr class="row">
		<td rowspan="${m_sampleNumber + 1 }" valign="middle" class="label"><fmt:message key="${workflowIName}.control.label"/></td>
		<td class="label">&nbsp;</td>
		<c:forEach var="s" items="${h_samples}">
		<td class="label"><c:out value="${s.name}" /></td>
		</c:forEach>
	</tr>
	<c:forEach var="sControl" items="${m_samples}" varStatus="statusControl">
	<tr class="row">
		<td class="label"><c:out value="${sControl.name}" /></td>
		<c:forEach var="sTest" items="${h_samples}" varStatus="statusTest">
			<td name="rowcolumn_${statusControl.count}_${statusTest.count}" class="input-centered" >
		      <c:set var="key" value="${samplePairStrPrefix}_${sTest.sampleDraftId}_${sControl.sampleDraftId}" />
		      <c:set var="checked" value="" />																																																						
		      <c:if test="${fn:contains(selectedSamplePairs, key)}">
		        <c:set var="checked" value="CHECKED" />
		      </c:if>
		      <input type="checkbox" value="1" ${checked} name="${key}" id="rowcolumn_${statusControl.count}_${statusTest.count}">
		    </td>
		</c:forEach>
		<td class="noBorder"><input id="row_${statusControl.count}_select_all" type="button" value="select all" onclick="toggleRow(${statusControl.count}, ${h_sampleNumber})"></td>
	</tr>
	</c:forEach>
	<tr>
		<td class="noBorder">&nbsp;</td>
		<td class="noBorder">&nbsp;</td>
		<c:forEach begin="1" end="${h_sampleNumber}" step="1" varStatus="status">
		<td><input id="col_${status.count}_select_all" type="button" value="select all" onclick="toggleCol(${status.count}, ${m_sampleNumber})"></td>
		</c:forEach>
		<td class="noBorder">&nbsp;</td>
	</tr>
</table>

<div class="submit">
	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
	<input type="submit" value="<fmt:message key="jobDraft.continue.label" />" />
</div>
</form>

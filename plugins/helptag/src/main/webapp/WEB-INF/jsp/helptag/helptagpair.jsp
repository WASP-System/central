<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<c:set var="workflowIName" value="${jobDraft.getWorkflow().getIName()}" />
<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="helptag.jobsubmit/helptag/pair.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>


<div class="instructions">
   <fmt:message key="helptag.pairing_instructions.label"/>
</div>

<c:set var="m_sampleNumber" value="${fn:length(m_samples)}" />
<c:set var="h_sampleNumber" value="${fn:length(h_samples)}" />

<form method="POST" class="helptagPairingform">
<table class="data">
	<tr class="row">
		<td class="label">&nbsp;</td>
		<td colspan="${m_sampleNumber + 2 }" align="center" class="label"><fmt:message key="helptag.control.label"/></td>
		<td class="noBorder">&nbsp;</td>
	</tr>
	<tr class="row">
		<td rowspan="${h_sampleNumber + 1 }" valign="middle" class="label"><fmt:message key="helptag.test.label"/></td>
		<td class="label">&nbsp;</td>
		<c:forEach var="s" items="${m_samples}">
			<td class="label"><c:out value="${s.name}" /></td>
		</c:forEach>
		<td class="label"><fmt:message key="helptag.stdref.label"/></td>
		<td class="noBorder">&nbsp;</td>
	</tr>
	<c:forEach var="sTest" items="${h_samples}" varStatus="statusTest">
	<tr class="row">
		<td class="label"><c:out value="${sTest.name}" /></td>
		<c:set var="stdref_checked" value="CHECKED" />
		<c:forEach var="sControl" items="${m_samples}" varStatus="statusControl">
			<td name="rowcolumn_${statusTest.count}_${statusControl.count}" class="input-centered" >
		      <c:set var="key" value="${samplePairStrPrefix}_${sTest.sampleDraftId}_${sControl.sampleDraftId}" />
		      <c:set var="checked" value="" />																																																						
		      <c:if test="${fn:contains(selectedSamplePairs, key)}">
		        <c:set var="checked" value="CHECKED" />
		        <c:set var="stdref_checked" value="" />
		      </c:if>
		      <input type="checkbox" value="1" ${checked} name="${key}" id="rowcolumn_${statusTest.count}_${statusControl.count}"  onchange="nonstdChanged(${statusTest.count}, ${m_sampleNumber})">
		    </td>
		</c:forEach>
		<td class="input-centered" >
			<input type="checkbox" value="1" ${stdref_checked} id="rowcolumn_${statusTest.count}_stdref" onchange="stdChanged(${statusTest.count}, ${m_sampleNumber})">
	    </td>
		<td class="noBorder">
			<input type="button" id="row_${statusTest.count}_select_all" value="<fmt:message key="helptag.selectall.label"/>" onclick="toggleRow(${statusTest.count}, ${m_sampleNumber})">
		</td>
	</tr>
	</c:forEach>
	<tr>
		<td class="noBorder">&nbsp;</td>
		<td class="noBorder">&nbsp;</td>
		<c:forEach begin="1" end="${m_sampleNumber}" step="1" varStatus="statusControl">
			<td class="noBorder"><input id="col_${statusControl.count}_select_all" type="button" value="<fmt:message key="helptag.selectall.label"/>" onclick="toggleCol(${statusControl.count}, ${h_sampleNumber}, ${m_sampleNumber})"></td>
		</c:forEach>
		<td class="noBorder" colspan=2>&nbsp;</td>
	</tr>
</table>

<div class="submit">
	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
	<input type="submit" value="<fmt:message key="jobDraft.continue.label" />" />
</div>
</form>

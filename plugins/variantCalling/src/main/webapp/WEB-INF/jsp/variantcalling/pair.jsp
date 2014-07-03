<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<c:set var="workflowIName" value="${jobDraft.getWorkflow().getIName()}" />
<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${workflowIName}.jobsubmit/variantcalling/pairings.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
   <fmt:message key="${workflowIName}.pairingInstructions.label"/>
</div>

<div class="fixed-width_scrollable">
	<form method="POST">
	<table class="data">
		<tr class="row">
			<td class="label">&nbsp;</td>
			<td colspan="${fn:length(samples) + 1 }" align="center" class="label"><fmt:message key="${workflowIName}.control.label"/></td>
			<td class="noBorder" rowspan="2">&nbsp;</td>
		</tr>
		<tr class="row">
			<td rowspan="${fn:length(samples) + 1 }" valign="middle" class="label"><fmt:message key="${workflowIName}.test.label"/></td>
			<td class="label">&nbsp;</td>
			<c:forEach var="s" items="${samples}">
				<td class="label"><c:out value="${s.name}" /></td>
			</c:forEach>
		</tr>
		<c:forEach var="sTest" items="${samples}" varStatus="statusTest">
			<tr class="row">
				<td class="label"><c:out value="${sTest.name}" /></td>
				<c:forEach var="sControl" items="${samples}" varStatus="statusControl">
					<c:choose>
						<c:when test="${sControl.id == sTest.id }" >
							<td class="input-centered" >&nbsp;</td>
						</c:when>
						<c:otherwise>
							<c:set var="cbDisabled" value="" />
							<c:if test="${sampleOrganismMap.get(sControl) != sampleOrganismMap.get(sTest)}"><!-- confirm sample pairs are from same organism -->
								<c:set var="cbDisabled" value="disabled='disabled'" />
							</c:if>
							<td class="input-centered" >
				      			<c:set var="key" value="testVsControl_${sTest.sampleDraftId}_${sControl.sampleDraftId}" />
				      			<c:set var="checked" value="" />
				      			<c:if test="${fn:contains(selectedSamplePairs, key)}">
				        			<c:set var="checked" value="checked='CHECKED'" />
				      			</c:if>
				     			<input type="checkbox" value="1" ${checked} ${cbDisabled} name="${key}" id="rowcolumn_${statusTest.count}_${statusControl.count}">
				     		</td>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<td class="noBorder"><input id="row_${statusTest.count}_select_all" type="button" value="select all" onclick="toggleRow(${statusTest.count}, ${fn:length(samples)})"></td>
			</tr>
		</c:forEach>
		<tr>
			<td class="noBorder">&nbsp;</td>
			<td class="noBorder">&nbsp;</td>
			<c:forEach begin="1" end="${fn:length(samples)}" step="1" varStatus="status">
				<td><input id="col_${status.count}_select_all" type="button" value="select all" onclick="toggleCol(${status.count}, ${fn:length(samples)})"></td>
			</c:forEach>
			<td class="noBorder">&nbsp;</td>
		</tr>
	</table>
</div>

<div class="submit">
    <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
    <input type="submit" value="<fmt:message key="jobDraft.continue.label" />" />
</div>
</form>

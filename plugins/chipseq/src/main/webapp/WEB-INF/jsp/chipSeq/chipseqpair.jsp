<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<c:set var="workflowIName" value="${jobDraft.getWorkflow().getIName()}" />
<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${workflowIName}.jobsubmit/chipSeq/pair.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
   <fmt:message key="${workflowIName}.pairing_instructions.label"/>
</div>


<form method="POST">
<table class="data">
	<tr class="row">
		<td class="label">&nbsp;</td>
		<td colspan="${fn:length(ipSamples) + 1 }" align="center" class="label"><fmt:message key="${workflowIName}.test.label"/></td>
		<td class="noBorder" rowspan="2">&nbsp;</td>
	</tr>
	<tr class="row">
		<td rowspan="${fn:length(inputSamples) + 1 }" valign="middle" class="label"><fmt:message key="${workflowIName}.control.label"/></td>
		<td class="label">&nbsp;</td>
		<c:forEach var="s" items="${ipSamples}">
			<td class="label"><c:out value="${s.name}" /></td>
		</c:forEach>
	</tr>
	<c:forEach var="sControl" items="${inputSamples}" varStatus="statusControl">
		<tr class="row">
			<td class="label"><c:out value="${sControl.name}" /></td>
			<c:forEach var="sTest" items="${ipSamples}" varStatus="statusTest">
				<c:choose>
					<c:when test="${sControl.id == sTest.id }" ><!-- should no longer ever be true; this is for backward compatibility only; generally no longer used -->
						<td class="input-centered" >&nbsp;</td>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${sampleOrganismMap.get(sControl) == sampleOrganismMap.get(sTest)}"><!-- confirm sample pairs are from same organism -->
								<td name="rowcolumn_${statusControl.count}_${statusTest.count}" class="input-centered" >
					      			<c:set var="key" value="testVsControl_${sTest.sampleDraftId}_${sControl.sampleDraftId}" />
					      			<c:set var="checked" value="" />
					      			<c:if test="${fn:contains(selectedSamplePairs, key)}">
					        			<c:set var="checked" value="CHECKED" />
					      			</c:if>
					     			<input type="checkbox" value="1" ${checked} name="${key}" id="rowcolumn_${statusControl.count}_${statusTest.count}">
					     		</td>
							</c:when>
							<c:otherwise>
								<td class="input-centered" >&nbsp;</td>
							</c:otherwise>
						</c:choose>
						
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<td class="noBorder"><input id="row_${statusControl.count}_select_all" type="button" value="select all" onclick="toggleRow(${statusControl.count}, ${fn:length(ipSamples)})"></td>
		</tr>
	</c:forEach>
	<tr>
		<td class="noBorder">&nbsp;</td>
		<td class="noBorder">&nbsp;</td>
		<c:forEach begin="1" end="${fn:length(ipSamples)}" step="1" varStatus="status">
			<td><input id="col_${status.count}_select_all" type="button" value="select all" onclick="toggleCol(${status.count}, ${fn:length(inputSamples)})"></td>
		</c:forEach>
		<td class="noBorder">&nbsp;</td>
	</tr>
</table>

<div class="submit">
    <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
    <input type="submit" value="<fmt:message key="jobDraft.continue.label" />" />
</div>
</form>

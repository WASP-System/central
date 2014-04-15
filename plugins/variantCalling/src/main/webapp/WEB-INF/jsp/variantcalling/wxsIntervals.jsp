<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="variantcalling.selectIntervals.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
   <fmt:message key="variantcalling.selectIntervalsInstructions.label"/>
</div>


<form method="POST" >
<table class="data">
	 <tr>
	 	<th><fmt:message key="variantcalling.buildTitle.label" /></th><th><fmt:message key="variantcalling.intervalFileSelection.label" /></th><c:if test="${not empty formErrors}" ><th></th></c:if>
	 </tr>
	 <c:forEach var="buildString" items="${buildHeadings.keySet()}">
	 	<c:set var="intervalFiles" value="${intervalFilesByBuild.get(buildString)}" />
		<c:set var="intervalFiles" value="${intervalFilesByBuild.get(buildString)}" />
		<tr class="FormData">
			<td class="CaptionTD"><c:out value="${buildHeadings.get(buildString)}" /></td>
			<td class="DataTD">
	    		<select class="FormElement ui-widget-content ui-corner-all" name="<c:out value="${buildHeadings.get(buildString)}" />" id="<c:out value="${buildHeadings.get(buildString)}" />" class="FormElement ui-widget-content ui-corner-all">
					<c:if test= "${fn:length(intervalFiles) > 1}">
						<option value=''><fmt:message key="wasp.default_select.label"/></option>
					</c:if>
					<c:forEach var="option" items="${intervalFiles}">
						<c:set var="selected" value = "" />
						<c:if test="${not empty currentOptions && not empty currentOptions.get(buildString) && currentOptions.get(buildString) == option}">
							<c:set var="selected" value="selected='selected'" />
						</c:if>
						<option value='<c:out value="${option}" />' <c:out value="${selected}" />><c:out value="${option}" /></option>
					</c:forEach>																				
				</select>		
				<span class="requiredField">*</span>					 						
			</td>
			<c:if test="${not empty formErrors && not empty formErrors.get(buildString)}" ><td class="CaptionTD error"><fmt:message key="${formErrors.get(buildString)}" />&nbsp;</td></c:if>				 
		</tr>	
	</c:forEach> 
</table>

<div class="submit">
    <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
    <input type="submit" value="<fmt:message key="jobDraft.continue.label" />" />
</div>
</form>

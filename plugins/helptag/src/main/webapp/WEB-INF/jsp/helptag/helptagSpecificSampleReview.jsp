<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:set var="workflowIName" value="${jobDraft.getWorkflow().getIName()}" />
<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${workflowIName}.jobsubmit/helptag/helptagSpecificSampleReview.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
 
<div class="instructions">
  <c:choose>
	<c:when test="${fn:length(sampleDraftList)==0}">
		<fmt:message key="${workflowIName}.helptagSpecificSampleReview_instructions_noWorkHere.label"/>
	</c:when>
	<c:when test="${atLeastOneSampleConversionOccurred==true}">
		<fmt:message key="${workflowIName}.helptagSpecificSampleReview_insructions_sampleConversionOccurred.label"/>
	</c:when>
	<c:otherwise>
		<fmt:message key="${workflowIName}.helptagSpecificSampleReview_instructions1.label"/>   		
   	</c:otherwise>
  </c:choose>
</div>

<br />

<c:set var="colspan" value = '0' scope="request"/>	
<form action="<wasp:relativeUrl value="jobsubmit/helptag/helptagSpecificSampleReview/${jobDraft.getId()}.do" />" method="POST" >
	<c:if test="${fn:length(sampleDraftList)>0}">
		<div class="fixed-width_scrollable">
			<table class="EditTable ui-widget ui-widget-content" style="margin: 0px 0px" >			
				<c:forEach items="${sampleDraftList}" var="sampleDraft" varStatus="sampleDraftStatus">
					<c:if test="${sampleDraftStatus.first}">
						<tr >						
							<td class="CaptionTD top-heading" nowrap><fmt:message key="helptag.helptagSpecificSampleReview_sampleName.label" /></td>
							<td class="CaptionTD top-heading" nowrap><fmt:message key="helptag.helptagSpecificSampleReview_sampleType.label" /></td>
							<td class="CaptionTD top-heading" nowrap><fmt:message key="helptag.helptagSpecificSampleReview_typeOfHelpLibraryRequested.label" /></td>
						</tr>
					</c:if>				
					<tr>					
						<td class="DataTD value-centered <c:if test="${sampleDraftStatus.count % 2 == 0}"> td-even-number</c:if>">					
							<c:out value="${sampleDraft.getName()}" />
						</td>
						<td class="DataTD value-centered <c:if test="${sampleDraftStatus.count % 2 == 0}"> td-even-number</c:if>">					
							<c:out value="${sampleDraft.getSampleType().getName()}" />
						</td>
						<td class="DataTD value-centered <c:if test="${sampleDraftStatus.count % 2 == 0}"> td-even-number</c:if>">					
							<c:out value="${sampleDraftTypeOfHelpLibraryRequestedMap.get(sampleDraft)}" />
						</td>					
					</tr>
				</c:forEach>			
			</table>
		</div>
	</c:if>
	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
	<input type="submit" name="submit" id="submit" value="<fmt:message key="jobDraft.continue.label"/>" />
</form>





<%@ include file="/WEB-INF/jsp/taglib.jsp" %>



<h1><fmt:message key="jobDraft.create.label"/> -- <c:out value="${workflowSoftwares.get(0).getSoftware().getResourceType().getName()}" /></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
    <fmt:message key="jobDraft.software_instructions.label"/>
</div>
<br />
<table >
<tr><td>
	<form method="POST" class="resourcelistform">
		<select class="FormElement ui-widget-content ui-corner-all" name="changeResource" onchange="this.parentNode.submit()">
		  <option value="-1"><fmt:message key="wasp.default_select.label"/></option>
		<c:forEach items="${workflowSoftwares}" var="w">
		  <c:if test="${w.software.isActive==1}">
		   <option value="<c:out value="${w.software.softwareId}" />" <c:if test="${w.software.softwareId == jobDraftSoftware.software.softwareId}"> SELECTED</c:if>>
		     <c:out value="${w.software.name}" />
		   </option>
		  </c:if>
		</c:forEach>
		</select>
	</form>
	</td>
	<td>
		<c:out value="${ jobDraftSoftware.software.description }" />
	<td>
</tr>
</table>
<br />
<br />
<form:form  cssClass="FormGrid" commandName="jobDraft">
	<div class="softwareForm" id="softwareForm">
	  <input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="name" value="<c:out value="${jobDraft.name}"/>">
	  <input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="workflowId" value="<c:out value="${jobDraft.workflowId}"/>">
	  <input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="labId" value="<c:out value="${jobDraft.labId}"/>">
	  <c:set var="_area" value = "${parentarea}" scope="request"/>
	  <c:set var="_metaArea" value = "${area}" scope="request"/>
	  <c:set var="_metaList" value = "${jobDraft.jobDraftMeta}" scope="request" />
	  <table class="EditTable ui-widget ui-widget-content">
	     <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
	  </table>
	</div>
	<div class="submit" id="buttonsDiv" style="display: none;" onload="displayme()">
	  <input class="fm-button" type="button" value="<fmt:message key="jobDraft.terminateDiscard.label" />" onClick="if(confirm('<fmt:message key="jobDraft.terminateDiscardThisJobDraft.label" />')){window.location='<wasp:relativeUrl value="jobsubmit/terminateJobDraft/${jobDraft.jobDraftId}.do"/>'}" /> 
	  <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
	  <input class="fm-button" type="button" value="<fmt:message key="jobDraft.editParams.label" />" onClick="editParams();" id='showParamsButton'/> 
	  <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.continue.label" />" />
	</div>
	
	</form:form>




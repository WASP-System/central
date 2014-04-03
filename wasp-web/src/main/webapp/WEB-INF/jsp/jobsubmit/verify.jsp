<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="pageTitle.jobsubmit/verify.label"/></h1>

<c:set var="jobDraftDb" value="${jobDraft}" />
<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
<div class="instructions"><fmt:message key="jobDraft.verify_instructions.label"/></div>
<form id="submissionForm" method="POST" >
  <div id="buttons" class="submit">
    <input id="waitButton" class="fm-button" type="button" onClick="location.href='<c:url value="dashboard.do" />';" value="<fmt:message key='jobDraft.submit_later_button.label'/>" /> 
    &nbsp;&nbsp;<input id="submitButton" class="fm-button" type="submit" value="<fmt:message key="jobDraft.submit_button.label"/>" />
  </div>
</form>



<script>

$('#submissionForm').submit(function(){
	$("#wait_dialog-modal").dialog("open");
	return true; 
});

</script>



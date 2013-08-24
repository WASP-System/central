<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script>

function submitDocument(){
	$( "#wait_dialog-modal" ).dialog("open");
	document.submissionForm.submit(); 
}
</script>

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="pageTitle.jobsubmit/verify.label"/></h1>

<c:set var="jobDraftDb" value="${jobDraft}" />
<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
<div class="instructions"><fmt:message key="jobDraft.verify_instructions.label"/></div>
<form name="submissionForm" method="POST" >
  <div id="buttons" class="submit">
    <input name="waitButton" class="fm-button" type="button" onClick="location.href='/wasp/dashboard.do';" value="<fmt:message key='jobDraft.submit_later_button.label'/>" /> 
    &nbsp;&nbsp;<input name="submitButton" onclick="submitDocument();" class="fm-button" type="button" value="<fmt:message key="jobDraft.submit_button.label"/>" />
  </div>
</form>
<div class="bottomtxt"></div>





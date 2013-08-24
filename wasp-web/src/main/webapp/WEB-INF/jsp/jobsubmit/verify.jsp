<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="pageTitle.jobsubmit/verify.label"/></h1>

<c:set var="jobDraftDb" value="${jobDraft}" />
<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
<div class="instructions"><fmt:message key="jobDraft.verify_instructions.label"/></div>
<form method="POST" onsubmit="return submitDocument(this)">
  <div id="buttons" class="submit">
    <input name="waitButton" class="fm-button" type="button" onClick="location.href='/wasp/dashboard.do';" value="<fmt:message key='jobDraft.submit_later_button.label'/>" /> 
    &nbsp;&nbsp;<input name="submitButton" class="fm-button" type="submit" value="<fmt:message key="jobDraft.submit_button.label"/>" />
  </div>
</form>
<div class="bottomtxt"></div>

<script type="text/javascript">
function submitDocument(thisForm){
	$( "#wait_dialog-modal" ).dialog("close");
	thisForm.submit(); 
	return true; 
}
</script>




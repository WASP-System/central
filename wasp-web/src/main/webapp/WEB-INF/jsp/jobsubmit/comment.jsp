<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="pageTitle.jobsubmit/comment.label"/></h1>

<c:set var="jobDraftDb" value="${jobDraft}" />
<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
<div class="instructions"><fmt:message key="jobDraft.comment_instructions.label"/></div>

<form method="POST">
<div >
	<table class="EditTable ui-widget ui-widget-content">
	<tr class="FormData"><td  class="CaptionTD"><fmt:message key="jobDraft.comment_optional.label" /></td></tr>
	<tr class="FormData"><td ><textarea id="comment" name="comment" cols="70" rows="6"><c:out value="${comment}"  /></textarea></td></tr>	
	<tr class="FormData"><td >&nbsp;</td></tr>	
	</table>
</div>
<br />

  <div class="submit">
    <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" />     
    <input class="fm-button" type="submit" value="<fmt:message key="jobDraft.continue.label"/>" />
  </div>
</form>

<div class="bottomtxt"></div>

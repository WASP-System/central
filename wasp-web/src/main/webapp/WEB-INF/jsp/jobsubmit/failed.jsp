<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="pageTitle.jobsubmit/failed.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions"><h2 id="error"><fmt:message key="jobDraft.submission_failed.label"/></h2></div>


  <div class="submit">
    <input class="fm-button" type="button" onClick="location.href='<c:url value="jobsubmit/verify/${jobDraft.jobDraftId}.do" />';" value="<fmt:message key='jobDraft.submit_retry_button.label'/>" /> 
    &nbsp;&nbsp;<input class="fm-button" type="button" onClick="location.href='<c:url value="dashboard.do" />';" value="<fmt:message key='jobDraft.submit_later_button.label'/>" />    
  </div>

<div class="bottomtxt"></div>

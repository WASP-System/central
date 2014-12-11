<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<h2><fmt:message key="jobHomeHomepage.jobId.label" /> <a href="<wasp:relativeUrl value="job/${job.getId()}/homepage.do" />" >J<c:out value="${job.jobId}" /></a>: <c:out value="${job.getName()}" /><br />
<fmt:message key="jobHomeHomepage.workflow.label" />: <c:out value="${job.getWorkflow().getName()}" /></h2>

<div id="resultpanel">
<span id="wasp-msg" style="display:none;"></span>
</div>



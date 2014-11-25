<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<h2><fmt:message key="jobHomeHomepage.jobId.label" /> J<c:out value="${job.jobId}" />: <c:out value="${job.getName()}" /><br />
<fmt:message key="jobHomeHomepage.workflow.label" />: <c:out value="${job.getWorkflow().getName()}" /></h2>
<%-- these dialog areas are not displayed until called --%>
<div id="modalDialog">
	<%-- used to use for iframe: style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%" --%>
	<iframe id="modalIframeId" name="modalIframeId"  class="iframeOnJobHomepage" ><p><fmt:message key="jobHomeHomepage.iframesNotSupported.label" /></p></iframe>
</div>
<div id="modalessDialog">
	<iframe id="modalessIframeId" name="modalessIframeId"  class="iframeOnJobHomepage" ><p><fmt:message key="jobHomeHomepage.iframesNotSupported.label" /></p></iframe>
</div>
<div id="smallModalessDialog">
	<iframe id="smallModalessIframeId" name="smallModalessIframeId"  class="iframeOnJobHomepage"><p><fmt:message key="jobHomeHomepage.iframesNotSupported.label" /></p></iframe>
</div>

<div id="tabs">	
  <ul>
		<li><a id="basicAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/basic.do" />" ><fmt:message key="jobHomeHomepage.basicTab.label" /></a></li>
		<li><a id="costAnchor"   href="<wasp:relativeUrl value="job/${job.getId()}/costManager.do" />" ><fmt:message key="jobHomeHomepage.costsTab.label" /></a></li>
		<li><a id="viewerManagerAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/viewerManager.do" />"><fmt:message key="jobHomeHomepage.shareTab.label" /></a></li>
		<li><a id="comments"  href="<wasp:relativeUrl value="job/${job.getId()}/comments.do" />" ><fmt:message key="jobHomeHomepage.commentsTab.label" /></a></li>
		<li><a id="fileUploadAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/fileUploadManager.do" />" ><fmt:message key="jobHomeHomepage.uploadedFilesTab.label" /></a></li>
		<c:if test="${job.getWorkflow().getIName()!='bioanalyzer'}">
		  <li><a id="requestsAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/requests.do" />" ><fmt:message key="jobHomeHomepage.requestsTab.label" /></a></li>
	    </c:if>
	    <li><a id="sampleDetailsAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/sampleDetails.do" />" ><fmt:message key="jobHomeHomepage.sampleDetailsTab.label" /></a></li>
	   	<c:if test="${job.getWorkflow().getIName()!='bioanalyzer'}">
	      <li><a id="samplesAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/samples.do" />" ><fmt:message key="jobHomeHomepage.samplesLibrariesRunsTab.label" /></a></li>
		</c:if>
		<li><a id="dataByTreeViewAnchor"  href="javascript:void(0);" onclick='window.location.href="<wasp:relativeUrl value="jobresults/treeview/job/${job.getId()}.do" />"; return false;' ><fmt:message key="jobHomeHomepage.dataTreeViewTab.label" /></a></li>
	</ul>
</div>

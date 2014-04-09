<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<%-- <h1><a  href="<wasp:relativeUrl value="job/${job.jobId}/homepage.do" />">JobID J<c:out value="${job.jobId}" /></a>: <c:out value="${job.getName()}" /></h1>	--%>
<h1>JobID J<c:out value="${job.jobId}" />: <c:out value="${job.getName()}" /></h1>
<%-- these dialog areas are not displayed until called --%>
<div id="modalDialog">
	<iframe id="modalIframeId" name="modalIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>
<div id="modalessDialog">
	<iframe id="modalessIframeId" name="modalessIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>
<div id="smallModalessDialog">
	<iframe id="smallModalessIframeId" name="smallModalessIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>

<%--  TODO: Internationalize this!!!! --%>

<div id="tabs">	
  <ul>
		<li><a id="basicAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/basic.do" />" >Basic</a></li>
		<li><a id="costAnchor"   href="<wasp:relativeUrl value="job/${job.getId()}/costManager.do" />" >Costs</a></li>
		<li><a id="viewerManagerAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/viewerManager.do" />">Share</a></li>
		<li><a id="comments"  href="<wasp:relativeUrl value="job/${job.getId()}/comments.do" />" >Comments</a></li>
		<li><a id="fileUploadAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/fileUploadManager.do" />" >Uploaded Files</a></li>
		<li><a id="requestsAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/requests.do" />" >Requests</a></li>
	    <li><a id="samplesAnchor"  href="<wasp:relativeUrl value="job/${job.getId()}/samples.do" />" >Samples, Libraries &amp; Runs</a></li>
		<li><a id="dataByTreeViewAnchor"  href="javascript:void(0);" onclick='window.location.href="<wasp:relativeUrl value="jobresults/treeview/job/${job.getId()}.do" />"; return false;' >Data Treeview</a></li>
		
		<%-- 
		<li><a id="dataByRunsAnchor"  href="javascript:void(0);" onclick='loadIFrameAnotherWay(this, "<wasp:relativeUrl value="datadisplay/mps/jobs/${job.getId()}/runs.do" />");' >Data By Runs</a></li>
		<li><a id="mpsResultsBySample"  href="<wasp:relativeUrl value="job/${job.getId()}/mpsResultsListedBySample.do" />" >Data By Samples</a></li>
		<li><a id="aggregateAnalysisAnchor"  href="javascript:void(0);" onclick='alert("not yet implemented"); return false;' >Analysis</a></li>
		--%>
	</ul>
</div>

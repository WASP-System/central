<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<br /><br />
<h1><a  href="<c:url value="/job/${job.jobId}/homepage.do" />">JobID J<c:out value="${job.jobId}" /></a>: <c:out value="${job.getName()}" /></h1>	
<%--these 2 dialog areas are not displayed until called; don't know where is best to put them, but they have to be somewhere or it doesn't work --%>
<div id="modalDialog">
	<iframe id="modalIframeId" name="modalIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>
<div id="modalessDialog">
	<iframe id="modalessIframeId" name="modalessIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>

<script>
  $(function() {
    $( "#tabs" ).tabs();
    //$( "#tabs" ).tabs( "destroy");
  });
  $(function() {
	    $( "#tab" ).tabs();
	  });
  </script>
 <%--  
<div id="tab">	
  <ul>	

    <li>
    	<a href="#tab-1">Basic</a>
    </li>
	  <li>
		<a href="<c:url value="/job/${job.getId()}/approvals.do" />" >Approvals</a>
	</li>
 
    <li>			
    	<a href="<c:url value="/job/${job.getId()}/viewerManager.do" />" >Viewer Manager</a>	
	</li>
	<li>
		<a href="<c:url value="/job/${job.getId()}/workflow.do" />" >Workflow</a>
	</li>
	<li>
		<a href="javascript:void(0);" target="myIframe"  onclick='loadIFrame(this, "<c:url value="/job/${job.getId()}/fileUploadManager.do" />");' >file upload test</a>
	</li>
    <li>
		<a href="<c:url value="/job/${job.getId()}/samples.do" />" >Samples</a>
	</li>
    <li>
		<a href="<c:url value="/datadisplay/mps/jobs/${job.jobId}/runs.do" />"  >Data By Runs</a>
	</li>
	
  </ul>

  <div id="tab-1">
	  <table>
		<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmitter.label" />:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
		<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobPI.label" />:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
		<tr><td class="CaptionTD">Submitted:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
		<tr><td class="CaptionTD">Status:</td><td class="DataTD"><c:out value="${jobStatus}" /></td></tr>
	  </table>  
	</div>
</div>

<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
--%>
<div id="tabs">	
  <ul>	
 
    <li>			
    	<a id="viewerManagerAnchor"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/job/${job.getId()}/basic.do" />");' >Basic</a>	
	</li>
	<li>
		<a id="workflowAnchor"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/job/${job.getId()}/workflow.do" />");' >Workflow</a>
	</li>
    <li>
		<a id="approvalsAnchor"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/job/${job.getId()}/approvals.do" />");' >Approvals</a>
	</li>
	  
 
    <li>			
    	<a id="viewerManagerAnchor"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/job/${job.getId()}/viewerManager.do" />");' >Share</a>	
	</li>
	<li>
		<a id="commentsAnchor"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/job/${job.getId()}/comments.do" />");' >Comments</a>
	</li>
	<%--this next one must be iframe, for dealing with the fileupload (ajax file upload is precarious; not supported by all browsers --%>
	<li>
		<a id="fileUploadAnchor"  href="javascript:void(0);" onclick='loadIFrameAnotherWay(this, "<c:url value="/job/${job.getId()}/fileUploadManager.do" />");' >Uploaded Files</a>
	</li>

    <li>
		<a id="samplesAnchor"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/job/${job.getId()}/samples.do" />");' >Samples, Libraries, Runs</a>
	</li>
<%-- Next one does not work properly. So, this must be an iframe
    <li>
		<a id="dataByRunsAnchor123"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.jobId}/runs.do" />");' >Data By Runs</a>
	</li>
--%>
	<li>
		<a id="dataByRunsAnchor"  href="javascript:void(0);" onclick='loadIFrameAnotherWay(this, "<c:url value="/datadisplay/mps/jobs/${job.getId()}/runs.do" />");' >Data By Runs</a>
	</li>
	
    <li>
		<a id="dataBySamplesAnchor"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.jobId}/samples.do" />");' >Data By Samples</a>
	</li>
  <%--  neither one of the next two functions properly. So, this must be an iframe
   <li>
		<a id="dataByViewAnchor123"  href="javascript:void(0);"    onclick='loadNewPage(this, "<c:url value="/jobresults/treeview/job/${job.jobId}.do" />");'   >AJView</a>
	</li>
   <li>
		<a id="dataByViewAnchor234"  href="<c:url value="/jobresults/treeview/job/${job.jobId}.do" />" >AJView</a>
	</li>
	--%>
	<li>
		<a id="dataByViewAnchor"  href="javascript:void(0);" onclick='loadIFrameAnotherWay(this, "<c:url value="/jobresults/treeview/job/${job.getId()}.do" />");' >Data Viewport</a>
	</li>
	<li>
		<a id="aggregateAnalysisAnchor"  href="javascript:void(0);" onclick='alert("not yet implemented"); return false;' >Analysis</a>
	</li>
	
  </ul>


<div class="pageContainer">

<%-- 
	<div id="selectionLeft" class="selectionLeft">
	
	<table>
		<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmitter.label" />:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
		<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobPI.label" />:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
		<tr><td class="CaptionTD">Submitted:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
		<tr><td class="CaptionTD">Status:</td><td class="DataTD"><c:out value="${jobStatus}" /></td></tr>
	</table>
		<div>  
			<a style="color:red; font-weight:bold; background-color:Aqua;" id="workflowAnchor"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/job/${job.getId()}/workflow.do" />");' >Workflow</a>	<br />
			<a id="approvalsAnchor"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/job/${job.getId()}/approvals.do" />");' >Approvals</a>	<br />
			<a id="viewerManagerAnchor"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/job/${job.getId()}/viewerManager.do" />");' >Viewer Manager</a>	<br />
			<a id="billingAnchor"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.getId()}/jobdetails.do" />");' >Billing</a>	<br />
			<a id="fileUploadAnchor"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/job/${job.getId()}/fileUploadManager.do" />");' >Uploaded Files</a>	<br />
			<a id="sampleAnchor"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/job/${job.getId()}/samples.do" />");' >Samples</a>	<br />
			<a id="requestedCellAssignmentAnchor"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.getId()}/jobdetails.do" />");' >Requested Cell Assignments</a>	<br />
			<a id="requestedAnalysisAnchor"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.getId()}/jobdetails.do" />");' >Requested Analysis Paramaters</a>	<br />
			<a id="jobCommentsAnchor"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.getId()}/jobdetails.do" />");' >Comments Feed</a>	<br />
			Data Files:<br />
			 &nbsp;&nbsp;&nbsp;<a style="color: #801A00;" href="<c:url value="/jobresults/treeview/job/${job.jobId}.do" />"><fmt:message key="jobdetail_for_import.dataFilesView.label" /></a> | <a style="color: #801A00;" href="<c:url value="/datadisplay/mps/jobs/${job.jobId}/runs.do" />">By Runs</a> | <a style="color: #801A00;" href="<c:url value="/datadisplay/mps/jobs/${job.jobId}/samples.do" />">By Samples</a> 	

			<br /><a id="fileuptest"  href="javascript:void(0);" target="myIframe"  onclick='toggleAnchors(this);  loadIFrame(this, "<c:url value="/job/${job.getId()}/fileUploadManager.do" />");' >file upload test</a>	<br />
			<br /><a id="fileuptest"  href="javascript:void(0);" target="myIframe"  onclick='toggleAnchors(this);  loadIFrame(this, "<c:url value="/job/${job.getId()}/approvals.do" />"); ' >approvals test</a>	<br />

			
		</div>
	</div>
--%>
	<%--<div class="viewerRight">--%>
		<div id="viewerFrame" style="display:block;">
  			<%--  <iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="500px" width="500px" ><p>iframes not supported</p></iframe> --%>
  			<%-- <iframe id="myIframe" name="myIframe" src="<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />" style="overflow-x: scroll; overflow-y: scroll" height="800px" width="600px" ><p>iframes not supported</p></iframe>--%>
   		</div>
   		<div id="viewerFrame2" style="display:none;">
  			<%--  <iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="500px" width="500px" ><p>iframes not supported</p></iframe> --%>
  			<%-- <iframe id="myIframe" name="myIframe" src="<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />" style="overflow-x: scroll; overflow-y: scroll" height="800px" width="600px" ><p>iframes not supported</p></iframe>--%>
  			<iframe id="myIframe" name="myIframe" src="" style="overflow-x: scroll; overflow-y:scroll;"  height="800px" width="800px"><p>iframes not supported</p></iframe>
   		</div>
	<%--</div>	--%>
	<%-- <div style="clear:both;"></div>	--%>
</div>


</div>


	<%-- do not remove without speaking to rob
	
	 	<br />more stuff: FOR DEMO ONLY; DO NOT NOW REMOVE PLEASE<br/>	
		 <input id="toggleButton" class="fm-button" type="button" value="Hide Viewport"  onClick="toggleViewerFrame(this)" />
		<br />
		<a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">Right Frame: View Fastqc report from /results/production_wiki</a>
		<br />
		<a href="<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">Right Frame: Wasp job 87's home page</a>
		<br />
		<a href="<c:url value="/datadisplay/showplay.do" />" target="myIframe">Right Frame: view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html');">Aligned Popup WINDOW: fastqc</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />');">Aligned Popup WINDOW: Wasp job 87's home page</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="window.open('<c:url value="/datadisplay/showplay.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">Popup WINDOW (left): view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/datadisplay/showplay.do" />');">Aligned Popup WINDOW: File on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modal Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modal Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/datadispaly/showplay.do" />");' >Modal Dialog: view a FILE stored on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modaless Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modaless Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/datadispaly/showplay.do" />");' >Modaless Dialog: view a FILE stored on Chiam</a>
		<br /><br />
		<a style="color:green; font-weight:bold; background-color:white;" id="jobDetailsAnchorzzzzzzzz"  href="javascript:void(0);"  onclick='loadNewPage(this, "<c:url value="/datadispaly/showplay.do" />");' >use SHOWPLAY INTO div on right</a>
		<br /><br />
		<a style="color:green; font-weight:bold; background-color:white;" id="jobDetailsAnchorzzzzzzzz"  href="javascript:void(0);" target="myIframe" onclick='loadNewPage(this, "<c:url value="/datadisplay/showplay.do" />");' >use SHOWPLAY INTO div on right</a>
--%>
<%--NO INNER TABLES, just 
job
	run
		lane
			control
			library:
			Library:
		lane
			library:
	run
	run
	
<div class="pageContainer">
	<div id="selectionLeft" class="selectionLeft">	  
		<label>Job Name: <c:out value="${job.getName()}" /></label>	[<a style="color:red; font-weight:bold; background-color:white;" id="jobDetailsAnchor"  href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.getId()}/jobdetails.do" />");' >DETAILS</a><c:if test="${fn:length(platformUnitSet) > 1}"> | <a id="openAllRunsAnchor"  href="javascript:void(0);" onclick='openAllRuns();' >open all runs</a> | <a id="closeAllRunsAnchor" href="javascript:void(0);"  onclick='closeAllRuns();' >close all runs</a></c:if>]
		<c:if test="${fn:length(platformUnitSet) > 0}">
		<div>
			<label>Aggregate Analysis</label> [<a id="aggregateAnalysis" href="javascript:void(0);" onclick='alert("Not yet implemented");'>details</a>] 
		</div>
		</c:if>	
		<c:forEach items="${platformUnitSet}" var="platformUnit">
			<c:set value="${platformUnitRunMap.get(platformUnit)}" var="run"/>
			<div id="runDivToHighlight_${run.getId()}">
			<label>Sequence Run:</label> <c:out value="${run.getName()}" />  
			[<a id="runDetailsAnchor_${run.getId()}" href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/rundetails.do" />");' >details</a> 
			| <a id="runExpandAnchor_${run.getId()}" href="javascript:void(0);"  onclick='toggleExpandHide(this);' >expand</a>] 
					<div id="runDivToToggle_${run.getId()}" style="display:none;">					
					<c:set value="${platformUnitOrderedCellListMap.get(platformUnit)}" var="cellList"/>
					<c:forEach items="${cellList}" var="cell">
						<div>
							<c:set value="${cellIndexMap.get(cell)}" var="index"/>
							<c:choose>
								<c:when test="${not empty index }">							
									<label>Lane: <c:out value="${index}" /></label> [<a id="cellDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/cells/${cell.getId()}/celldetails.do" />");' >details</a> | <a id="fastQCDetailsAnchor_${run.getId()}" href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >fastqc</a> | <a id="statsDetailsAnchor_${run.getId()}"href="javascript:void(0);" onclick='showPopupWindow("http://wasp.einstein.yu.edu/results/production_wiki/JLocker/JTian/P520/J10728/stats/stats_TrueSeqUnknown.BC1G0RACXX.lane_5_P0_I0.fastq.html");' >graphical Stats</a> | <a id="cellSequencesDetailsAnchor_${cell.getId()}"  href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/sampleDnaToLibrary/cellSequencesDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >sequences</a> | <a id="cellAlignmentsDetailsAnchor_${cell.getId()}"  href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/sampleDnaToLibrary/cellAlignmentsDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >alignments</a>] 
								</c:when>
								<c:otherwise>
									<label>Lane: <c:out value="${cell.getName()}" /></label> [<a id="cellDetailsAnchor_${cell.getId()}"  href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/cells/${cell.getId()}/celldetails.do" />");' >details</a>] 
								</c:otherwise>
							</c:choose>													
							<c:set value="${cellControlLibraryListMap.get(cell)}" var="controlLibraryList"/>
							<c:if test="${not empty controlLibraryList }">
								<c:forEach items="${controlLibraryList}" var="controlLibrary">
								  <div>									
									<label>Control:</label> 
									<c:set value="${libraryAdaptorMap.get(controlLibrary)}" var="adaptor"/>
									<c:if test="${not empty adaptor }">
										
										[Index <c:out value="${adaptor.getBarcodenumber()}" />; <c:out value="${adaptor.getBarcodesequence()}" />]
									</c:if>
								  </div>									
								</c:forEach>
							</c:if>						
							<c:set value="${cellLibraryListMap.get(cell)}" var="libraryList"/>
							<c:forEach items="${libraryList}" var="library">
								<div>
									<label>Library:</label> <c:out value="${library.getName()}" />
									 
									<c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
									<c:if test="${not empty adaptor }">
										
										[Index <c:out value="${adaptor.getBarcodenumber()}" />; <c:out value="${adaptor.getBarcodesequence()}" />]
									</c:if>
									
									<c:set value="${libraryMacromoleculeMap.get(library)}" var="parentMacromolecule"/>
									<c:choose>
									<c:when test="${not empty parentMacromolecule }">
										(<label>Parent:</label> <c:out value="${parentMacromolecule.getName()}" />)
									</c:when>
									<c:otherwise>
										(<label>Parent:</label> N/A)
									</c:otherwise>
									</c:choose>
								</div>
							</c:forEach>
						</div>
					</c:forEach>
				</div>
		  	</div>
		</c:forEach>

		<br /><br />
		________________________________	
	 	<br /><br />more stuff: FOR DEMO ONLY; DO NOT NOW REMOVE PLEASE<br/>	
		 <input id="toggleButton" class="fm-button" type="button" value="Hide Viewport"  onClick="toggleViewerFrame(this)" />
		<br />
		<a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">Right Frame: View Fastqc report from /results/production_wiki</a>
		<br />
		<a href="<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">Right Frame: Wasp job 87's home page</a>
		<br />
		<a href="<c:url value="/datadisplay/showplay.do" />" target="myIframe">Right Frame: view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html');">Aligned Popup WINDOW: fastqc</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />');">Aligned Popup WINDOW: Wasp job 87's home page</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="window.open('<c:url value="/datadisplay/showplay.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">Popup WINDOW (left): view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/datadisplay/showplay.do" />');">Aligned Popup WINDOW: File on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modal Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modal Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/datadisplay/showplay.do" />");' >Modal Dialog: view a FILE stored on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modaless Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modaless Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/datadisplay/showplay.do" />");' >Modaless Dialog: view a FILE stored on Chiam</a>
		<br /><br />
		<a style="color:green; font-weight:bold; background-color:white;" id="jobDetailsAnchorzzzzzzzz"  href="javascript:void(0);"  onclick='loadNewPage(this, "<c:url value="/datadisplay/showplay.do" />");' >use SHOWPLAY INTO div on right</a>
 	</div>
	<div class="viewerRight">
		<div id="viewerFrame" style="display:block;">
   		</div>
	</div>	
	<div style="clear:both;"></div>	
</div>	
	
	
	
	 --%>

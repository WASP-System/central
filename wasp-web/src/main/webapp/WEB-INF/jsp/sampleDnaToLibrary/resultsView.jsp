<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<br />
<br />

<script type="text/javascript">

$(document).ready(function() {
	//http://api.jqueryui.com/dialog/
	$("#modalDialog").dialog({
        autoOpen: false,
        modal: true,
        height: 800,
        width: 800,
        position: { my: "right top", at: "right top", of: window } <%--could user "#container" too, which is set by wasp css --%>
    });
	$("#modalessDialog").dialog({
        autoOpen: false,
        modal: false,
        height: 800,
        width: 800,
        position: { my: "right top", at: "right top", of: window }//http://docs.jquery.com/UI/API/1.8/Position
    }); 
});
function toggleViewerFrame(toggleButton){
	var viewerFrame = document.getElementById('viewerFrame');
	if(toggleButton.value == "Show Window"){
		toggleButton.value = "Hide Window";
		viewerFrame.style.display = "block";
	}
	else if(toggleButton.value == "Hide Window"){
		toggleButton.value = "Show Window";
		viewerFrame.style.display = "none";		
	}	
}
function showModalDialog(url){
	//http://clarkupdike.blogspot.com/2009/03/basic-example-of-jquerys-uidialog.html
	//alert("robert's test alert");
	//var iframe = document.getElementById("modalIframeId");
	$("#modalIframeId").attr("src", url);
	$( "#modalDialog" ).dialog("open");
}
function showModalessDialog(url){
	$("#modalessIframeId").attr("src", url);
	$( "#modalessDialog" ).dialog("open");
}
function showPopupWindow(url) 
{//from http://stackoverflow.com/questions/10728207/position-a-window-on-screen 
	//also could see http://stackoverflow.com/questions/10728207/position-a-window-on-screen 
 var width  = 1200;
 var height = 800;
 var left   = screen.width - width;
 var top    = 0;
 var params = 'width='+width+', height='+height;
 params += ', top='+top+', left='+left;
 params += ', directories=no';
 params += ', location=no';
 params += ', menubar=no';
 params += ', resizable=no';
 params += ', scrollbars=yes';
 params += ', status=no';
 params += ', toolbar=no';
 newwin=window.open(url,'customWindow', params);
 if (window.focus) {newwin.focus();}
 return false;
}
function toggleNextDivVisibility(anchor, nextDivId){

	var nextDivToToggle = document.getElementById(nextDivId);
	var parentDiv = anchor.parentNode;
	if(nextDivToToggle.style.display == "none"){
		nextDivToToggle.style.display = "block";
		parentDiv.style.border= "2px solid red";
		anchor.innerHTML = "hide";
	}
	else{
		nextDivToToggle.style.display = "none";
		parentDiv.style.border= "";
		anchor.innerHTML = "expand";
	}
}
</script>






 
<style>
	.pageContainer {width:100%; overflow:hidden;}
	.selectionLeft {float:left; width:40%; }
	.viewerRight {float:left; width:55%; padding-left:0.3cm; border-left:2px solid black; overflow:hidden;}
	.selectionLeft div {margin:5px 0px 5px 10px;}
	.rob div {margin:5px 0px 5px 20px;}
</style>
<%--
<style>
	.pageContainer {width:850px; overflow:hidden;}
	.selectionLeft {float:left; width:400px; border-right:2px solid black;}
	.viewerRight {float:left; width:400px; padding-left:0.3cm; overflow:hidden;}
	.selectionLeft div {margin:5px 0px 5px 10px;}
	.rob div {margin:5px 0px 5px 10px;}
</style>
--%>
<%--these dialogs are not displayed until called; don't know where is best to put them, but they have to be somewhere or it doesn't work --%>
<div id="modalDialog">
	<iframe id="modalIframeId" name="modalIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>
<div id="modalessDialog">
	<iframe id="modalessIframeId" name="modalessIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>


<h1><a style="color: #801A00;" href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do" />">JobID J<c:out value="${job.jobId}" /></a></h1>		
		

<div class="pageContainer">
	<div class="selectionLeft">	  
		<label>Job Name: <c:out value="${job.getName()}" /></label>	[<a style="color: #801A00;" href="<c:url value="/sampleDnaToLibrary/jobDetails/${job.jobId}.do" />" target="myIframe" >details</a>]		
		<div>
			<label>Aggregate Analysis</label> [<a href="javascript:void(0);" onclick='alert("Not yet implemented");'>details</a>] 
		</div>	
		<c:forEach items="${platformUnitSet}" var="platformUnit">
			<c:set value="${platformUnitRunMap.get(platformUnit)}" var="run"/>
			<div>
			<label>Sequence Run:</label> <c:out value="${run.getName()}" /> <%-- (<label>FlowCell:</label> <c:out value="${platformUnit.getName()}" />)--%> [<a style="color: #801A00;" href="<c:url value="/sampleDnaToLibrary/runDetails/${run.getId()}.do" />" target="myIframe" >details </a> | <a href="javascript:void(0);" onclick='toggleNextDivVisibility(this, "run${run.getId()}");'> expand</a>] 
				<div id="run${run.getId()}" style="display:none;">					
					<c:set value="${platformUnitOrderedCellListMap.get(platformUnit)}" var="cellList"/>
					<c:forEach items="${cellList}" var="cell">
						<div>
							<c:set value="${cellIndexMap.get(cell)}" var="index"/>
							<c:choose>
								<c:when test="${not empty index }">							
									<label>Lane <c:out value="${index}" /></label>
								</c:when>
								<c:otherwise>
									<label>Lane <c:out value="${cell.getName()}" /></label>
								</c:otherwise>
							</c:choose>													
							<c:set value="${cellControlLibraryListMap.get(cell)}" var="controlLibraryList"/>
							<c:if test="${not empty controlLibraryList }">
								<c:forEach items="${controlLibraryList}" var="controlLibrary">
								  <div>									
									<label>Control:</label> <c:out value="${controlLibrary.getName()}" />
									<c:set value="${libraryAdaptorMap.get(controlLibrary)}" var="adaptor"/>
									<c:if test="${not empty adaptor }">
										<%-- [<c:out value="${adaptor.getName()}" />]--%>
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
										<%--[<c:out value="${adaptor.getName()}" />]--%>
										[Index <c:out value="${adaptor.getBarcodenumber()}" />; <c:out value="${adaptor.getBarcodesequence()}" />]
									</c:if>
									
									<c:set value="${libraryMacromoleculeMap.get(library)}" var="parentMacromolecule"/>
									<c:if test="${not empty parentMacromolecule }">
										(<label>Parent:</label> <c:out value="${parentMacromolecule.getName()}" />)
									</c:if>
									<%--
									<div>
										<c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
										<c:if test="${not empty adaptor }">
											<label>Adaptor:</label> <c:out value="${adaptor.getName()}" />
										</c:if>
									</div>
									--%>
								</div>
							</c:forEach>
						</div>
					</c:forEach>
				</div>
		  	</div>
		</c:forEach>
	
	 	<br /><br />more stuff:<br/>	
		<input id="toggleButton" class="fm-button" type="button" value="Hide Window"  onClick="toggleViewerFrame(this)" />
		<br />
		<a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">Right Frame: View Fastqc report from /results/production_wiki</a>
		<br />
		<a href="<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">Right Frame: Wasp job 87's home page</a>
		<br />
		<a href="<c:url value="/sampleDnaToLibrary/showplay.do" />" target="myIframe">Right Frame: view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html');">Aligned Popup WINDOW: fastqc</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />');">Aligned Popup WINDOW: Wasp job 87's home page</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="window.open('<c:url value="/sampleDnaToLibrary/showplay.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">Popup WINDOW (left): view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/sampleDnaToLibrary/showplay.do" />');">Aligned Popup WINDOW: File on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modal Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modal Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/showplay.do" />");' >Modal Dialog: view a FILE stored on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modaless Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modaless Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/showplay.do" />");' >Modaless Dialog: view a FILE stored on Chiam</a>
		
 	</div>
	<div class="viewerRight">
		<div id="viewerFrame" style="display:block;">
  			<%-- <iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="600" width="99%"><p>iframes not supported</p></iframe>--%>
  			<%-- <iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="100%" width="100%" ><p>iframes not supported</p></iframe>--%>
  			<%--  <iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="500px" width="500px" ><p>iframes not supported</p></iframe> --%>
  			
  			 <iframe id="myIframe" name="myIframe" src="<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />" style="overflow-x: scroll; overflow-y: scroll" height="800px" width="600px" ><p>iframes not supported</p></iframe>
   		</div>
	</div>	
	<div style="clear:both;"></div>	
</div>




 <%-- 
<style>
	.pageContainer {width:100%; overflow:hidden;}
	.selectionLeft {float:left; width:15%; }
	.viewerRight {float:left; width:80%; padding-left:0.3cm; overflow:hidden;};
</style>
<div class="pageContainer">
	<div class="selectionLeft">
	   <div id="modalDialog">
	   			<iframe id="modalIframeId" name="modalIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
	   	</div>
	   	<div id="modalessDialog">
	   			<iframe id="modalessIframeId" name="modalessIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
	   	</div>		
		<input id="toggleButton" class="fm-button" type="button" value="Hide Window"  onClick="toggleViewerFrame(this)" />
		<br />
		<a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">Right Frame: View Fastqc report from /results/production_wiki</a>
		<br />
		<a href="<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">Right Frame: Wasp job 87's home page</a>
		<br />
		<a href="<c:url value="/sampleDnaToLibrary/showplay.do" />" target="myIframe">Right Frame: view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html');">Aligned Popup WINDOW: fastqc</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />');">Aligned Popup WINDOW: Wasp job 87's home page</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="window.open('<c:url value="/sampleDnaToLibrary/showplay.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">Popup WINDOW (left): view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/sampleDnaToLibrary/showplay.do" />');">Aligned Popup WINDOW: File on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modal Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modal Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/showplay.do" />");' >Modal Dialog: view a FILE stored on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modaless Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modaless Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/showplay.do" />");' >Modaless Dialog: view a FILE stored on Chiam</a>
		
 	</div>
	<div class="viewerRight">
		<div id="viewerFrame" style="display:block;">
  			<iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="600" width="99%"><p>iframes not supported</p></iframe>
  		</div>
	</div>	
	<div style="clear:both;"></div>	
</div>
--%>


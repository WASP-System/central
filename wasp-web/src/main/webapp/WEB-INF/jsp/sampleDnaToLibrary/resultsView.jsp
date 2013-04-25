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

function toggleExpandHide(thisAnchorObject, url1, url2){
	
	//when expand, show url1 and hilight it's details
	//when close, ID any of it's details are highlighed, close, unhiglhigh, and show url2 
	
	
	var runIdNumberToToggle = thisAnchorObject.id.split("_").pop();
	var temp = "runDivToToggle_" + runIdNumberToToggle;
	var runDivToToggle = document.getElementById(temp);
	var parentDiv = thisAnchorObject.parentNode;
	
	if(runDivToToggle.style.display == "none"){//selected region is closed, so open it 
		runDivToToggle.style.display = "block";
		//"1px dashed gray";
		thisAnchorObject.innerHTML = "hide";
		parentDiv.style.border= "1px dashed gray";
		//populateIFrameAndHighlightThisRun(thisAnchorObject, url1);
		
		//in case this expand/hide anchor is highlighted (see below in this method, unhighlight it) 
		thisAnchorObject.style.color = unhighlightedAnchorColor;
		thisAnchorObject.style.background = unhighlightedAnchorBackground; 
		thisAnchorObject.style.fontWeight = unhighlightedAnchorFontWeight;
		thisAnchorObject.innerHTML = thisAnchorObject.innerHTML.toLowerCase();
	}
	else{//it's open, so close it 
		runDivToToggle.style.display = "none";
		thisAnchorObject.innerHTML = "expand";		
		parentDiv.style.border= "";
		
		//unhighlightOtherRuns(runIdNumberToHighlight);
		//populateIFrame(thisAnchorObject, url2); ///thisAnchorObject.href=url2;
		
		/*  on second thought, don't do this
		//also unhighlight any highlighted anchors within this region)
		var allAnchorsInRunDivToToggle = runDivToToggle.getElementsByTagName("a");
		for(var i = 0; i < allAnchorsInRunDivToToggle.length; i++){
				allAnchorsInRunDivToToggle[i].style.color = unhighlightedAnchorColor;
				allAnchorsInRunDivToToggle[i].style.background = unhighlightedAnchorBackground; 
				allAnchorsInRunDivToToggle[i].style.fontWeight = unhighlightedAnchorFontWeight;	
				allAnchorsInRunDivToToggle[i].innerHTML = allAnchorsInRunDivToToggle[i].innerHTML.toLowerCase();
		}
		*/
		//when closing (hiding) this area 
		//if any of the anchors within this area are highlighted, 
		//then leave them highlighted and also highlight the hide anchor (to indicate that some details anchor now hidden is currently hightlighted) 
		var allAnchorsInRunDivToToggle = runDivToToggle.getElementsByTagName("a");
		for(var i = 0; i < allAnchorsInRunDivToToggle.length; i++){
			if(allAnchorsInRunDivToToggle[i].style.color == highlightedAnchorColor){
				thisAnchorObject.style.color = highlightedAnchorColor;
				thisAnchorObject.style.background = highlightedAnchorBackground; 
				thisAnchorObject.style.fontWeight = highlightedAnchorFontWeight;
				thisAnchorObject.innerHTML = thisAnchorObject.innerHTML.toUpperCase();				
				break;
			}
		}
		
	}
}

function toggleView(thisAnchorObject, url1, url2){
	var runIdNumberToHighlight = thisAnchorObject.id.split("_").pop();
	var temp = "runDivToToggle_" + runIdNumberToHighlight;
	var runDivToToggle = document.getElementById(temp);
	if(runDivToToggle.style.display == "none"){//selected region is closed, so open it up and populate the iframe with run info
		runDivToToggle.style.display = "block";
		thisAnchorObject.innerHTML = "hide";
		populateIFrameAndHighlightThisRun(thisAnchorObject, url1);
	}
	else{
		runDivToToggle.style.display = "none";
		var parentDiv = thisAnchorObject.parentNode;
		parentDiv.style.border= "";
		thisAnchorObject.innerHTML = "expand";
		unhighlightOtherRuns(runIdNumberToHighlight);
		populateIFrame(thisAnchorObject, url2); ///thisAnchorObject.href=url2;
	}	
}

function populateIFrame(thisAnchorObject, url){
	var targetId = thisAnchorObject.target;
	if(targetId == ""){
		return false;
	}
	//var myIframeObj = document.getElementById("myIframe"); 
	var myIframeObj = document.getElementById(targetId);
//	alert("requested url = " + url);
//	alert("myIframeObj.src = " + myIframeObj.src);
//	alert("myIframeObj.src.indexOf(url) = " + myIframeObj.src.indexOf(url));
	//alert("myIframeObj.src.indexOf(url) ===-1 is " + myIframeObj.src.indexOf(url)===-1);
	if(myIframeObj.src.indexOf(url)===-1){//they are different, so execute 
//		alert("we are within the exeutable part");
		myIframeObj.src = url;//simply changes the src information stored in myIframe object (save for next time); actually has no effect on making the http call 
		thisAnchorObject.href=url;//makes the http call 
	}
	else{
		thisAnchorObject.href = "javascript:void(0);";
		alert("The viewport is currently displaying this information");
		return false;
	}
}

//globals 
unhighlightedAnchorColor = "";
unhighlightedAnchorBackground = "";
unhighlightedAnchorFontWeight = "";
highlightedAnchorColor = "red";
highlightedAnchorBackground = "white";
highlightedAnchorFontWeight = "bold";

function toggleAnchors(thisAnchorObject){
	
	//anchor is already the currently highlighted anchor, so nothing needed to be done 
	if(thisAnchorObject.style.color == highlightedAnchorColor){
		return;
	}
	
	//change this anchor to highlighted attributes 
	thisAnchorObject.style.color = highlightedAnchorColor;// for example "blue" 
	thisAnchorObject.style.background = highlightedAnchorBackground;
	thisAnchorObject.style.fontWeight = highlightedAnchorFontWeight;
	thisAnchorObject.innerHTML = thisAnchorObject.innerHTML.toUpperCase();
	
	//for all other anchors (in div with id selectionLeftDiv), change back to the original, unhilighted attributes 
	var selectionLeftDiv = document.getElementById("selectionLeft");
	var allAnchors = selectionLeftDiv.getElementsByTagName("a");
	for(var i = 0; i < allAnchors.length; i++){
		if(thisAnchorObject.id != allAnchors[i].id){
			allAnchors[i].style.color = unhighlightedAnchorColor;
			allAnchors[i].style.background = unhighlightedAnchorBackground; 
			allAnchors[i].style.fontWeight = unhighlightedAnchorFontWeight;	
			allAnchors[i].innerHTML = allAnchors[i].innerHTML.toLowerCase();
		}
	}
	

}
function populateIFrameAndHighlightThisRun(thisAnchorObject, url){

	var runIdNumberToHighlight = thisAnchorObject.id.split("_").pop();
	highlightThisRunAndUnhighlightOtherRuns(runIdNumberToHighlight);
	
	//alert("runIdNumber = " + runIdNumber);
	//var temp = "runDivToHighlight_" + runIdNumber;
	//var runDivToHighlight = document.getElementById(temp);
	//runDivToHighlight.style.border = "2px solid red";
	//////thisAnchorObject.href=url;
	populateIFrame(thisAnchorObject, url);// thisAnchorObject.href=url; 
}
function highlightThisRunAndUnhighlightOtherRuns(runIdNumberToHighlight){
	
	var allRunDivsToHighlight = document.querySelectorAll('*[id^="runDivToHighlight_"]');
	for(var i = 0; i < allRunDivsToHighlight.length; i++){
		var runDivToHighlight = allRunDivsToHighlight[i];
		if(runDivToHighlight.id.split("_").pop() == runIdNumberToHighlight){
			runDivToHighlight.style.border = "2px solid red";
		}
		else{
			var temp = "runDivToToggle_" + runDivToHighlight.id.split("_").pop();
			var runDivToToggle = document.getElementById(temp);
			if(runDivToToggle.style.display == "block"){//it's already open 
				runDivToHighlight.style.border = "1px dashed gray";
			}
			else{
				runDivToHighlight.style.border = "";
			}
		}		
	}
}
function unhighlightOtherRuns(runIdNumberToHighlight){
	
	var allRunDivsToHighlight = document.querySelectorAll('*[id^="runDivToHighlight_"]');
	for(var i = 0; i < allRunDivsToHighlight.length; i++){
		var runDivToHighlight = allRunDivsToHighlight[i];
		if(runDivToHighlight.id.split("_").pop() != runIdNumberToHighlight){
			var temp = "runDivToToggle_" + runDivToHighlight.id.split("_").pop();
			var runDivToToggle = document.getElementById(temp);
			if(runDivToToggle.style.display == "block"){//it's already open 
				runDivToHighlight.style.border = "1px dashed gray";
			}
			else{
				runDivToHighlight.style.border = "";
			}
		}		
	}
}

</script>






 
<style>
	.pageContainer {width:100%; overflow:hidden; }
	.selectionLeft {float:left; width:40%; margin-right:0.2cm; }
	.viewerRight {float:left; width:50%; padding-left:0.2cm; border-left:3px solid black; overflow:hidden;}
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


<h1><a  href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do" />">JobID J<c:out value="${job.jobId}" /></a></h1>		
		

<div class="pageContainer">
	<div id="selectionLeft" class="selectionLeft">	  
		<%-- <label>Job Name: <c:out value="${job.getName()}" /></label>	[<a id="jobDetailsAnchor"  href="<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />" target="myIframe" >details</a>]	--%>	
		<label>Job Name: <c:out value="${job.getName()}" /></label>	[<a style="color:red; font-weight:bold; background-color:white;" id="jobDetailsAnchor"  href="javascript:void(0);" target="myIframe" onclick='toggleAnchors(this); populateIFrame(this, "<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />");' >DETAILS</a>]
		<div>
			<label>Aggregate Analysis</label> [<a id="aggregateAnalysis" href="javascript:void(0);" onclick='<%--toggleAnchors(this);--%> alert("Not yet implemented");'>details</a>] 
		</div>	
		<c:forEach items="${platformUnitSet}" var="platformUnit">
			<c:set value="${platformUnitRunMap.get(platformUnit)}" var="run"/>
			<div id="runDivToHighlight_${run.getId()}">
			<label>Sequence Run:</label> <c:out value="${run.getName()}" /> <%-- (<label>FlowCell:</label> <c:out value="${platformUnit.getName()}" />)--%> 
			<%-- [<a  href="<c:url value="/sampleDnaToLibrary/runDetails/${run.getId()}.do" />" target="myIframe" onclick='alert("in this anchor alert")";' >idetails</a> --%>
			<%-- [<a id="runDetailsAnchor_${run.getId()}" href="javascript:void(0);" target="myIframe" onclick='toggleAnchors(this); populateIFrameAndHighlightThisRun(this, "<c:url value="/sampleDnaToLibrary/runDetails/${run.getId()}.do" />");' >details</a> --%>
			[<a id="runDetailsAnchor_${run.getId()}" href="javascript:void(0);" target="myIframe" onclick='toggleAnchors(this); populateIFrame(this, "<c:url value="/sampleDnaToLibrary/runDetails/${run.getId()}.do" />");' >details</a> 
			<%-- | <a id="runExpandAnchor_${run.getId()}" href="javascript:void(0);" target="myIframe" onclick='toggleAnchors(this); toggleView(this, "<c:url value="/sampleDnaToLibrary/runDetails/${run.getId()}.do" />", "<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />");' >expand</a>] --%>
			| <a id="runExpandAnchor_${run.getId()}" href="javascript:void(0);" target="myIframe" onclick='toggleExpandHide(this, "<c:url value="/sampleDnaToLibrary/runDetails/${run.getId()}.do" />", "<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />");' >expand</a>] 
					<div id="runDivToToggle_${run.getId()}" style="display:none;">					
					<c:set value="${platformUnitOrderedCellListMap.get(platformUnit)}" var="cellList"/>
					<c:forEach items="${cellList}" var="cell">
						<div>
							<c:set value="${cellIndexMap.get(cell)}" var="index"/>
							<c:choose>
								<c:when test="${not empty index }">							
									<%-- <label>Lane <c:out value="${index}" /></label> [<a  href="<c:url value="/sampleDnaToLibrary/cellDetails/${cell.getId()}.do?runId=${run.getId()}" />" target="myIframe" >details</a> | <a href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Fastqc</a> | <a href="javascript:void(0);" onclick='showPopupWindow("http://wasp.einstein.yu.edu/results/production_wiki/JLocker/JTian/P520/J10728/stats/stats_TrueSeqUnknown.BC1G0RACXX.lane_5_P0_I0.fastq.html");' >Graphical Stats</a>]--%> 
									<label>Lane <c:out value="${index}" /></label> [<a id="cellDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" target="myIframe" onclick='toggleAnchors(this); populateIFrameAndHighlightThisRun(this, "<c:url value="/sampleDnaToLibrary/cellDetails/${cell.getId()}.do?runId=${run.getId()}" />");' >details</a> | <a id="fastQCDetailsAnchor_${run.getId()}" href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Fastqc</a> | <a id="statsDetailsAnchor_${run.getId()}"href="javascript:void(0);" onclick='showPopupWindow("http://wasp.einstein.yu.edu/results/production_wiki/JLocker/JTian/P520/J10728/stats/stats_TrueSeqUnknown.BC1G0RACXX.lane_5_P0_I0.fastq.html");' >Graphical Stats</a>] 
								</c:when>
								<c:otherwise>
									<label>Lane <c:out value="${cell.getName()}" /></label> [<a id="cellDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" target="myIframe" onclick='toggleAnchors(this); populateIFrameAndHighlightThisRun(this, "<c:url value="/sampleDnaToLibrary/cellDetails/${cell.getId()}.do?runId=${run.getId()}" />");' >details</a>] 
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
									<c:choose>
									<c:when test="${not empty parentMacromolecule }">
										(<label>Parent:</label> <c:out value="${parentMacromolecule.getName()}" />)
									</c:when>
									<c:otherwise>
										(<label>Parent:</label> None Submitted)
									</c:otherwise>
									</c:choose>
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


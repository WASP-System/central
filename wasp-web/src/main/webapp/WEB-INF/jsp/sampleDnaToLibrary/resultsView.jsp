<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<script type="text/javascript">

$(document).ready(function() {
	
	//////$("#viewerFrame").load('<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />');
	
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


//globals 
unhighlightedAnchorColor = "";
unhighlightedAnchorBackground = "";
unhighlightedAnchorFontWeight = "";
highlightedAnchorColor = "red";
highlightedAnchorBackground = "white";
highlightedAnchorFontWeight = "bold";
urlDisplayedOnRight = "";

function showModalDialog(url){
	//http://clarkupdike.blogspot.com/2009/03/basic-example-of-jquerys-uidialog.html
	$("#modalIframeId").attr("src", url);
	$( "#modalDialog" ).dialog("open");
}
function showModalessDialog(url){
	$("#modalessIframeId").attr("src", url);
	$( "#modalessDialog" ).dialog("open");
}
function showPopupWindow(url) 
{	
	//from http://stackoverflow.com/questions/10728207/position-a-window-on-screen 
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

function toggleExpandHide(thisAnchorObject){

	var runIdNumberToToggle = thisAnchorObject.id.split("_").pop();
	var temp = "runDivToToggle_" + runIdNumberToToggle;
	var runDivToToggle = document.getElementById(temp);
	var parentDiv = thisAnchorObject.parentNode;
	
	if(runDivToToggle.style.display == "none"){//selected region is closed, so open it 
		runDivToToggle.style.display = "block";
		thisAnchorObject.innerHTML = "hide";
		parentDiv.style.border= "1px dashed gray";
		
		//in case this expand/hide anchor (the thisAnchorObject object) happens to be highlighted, then unhighlight it 
		//(note: it can become highlighted in certain situations; see this method, below) 
		thisAnchorObject.style.color = unhighlightedAnchorColor;
		thisAnchorObject.style.background = unhighlightedAnchorBackground; 
		thisAnchorObject.style.fontWeight = unhighlightedAnchorFontWeight;
		thisAnchorObject.innerHTML = thisAnchorObject.innerHTML.toLowerCase();
	}
	else{//it's open, so close it 
		runDivToToggle.style.display = "none";
		thisAnchorObject.innerHTML = "expand";		
		parentDiv.style.border= "";
				
		//when closing (hiding) this area, 
		//if ANY of its internal anchors (within this area) are highlighted, 
		//then leave them highlighted and ALSO highlight the hide/expand anchor (to indicate that some detail anchor, now hidden, is currently hightlighted) 
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

function populateIFrame(thisAnchorObject, url){

	var targetId = thisAnchorObject.target;
	if(targetId == ""){
		return false;
	}
	
	// no longer needed: var myIframeObj = document.getElementById("myIframe"); 
	var myIframeObj = document.getElementById(targetId);
	if(myIframeObj.src.indexOf(url)===-1){//they are different, so execute 
		myIframeObj.src = url;//simply changes the src information stored in myIframe object (save for next time); this actually has no effect on making the http call 
		thisAnchorObject.href=url;//it is this line that actually makes the http call 
	}
	else{
		thisAnchorObject.href = "javascript:void(0);";
		alert("The viewport on the right is currently displaying this information");
		return false;
	}
}

function loadNewPage(thisAnchorObject, urlToDisplay) {
	
	//from http://bytes.com/topic/javascript/answers/658337-loading-html-pages-inside-div-id-x-div 
	if(urlDisplayedOnRight == urlToDisplay){//urlDisplayedOnRight is a javascript global variable 
		alert("The viewport on the right is currently displaying this information");
		return false;
	}
	
	var req = new XMLHttpRequest();
	req.open("GET", urlToDisplay, false);
	req.send(null);
	var page = req.responseText;
	if(req.status == 404 || req.status == 500){
		page = "Error! Unable to load data. Please try again.";
	}
	document.getElementById("viewerFrame").innerHTML = page;
	urlDisplayedOnRight = urlToDisplay;
}
	
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

function toggleViewerFrame(toggleButton){
	var viewerFrame = document.getElementById('viewerFrame');
	if(toggleButton.value == "Show Viewport"){
		toggleButton.value = "Hide Viewport";
		viewerFrame.style.display = "block";
	}
	else if(toggleButton.value == "Hide Viewport"){
		toggleButton.value = "Show Viewport";
		viewerFrame.style.display = "none";	
	}	
}
function openAllRuns(){
	
	//for first line, see http://stackoverflow.com/questions/10111668/find-all-elements-whose-id-begins-with-a-common-string 
	var runExpandAnchorArray = document.querySelectorAll('*[id^="runExpandAnchor"]');
	for(var i = 0; i < runExpandAnchorArray.length; i++){
		thisAnchorObject = runExpandAnchorArray[i];
		var runIdNumberToToggle = thisAnchorObject.id.split("_").pop();
		var temp = "runDivToToggle_" + runIdNumberToToggle;
		var runDivToToggle = document.getElementById(temp);
		var parentDiv = thisAnchorObject.parentNode;
		
		if(runDivToToggle.style.display == "none"){//selected region is closed, so open it. If it's open, then do nothing 
			runDivToToggle.style.display = "block";
			thisAnchorObject.innerHTML = "hide";
			parentDiv.style.border= "1px dashed gray";
			
			//in case this expand/hide anchor (the thisAnchorObject object) happens to be highlighted, then unhighlight it 
			//(note: it can become highlighted in certain situations. See function toggleExpandHide() above) 
			thisAnchorObject.style.color = unhighlightedAnchorColor;
			thisAnchorObject.style.background = unhighlightedAnchorBackground; 
			thisAnchorObject.style.fontWeight = unhighlightedAnchorFontWeight;
			thisAnchorObject.innerHTML = thisAnchorObject.innerHTML.toLowerCase();
		}
	}
}
function closeAllRuns(){
	//for first line, see http://stackoverflow.com/questions/10111668/find-all-elements-whose-id-begins-with-a-common-string 
	var runExpandAnchorArray = document.querySelectorAll('*[id^="runExpandAnchor"]');
	for(var i = 0; i < runExpandAnchorArray.length; i++){
		thisAnchorObject = runExpandAnchorArray[i];
		var runIdNumberToToggle = thisAnchorObject.id.split("_").pop();
		var temp = "runDivToToggle_" + runIdNumberToToggle;
		var runDivToToggle = document.getElementById(temp);
		var parentDiv = thisAnchorObject.parentNode;
		
		if(runDivToToggle.style.display == "block"){//selected region is open, so close it. If it's closed, then do nothing 
			runDivToToggle.style.display = "none";
			thisAnchorObject.innerHTML = "expand";		
			parentDiv.style.border= "";
					
			//when closing (hiding) this area, 
			//if ANY of its internal anchors (within this area) are highlighted, 
			//then leave them highlighted and ALSO highlight the hide/expand anchor (to indicate that some detail anchor, now hidden, is currently hightlighted) 
			var allAnchorsInRunDivToToggle = runDivToToggle.getElementsByTagName("a");
			for(var z = 0; z < allAnchorsInRunDivToToggle.length; z++){
				if(allAnchorsInRunDivToToggle[z].style.color == highlightedAnchorColor){
					thisAnchorObject.style.color = highlightedAnchorColor;
					thisAnchorObject.style.background = highlightedAnchorBackground; 
					thisAnchorObject.style.fontWeight = highlightedAnchorFontWeight;
					thisAnchorObject.innerHTML = thisAnchorObject.innerHTML.toUpperCase();				
					break;
				}
			}
		}
	}	
}

window.onload = function (){loadNewPage('fakeAnchor', '<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />'); }

</script>
 
<style>
	.pageContainer {width:100%; overflow:hidden; }
	.selectionLeft {float:left; width:40%; margin-right:0.2cm; }
	.viewerRight {float:left; width:50%; padding-left:0.2cm; border-left:3px solid black; overflow:auto; }
	.selectionLeft div {margin:5px 0px 5px 10px;}
	.rob div {margin:5px 0px 5px 20px;}
</style>
<%--  .viewerRight {float:left; width:50%; padding-left:0.2cm; border-left:3px solid black; overflow:hidden;}
	.viewerRight {float:left; width:50%; padding-left:0.2cm; border-left:3px solid black; overflow-x:scroll; overflow-y:hidden;}

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

<br /><br />
<h1><a  href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do" />">JobID J<c:out value="${job.jobId}" /></a></h1>	

<div class="pageContainer">
	<div id="selectionLeft" class="selectionLeft">	  
		<label>Job Name: <c:out value="${job.getName()}" /></label>	[<a style="color:red; font-weight:bold; background-color:white;" id="jobDetailsAnchor"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />");' >DETAILS</a><c:if test="${fn:length(platformUnitSet) > 1}"> | <a id="openAllRunsAnchor"  href="javascript:void(0);" onclick='openAllRuns();' >open all runs</a> | <a id="closeAllRunsAnchor" href="javascript:void(0);"  onclick='closeAllRuns();' >close all runs</a></c:if>]
		<c:if test="${fn:length(platformUnitSet) > 0}">
		<div>
			<label>Aggregate Analysis</label> [<a id="aggregateAnalysis" href="javascript:void(0);" onclick='<%--toggleAnchors(this);--%> alert("Not yet implemented");'>details</a>] 
		</div>
		</c:if>	
		<c:forEach items="${platformUnitSet}" var="platformUnit">
			<c:set value="${platformUnitRunMap.get(platformUnit)}" var="run"/>
			<div id="runDivToHighlight_${run.getId()}">
			<label>Sequence Run:</label> <c:out value="${run.getName()}" /> <%-- (<label>FlowCell:</label> <c:out value="${platformUnit.getName()}" />)--%> 
			[<a id="runDetailsAnchor_${run.getId()}" href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/sampleDnaToLibrary/runDetails/${run.getId()}.do" />");' >details</a> 
			| <a id="runExpandAnchor_${run.getId()}" href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleExpandHide(this);' >expand</a>] 
					<div id="runDivToToggle_${run.getId()}" style="display:none;">					
					<c:set value="${platformUnitOrderedCellListMap.get(platformUnit)}" var="cellList"/>
					<c:forEach items="${cellList}" var="cell">
						<div>
							<c:set value="${cellIndexMap.get(cell)}" var="index"/>
							<c:choose>
								<c:when test="${not empty index }">							
									<label>Lane: <c:out value="${index}" /></label> [<a id="cellDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/sampleDnaToLibrary/cellDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >details</a> | <a id="fastQCDetailsAnchor_${run.getId()}" href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >fastqc</a> | <a id="statsDetailsAnchor_${run.getId()}"href="javascript:void(0);" onclick='showPopupWindow("http://wasp.einstein.yu.edu/results/production_wiki/JLocker/JTian/P520/J10728/stats/stats_TrueSeqUnknown.BC1G0RACXX.lane_5_P0_I0.fastq.html");' >graphical Stats</a> | <a id="cellSequencesDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/sampleDnaToLibrary/cellSequencesDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >sequences</a> | <a id="cellAlignmentsDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/sampleDnaToLibrary/cellAlignmentsDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >alignments</a>] 
								</c:when>
								<c:otherwise>
									<label>Lane: <c:out value="${cell.getName()}" /></label> [<a id="cellDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<c:url value="/sampleDnaToLibrary/cellDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >details</a>] 
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
	 	<br /><br />more stuff:<br/>	
		 <input id="toggleButton" class="fm-button" type="button" value="Hide Viewport"  onClick="toggleViewerFrame(this)" />
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
		<br /><br />
		<a style="color:green; font-weight:bold; background-color:white;" id="jobDetailsAnchorzzzzzzzz"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='loadNewPage(this, "<c:url value="/sampleDnaToLibrary/showplay.do" />");' >use SHOWPLAY INTO div on right</a>
 	</div>
	<div class="viewerRight">
		<div id="viewerFrame" style="display:block;">
  			<%-- <iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="600" width="99%"><p>iframes not supported</p></iframe>--%>
  			<%-- <iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="100%" width="100%" ><p>iframes not supported</p></iframe>--%>
  			<%--  <iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="500px" width="500px" ><p>iframes not supported</p></iframe> --%>
  			
  			<%-- <iframe id="myIframe" name="myIframe" src="<c:url value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />" style="overflow-x: scroll; overflow-y: scroll" height="800px" width="600px" ><p>iframes not supported</p></iframe>--%>
   		</div>
	</div>	
	<div style="clear:both;"></div>	
</div>




 <%-- original experimental section
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


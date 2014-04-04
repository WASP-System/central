<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<style>
	.pageContainer {width:100%; overflow:hidden; }
	.selectionLeft {float:left; width:44%; margin-right:0.2cm; overflow:auto;}
	.viewerRight {float:left; width:54%; padding-left:0.2cm; border-left:3px solid black; overflow:auto; }
	.selectionLeft div {margin:5px 0px 5px 6px;}
</style>

<script type="text/javascript">

$(document).ready(function() {
	
	//////$("#viewerFrame").load('<wasp:relativeUrl value="sampleDnaToLibrary/jobDetails/${job.getId()}.do" />');
	
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
highlightedAnchorBackground = "Aqua";
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
		//parentDiv.style.borderRight="";//in case of overflow and need for horizontal scrollbar 
		
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

window.onload = function (){loadNewPage('fakeAnchor', '<wasp:relativeUrl value="datadisplay/mps/jobs/${job.getId()}/jobdetails.do" />'); }

</script>
 

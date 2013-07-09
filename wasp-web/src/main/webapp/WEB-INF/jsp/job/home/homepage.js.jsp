<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  
<style>
	.pageContainer {width:100%; overflow:hidden; }
	.selectionLeft {float:left; width:15%; margin-right:0.2cm; border:3px solid black; overflow:auto;}
	.viewerRight {float:left; width:80%; padding-left:0.2cm; border-left:0px solid black; overflow:auto; }
	.selectionLeft div {margin:5px 0px 5px 6px;}
</style>

<script type="text/javascript">

$(document).ready(function() {
	
	  $(function() {
		    $( "#tabs" ).tabs();
		    //$( "#tabs" ).tabs( "destroy");
		  }); 

	
	   
	  $("#hiddenIFrame").load(function() { ////http://blog.manki.in/2011/08/ajax-fie-upload.html 
		  
		  var responseText = $('#hiddenIFrame').contents().find('body').html();
		  
		  if (!responseText) {
			    return;
		  }
		  
		  $('#viewerFrame').html(responseText);
		  //clear contents of iframe; don't know which is best 
		  this.src = "about:blank";  //http://stackoverflow.com/questions/1785040/how-to-clear-the-content-of-an-iframe 
		  this.contentDocument.location.href = "about:blank"; ///'/img/logo.png';
		});
	  
	  
	  
	  
	  
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
	
	$("#smallModalessDialog").dialog({
        autoOpen: false,
        modal: false,
        height: 400,
        width: 400,
        position: { my: "right top", at: "right top", of: window } <%--could user "#container" too, which is set by wasp css --%>
    });
	 $.fn.serializeObject = function() { //http://stackoverflow.com/questions/1184624/convert-form-data-to-js-object-with-jquery   
	    	var o = {};
	    	var a = this.serializeArray(); //http://api.jquery.com/serializeArray/ 
	    	$.each(a, function() {
	    		if (o[this.name]) {
	    			if (!o[this.name].push) {
	    				o[this.name] = [o[this.name]];
	    			}
	    			o[this.name].push(this.value || '');
	    		} else {
	    			o[this.name] = this.value || '';
	    		}
	    	});
	    	return o;
	    };
 
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
function showSmallModalessDialog(url){
	$("#smallModalessIframeId").attr("src", url);
	$( "#smallModalessDialog" ).dialog("open");
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

function loadIFrameAnotherWay(thisAnchorObject, url){//alert("I am here with url of : " + url);
	///var viewerFrame = document.getElementById("viewerFrame");
	///var viewerFrame2 = document.getElementById("viewerFrame2");
	///if(viewerFrame.style.display=="block"){
	///	viewerFrame.style.display="none";
	///	viewerFrame2.style.display = "block";
	///} 
	//thisAnchorObject.href=url; 
	
	$('html, body').animate({ scrollTop: 0 }, 0); //got to top of page: http://www.nomadjourney.com/2009/09/go-to-top-of-page-using-jquery/ 
	
	var myIframe = document.getElementById("myIframe");//ok, works with the tabs 
	myIframe.src = url;
	myIframe.style.width="100%";
	myIframe.style.height="1000px";
	//note: neither of the two next lines works in Firefox. It doesn't know height of what, with %.  
	//see http://www.daniweb.com/web-development/web-design-html-and-css/threads/283687/css-height-in-not-working 
	//myIframe.style.height="100%";  
	//myIframe.height="100%"; 

	var viewerFrame = document.getElementById("viewerFrame");
	var viewerFrame2 = document.getElementById("viewerFrame2");
	if(viewerFrame.style.display=="block"){
		viewerFrame.style.display="none";
		viewerFrame2.style.display = "block";
	} 
}

function loadIFrame(thisAnchorObject, url){//alert("I am here with url of : " + url);
	var viewerFrame = document.getElementById("viewerFrame");
	var viewerFrame2 = document.getElementById("viewerFrame2");
	if(viewerFrame.style.display=="block"){
		viewerFrame.style.display="none";
		viewerFrame2.style.display = "block";
	} 
	thisAnchorObject.href=url;
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
//	if(urlDisplayedOnRight == urlToDisplay){//urlDisplayedOnRight is a javascript global variable 
//		alert("The viewport on the right is currently displaying this information");
//		return false;
//	} 
	
	///var viewerFrame = document.getElementById("viewerFrame");
	///var viewerFrame2 = document.getElementById("viewerFrame2");
	///if(viewerFrame.style.display=="none"){
	///	viewerFrame.style.display="block";
	///	viewerFrame2.style.display = "none";
	///} 
	
	$('html, body').animate({ scrollTop: 0 }, 0); //got to top of page: http://www.nomadjourney.com/2009/09/go-to-top-of-page-using-jquery/ 

	var req = new XMLHttpRequest();
	req.open("GET", urlToDisplay, false);
	req.setRequestHeader("X-Requested-With","XMLHttpRequest");//it's ajax 
	req.send(null);
	var page = req.responseText;
	if(req.status == 404 || req.status == 500){
		page = "Error! Unable to load data. Please try again.";
	}
	else if(req.status == 599){//session expired; user defined 
		document.location.href = '<c:url value="/auth/login.do" />';
		return;
	}
	document.getElementById("viewerFrame").innerHTML = page;
	urlDisplayedOnRight = urlToDisplay;//I think no longer used 
	
	var viewerFrame = document.getElementById("viewerFrame");
	var viewerFrame2 = document.getElementById("viewerFrame2");
	if(viewerFrame.style.display=="none"){
		viewerFrame.style.display="block";
		viewerFrame2.style.display = "none";
		var myIframe = document.getElementById("myIframe");//ok, works with the tabs 
		myIframe.src = "about:blank";  //http://stackoverflow.com/questions/1785040/how-to-clear-the-content-of-an-iframe
	}

	//how one could possibly get javascript with ajax and then add it to dom 
	//http://ntt.cc/2008/02/10/4-ways-to-dynamically-load-external-javascriptwith-source.html 
	/*
	var oHead = document.getElementsByTagName('HEAD').item(0);
	var oScript = document.createElement( "script" );
	oScript.language = "javascript";
	oScript.type = "text/javascript";
	oScript.id = "sId";
	oScript.defer = true;
	oScript.text = 'function test12345(){alert("inside the 1234567890 alert within homepage.js.jsp");}';  
	oHead.appendChild( oScript );	
	*/
}
function loadNewPageWithoutMoving(thisAnchorObject, urlToDisplay) {
	
	var req = new XMLHttpRequest();
	req.open("GET", urlToDisplay, false);
	req.setRequestHeader("X-Requested-With","XMLHttpRequest");//it's ajax 
	req.send(null);
	var page = req.responseText;
	if(req.status == 404 || req.status == 500){
		$('html, body').animate({ scrollTop: 0 }, 0); //got to top of page: http://www.nomadjourney.com/2009/09/go-to-top-of-page-using-jquery/ 
		page = "Error! Unable to load data. Please try again.";
	}
	else if(req.status == 599){//session expired; user defined 
		document.location.href = '<c:url value="/auth/login.do" />';
		return;
	}
	document.getElementById("viewerFrame").innerHTML = page;
	urlDisplayedOnRight = urlToDisplay;//I think no longer used 
	
	var viewerFrame = document.getElementById("viewerFrame");
	var viewerFrame2 = document.getElementById("viewerFrame2");
	if(viewerFrame.style.display=="none"){
		viewerFrame.style.display="block";
		viewerFrame2.style.display = "none";
		var myIframe = document.getElementById("myIframe");//ok, works with the tabs 
		myIframe.src = "about:blank";  //http://stackoverflow.com/questions/1785040/how-to-clear-the-content-of-an-iframe 
	}
}
function postForm(formId, urlToPost) {//added 5-16-13 
	//alert("yes, I am in postForm");alert("url: "+urlToPost);alert("formId = " + formId);
	//http://www.w3schools.com/ajax/ajax_xmlhttprequest_send.asp 
	//alert("urlToPost: " + urlToPost);
	var theForm = document.getElementById(formId);
	//alert("theForm name = "+ theForm.name);
	var arrayOfInputs = theForm.getElementsByTagName("input");
	var arrayOfTextInputs = [];
	
	for(var i = 0; i < arrayOfInputs.length; i++ ){
		if(arrayOfInputs[i].type == 'text' || arrayOfInputs[i].type == 'hidden'){
			arrayOfTextInputs.push(arrayOfInputs[i]);
		}
	}
	
	var arrayOfTextAreas = theForm.getElementsByTagName("textarea");
	for(var i = 0; i < arrayOfTextAreas.length; i++ ){
		arrayOfTextInputs.push(arrayOfTextAreas[i]);
	}
	
	var inputParameters = "";
	for(var i = 0; i < arrayOfTextInputs.length; i++ ){
		var theName = arrayOfTextInputs[i].getAttribute("name");
		var theValue = arrayOfTextInputs[i].value;
		if(i>0){
			inputParameters += "&";
		}
		inputParameters += theName + "=" + theValue; 
	}
	
	var arrayOfSelects = theForm.getElementsByTagName("select");
	for(var i = 0; i < arrayOfSelects.length; i++ ){
		var s = arrayOfSelects[i];
		var theName = s.getAttribute("name");
		var theValue = s.options[s.selectedIndex].value;
		if(inputParameters != ""){
			inputParameters += "&";
		}
		inputParameters += theName + "=" + theValue; 
	}
	
	$('html, body').animate({ scrollTop: 0 }, 0); //got to top of page: http://www.nomadjourney.com/2009/09/go-to-top-of-page-using-jquery/ 

	var req = new XMLHttpRequest();
	req.open("POST", urlToPost, false);
	req.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	req.setRequestHeader("X-Requested-With","XMLHttpRequest");//it's ajax 
	//xmlhttp.send("fname=Henry&lname=Ford"); 
	req.send(inputParameters);
	var page = req.responseText;
	if(req.status == 404 || req.status == 500){
		page = "Error! Unable to process form data. Please try again.";
	}
	else if(req.status == 599){//session expired; user defined 
		document.location.href = '<c:url value="/auth/login.do" />';
		return;
	}
	document.getElementById("viewerFrame").innerHTML = page;
	//document.location.href = "http://www.google.com"; //works  
	
	//document.getElementById("tab-1").innerHTML = page;
	
	/*FOR GET FOR TESTING
	var req = new XMLHttpRequest();
	req.open("GET", urlToPost, false);
	req.send(null);
	var page = req.responseText;
	if(req.status == 404 || req.status == 500){
		page = "Error! Unable to load data. Please try again.";
	}
	document.getElementById("viewerFrame").innerHTML = page;
	*/
}

function postFormWithoutMoving(formId, urlToPost) {//added 5-16-13 
	//alert("yes, I am in postForm");alert("url: "+urlToPost);alert("formId = " + formId);
	//http://www.w3schools.com/ajax/ajax_xmlhttprequest_send.asp 
	//alert("urlToPost: " + urlToPost);
	var theForm = document.getElementById(formId);
	//alert("theForm name = "+ theForm.name);
	var arrayOfInputs = theForm.getElementsByTagName("input");
	var arrayOfTextInputs = [];
	
	for(var i = 0; i < arrayOfInputs.length; i++ ){
		if(arrayOfInputs[i].type == 'text' || arrayOfInputs[i].type == 'hidden'){
			arrayOfTextInputs.push(arrayOfInputs[i]);
		}
	}
	
	var arrayOfTextAreas = theForm.getElementsByTagName("textarea");
	for(var i = 0; i < arrayOfTextAreas.length; i++ ){
		arrayOfTextInputs.push(arrayOfTextAreas[i]);
	}
	
	var inputParameters = "";
	for(var i = 0; i < arrayOfTextInputs.length; i++ ){
		var theName = arrayOfTextInputs[i].getAttribute("name");
		var theValue = arrayOfTextInputs[i].value;
		if(i>0){
			inputParameters += "&";
		}
		inputParameters += theName + "=" + theValue; 
	}

	var arrayOfSelects = theForm.getElementsByTagName("select");
	for(var i = 0; i < arrayOfSelects.length; i++ ){
		var s = arrayOfSelects[i];
		var theName = s.getAttribute("name");
		var theValue = s.options[s.selectedIndex].value;
		if(inputParameters != ""){
			inputParameters += "&";
		}
		inputParameters += theName + "=" + theValue; 
	}

	var req = new XMLHttpRequest();
	req.open("POST", urlToPost, false);
	req.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	req.setRequestHeader("X-Requested-With","XMLHttpRequest");//it's ajax 
	//xmlhttp.send("fname=Henry&lname=Ford"); 
	req.send(inputParameters);
	var page = req.responseText;
	if(req.status == 404 || req.status == 500){
		page = "Error! Unable to process form data. Please try again.";
	}
	else if(req.status == 599){//session expired; user defined 
		document.location.href = '<c:url value="/auth/login.do" />';
		return;
	}

	document.getElementById("viewerFrame").innerHTML = page;
	//document.getElementById("tab-1").innerHTML = page;
	
	/*FOR GET FOR TESTING
	var req = new XMLHttpRequest();
	req.open("GET", urlToPost, false);
	req.send(null);
	var page = req.responseText;
	if(req.status == 404 || req.status == 500){
		page = "Error! Unable to load data. Please try again.";
	}
	document.getElementById("viewerFrame").innerHTML = page;
	*/
}


function postMultipartForm(formId, urlToPost) {//added 5-17-13 
//don't think being used !!!  
//http://www.w3schools.com/ajax/ajax_xmlhttprequest_send.asp 
	//http://stackoverflow.com/questions/5933949/how-to-send-multipart-form-data-form-content-by-ajax-no-jquery 
	//alert("urlToPost: " + urlToPost); 
	
	var boundary=Math.random().toString().substr(2);
	
	var theForm = document.getElementById(formId);
	var arrayOfInputs = theForm.getElementsByTagName("input");
	var arrayOfTextInputs = [];
	for(var i = 0; i < arrayOfInputs.length; i++ ){
		if(arrayOfInputs[i].type == 'text'){
			arrayOfTextInputs.push(arrayOfInputs[i]);
		}
	}
	var inputParameters = "";
	for(var i = 0; i < arrayOfTextInputs.length; i++ ){
		var theName = arrayOfTextInputs[i].getAttribute("name");
		var theValue = arrayOfTextInputs[i].value;
		if(i>0){
			inputParameters += "&";
		}
		inputParameters += theName + "=" + theValue; 
	}
	$('html, body').animate({ scrollTop: 0 }, 0); //got to top of page: http://www.nomadjourney.com/2009/09/go-to-top-of-page-using-jquery/ 

	var req = new XMLHttpRequest();
	req.open("POST", urlToPost, false);
	req.setRequestHeader("content-type", "multipart/form-data; charset=utf-8; boundary=" + boundary);	
	//xmlhttp.send("fname=Henry&lname=Ford"); 
	req.send(inputParameters);
	var page = req.responseText;
	if(req.status == 404 || req.status == 500){
		page = "Error! Unable to process form data. Please try again.";
	}
	document.getElementById("viewerFrame").innerHTML = page;
}

function doGetWithAjax(url) {
	
	var req = new XMLHttpRequest();
	req.open("GET", url, false);
	req.send(null);
	var page = req.responseText;
	if(req.status == 404 || req.status == 500){
		page = "Error! Unable to perform action. Please try again.";
	}
	document.getElementById("viewerFrame").innerHTML = page;
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

window.onload = function (){
	loadNewPage('fakeAnchor', '<c:url value="/job/${job.getId()}/basic.do" />'); 
}

</script>
 
<script type="text/javascript">
   /*
   var frm = $('#commentForm');
    frm.submit(function () {
        $.ajax({
            type: frm.attr('method'),
            url: frm.attr('action'),
            data: frm.serialize(),
            success: function (response) {
                //alert('ok');
                //document.getElementById("viewerFrame").innerHTML = response;
                $('#viewerFrame').html = "This is a test";
            }
        });

        return false;
    });
    */
    function postFormWithAjax(formObject, theUrl){
    	var frm = $("#" + formObject.id);//must do this; cannot simply use formObject; don't know why not 
    	$.ajax({
            type: frm.attr('method'),
            url: theUrl,
            data: frm.serialize(), // for example sampleSubtypeId=5&sampleTypeId=2&name=input1 
            success: function (response) {
                //document.getElementById("viewerFrame").innerHTML = htmlResponse;//works just as well 
                $('#viewerFrame').html(response);
            }
        });
    	return false; // avoid to execute the actual submit of the form 
    }	
    //look at this, it's good: http://blog.springsource.org/2010/01/25/ajax-simplifications-in-spring-3-0/
    function postFormWithAjaxJson(formObjectId, theUrl){
    	var frm = $("#" + formObjectId);//must do this; cannot simply use formObject; don't know why not
    	//////alert("frm id = " + frm.attr('id'));
    	//////alert("the url = " + theUrl);
    	//var array = jQuery(frm).serializeArray();
    	//////var sf = frm.serialize();//http://stackoverflow.com/questions/1184624/convert-form-data-to-js-object-with-jquery
    	//////alert("serialize sf =: " + sf);
    	//sf = sf.replace(/"/g, '\"');         // be sure all " are escaped
    	//sf = '{"' + sf.replace(/&/g, '","'); // start "object", replace tupel delimiter &
    	//sf = sf.replace(/=/g, '":"') + '"}'; // replace equal sign, add closing "object"

    	//////alert("ABCD");
    	//var serializedObject = frm.serializeObject(); //such as [{ name: "a", value: "1"},{name: "b", value: "2"}] 
    	
    	
    	var serializedObject = {};
    	var a = frm.serializeArray(); //http://api.jquery.com/serializeArray/ 
    	$.each(a, function() {
    		if (serializedObject[this.name]) {
    			if (!serializedObject[this.name].push) {
    				serializedObject[this.name] = [serializedObject[this.name]];
    			}
    			serializedObject[this.name].push(this.value || '');
    		} else {
    			serializedObject[this.name] = this.value || '';
    		}
    	});
    	
    	
    	//////alert("defgzyx");
    	//////alert("serialziedObject is: " + serializedObject);
    	var jsonData = JSON.stringify(serializedObject);// such as {"sampleSubtypeId":"5","sampleTypeId":"2"} 
    	//alert("serialziedObject as JSON: " + jsonData);
    	//return false;
    	$.ajax({
            type: frm.attr('method'),
            url: theUrl,
            //dataType : 'json', //coming back from server
            data: jsonData,
            contentType: 'application/json',
            //data: $.toJSON(frm),
            success: function (response) {
                //document.getElementById("viewerFrame").innerHTML = htmlResponse;//works just as well 
                $('#viewerFrame').html(response);
            },
    		error: function (response) {
            	//document.getElementById("viewerFrame").innerHTML = htmlResponse;//works just as well 
            	$('#viewerFrame').html("Unexpected Failure");
        }
        });
    	return false;
    }	
   
    $.fn.serializeObject = function() { //http://stackoverflow.com/questions/1184624/convert-form-data-to-js-object-with-jquery   
    	var o = {};
    	var a = this.serializeArray(); //http://api.jquery.com/serializeArray/ 
    	$.each(a, function() {
    		if (o[this.name]) {
    			if (!o[this.name].push) {
    				o[this.name] = [o[this.name]];
    			}
    			o[this.name].push(this.value || '');
    		} else {
    			o[this.name] = this.value || '';
    		}
    	});
    	return o;
    };
    
    /*
   function serializeObject() { //http://stackoverflow.com/questions/1184624/convert-form-data-to-js-object-with-jquery  
    	var o = {};
    	var a = this.serializeArray(); //http://api.jquery.com/serializeArray/ 
    	$.each(a, function() {
    		if (o[this.name]) {
    			if (!o[this.name].push) {
    				o[this.name] = [o[this.name]];
    			}
    			o[this.name].push(this.value || '');
    		} else {
    			o[this.name] = this.value || '';
    		}
    	});
    	return o;
    };
    */
</script>
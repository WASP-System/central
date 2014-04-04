<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="http://malsup.github.com/jquery.form.js"></script>

<script type="text/javascript">

$(document).ready(function() {
	
	$("html, body").animate({ scrollTop: 0 }, "fast");

	$(function() {
		    $( "#tabs" ).tabs();
	}); 
	
	//http://api.jqueryui.com/dialog/
	$("#modalDialog").dialog({
        autoOpen: false,
        modal: true,
        height: 800,
        width: 800,
        position: { my: "right top", at: "right top", of: window } //http://docs.jquery.com/UI/API/1.8/Position
    });
	$("#modalessDialog").dialog({
        autoOpen: false,
        modal: false,
        height: 800,
        width: 650,
        position: { my: "right top", at: "right top", of: window } 
    }); 
	
	$("#smallModalessDialog").dialog({
        autoOpen: false,
        modal: false,
        height: 500,
        width: 500,
        position: { my: "right top", at: "right top", of: window } 
    });
	
 
	
	//incase the url is ..../homepage.do#tabs-2 (meaning go directly to the second tab) 
	//get the index from URL hash and use it to select the correct tab: from http://stackoverflow.com/questions/2554951/jquery-ui-tabs-how-do-i-navigate-directly-to-a-tab-from-another-page 
	
	

	
});

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
function showPopupWindow(url){//not currently used, but could be useful in future	
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

//used to send contents of form via GET request and display response in modelessdialog 
function sendFormViaGetAndShowModlessDialog(formObjectId, theUrl){	
	$("html, body").animate({ scrollTop: 0 }, "fast");

	var frm = $("#" + formObjectId);
	showModalessDialog(theUrl+"?"+frm.serialize());//frm.serialize() returns, for example, sampleSubtypeId=5&sampleTypeId=2&name=input1 
	$("#modalessDialog").scrollTop("0");//bring dialog scrollbar to top of page; see http://stackoverflow.com/questions/10816279/how-to-get-jqueryui-dialog-scrolltop-to-scroll-dialog-content-to-top 
	$("#modalessDialog").dialog({        
        position: { my: "right top", at: "right top", of: $(document).scrollTop("0") } //of used to be of: window 
    });
	return false; 
}

//used for comments, viewerManager 
function postFormWithAjax(formObjectId, theUrl){
	var frm = $("#" + formObjectId);
	var selectedPanel = $('#tabs').find("[aria-expanded=true]");//the div for this selected tabs panel; http://stackoverflow.com/questions/1331335/how-to-get-the-selected-tab-panel-element-in-jquery-ui-tabs 
	$.ajax({
        type: frm.attr('method'),
        url: theUrl,
        data: frm.serialize(), // for example sampleSubtypeId=5&sampleTypeId=2&name=input1 
        success: function (response) {
        	selectedPanel.html(response);
        },
        error: function (response) {
        	selectedPanel.html("Unexpected Failure");
        }
    });
	return false; // avoid to execute the actual submit of the form 
}
    
//used for the samples and libraries; this way, the objects on the page are sent as json objects 
//look at this, it's good: http://blog.springsource.org/2010/01/25/ajax-simplifications-in-spring-3-0/
function postFormWithAjaxJson(formObjectId, theUrl){
	var frm = $("#" + formObjectId);  
	var selectedPanel = $('#tabs').find("[aria-expanded=true]");//the div for this selected tabs panel 
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
	var jsonData = JSON.stringify(serializedObject);// such as {"sampleSubtypeId":"5","sampleTypeId":"2"} 
	
	$.ajax({
        type: frm.attr('method'),
        url: theUrl,
        //dataType : 'json', //dataType specifies the data coming back from server (in this case it's html, not json) 
        data: jsonData,
        contentType: 'application/json',
        success: function (response) {
        	selectedPanel.html(response);
        },
		error: function (response) {
			selectedPanel.html("Unexpected Failure");
    	}
    });
	return false;
}	
  
//used here to upload files via ajax 
//from http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/ 
//uses plugin jquery.form.js; see script tag at top of this page:  see http://malsup.com/jquery/form/
function uploadJqueryForm(formObjectId){	
	$("#wait_dialog-modal").dialog("open");
	var frm = $("#" + formObjectId);
   	var selectedPanel = $('#tabs').find("[aria-expanded=true]");//the div for this selected tabs panel 
	frm.ajaxForm({ 
    success:function(data) { 
    	$("#wait_dialog-modal").dialog("close");
    	selectedPanel.html(data);
     },
     dataType:"text" 
   }).submit();       
	return false;//I added this last line. Not sure if it's required 
}
     
//used on viewerManager to remove a viewer via an ajax GET, using only parth variable parameters in the rest part of URL 
function doGetWithAjax(theUrl) {
   	var selectedPanel = $('#tabs').find("[aria-expanded=true]");//the div for this selected tabs panel 
	$.ajax({
        type: "GET",
        url: theUrl,
        success: function (response) {
        	selectedPanel.html(response);
        },
        error: function (response) {
        	selectedPanel.html("Unexpected Failure");
        }
    });
	return false; // avoid 
}
//used to navigate  
function loadNewPageWithAjax(theUrl){
	doGetWithAjax(theUrl);
	return false;
}

// the next two functions are no longer needed; can simply load the script from the ajax-called page using a script tag and storing/calling the .js page in /wasp/scripts/js           
function loadNewScriptAjax(scriptLocation){  //no longer used    	
    var selectedPanel = $('#tabs').find("[aria-expanded=true]");//the div for this selected tabs panel       	 
    $.ajax({
        type: "GET",
        url: scriptLocation,
        dataType: "script",
        success: function (response) {
          	selectedPanel.html(response);
        },
        error: function (response) {
           	selectedPanel.html("Unexpected Failure with loadNewScriptAjax()");
        }
     });
    return false;  
}
function loadNewPageThenLoadJSWithAjax(theUrl, scriptLocation) { //no longer used   
   	var selectedPanel = $('#tabs').find("[aria-expanded=true]");//the div for this selected tabs panel 
  	 $.ajax({
        type: "GET",
        url: theUrl,
        success: function (response) {
            selectedPanel.html(response);//the webpage  
            loadNewScriptAjax(scriptLocation);//the js script 
        },
        error: function (response) {
        	selectedPanel.html(response);//the webpage
        }
    });    	 
  	return false; // avoid 
}
function robtest_autocomplete(obj) {
	var jQueryObject = $(obj);
    var availableTags = [
      "ActionScript",
      "AppleScript",
      "Asp",
      "BASIC",
      "C",
      "C++",
      "Clojure",
      "COBOL",
      "ColdFusion",
      "Erlang",
      "Fortran",
      "Groovy",
      "Haskell",
      "Java",
      "JavaScript",
      "Lisp",
      "Perl",
      "PHP",
      "Python",
      "Ruby",
      "Scala",
      "Scheme"
    ];
    jQueryObject.autocomplete({
      source: availableTags
    });
}
//Next call is No Longer Used: I previously used this to load up something on page load 
//window.onload = function (){ 
	//loadNewPageWithAjax('<wasp:relativeUrl value="job/${job.getId()}/basic.do" />'); 
	//to avoid hardcoding, use below code (from http://stackoverflow.com/questions/906486/how-can-i-programmatically-invoke-an-onclick-event-from-a-anchor-tag-while-kee)
//	var firstTabAnchor = document.getElementById('first_tab').getElementsByTagName('a')[0];
//	if (typeof firstTabAnchor.onclick == "function") {
//		firstTabAnchor.onclick.apply(firstTabAnchor);
//	} 
//} 
    
</script>
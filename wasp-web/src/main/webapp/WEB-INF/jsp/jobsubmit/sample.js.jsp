<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="http://malsup.github.com/jquery.form.js"></script>
<script type="text/javascript">
function verifyRemove(targetURL){
	if (confirm('<fmt:message key="jobDraft.remove_confirm.label" />')){
		window.location = targetURL;
	}
}

function addFileUploadRow(){
	var table = document.getElementById("fileUploadTbl");
	var rowNum = table.rows.length;
	var newRow = table.insertRow(rowNum);
	
	var td1 = newRow.insertCell(0);
	td1.className = "DataTD value-centered";
	var fileInput = document.createElement("input");
	fileInput.type="file";
	fileInput.name="file_upload";
	fileInput.onchange=function() { addFileUploadRow(); };
	td1.appendChild(fileInput);
	
	var td2 = newRow.insertCell(1);
	td2.className = "DataTD value-centered";
	var textInput = document.createElement("input");
	textInput.type="text";
	textInput.name="file_description";
	textInput.className = "FormElement ui-widget-content ui-corner-all";
	td2.appendChild(textInput);
	
	var td3 = newRow.insertCell(2);
	td3.className = "DataTD value-centered";
	td3.innerHTML = '<fmt:message key="jobDraft.file_not_applicable.label"/>';
}

function validate(){
	var thisForm = document.getElementById("submissionForm");
	var fileUploads = thisForm.file_upload;
	var fileDescriptions = thisForm.file_description;
	for(var i = 0; i < fileUploads.length; i++){
		if(fileUploads[i].value != "" && fileDescriptions[i].value == ""){ 
			alert("<fmt:message key="jobDraft.file_description_missing.label"/>");
			fileDescriptions[i].focus();
			return false;
		} 
	}
	return true;
}

function submitForm(){
	var result = validate();
	if (result == false)
		return false;
	//used here to upload files via ajax 
	//from http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/ 
	//uses plugin jquery.form.js; see script tag at top of this page:  see http://malsup.com/jquery/form/
	$("#wait_dialog-modal").dialog("open");
	$("#submissionForm").ajaxForm({ 
		success: function (data, textStatus, jqXHR) {
        	// data represents the entire html from returned page. We just need to replace the head and body sections of the current page.
        	// strip loaded scripts from head and use LazyLoad to load them. This means we can take advantage of the callback on script loading
        	// to call readyFn(). Left as was, the scripts load but readyFn() may execute before this is finished and try and access not-yet available
        	// methods. Sadly document-ready code ONLY works when the DOM is created, not when modified by ajax so this is our best solution.
        	var newHeadCode = data.replace(/^[\s\S]*[\s>;]<head>([\s\S]*)<\/head>[\s\S]*$/, "$1");
        	var regEx = /^[\s\S]*?<script[^>]*?src=["']([^"']*?)["'][^>]*?><\/script>[\s\S]*?$/gm;
        	var scripts = [];
        	var match;
        	while (match = regEx.exec(newHeadCode)){
        		scripts.push(match[1]);
        	}
        	var strippedNewHeadCode = newHeadCode.replace(/<script[^>]*?src=["']([^"']*?)["'][^>]*?><\/script>/g, "");
        	$('head').html(newHeadCode);
        	LazyLoad.js(scripts, function(){
        		$('body').html(data.replace(/^[\s\S]*<body>([\s\S]*)<\/body>[\s\S]*$/, "$1"));
        		readyFn(); // run document ready code when all dependencies loaded
        		$("#wait_dialog-modal").dialog("close");
        	});
     	},
     dataType:"text" 
   }).submit();
	return true;
}
</script>
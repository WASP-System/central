<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 
 

<script>

$(document).ready(function() {
    $("#accordion").accordion({
		collapsible: true,
		autoHeight: false,
		navigation: true,
		active: false,
		header: 'h4'			
	});

  	$("#requested_coverage_show_hide_button").click(function() {
  	  $("#user_requested_coverage_data").fadeToggle("slow", "linear");
  	  if($(this).prop("value")=="<fmt:message key="listJobSamples.showUserRequestedCoverage.label" />"){$(this).prop("value", "<fmt:message key="listJobSamples.hideUserRequestedCoverage.label" />");}
  	  else{$(this).prop("value", "<fmt:message key="listJobSamples.showUserRequestedCoverage.label" />");}
  	}); 
  	
  	$("#jobFiles_show_hide_button").click(function() {
    	  $("#jobFiles").fadeToggle("slow", "linear");
    	  if($(this).prop("value")=="<fmt:message key="listJobSamples.showJobFiles.label" />"){$(this).prop("value", "<fmt:message key="listJobSamples.hideJobFiles.label" />");}
    	  else{$(this).prop("value", "<fmt:message key="listJobSamples.showJobFiles.label" />");}
    	}); 
  	
  	$("#modalessDialog").dialog({
        autoOpen: false,
        modal: false,
        height: 800,
        width: 800,
        position: { my: "right top", at: "right top", of: window }//http://docs.jquery.com/UI/API/1.8/Position
    }); 
});

function showModalessDialog(url){
	$("#modalessIframeId").attr("src", url);
	$( "#modalessDialog" ).dialog("open");
}

function toggleDisplayOfAddLibraryForm(instruction, idCounter){
	
	var formName = "addLibraryForm_" + idCounter;
	var theForm = document.getElementById(formName);
	var showButtonName = "showButton_" + idCounter;
	var showButton = document.getElementById(showButtonName);
	
	if(instruction == 'show'){
		showButton.style.display = 'none';
		theForm.style.display = 'block';
	}
	else if(instruction == 'cancel'){
		showButton.style.display = 'block';
		theForm.style.display = 'none';
		//reset the select and edit box in case they were altered
		var selectboxName = "cellsampleid_" + idCounter;
		var selectbox = document.getElementById(selectboxName);
		selectbox.selectedIndex = 0;
		var editboxPicoName = "libConcInCellPicoM_" + idCounter;
		var editboxPico = document.getElementById(editboxPicoName);
		editboxPico.value = "";
	}	
}
function validate_email(){

	var emailFormat=new RegExp("^\\s*[\\w\\-\\+_]+(\\.[\\w\\-\\+_]+)*\\@[\\w\\-\\+_]+\\.[\\w\\-\\+_]+(\\.[\\w\\-\\+_]+)*\\s*$");
	var email = $('#newViewerEmailAddress').val();//may not always be defined 
	if(typeof(email) !== 'undefined' && email != null){
		//could have subsituted if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0) with if(submitter && submitter.length>0)  
		if(email.length==0){
			alert("<fmt:message key="listJobSamples.missingEmailAddress.label" />");
			$('#newViewerEmailAddress').focus();
			return false;//do not perform search 
		}
		else if(email.length>0 && !emailFormat.test(email)){
			alert("<fmt:message key="listJobSamples.invalidFormatEmailAddress.label" />");
			$('#newViewerEmailAddress').focus();
			return false;//do not perform search 
		}
	}
	return true;
};
function validate(obj){
	if(obj.value==0){
		alert("<fmt:message key="listJobSamples.youMustSelectCell_alert.label" />");
		return false;
	}
}
function validate_submit(obj){
	if(obj.cellsampleid.value==0){
		alert("<fmt:message key="listJobSamples.youMustSelectCell_alert.label" />");
		obj.cellsampleid.focus();
		return false;
	}
	if(obj.libConcInCellPicoM.value =="" || obj.libConcInCellPicoM.value.replace(/^\s+|\s+$/g, '') ==""){ //trim from both ends
		alert("<fmt:message key="listJobSamples.valFinalConc_alert.label" />");
		obj.libConcInCellPicoM.value = "";
		obj.libConcInCellPicoM.focus();
		return false;
	}
	
	var regExpr = new RegExp("^[0-9]+\.?[0-9]*$");//modified from http://stackoverflow.com/questions/469357/html-text-input-allow-only-numeric-input (modified example 14)
    if (!regExpr.test(obj.libConcInCellPicoM.value)) {
    	alert("<fmt:message key="listJobSamples.numValFinalConc_alert.label" />");
		obj.libConcInCellPicoM.focus();
		return false;
    }
	
	return true;
}
function validateFileUploadForm(thisForm){
	var fileUpload = thisForm.file_upload;
	var fileDescription = thisForm.file_description;
	if(fileUpload.value == ""){
		alert("<fmt:message key="listJobSamples.file_empty.label"/>");
		fileUpload.focus();
		return false;
	}
	else if(fileDescription.value == ""){ //fileUpload.value != "" 
		alert("<fmt:message key="listJobSamples.file_description_missing.label"/>");
		fileDescription.focus();
		return false;
	}
	var submitButton = thisForm.file_upload_submit_button;
	submitButton.disabled=true;
	submitButton.value = "<fmt:message key="listJobSamples.processing.label" />";
	
	return true;
}
</script>

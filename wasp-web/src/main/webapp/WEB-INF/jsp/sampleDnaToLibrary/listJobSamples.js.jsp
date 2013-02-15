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
  	
  	//$(".wasptooltip a[title]").tooltip({ position: "top right"});
});


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
		var selectboxName = "lanesampleid_" + idCounter;
		var selectbox = document.getElementById(selectboxName);
		selectbox.selectedIndex = 0;
		var editboxPicoName = "libConcInLanePicoM_" + idCounter;
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
	if(obj.lanesampleid.value==0){
		alert("<fmt:message key="listJobSamples.youMustSelectCell_alert.label" />");
		obj.lanesampleid.focus();
		return false;
	}
	if(obj.libConcInLanePicoM.value =="" || obj.libConcInLanePicoM.value.replace(/^\s+|\s+$/g, '') ==""){ //trim from both ends
		alert("<fmt:message key="listJobSamples.valFinalConc_alert.label" />");
		obj.libConcInLanePicoM.value = "";
		obj.libConcInLanePicoM.focus();
		return false;
	}
	
	var regExpr = new RegExp("^[0-9]+\.?[0-9]*$");//modified from http://stackoverflow.com/questions/469357/html-text-input-allow-only-numeric-input (modified example 14)
    if (!regExpr.test(obj.libConcInLanePicoM.value)) {
    	alert("<fmt:message key="listJobSamples.numValFinalConc_alert.label" />");
		obj.libConcInLanePicoM.focus();
		return false;
    }
	
	return true;
}
</script>

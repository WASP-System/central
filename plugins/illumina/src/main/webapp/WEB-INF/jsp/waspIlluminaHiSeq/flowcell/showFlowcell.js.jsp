<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<link rel="stylesheet" type="text/css" media="screen" href="<wasp:relativeUrl value='css/reset.css' />" />
<link rel="stylesheet" type="text/css" media="screen" href="<wasp:relativeUrl value='css/base.css' />" />
<link rel="stylesheet" type="text/css" media="screen" href="<wasp:relativeUrl value='css/jquery/jquery-ui.css' />" />
<link rel="stylesheet" type="text/css" media="screen" href="<wasp:relativeUrl value='css/jquery/ui.jqgrid.css' />" />
	
<script type="text/javascript" src="<wasp:relativeUrl value='scripts/js/date_validate_modified.js' />"></script> 
<script>

//think this section enclosed in  document.ready(function()  
//is no longer used; rob 11/30/12; replaced by a new page for creating platform unit )  
//however the code below document ready IS STILL NEEDED 
$(document).ready(function(){
	$( "#runStartDate" ).datepicker({ dateFormat: "yy/mm/dd" });
	$("#resourceId").change(function(){
		var id = $(this).val();
		//alert('jquery test; resourceId = ' + resourceId);		
		//$(".readType").html('<option value="">---SELECT A READ TYPE---</option><option value="50">---50---</option>');
		if(id != ''){
			$.ajax({
				type: "POST",
				url: "<wasp:relativeUrl value='waspIlluminaHiSeq/flowcell/ajaxReadType.do' />",
				data: "resourceId=" + id,
				cache: false,
				success: function(response){
					var partsArray = response.split('****');
					$("#readType").html(partsArray[0]);
					$("#readLength").html(partsArray[1]);
					$("#submitButtonCreateNewRun").attr("disabled", false);
				} 
			}); 
		}
		if(id == ''){
			$("#readType").html("");
			$("#readLength").html("");
			$("#submitButtonCreateNewRun").attr("disabled", true);			
		}
	});
});

function toggleDisplayOfCreateNewRunForm(action){
	//alert("in new div; action = " + action);
	var newCreateRunButtonDiv = document.getElementById("newCreateRunButtonDiv");
	var newCreateRunFormDiv = document.getElementById("newCreateRunFormDiv");
	if(action=="create"){
		newCreateRunButtonDiv.style.display = 'none';
		newCreateRunFormDiv.style.display = 'block';
	}
	if(action=="cancel"){
		newCreateRunButtonDiv.style.display = 'block';
		newCreateRunFormDiv.style.display = 'none';
		var runName = document.getElementById("runName");
		var resourceId = document.getElementById("resourceId");
		var readLength = document.getElementById("readLength");
		var readType = document.getElementById("readType");
		var technicianId = document.getElementById("technicianId");
		var runStartDate = document.getElementById("runStartDate");
		var submitButtonCreateNewRun = document.getElementById("submitButtonCreateNewRun");
		runName.value="";
		resourceId.value="";
		readLength.innerHTML="";
		readType.innerHTML="";
		technicianId.value="";
		runStartDate.value="";
		
		submitButtonCreateNewRun.disabled = "true";
	}
}
function validateCreateNewRunForm(){
	var runName = document.getElementById("runName");
	var resourceId = document.getElementById("resourceId");
	var readLength = document.getElementById("readLength");
	var readType = document.getElementById("readType");
	var technicianId = document.getElementById("technicianId");
	var runStartDate = document.getElementById("runStartDate");
	if(runName.value=="" || runName.value.replace(/^\s+|\s+$/g, '') ==""){
		alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseProvideValidName_alert.label"/>");
		runName.focus();
		return false;
	}
	if(resourceId.value==""){
		alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseSelectMachine_alert.label"/>");
		resourceId.focus();
		return false;
	}
	if(readLength.value==""){
		alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseSelectReadLength_alert.label"/>");
		readLength.focus();
		return false;
	}
	if(readType.value==""){
		alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseSelectReadType_alert.label"/>");
		readType.focus();
		return false;
	}
	if(technicianId.value==""){
		alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseSelectTechnician_alert.label"/>");
		technicianId.focus();
		return false;
	}
	if(runStartDate.value == "" || runStartDate.value.replace(/^\s+|\s+$/g, '') ==""){
		alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseProvideStartDate_alert.label"/>");
		runStartDate.focus();
		return false;
	}
    if (runStartDate.value != "" && !isDate(runStartDate.value)) {
    	alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseProvideValidStartDate_alert.label"/>");
    	runStartDate.focus();
		return false;
    }
	if(!confirm("<fmt:message key="showPlatformUnit.submit_alert.label"/>")){
		return false;
	}
	
	document.getElementById("newRunForm").submit();
}
function getResourceMeta(obj){//no longer used
	if(obj.value != ""){
		//alert("inside getResourceMeta()");
		document.getElementById("testdiv3").style.display = 'block';
	}
}
function toggleDisplayAddNewControlForm(action, counter){
	//alert("inside toggleTest; counter = " + counter);
	var newControlAnchorName = "newControlAnchor_" + counter;
	var newControlAnchor = document.getElementById(newControlAnchorName);
	var newControlFormDivName = "idNewControlFormDiv_" + counter;
	var newControlFormDiv = document.getElementById(newControlFormDivName);
	if(action == "show_form"){
		newControlAnchor.style.display = 'none';
		newControlFormDiv.style.display = 'block';
	}
	else if(action == "cancel_form"){
		newControlAnchor.style.display = 'block';
		newControlFormDiv.style.display = 'none';
		var newControlIdName = "newControlId_" + counter;
		var newControlId = document.getElementById(newControlIdName);
		var newControlConcInCellPicoMName = "newControlConcInCellPicoM_" + counter;
		var newControlConcInCellPicoM = document.getElementById(newControlConcInCellPicoMName);
		newControlId.value="";
		newControlConcInCellPicoM.value="";
	}
}
function validateAddNewControlToLaneForm(counter){
	//alert("testing alert in validate add new control. counter=" + counter);
	var newControlIdName = "newControlId_" + counter;
	var newControlId = document.getElementById(newControlIdName);
	var newControlConcInCellPicoMName = "newControlConcInCellPicoM_" + counter;
	var newControlConcInCellPicoM = document.getElementById(newControlConcInCellPicoMName);
	if(newControlId.value==""){
		alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseSelectControl_alert.label"/>");
		newControlId.focus();
		return false;
	}
	var trimmed = newControlConcInCellPicoM.value.replace(/^\s+|\s+$/g, '') ;//trim from both ends
	if(trimmed == ""){//must trim before testing
		alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseProvideControlConc_alert.label"/>");
		newControlConcInCellPicoM.value = "";
		newControlConcInCellPicoM.focus();
		return false;
	}
	else{
		//alert("OK to submit, but not doing it");
		var theFormName = "addNewControlToLaneForm_" + counter;
		var theForm = document.getElementById(theFormName);
		theForm.submit();
	}
}

function toggleDisplayOfUpdateForm(instruction, idCounter){
	//alert ("more testing; id = " + idCounter);
	var formDivName = "updatePicoFormDiv_" + idCounter;
	var formDiv = document.getElementById(formDivName);
	var editAnchorDivName = "editAnchorDiv_" + idCounter;
	var editAnchorDiv = document.getElementById(editAnchorDivName);
	
	if(instruction == 'show'){
		editAnchorDiv.style.display = 'none';
		formDiv.style.display = 'block';
	}
	else if(instruction == 'cancel'){
		editAnchorDiv.style.display = 'block';
		formDiv.style.display = 'none';
		var textInputName = "libConcInCellPicoM_" + idCounter;
		var textInputObject = document.getElementById(textInputName);
		textInputObject.value="";
	}	
}
function validateUpdateForm(idCounter){
	//alert("validateUpdate; id = " + idCounter);
	var textInputName = "libConcInCellPicoM_" + idCounter;
	var textInputObject = document.getElementById(textInputName);
	var trimmed = textInputObject.value.replace(/^\s+|\s+$/g, '') ;//trim from both ends
	if(trimmed == ""){
		alert("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_pleaseProvideValue_alert.label"/>");
		textInputObject.value = "";
		textInputObject.focus();
		return false;
	}
	else{
		//alert("submit");
		var theFormName = "updatePicoForm_" + idCounter;
		var theForm = document.getElementById(theFormName);
		theForm.submit();
	}
	
}
function confirmRemove(libraryType){
	var returnValue = false;
	if(libraryType=="controlLibrary"){
		returnValue = confirm("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_removeControlFromThisCell.label"/>");
	}
	else{
		returnValue = confirm("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_removeLibFromCell_alert.label"/>");
	}
	if(returnValue==true){
		$("#wait_dialog-modal").dialog("open");
	}
	return returnValue;
}
</script>
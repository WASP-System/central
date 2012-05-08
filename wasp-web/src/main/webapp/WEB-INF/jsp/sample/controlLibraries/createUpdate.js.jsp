<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/reset.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/ui.jqgrid.css" />
	
<script src="/wasp/scripts/jquery/jquery-1.7.1.js" type="text/javascript"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="/wasp/scripts/js/date_validate_modified.js"></script> 
<script>

$(document).ready(function(){
	
	$("#adaptorsetId").change(function(){
		var id = $(this).val();
		//alert('jquery test; adaptorsetId = ' + id);		
		//$("#adaptorId").html('<option value="">---SELECT AN ADAPTOR---</option><option value="1">Index 1 (AAGCTT)</option>');
		
		if(id != ''){
			$.ajax({
				type: "POST",
				url: "/wasp/sample/ajaxAdaptorsByAdaptorId.do",
				data: "adaptorsetId=" + id,
				cache: false,
				success: function(response){
					
					$("#adaptorId").html(response);
					//$("#readLength").html(partsArray[1]);
					$("#submitButton").attr("disabled", false);
				} 
			}); 
		}
		if(id == ''){
			$("#adaptorId").html("");
			$("#submitButton").attr("disabled", true);			
		}
		
	});
});
function validate(){
	
	//alert("test");
	document.form.name.value = document.form.name.value.trim();
	if(document.form.name.value == ""){
		alert("Please provide a name for this control");
		document.form.name.focus();
		return false;
	}
	if(document.form.adaptorsetId.value ==""){
		alert("Please select an Adaptor Set");
		document.form.adaptorsetId.focus();
		return false;
	}
	if(document.form.adaptorId.value ==""){
		alert("Please select an Adaptor");
		document.form.adaptorId.focus();
		return false;
	}
	if(document.form.active[0].value=="" && document.form.active[1].value==""){
		alert("Please select Active or Inactive for this control");
		return false;
	}
	document.form.submit();
	return true;
	
}
/*
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
		alert("Please provide a valid name for this run");
		runName.focus();
		return false;
	}
	if(resourceId.value==""){
		alert("Please select a machine");
		resourceId.focus();
		return false;
	}
	if(readLength.value==""){
		alert("Please select a read length");
		readLength.focus();
		return false;
	}
	if(readType.value==""){
		alert("Please select a read type");
		readType.focus();
		return false;
	}
	if(technicianId.value==""){
		alert("Please select a Technician");
		technicianId.focus();
		return false;
	}
	if(runStartDate.value == "" || runStartDate.value.replace(/^\s+|\s+$/g, '') ==""){
		alert("Please select/provide a Start Date");
		runStartDate.focus();
		return false;
	}
    if (runStartDate.value != "" && !isDate(runStartDate.value)) {
    	alert("Please provide a Start Date in the proper format");
    	runStartDate.focus();
		return false;
    }
	if(!confirm("Submit?")){
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
		var newControlConcInLanePicoMName = "newControlConcInLanePicoM_" + counter;
		var newControlConcInLanePicoM = document.getElementById(newControlConcInLanePicoMName);
		newControlId.value="";
		newControlConcInLanePicoM.value="";
	}
}
function validateAddNewControlToLaneForm(counter){
	//alert("testing alert in validate add new control. counter=" + counter);
	var newControlIdName = "newControlId_" + counter;
	var newControlId = document.getElementById(newControlIdName);
	var newControlConcInLanePicoMName = "newControlConcInLanePicoM_" + counter;
	var newControlConcInLanePicoM = document.getElementById(newControlConcInLanePicoMName);
	if(newControlId.value==""){
		alert("Please select a control");
		newControlId.focus();
		return false;
	}
	var trimmed = newControlConcInLanePicoM.value.replace(/^\s+|\s+$/g, '') ;//trim from both ends
	if(trimmed == ""){//must trim before testing
		alert("Please provide a final concentration for this control");
		newControlConcInLanePicoM.value = "";
		newControlConcInLanePicoM.focus();
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
		var textInputName = "libConcInLanePicoM_" + idCounter;
		var textInputObject = document.getElementById(textInputName);
		textInputObject.value="";
	}	
}
function validateUpdateForm(idCounter){
	//alert("validateUpdate; id = " + idCounter);
	var textInputName = "libConcInLanePicoM_" + idCounter;
	var textInputObject = document.getElementById(textInputName);
	var trimmed = textInputObject.value.replace(/^\s+|\s+$/g, '') ;//trim from both ends
	if(trimmed == ""){
		alert("Please provide a value");
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
*/
</script>
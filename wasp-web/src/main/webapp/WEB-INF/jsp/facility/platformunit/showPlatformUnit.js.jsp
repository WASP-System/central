<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/reset.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/ui.jqgrid.css" />
	
<script type="text/javascript" src="/wasp/scripts/js/date_validate_modified.js"></script> 
<script>

//think this section enclosed in  document.ready(function()  
//is no longer used; rob 11/30/12; replaced by a new page for creating platform unit )  
//however the code below document ready IS STILL NEEDED 
$(document).ready(function(){
	$( "#runStartDate" ).datepicker();
	$("#resourceId").change(function(){
		var id = $(this).val();
		//alert('jquery test; resourceId = ' + resourceId);		
		//$(".readType").html('<option value="">---SELECT A READ TYPE---</option><option value="50">---50---</option>');
		if(id != ''){
			$.ajax({
				type: "POST",
				url: "/wasp/facility/platformunit/ajaxReadType.do",
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
		alert("<fmt:message key="showPlatformUnit.pleaseProvideValidName_alert.label"/>");
		runName.focus();
		return false;
	}
	if(resourceId.value==""){
		alert("<fmt:message key="showPlatformUnit.pleaseSelectMachine_alert.label"/>");
		resourceId.focus();
		return false;
	}
	if(readLength.value==""){
		alert("<fmt:message key="showPlatformUnit.pleaseSelectReadLength_alert.label"/>");
		readLength.focus();
		return false;
	}
	if(readType.value==""){
		alert("<fmt:message key="showPlatformUnit.pleaseSelectReadType_alert.label"/>");
		readType.focus();
		return false;
	}
	if(technicianId.value==""){
		alert("<fmt:message key="showPlatformUnit.pleaseSelectTechnician_alert.label"/>");
		technicianId.focus();
		return false;
	}
	if(runStartDate.value == "" || runStartDate.value.replace(/^\s+|\s+$/g, '') ==""){
		alert("<fmt:message key="showPlatformUnit.pleaseProvideStartDate_alert.label"/>");
		runStartDate.focus();
		return false;
	}
    if (runStartDate.value != "" && !isDate(runStartDate.value)) {
    	alert("<fmt:message key="showPlatformUnit.pleaseProvideValidStartDate_alert.label"/>");
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
		alert("<fmt:message key="showPlatformUnit.pleaseSelectControl_alert.label"/>");
		newControlId.focus();
		return false;
	}
	var trimmed = newControlConcInLanePicoM.value.replace(/^\s+|\s+$/g, '') ;//trim from both ends
	if(trimmed == ""){//must trim before testing
		alert("<fmt:message key="showPlatformUnit.pleaseProvideControlConc_alert.label"/>");
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
		alert("<fmt:message key="showPlatformUnit.pleaseProvideValue_alert.label"/>");
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

</script>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script src="/wasp/scripts/jquery/jquery-1.7.1.js" type="text/javascript"></script>
<script>
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
	}	
}
function validate(obj){
	if(obj.value==0){
		alert("You must select a Lane");
		return false;
	}
}
function validate_submit(obj){
	if(obj.lanesampleid.value==0){
		alert("You must select a Lane");
		obj.lanesampleid.focus();
		return false;
	}
	if(obj.libConcInLanePicoM.value ==""){
		alert("Please provide a value for Final Concentration In Lane (pM)");
		obj.libConcInLanePicoM.focus();
		return false;
	}
	
	var regExpr = new RegExp("^[0-9]+\.?[0-9]*$");//modified from http://stackoverflow.com/questions/469357/html-text-input-allow-only-numeric-input (modified example 14)
    if (!regExpr.test(obj.libConcInLanePicoM.value)) {
    	alert("Please provide a numeric value for Final Concentration In Lane (pM)");
		obj.libConcInLanePicoM.focus();
		return false;
    }
	
	return true;
}
</script>

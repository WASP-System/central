<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery.cookie.js"></script>

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

</script>
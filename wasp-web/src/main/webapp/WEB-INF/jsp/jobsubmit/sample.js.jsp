<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18/external/jquery.cookie.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script>

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
	var fileInput = document.createElement("input");
	fileInput.type="file";
	fileInput.name="file_upload";
	fileInput.onchange=function() { addFileUploadRow(); };
	td1.appendChild(fileInput);
	
	var td2 = newRow.insertCell(1);
	var textInput = document.createElement("input");
	textInput.type="text";
	textInput.name="file_description";
	td2.appendChild(textInput);
}

</script>
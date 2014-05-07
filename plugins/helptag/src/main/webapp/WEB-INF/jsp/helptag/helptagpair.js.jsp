<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script>

function toggleCol(controlIndex, testCount, controlCount){
	var button = document.getElementById("col_"+controlIndex+"_select_all");
	
	for (var i=1; i<=testCount; i++){
		var checkbox = document.getElementById("rowcolumn_"+i+"_"+controlIndex);
		if (checkbox != undefined){
			if (button.value == '<fmt:message key="helptag.selectall.label"/>'){	
				checkbox.checked = true;
			} else{
				checkbox.checked = false;
			}
		}
	}
	if (button.value == '<fmt:message key="helptag.selectall.label"/>'){	
		button.value = '<fmt:message key="helptag.deselectall.label"/>';
		
		for (var i=1; i<=testCount; i++){
			var stdbox = document.getElementById("rowcolumn_"+i+"_stdref");
			if (stdbox!=undefined)
				stdbox.checked = false;
		}
	} else {
		button.value = '<fmt:message key="helptag.selectall.label"/>';
		setAllStdRef(testCount, controlCount)
	}
	return true;
}

function toggleRow(testIndex, controlCount){
	var button = document.getElementById("row_"+testIndex+"_select_all");
	
	for (var i=1; i<=controlCount; i++){
		var checkbox = document.getElementById("rowcolumn_"+testIndex+"_"+i);
		if (checkbox != undefined){
			if (button.value == '<fmt:message key="helptag.selectall.label"/>'){	
				checkbox.checked = true;
			} else{
				checkbox.checked = false;
			}
		}
	}
	var stdbox = document.getElementById("rowcolumn_"+testIndex+"_stdref");
	if (button.value == '<fmt:message key="helptag.selectall.label"/>'){	
		button.value = '<fmt:message key="helptag.deselectall.label"/>';

		if (stdbox!=undefined)
			stdbox.checked = false;
	} else {
		button.value = '<fmt:message key="helptag.selectall.label"/>';

		if (stdbox!=undefined)
			stdbox.checked = true;
	}
	return true;
}

function stdChanged(testIndex, controlCount){
	var stdbox = document.getElementById("rowcolumn_"+testIndex+"_stdref");
	if (stdbox==undefined)
		return false;
	
	if (stdbox.checked){
		for (var i=1; i<=controlCount; i++){
			var checkbox = document.getElementById("rowcolumn_"+testIndex+"_"+i);
			if (checkbox != undefined){
					checkbox.checked = false;
			}
		}
	}
	return true;
}

function nonstdChanged(testIndex, controlCount){
	var stdbox = document.getElementById("rowcolumn_"+testIndex+"_stdref");
	if (stdbox==undefined)
		return false;
	
	for (var i=1; i<=controlCount; i++){
		var checkbox = document.getElementById("rowcolumn_"+testIndex+"_"+i);
		if (checkbox != undefined && checkbox.checked){
				stdbox.checked = false;
				return true;
		}
	}
	stdbox.checked = true;
	return true;
}

function setAllStdRef(testCount, controlCount){
	for (var i=1; i<=testCount; i++){
		var stdbox = document.getElementById("rowcolumn_"+i+"_stdref");
		if (stdbox!=undefined){
			stdbox.checked = true;
			for (var ii=1; ii<=controlCount; ii++){
				var checkbox = document.getElementById("rowcolumn_"+i+"_"+ii);
				if (checkbox != undefined && checkbox.checked){
						stdbox.checked = false;
						break;
				}
			}
		}
	}
}

</script>

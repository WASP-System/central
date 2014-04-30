<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>

function toggleCol(controlIndex, testCount){
	var button = document.getElementById("col_"+controlIndex+"_select_all");
	
	for (var i=1; i<=testCount; i++){
		var checkbox = document.getElementById("rowcolumn_"+i+"_"+controlIndex);
		if (checkbox != undefined && checkbox.disabled == false){
			if (button.value == "<fmt:message key='variantcalling.selectAll.label' />"){	
				checkbox.checked = true;
			} else{
				checkbox.checked = false;
			}
		}
	}
	if (button.value == "<fmt:message key='variantcalling.selectAll.label' />"){	
		button.value = "<fmt:message key='variantcalling.deselectAll.label' />";
	} else {
		button.value = "<fmt:message key='variantcalling.selectAll.label' />";
	}
	return true;
}

function toggleRow(testIndex, controlCount){
	var button = document.getElementById("row_"+testIndex+"_select_all");
	
	for (var i=1; i<=controlCount; i++){
		var checkbox = document.getElementById("rowcolumn_"+testIndex+"_"+i);
		if (checkbox != undefined && checkbox.disabled == false){
			if (button.value == "<fmt:message key='variantcalling.selectAll.label' />"){	
				checkbox.checked = true;
			} else{
				checkbox.checked = false;
			}
		}
	}
	if (button.value == "<fmt:message key='variantcalling.selectAll.label' />"){	
		button.value = "<fmt:message key='variantcalling.deselectAll.label' />";
	} else {
		button.value = "<fmt:message key='variantcalling.selectAll.label' />";
	}
	return true;
}

</script>

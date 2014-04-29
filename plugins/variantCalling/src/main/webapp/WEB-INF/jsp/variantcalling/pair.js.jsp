<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>

function toggleRow(controlIndex, testCount){
	var button = document.getElementById("row_"+controlIndex+"_select_all");
	
	for (var i=1; i<=testCount; i++){
		var checkbox = document.getElementById("rowcolumn_"+controlIndex+"_"+i);
		if (checkbox != undefined){
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

function toggleCol(testIndex, controlCount){
	var button = document.getElementById("col_"+testIndex+"_select_all");
	
	for (var i=1; i<=controlCount; i++){
		var checkbox = document.getElementById("rowcolumn_"+i+"_"+testIndex);
		if (checkbox != undefined){
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

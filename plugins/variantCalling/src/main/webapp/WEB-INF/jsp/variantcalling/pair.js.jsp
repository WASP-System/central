
<script>

function toggleRow(controlIndex, testCount){
	var button = document.getElementById("row_"+controlIndex+"_select_all");
	
	for (var i=1; i<=testCount; i++){
		var checkbox = document.getElementById("rowcolumn_"+controlIndex+"_"+i);
		if (checkbox != undefined){
			if (button.value == "select all"){	
				checkbox.checked = true;
			} else{
				checkbox.checked = false;
			}
		}
	}
	if (button.value == "select all"){	
		button.value = "deselect all";
	} else {
		button.value = "select all";
	}
	return true;
}

function toggleCol(testIndex, controlCount){
	var button = document.getElementById("col_"+testIndex+"_select_all");
	
	for (var i=1; i<=controlCount; i++){
		var checkbox = document.getElementById("rowcolumn_"+i+"_"+testIndex);
		if (checkbox != undefined){
			if (button.value == "select all"){	
				checkbox.checked = true;
			} else{
				checkbox.checked = false;
			}
		}
	}
	if (button.value == "select all"){	
		button.value = "deselect all";
	} else {
		button.value = "select all";
	}
	return true;
}

</script>

<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">

(function($){ 
	
	$(document).ready(function(){
		$(".chosen-select").chosen();
		
		//on load 
		$('#manyToOne_select_ip').prop('disabled', true); 
		$('#manyToOne_select_input').prop('disabled', true); 
		$("#manyToOne_tr").hide(); 
		
		$("#toggleAnchorId").bind('click', function(){
			
			if($("#oneToMany_tr").is(':visible')){ 
				$('#oneToMany_select_ip').prop('disabled', true); 
				$('#oneToMany_select_input').prop('disabled', true);
				$("#oneToMany_tr").hide();
				
				$('#manyToOne_select_ip').prop('disabled', false); 
				$('#manyToOne_select_input').prop('disabled', false); 
				$("#manyToOne_tr").show();
			}
			else{
				$('#oneToMany_select_ip').prop('disabled', false); 
				$('#oneToMany_select_input').prop('disabled', false);
				$("#oneToMany_tr").show();
				
				$('#manyToOne_select_ip').prop('disabled', true); 
				$('#manyToOne_select_input').prop('disabled', true); 
				$("#manyToOne_tr").hide();				
			}
		});		
	});
	
	
})(jQuery);

function confirmPairing(){	
	var selects = document.getElementsByTagName('select');
	var num_ip_samples = selects.length;
	var num_paired_ip_samples = 0;
	for(var i = 0; i < num_ip_samples; i++) {
		if(selects[i].value > 0){
			num_paired_ip_samples++;
		}
	}
	if(num_paired_ip_samples < num_ip_samples){//not all were paired 
		//return confirm("Please confirm (by selecting OK) that you have paired " + num_paired_ip_samples + " of your " + num_ip_samples + " IP samples with a control/input"); 
		return confirm("<fmt:message key='chipseqpair.confirm_1.label' />" + " " + num_paired_ip_samples + " " + "<fmt:message key='chipseqpair.confirm_2.label' />" + " " + num_ip_samples + " " + "<fmt:message key='chipseqpair.confirm_3.label' />");
	}
	else{
		return true;
	}
}

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

<script>

$(document).ready(function() {	
	$("#libraryStrategyAnchor").click(function() {
	    $('#libraryStrategyTable').toggle();
	});
});

function checkParent(obj, parentId){
	if (obj.checked == true && document.getElementById(parentId).checked == false){
		document.getElementById(parentId).checked = true;
	}
}

</script>
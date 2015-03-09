<script>

$(document).ready(function() {	
	$("#libraryStrategyAnchor").click(function() {
	    $('#libraryStrategyTable').toggle();
	});
	$("#checkAllStrategies").click(function() {
	    $('[id^=strategyKey]').prop( "checked", true );
	});
	$("#uncheckAllStrategies").click(function() {
	    $('[id^=strategyKey]').prop( "checked", false );
	});
	$("#checkAllAdaptorsets").click(function() {
	    $('[id^=adaptorset]').prop( "checked", true );
	});
	$("#uncheckAllAdaptorsets").click(function() {
	    $('[id^=adaptorset]').prop( "checked", false );
	});
});

function checkParent(obj, parentId){
	if (obj.checked == true && document.getElementById(parentId).checked == false){
		document.getElementById(parentId).checked = true;
	}
}

</script>
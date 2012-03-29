<script>
	function checkParent(obj, parentId){
		if (obj.checked == true && document.getElementById(parentId).checked == false){
			document.getElementById(parentId).checked = true;
		}
	}
</script>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#instituteOther").keyup(function(){getInstituteNames();});
     });
      
     function getInstituteNames(){        
     	if( $("#instituteOther").val().length == 1){
	        	$.getJSON("<wasp:relativeUrl value='autocomplete/getInstitutesForDisplay.do' />", { instituteNameFragment: $("#instituteOther").val() }, function(data) { $("input#instituteOther").autocomplete(data);} );
     	}
	 }
 </script>

<script type="text/javascript">
	<!--
	function selectChange(){
		if (document.f.instituteSelect.options[document.f.instituteSelect.selectedIndex].value == "other"){
			document.f.instituteOther.readOnly = false;
			document.f.instituteOther.focus();
		}
		else{
			document.f.instituteOther.value = "";
			document.f.instituteOther.readOnly = true;
		}
		return true;
	}
	//->
</script> 
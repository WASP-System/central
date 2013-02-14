 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#institution").keyup(function(){getInstituteNames();});
     });
      
     function getInstituteNames(){        
     	if( $("#institution").val().length == 1){
	        	$.getJSON("/wasp/autocomplete/getInstitutesForDisplay.do", { instituteNameFragment: $("#institution").val() }, function(data) { $("input#institution").autocomplete(data);} );
     	}
	 }
 </script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>

<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 


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
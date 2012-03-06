<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 


 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#billing_institution").keyup(function(){getInstituteNames();});
     });
      
     function getInstituteNames(){        
     	if( $("#billing_institution").val().length == 1){
	        	$.getJSON("/wasp/autocomplete/getInstitutesForDisplay.do", { instituteNameFragment: $("#billing_institution").val() }, function(data) { $("input#billing_institution").autocomplete(data);} );
     	}
	 }
 </script>
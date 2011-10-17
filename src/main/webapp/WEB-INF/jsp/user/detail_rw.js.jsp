<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.6.2.js"></script>
<link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.16.custom.min.js"></script> 


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
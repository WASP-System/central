 <%@ include file="/WEB-INF/jsp/taglib.jsp"%>
 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#institution").keyup(function(){getInstituteNames();});
     });
      
     function getInstituteNames(){        
     	if( $("#institution").val().length == 1){
	        	$.getJSON("<wasp:relativeUrl value='autocomplete/getInstitutesForDisplay.do' />", { instituteNameFragment: $("#institution").val() }, function(data) { $("input#institution").autocomplete(data);} );
     	}
	 }
 </script>
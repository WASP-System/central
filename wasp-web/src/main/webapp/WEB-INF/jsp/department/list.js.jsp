<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">
     $(document).ready(function() {            
	       $("#adminName").keyup(function(){getAuthNames();});
     });
      
     function getAuthNames(){        
     	if( $("#adminName").val().length == 1){
	        	$.getJSON("<c:url value='autocomplete/getUserNamesAndLoginForDisplay.do' />", { adminNameFragment: $("#adminName").val() }, function(data) { $("input#adminName").autocomplete(data);} );
     	}
	    }
 </script>
 


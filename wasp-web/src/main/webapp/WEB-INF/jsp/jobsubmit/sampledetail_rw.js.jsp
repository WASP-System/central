<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18/external/jquery.cookie.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script>

<script>
	/* enable dynamic adaptorset /adaptor selection  */
	$('#genericLibrary\\.adaptorset').change(function () {      
			var selectedAdaptorSet=$('#genericLibrary\\.adaptorset option:selected').val();
			
			if (!selectedAdaptorSet) {
				$('#genericLibrary\\.adaptor').children().remove().end();                 		            
				return;
			}
			                		      				    
			 var options = '<option value=""><fmt:message key="wasp.default_select.label" /></option>';
			 
			 $.getJSON("/wasp/jobsubmit/adaptorsByAdaptorsetId.do",{adaptorsetId: selectedAdaptorSet, ajax: 'true'}, function(data, textStatus, jqXHR){
			
				 $.each(data, function (index, name) {                				    
					 options += '<option value="' + index + '">' + name + '</option>';
				  });
				                 			  
			   })
			   
			   $('#genericLibrary\\.adaptor').html(options);
			                    	                		    
	    });

</script>
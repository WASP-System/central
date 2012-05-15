<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>

<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script>

<script type="text/javascript">
	// enable dynamic adaptorset /adaptor selection 
	$(document).ready(function() {
		var selectedAdaptorSet=$('select#adaptorset option:selected').val();
		if (!selectedAdaptorSet) {
			$('tr#row_adaptor').hide();
		}
	});
	
    $(function(){
		$('select#adaptorset').change(function() {      
				var selectedAdaptorSet=$('select#adaptorset option:selected').val();
				var options = '<option value=""><fmt:message key="wasp.default_select.label" /></option>';
				var url = "/wasp/jobsubmit/adaptorsByAdaptorsetId.do?adaptorsetId=" + selectedAdaptorSet;
				if (!selectedAdaptorSet) {
					$('tr#row_adaptor').hide();
					$('select#adaptor').children().remove().end();                 		            
					return;
				}
				 
				$.ajax({
					async: false,
				   	url: url,
				    dataType: "json",
				    success: function(data) {
				    	$.each(data, function (index, name) {         				    
							options += '\n<option value="' + index + '">' + name + '</option>';
						});
				    }
				});   
				$('select#adaptor').html(options);    
				$('tr#row_adaptor').show();
		   	});
	    });
</script>
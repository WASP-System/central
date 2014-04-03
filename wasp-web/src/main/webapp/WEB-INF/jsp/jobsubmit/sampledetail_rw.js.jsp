<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
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
				var options = '';
				var url = "<wasp:relativeUrl value='jobsubmit/adaptorsByAdaptorsetId.do?adaptorsetId=' />" + selectedAdaptorSet;
				var adaptorCount = 0;
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
							options += '<option value="' + index + '">' + name + '</option>\n';
							adaptorCount++;
						});
				    }
				});   
				if (adaptorCount > 1){
					options = '<option value=""><fmt:message key="wasp.default_select.label" /></option>' + options;
				}
				$('select#adaptor').html(options);
				$('tr#row_adaptor').show();
		   	});
	    });
</script>
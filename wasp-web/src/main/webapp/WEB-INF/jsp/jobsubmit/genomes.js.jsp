<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">
	// enable dynamic genome / build selection 
	$(document).ready(function() {
		var selectedGenomePathStr=$('select#genomeSelect option:selected').val();
		if (!selectedGenomePathStr) {
			$('tr#buildSelectTr').hide();
		}
	});
	
    $(function(){
		$('select#genomeSelect').change(function() {    
				var selectedGenomePathStr=$('select#genomeSelect option:selected').val();
				var options = '';
				var url = "/wasp/jobsubmit/" + selectedGenomePathStr + "/getBuilds.do";
				var itemCount = 0;
				if (!selectedGenomePathStr) {
					$('tr#buildSelectTr').hide();
					$('select#buildSelect').children().remove().end();                 		            
					return;
				}
				 
				$.ajax({
					async: false,
				   	url: url,
				    dataType: "json",
				    success: function(data) {
				    	$.each(data, function (index, name) {         				    
							options += '<option value="' + index + '">' + name + '</option>\n';
							itemCount++;
						});
				    }
				});   
				if (itemCount > 1){
					options = '<option value=""><fmt:message key="wasp.default_select.label" /></option>' + options;
				}
				$('select#buildSelect').html(options);
				$('tr#buildSelectTr').show();
		   	});
	    });
</script>
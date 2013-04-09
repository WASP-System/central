<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">
	// enable dynamic genome / build selection 
	
	var buildDescriptions = new Array();
	
	$(function(){
		
		<c:forEach items="${sampleDraftsByOrganism.keySet()}" var="organism" varStatus="statusOrg">
			var selectedGenomePathStr=$('select#genomeSelect_${organism.getNcbiID()} option:selected').val();
			if (!selectedGenomePathStr) {
				$('tr#buildSelectTr_${organism.getNcbiID()}').hide();
			}
			
			var selectedBuildPathStr=$('select#buildSelect_${organism.getNcbiID()} option:selected').val();
			if (!selectedBuildPathStr) {
				$('tr#buildDescriptionTr_${organism.getNcbiID()}').hide();
			}
			
			$('select#genomeSelect_${organism.getNcbiID()}').change(function() {    
					var selectedGenomePathStr=$('select#genomeSelect_${organism.getNcbiID()} option:selected').val();
					var options = '';
					var url = "/wasp/jobsubmit/" + selectedGenomePathStr + "/getBuilds.do";
					var itemCount = 0;
					if (!selectedGenomePathStr) {
						$('tr#buildSelectTr_${organism.getNcbiID()}').hide();
						$('tr#buildDescriptionTr_${organism.getNcbiID()}').hide();
						$('select#buildSelect_${organism.getNcbiID()}').children().remove().end();   
						buildDescriptions = new Array();
						return;
					}
					 
					$.ajax({
						async: false,
					   	url: url,
					    dataType: "json",
					    success: function(data) {
					    	$.each(data, function (displayValue, description) {      
					    		selected = '';
					    		if (displayValue.indexOf(" (default)") != -1){
					    			buildDescription = description;
					    			selected = " selected='selected'";
					    			name = displayValue.replace(" (default)", "");
					    		} else {
					    			name = displayValue;
					    		}
					    		buildDescriptions[name] = description;
								options += '<option value="' + name + '" ' + selected + '>' + displayValue + '</option>\n';
								itemCount++;
							});
					    }
					});   
					if (itemCount > 1){
						options = '<option value=""><fmt:message key="wasp.default_select.label" /></option>' + options;
					}
					$('select#buildSelect_${organism.getNcbiID()}').html(options);
					$('tr#buildSelectTr_${organism.getNcbiID()}').show();
					$('td#buildDescriptionTd_${organism.getNcbiID()}').html(buildDescription);
					$('tr#buildDescriptionTr_${organism.getNcbiID()}').show();
			   	});
			
			$('select#buildSelect_${organism.getNcbiID()}').change(function() {
				var selectedBuildPathStr=$('select#buildSelect_${organism.getNcbiID()} option:selected').val();
				if (selectedBuildPathStr) {
					buildName = selectedBuildPathStr.substr( selectedBuildPathStr.lastIndexOf("/") + 1 );
					$('td#buildDescriptionTd_${organism.getNcbiID()}').html(buildDescriptions[buildName]);
					$('tr#buildDescriptionTr_${organism.getNcbiID()}').show();
				} else {
					$('tr#buildDescriptionTr_${organism.getNcbiID()}').hide();
				}
			});
	
		</c:forEach>
	 });
</script>
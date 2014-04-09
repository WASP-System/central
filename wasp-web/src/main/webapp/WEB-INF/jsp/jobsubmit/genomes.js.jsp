<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">
	// enable dynamic genome / build selection 
	
	var buildDescriptions = new Array();
	var currentlySelectedBuildByGenomeName = new Array();
	
	<c:forEach items="${sampleDraftsByOrganism.keySet()}" var="organism" varStatus="statusOrg">
		<c:set var="build" value="${currentBuildByOrganism.get(organism)}" />
		<c:if test="${build != null}">
			currentlySelectedBuildByGenomeName['<c:out value="${build.getGenome().getName()}" />'] = "<c:out value="${build.getName()}" />";
		</c:if>
	</c:forEach>
	
	$(function(){
		
		function updateGenomeSelect(id){
			var selectedGenomePathStr=$('select#genomeSelect_' + id + ' option:selected').val();
			var options = '';
			var url = "<wasp:relativeUrl value='jobsubmit/' />" + selectedGenomePathStr + "/getBuilds.do";
			var genomeName = selectedGenomePathStr.substring( selectedGenomePathStr.lastIndexOf("/") + 1 );
			var itemCount = 0;
			var currentlySelectedBuild= "";
			if (!selectedGenomePathStr) {
				$('tr#buildSelectTr_' + id).hide();
				$('tr#buildDescriptionTr_' + id).hide();
				$('select#buildSelect_' + id).children().remove().end();   
				$('select#buildSelect_' + id).html('<option value=""><fmt:message key="wasp.default_select.label" /></option>');
				buildDescriptions = new Array();
				return;
			}
			 
			$.ajax({
				async: false,
			   	url: url,
			    dataType: "json",
			    success: function(data) {
			    	$.each(data, function (displayValue, description) {    
			    		isDefaultBuild = false;
			    		selected = '';
			    		buildName = '';
			    		if (displayValue.indexOf(" (default)") != -1){
			    			isDefaultBuild = true;
			    			buildName = displayValue.replace(" (default)", "");
			    		} else {
			    			buildName = displayValue;
			    		}
			    		if (currentlySelectedBuild.length == 0){
			    			if (currentlySelectedBuildByGenomeName[genomeName] != null && currentlySelectedBuildByGenomeName[genomeName] != "")
			    				currentlySelectedBuild = currentlySelectedBuildByGenomeName[genomeName];
			    			else if (isDefaultBuild == true)
			    				currentlySelectedBuild = buildName;
			    		}
			    		if (buildName == currentlySelectedBuild)
			    			selected = " selected='selected'";

			    		buildDescriptions[buildName] = description;
						options += '<option value="' + buildName + '" ' + selected + '>' + displayValue + '</option>\n';
						itemCount++;
					});
			    }
			});   
			if (itemCount > 1){
				options = '<option value=""><fmt:message key="wasp.default_select.label" /></option>' + options;
			}
			$('select#buildSelect_' + id).html(options);
			$('tr#buildSelectTr_' + id).show();
			$('td#buildDescriptionTd_' + id).html(buildDescriptions[currentlySelectedBuild]);
			$('tr#buildDescriptionTr_' + id).show();
		}
		
		function updateBuildSelect(id){
			var selectedBuildPathStr=$('select#buildSelect_' + id + ' option:selected').val();
			if (selectedBuildPathStr) {
				buildName = selectedBuildPathStr.substr( selectedBuildPathStr.lastIndexOf("/") + 1 );
				$('td#buildDescriptionTd_' + id).html(buildDescriptions[buildName]);
				$('tr#buildDescriptionTr_' + id).show();
			} else {
				$('tr#buildDescriptionTr_' + id).hide();
			}
		}
		
		<c:forEach items="${sampleDraftsByOrganism.keySet()}" var="organism" varStatus="statusOrg">
			var selectedGenomePathStr=$('select#genomeSelect_${organism.getNcbiID()} option:selected').val();
			if (!selectedGenomePathStr) {
				$('tr#buildSelectTr_${organism.getNcbiID()}').hide();
			} else {
				updateGenomeSelect(<c:out value="${organism.getNcbiID()}" />);
			}

			$('select#genomeSelect_${organism.getNcbiID()}').change(function() { 
				updateGenomeSelect(<c:out value="${organism.getNcbiID()}" />);
			});
					
			
			$('select#buildSelect_${organism.getNcbiID()}').change(function() {
				updateBuildSelect(<c:out value="${organism.getNcbiID()}" />)
			});
	
		</c:forEach>
	 });
</script>
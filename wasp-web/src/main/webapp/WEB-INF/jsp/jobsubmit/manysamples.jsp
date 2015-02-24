<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<script type="text/javascript" src="<wasp:relativeUrl value='scripts/jquery/jquery.table.addrow.js' />"></script>
<script type="text/javascript">
	(function($){ 
		$(document).ready(function(){
			
			<c:if test="${empty edit}">
			
				//needed for the addrow and delete row functionality:
				$(".addRow").btnAddRow();
				$(".delRow").btnDelRow();
							
				$( "#addMoreRowsAnchor" ).click(function() {				
					var numRowsToAdd = $("#addMoreRows").val();
					
					for(var i = 0; i < numRowsToAdd; i++){
						$(".addRow").trigger("click");
					}
					$("#addMoreRows").val("");
				});
			
			</c:if>
			
			//if these are libraries, the next two lines are absolutely needed to dynamically set this colspan
			var tableColspan = $("#singleCellInVeryLastTableRow").attr('colspan');//last row 
			$("#singleCellInOptionalAdaptorsetTableRow").attr('colspan',tableColspan);//first row 

			var selectedAdaptorSet=$('select#adaptorset option:selected').val();
			if (!selectedAdaptorSet) {
				$('tr#row_adaptor').hide();
			}

			//if libraries, then absolutely necessary to update these hidden attributes just before submitting form 
			$( "#submit" ).click(function() {
				var selectedAdaptorSet=$('select#adaptorset option:selected').val();
				$('input[type=hidden]#adaptorset').val(selectedAdaptorSet);
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
						$('input[type=hidden]#adaptorset').val(selectedAdaptorSet);
				   	});
			    });
		});

	})(jQuery);
</script>

<br />

<h1><fmt:message key="jobDraft.create.label"/> -- <c:out value="${heading}"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<div id="container_div_for_adding_rows" >
	<h2><fmt:message key="jobsubmitManySamples.sampleType.label" />: <c:out value="${sampleType.name}" /></h2>
	<h2><fmt:message key="jobsubmitManySamples.sampleSubType.label" />: <c:out value="${sampleSubtype.name}" /></h2>
	<c:if test="${empty edit}">
		<a style="font-weight:bold" href="javascript:void(0);"  id="addMoreRowsAnchor"><fmt:message key="jobsubmitManySamples.click.label" /></a> <fmt:message key="jobsubmitManySamples.toAdd.label" /> <input type='text' style="text-align:right;" size='3' maxlength='3' name='addMoreRows' id='addMoreRows' > <fmt:message key="jobsubmitManySamples.moreRows.label" />
		<br />
	</c:if>
	<br />
	
	<c:set var="colspan" value = '0' scope="request"/>
	
	<form action="<wasp:relativeUrl value="jobsubmit/manysamples/add/${jobDraft.getId()}/${sampleSubtype.getId()}.do" />" method="POST" >
	
		<span style="font-size:x-small"><fmt:message key="jobsubmitManySamples.clickFirst.label" /></span>
		
		<div class="fixed-width_scrollable">
			<table class="data" style="margin: 0px 0px" >
			
			<c:if test="${not empty adaptorsets}">
				<tr class="FormData">
					<td id="singleCellInOptionalAdaptorsetTableRow" colspan="1" <%--this colspan will be dynamically changed; see javascript--%>align='center' style="background-color:#FAF2D6; color:red; font-size:large; font-weight:bold; padding:15px 15px 15px 15px;" nowrap><fmt:message key="jobsubmitManySamples.selectAnAdaptorSet.label" /> 
						<select class="FormElement ui-widget-content ui-corner-all" name="theSelectedAdaptorset" id="adaptorset" class="FormElement ui-widget-content ui-corner-all">
							<option value=''><fmt:message key="wasp.default_select.label"/></option>
							<c:forEach items="${adaptorsets}" var="adaptorset">
								<option value='${adaptorset.getId()}'  <c:if test="${theSelectedAdaptorset == adaptorset.getId()}">selected</c:if>      ><c:out value="${adaptorset.getName()}"></c:out></option>	
							</c:forEach>
						</select>
						<fmt:message key="jobsubmitManySamples.selectAnAdaptorSet.label" />				
					</td>
				</tr>
			</c:if>
			
			
			<c:forEach items="${sampleDraftList}" var="sampleDraft" varStatus="sampleDraftStatus">
				<c:if test="${sampleDraftStatus.first}">
					<tr class="FormData">
						<c:if test="${fn:length(errorList)>0}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold; color:red" nowrap><fmt:message key="jobsubmitManySamples.errors.label" /></td>
							<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
						</c:if>
						<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="jobsubmitManySamples.sampleName.label" /><span style="color:red">*</span></td>
						<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
					    <c:set var="_area" value = "sampleDraft" scope="request"/>
						<c:set var="_metaList" value = "${sampleDraft.getSampleDraftMeta()}" scope="request" />		
					    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
							<c:if test="${_meta.property.formVisibility != 'ignore'}">
								<c:set var="_myArea">${_area}.</c:set>
								<c:set var="_myCtxArea">${_area}.</c:set>
								<c:if test="${_metaArea != null}">		
									<c:set var="_myCtxArea">${_metaArea}.</c:set>
								</c:if>
								<c:set var="labelKey" value="${_meta.property.label}" />
								<c:if test="${fn:contains(labelKey,'Average')}">
									<c:set var="labelKey" value="${fn:replace(labelKey, 'Average', 'Aver.')}" />
								</c:if>
								<c:if test="${fn:contains(labelKey,'Fragmentation')}">
									<c:set var="labelKey" value="${fn:replace(labelKey, 'Fragmentation', 'Frag.')}" />
								</c:if>
								<c:if test="${fn:contains(labelKey,'Concentration')}">
									<c:set var="labelKey" value="${fn:replace(labelKey, 'Concentration', 'Conc.')}" />
								</c:if>
								<c:if test="${fn:contains(labelKey,'Volume')}">
									<c:set var="labelKey" value="${fn:replace(labelKey, 'Volume', 'Vol.')}" />
								</c:if>
								<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
								<c:if test="${id!='adaptorset' }">
									<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap>${labelKey}
										<c:if test="${not empty _meta.property.constraint}">
											<span style="color:red">*</span>
										</c:if>
										<c:if test="${not empty _meta.property.tooltip}">
											<wasp:tooltip value="${_meta.property.tooltip}" />
										</c:if>	
										<%-- <br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; var idRE = /^<c:out value="${id}" />/; var dates=[]; var els=document.getElementsByTagName("*"); for (var i=0; i < els.length; i++){ if ( idRE.test(els[i].id) ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } ' >first &rarr; all</a>--%>		
										<br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; var id = "<c:out value="${id}" />"; var dates=[]; var els=document.getElementsByTagName("*"); for (var i=0; i < els.length; i++){ if ( id==els[i].id ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } ' ><fmt:message key="jobsubmitManySamples.firstOthers.label" /></a>		
									</td>
								<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
								</c:if>
							</c:if>
						</c:forEach>
						<c:if test="${empty edit}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="jobsubmitManySamples.action.label" /></td>
						</c:if>
						<c:if test="${edit=='true'}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap>
								<fmt:message key="jobsubmitManySamples.deleteRow.label" />
								<br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; var id = "deleteRow"; var dates=[]; var els=document.getElementsByTagName("*"); for (var i=0; i < els.length; i++){ if ( id==els[i].id ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } ' ><fmt:message key="jobsubmitManySamples.firstOthers.label" /></a>			
							</td>
						</c:if>
						<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
					</tr>
				</c:if>
				
				<tr>
					<c:if test="${fn:length(errorList)>0}">
						<c:choose>
							<c:when test="${empty errorList.get(sampleDraftStatus.index)}">
								<td >&nbsp;</td>
							</c:when>
							<c:otherwise>
								<td id="errorMessageThatShouldNotBeCopied" align='center' style="background-color:red;" nowrap><wasp:tooltip value="${errorList.get(sampleDraftStatus.index)}" /></td>
							</c:otherwise>
						</c:choose>
					</c:if>
					<td>					
						<input type='hidden' name="sampleId" id="" value='${sampleDraft.getId()}'/>
						<input type="text" class="FormElement ui-widget-content ui-corner-all"   name='sampleName' id='sampleName' value="${sampleDraft.getName()}">
					</td>
					<c:set var="_area" value = "sampleDraft" scope="request"/>
					<c:set var="_metaList" value = "${sampleDraft.getSampleDraftMeta()}" scope="request" />		
					<c:forEach items="${_metaList}" var="_meta" varStatus="status">
						<c:if test="${_meta.property.formVisibility != 'ignore'}">
							<c:set var="_myArea">${_area}.</c:set>
							<c:set var="_myCtxArea">${_area}.</c:set>
							<c:if test="${_metaArea != null}">		
								<c:set var="_myCtxArea">${_metaArea}.</c:set>
							</c:if>
							<c:set var="labelKey" value="${_meta.property.label}" />
							<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
							<c:if test="${ id!='adaptorset' }">
							<td align='center' class="DataTD">
								<c:choose>
									<c:when test="${not empty _meta.property.control}">
								    <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
								    <wasp:metaSelect control="${_meta.property.control}"/>
						       			<select class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id}" class="FormElement ui-widget-content ui-corner-all">
											<c:if test= "${fn:length(selectItems) > 1 && _meta.property.formVisibility != 'immutable'}">
												<option value=''><fmt:message key="wasp.default_select.label"/></option>
											</c:if>
											<c:set var="useDefault" value="0" />
											<c:if test="${not empty _meta.property.defaultVal}">
												<c:set var="useDefault" value="1" />
												<c:forEach var="option" items="${selectItems}">
													<c:if test="${option[itemValue] == _meta.v}" >
														<c:set var="useDefault" value="0" />
													</c:if>
												</c:forEach>
											</c:if>
											<c:forEach var="option" items="${selectItems}">
												<c:if test="${fn:length(selectItems) == 1 || option[itemValue] == _meta.v || _meta.property.formVisibility != 'immutable' }">
													<option value="${option[itemValue]}"<c:if test="${fn:length(selectItems) == 1 || (not empty _meta.v && option[itemValue] == _meta.v) || (useDefault==1 && option[itemValue] == _meta.property.defaultVal)}"> selected</c:if>>
													<c:out value="${option[itemLabel]}"/></option>
												</c:if>
											</c:forEach>																									
										</select>									 						
									</c:when>
									<c:otherwise>
										<c:set var="inputVal" value="${_meta.v}" />
										<c:if test="${(empty inputVal) &&  (not empty _meta.property.defaultVal)}">
											<c:set var="inputVal" value="${_meta.property.defaultVal}" />
										</c:if>
										<input type="text" class="FormElement ui-widget-content ui-corner-all" size="10" maxlength="25" name="${_area}Meta_${_meta.k}" id="${id}"  value="${inputVal}" <c:if test= "${_meta.property.formVisibility == 'immutable'}"> readonly="readonly"</c:if> />
									</c:otherwise>
								</c:choose>
							</td>
							</c:if>
							<c:if test="${ id=='adaptorset' }">
								 <input type='hidden' name="${_area}Meta_${_meta.k}" id="${id}" value=''/>			 				
							</c:if>
						</c:if>			 
					</c:forEach>
					<td align='center'>
						<c:if test="${empty edit}">
							<input type="button" class="delRow" value="<fmt:message key="jobsubmitManySamples.deleteRow.label" />"/><%--this button IS controlled by the javascript that removes a new row; it's used for new samples or new libraries --%>
						</c:if>
						<c:if test="${edit=='true'}">
							<%-- <input type="button" class="delRow" value="Need To Do: Delete Row"/>--%><%--NOT controlled by that javascript that removes new rows --%>
							<select name="deleteRow" id="deleteRow" class="FormElement ui-widget-content ui-corner-all">
								<option value="no"  <c:if test="${deleteRowsList.get(sampleDraftStatus.index)=='no'}">selected</c:if>  ><fmt:message key="jobsubmitManySamples.NO.label" /></option>
								<option value="yes" <c:if test="${deleteRowsList.get(sampleDraftStatus.index)=='yes'}">selected</c:if>  ><fmt:message key="jobsubmitManySamples.YES.label" /></option>
							</select>
							<input type='hidden' name="edit" id="" value='${edit}'/><%--need this here if user hits save button and there are errors in the post; we need to inform that this is an edit --%>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			<%--do NOT remove this next line; it's colspan is needed to set colspan of first table row if there are libraries!! --%>
			<tr ><td id="singleCellInVeryLastTableRow" colspan="${colspan}" align="center"><c:if test="${empty edit}"><input style="width:300" type="button" class="addRow" value="<fmt:message key="jobsubmitManySamples.addAdditionalRow.label" />"/></c:if></td></tr>
			
			
			
			<%--THE VERY FINAL ROW, WHICH PUTS ANOTHER HEADER ROW AT BOTTOM OF TABLE --%>
			<c:forEach items="${sampleDraftList}" var="sampleDraft" varStatus="sampleDraftStatus">
				<c:if test="${sampleDraftStatus.first}">
					<tr class="FormData">
						<c:if test="${fn:length(errorList)>0}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold; color:red" nowrap><fmt:message key="jobsubmitManySamples.errors.label" /></td>
						</c:if>
						<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="jobsubmitManySamples.sampleName.label" /><span style="color:red">*</span></td>
						<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
					    <c:set var="_area" value = "sampleDraft" scope="request"/>
						<c:set var="_metaList" value = "${sampleDraft.getSampleDraftMeta()}" scope="request" />		
					    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
							<c:if test="${_meta.property.formVisibility != 'ignore'}">
								<c:set var="_myArea">${_area}.</c:set>
								<c:set var="_myCtxArea">${_area}.</c:set>
								<c:if test="${_metaArea != null}">		
									<c:set var="_myCtxArea">${_metaArea}.</c:set>
								</c:if>
								<c:set var="labelKey" value="${_meta.property.label}" />
								<c:if test="${fn:contains(labelKey,'Average')}">
									<c:set var="labelKey" value="${fn:replace(labelKey, 'Average', 'Aver.')}" />
								</c:if>
								<c:if test="${fn:contains(labelKey,'Fragmentation')}">
									<c:set var="labelKey" value="${fn:replace(labelKey, 'Fragmentation', 'Frag.')}" />
								</c:if>
								<c:if test="${fn:contains(labelKey,'Concentration')}">
									<c:set var="labelKey" value="${fn:replace(labelKey, 'Concentration', 'Conc.')}" />
								</c:if>
								<c:if test="${fn:contains(labelKey,'Volume')}">
									<c:set var="labelKey" value="${fn:replace(labelKey, 'Volume', 'Vol.')}" />
								</c:if>
								<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
								<c:if test="${id!='adaptorset' }">
									<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap>${labelKey}
										<c:if test="${not empty _meta.property.constraint}">
											<span style="color:red">*</span>
										</c:if>
										<c:if test="${not empty _meta.property.tooltip}">
											<wasp:tooltip value="${_meta.property.tooltip}" />
										</c:if>	
										<%-- <br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; var idRE = /^<c:out value="${id}" />/; var dates=[]; var els=document.getElementsByTagName("*"); for (var i=0; i < els.length; i++){ if ( idRE.test(els[i].id) ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } ' >first &rarr; all</a>--%>		
										<br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; var id = "<c:out value="${id}" />"; var dates=[]; var els=document.getElementsByTagName("*"); for (var i=0; i < els.length; i++){ if ( id==els[i].id ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } ' ><fmt:message key="jobsubmitManySamples.firstOthers.label" /></a>		
									</td>
								</c:if>
							</c:if>
						</c:forEach>
						<c:if test="${empty edit}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="jobsubmitManySamples.action.label" /></td>
						</c:if>
						<c:if test="${edit=='true'}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap>
								<fmt:message key="jobsubmitManySamples.deleteRow.label" />
								<br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; var id = "deleteRow"; var dates=[]; var els=document.getElementsByTagName("*"); for (var i=0; i < els.length; i++){ if ( id==els[i].id ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } ' ><fmt:message key="jobsubmitManySamples.firstOthers.label" /></a>			
							</td>
						</c:if>
						</tr>
					</c:if>
				</c:forEach>
				<%--END OF SECTION : THE VERY FINAL ROW, WHICH PUTS ANOTHER HEADER ROW AT BOTTOM OF TABLE --%>
			
				
			
			
			</table>
		</div>
		<input class="fm-button" type="button" value="<fmt:message key="jobDraft.terminateDiscard.label" />" onClick="if(confirm('<fmt:message key="jobDraft.terminateDiscardThisJobDraft.label" />')){window.location='<wasp:relativeUrl value="jobsubmit/terminateJobDraft/${jobDraft.jobDraftId}.do"/>'}" /> 
		<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
		<input type="submit" name="submit" value="<fmt:message key="jobDraft.cancel.label"/>" />
		<input type="submit" name="submit" id="submit" value="<fmt:message key="jobDraft.save.label"/>" />
	</form>
</div>


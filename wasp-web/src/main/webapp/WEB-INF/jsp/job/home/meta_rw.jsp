<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:forEach items="${_metaList}" var="_meta" varStatus="status">
	<c:if test="${_meta.property.formVisibility != 'ignore'}">
		<c:set var="_myArea">${_area}.</c:set>
		<c:set var="_myCtxArea">${_area}.</c:set>
		<c:if test="${_metaArea != null}">		
			<c:set var="_myCtxArea">${_metaArea}.</c:set>
		</c:if>
		<c:set var="labelKey" value="${_meta.property.label}" />
		<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />

		<tr class="FormData" id="row_${id}">
			<td class="CaptionTD">${labelKey}:</td>
			<td class="DataTD">
			<c:choose>
				<c:when test="${not empty _meta.property.control}">
				
			    <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
			    <wasp:metaSelect control="${_meta.property.control}"/>
			    
			   
			    	<select class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id}" class="FormElement ui-widget-content ui-corner-all" 

 						<c:if test='${id=="adaptorset"}'>
					    	onchange='			    	
					    		var selectedAdaptorSet = this.options[this.selectedIndex].value;			    	
						    	var options = "";
						    	var url = "<wasp:relativeUrl value="/jobsubmit/adaptorsByAdaptorsetId.do?adaptorsetId="/>"+selectedAdaptorSet;
								var adaptorCount = 0;
								if (!selectedAdaptorSet) {
									$("tr#row_adaptor").hide();
									$("select#adaptor").children().remove().end();                 		            
								return;
								}						
								$.ajax({
									async: false,
								   	url: url,
								    dataType: "json",
								    success: function(data) {
								    	$.each(data, function (index, name) {         				    
											options += "<option value=" + index + ">" + name + "</option>\n";
											adaptorCount++;
										});
								    }
								});   
								if (adaptorCount > 1){
									options = "<option value=><fmt:message key="wasp.default_select.label" /></option>\n" + options;
								}
								$("select#adaptor").html(options);
								$("tr#row_adaptor").show();						
				    		'
			    		</c:if>
			    	>
			    			    
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
					<input class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id}"  value="${inputVal}" <c:if test= "${_meta.property.formVisibility == 'immutable'}"> readonly="readonly"</c:if> />
				</c:otherwise>
			</c:choose>
			<c:if test="${not empty _meta.property.constraint}">
				<span class="requiredField">*</span>
			</c:if>
			<c:if test="${not empty _meta.property.tooltip}">
				<wasp:tooltip value="${_meta.property.tooltip}" />
			</c:if>
			</td>		
		
			<td class="CaptionTD error"><form:errors path="${_area}Meta[${status.index}].k" /> </td>					 
		</tr>	
	</c:if>			 
</c:forEach>

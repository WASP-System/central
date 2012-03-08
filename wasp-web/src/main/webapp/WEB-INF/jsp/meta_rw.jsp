<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:forEach items="${_metaList}" var="_meta" varStatus="status">
	<c:if test="${_meta.property.formVisibility != 'ignore'}">
		<c:set var="_myArea">${_area}.</c:set>
		<c:set var="_myCtxArea">${_area}.</c:set>
		<c:if test="${_metaArea != null}">		
			<c:set var="_myCtxArea">${_metaArea}.</c:set>
		</c:if>
		<c:set var="labelKey" value="${fn:replace(_meta.k, _myArea, _myCtxArea)}.label" />
		<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />

		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="${labelKey}"/>:</td>
			<td class="DataTD">
			<c:choose>
				<c:when test="${not empty _meta.property.control}">
				
			    <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
			    <wasp:metaSelect control="${_meta.property.control}"/>
       			<select class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id}" class="FormElement ui-widget-content ui-corner-all">
					<c:if test= "${fn:length(selectItems) > 1 && _meta.property.formVisibility != 'immutable'}">
						<option value=''><fmt:message key="wasp.default_select.label"/></option>
					</c:if>
					<c:forEach var="option" items="${selectItems}">
						<c:if test="${fn:length(selectItems) == 1 || option[itemValue] == _meta.v || _meta.property.formVisibility != 'immutable'}">
							<option value="${option[itemValue]}"<c:if test="${fn:length(selectItems) == 1 || option[itemValue] == _meta.v}"> selected</c:if>>
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
					<input class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id}"  value="${inputVal}" <c:if test= "${_meta.property.formVisibility == 'immutable'}"> readonly="readonly"</c:if> class="FormElement ui-widget-content ui-corner-all" />
				</c:otherwise>
			</c:choose>
			<c:if test="${not empty _meta.property.constraint}">
			<span class="requiredField">*</span>
			</c:if>
			</td>		
		
			<td class="CaptionTD error"><form:errors path="${_area}Meta[${status.index}].k" /> </td>					 
		</tr>	
	</c:if>			 
</c:forEach>

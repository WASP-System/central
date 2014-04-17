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
		<c:set var="eventHandlers"><c:if test="${not empty _meta.property.onChange}"> onchange="<c:out value="${_meta.property.onChange}" escapeXml="false" />"</c:if><c:if test="${not empty _meta.property.onClick}"> onclick="<c:out value="${_meta.property.onClick}" escapeXml="false" />"</c:if></c:set>

		<tr class="FormData" id="row_${id}">
			<td class="CaptionTD">${labelKey}:</td>
			<td class="DataTD">
			<c:choose>
				<c:when test="${not empty _meta.property.control}">
				
			    <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
			    <wasp:metaSelect control="${_meta.property.control}"/>
       			<select class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id}"<c:out value="${eventHandlers}" escapeXml="false" />>
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
					<input class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id}"  value="${inputVal}" <c:if test= "${_meta.property.formVisibility == 'immutable'}"> readonly="readonly"</c:if><c:out value="${eventHandlers}" escapeXml="false" /> />
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
		<c:if test="${not empty _meta.property.onRender}"><script><c:out value="${_meta.property.onRender}" escapeXml="false" /></script></c:if>
	</c:if>			 
</c:forEach>

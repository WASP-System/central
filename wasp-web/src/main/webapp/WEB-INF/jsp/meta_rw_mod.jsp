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

		<tr>
			<td class="label"><fmt:message key="${labelKey}"/></td>
			<td class="input">
			<c:choose>
			
				<c:when test="${fn:substringAfter(_meta.k,'.')=='adaptor'}">
				
				<input type='hidden' name="${_area}Meta_${_meta.k}" id="${id}" value="<c:out value='${selectedAdaptorset.IName}' />"/>
					<c:out value="${selectedAdaptorset.name}"></c:out>		
				
				</c:when>
				
				<c:when test="${fn:substringAfter(_meta.k,'.')=='adaptorindex'}">
				
					<select name="${_area}Meta_${_meta.k}" id="${id}" size="1">
						<option value="0">--SELECT ADAPTOR--
						<c:forEach items="${selectedAdaptorset.adaptor}" var="adaptor">
							<option value="<c:out value="${adaptor.adaptorId}" />" >Index: <c:out value="${adaptor.barcodenumber}" /> [<c:out value="${adaptor.barcodesequence}" />] 
						</c:forEach>
					</select>		
				
				</c:when>
				
			
				<c:when test="${not empty _meta.property.control}">
				
			    <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
			    <wasp:metaSelect control="${_meta.property.control}"/>
       
				<select name="${_area}Meta_${_meta.k}" id="${id}">
					<c:if test= "${_meta.property.formVisibility != 'immutable'}">
						<option value=''><fmt:message key="wasp.default_select.label"/></option>
					</c:if>
					<c:forEach var="option" items="${selectItems}">
						<c:if test="${option[itemValue] == _meta.v || _meta.property.formVisibility != 'immutable'}">
							<option value="${option[itemValue]}"<c:if test="${option[itemValue] == _meta.v}"> selected</c:if>>
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
					<input name="${_area}Meta_${_meta.k}" id="${id}"  value="${inputVal}" <c:if test= "${_meta.property.formVisibility == 'immutable'}"> readonly="readonly"</c:if>/>
				</c:otherwise>
			</c:choose>
			<c:if test="${not empty _meta.property.constraint}">
			<span class="requiredField">*</span>
			</c:if>
			</td>		
		
			<td class="error"><form:errors path="${_area}Meta[${status.index}].k" /> </td>					 
		</tr>	
	</c:if>			 
</c:forEach>

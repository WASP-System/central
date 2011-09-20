<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:forEach items="${_metaList}" var="_meta" varStatus="status">
	<c:if test="${_meta.property.formVisibility != 'ignore'}">
		<tr>
		  <c:set var="_myArea">${_area}.</c:set>
		  <c:set var="_myCtxArea">${_area}.</c:set>
		  <c:if test="${_metaArea != null}">  	
		    <c:set var="_myCtxArea">${_metaArea}.</c:set>
		  </c:if>
		
		  <c:set var="labelKey" value="${fn:replace(_meta.k, _myArea, _myCtxArea)}.label" />
		  
		  <td><fmt:message key="${labelKey}"/>:</td>
		  <td>
		  <c:choose>
			  <c:when test="${not empty _meta.property.control}">
			  	<c:choose>
				  	<c:when test="${_meta.property.control.items != null}">  	
				  		<c:set var="selectItems" scope="request" value="${requestScope[_meta.property.control.items]}"/>
				  		<c:set var="itemValue" scope="request">${_meta.property.control.itemValue}</c:set>
				  		<c:set var="itemLabel" scope="request">${_meta.property.control.itemLabel}</c:set>   	
				  	</c:when>
				 	<c:otherwise>
				 		<c:set var="selectItems" scope="request" value="${_meta.property.control.options}" />
				 		<c:set var="itemValue" scope="request">value</c:set>
				 		<c:set var="itemLabel" scope="request">label</c:set>  	
				 	</c:otherwise>
				</c:choose>
			    <select name="${_area}Meta_${_meta.k}">
				                <option value=''>-- select --</option>
			                	<c:forEach var="option" items="${selectItems}">
			                	<option value="${option[itemValue]}"<c:if test="${option[itemValue] == _meta.v}"> selected</c:if>
			                	<c:if test= "${_meta.property.formVisibility == 'immutable'}"> disabled="disabled"</c:if>><c:out value="${option[itemLabel]}"/></option>
			                	</c:forEach>               	                                	
			    </select>
			             	    	    
			  </c:when>
			  <c:otherwise>
			  		<input name="${_area}Meta_${_meta.k}" value="${_meta.v}" <c:if test= "${_meta.property.formVisibility == 'immutable'}"> disabled="disabled"</c:if>/>
			  </c:otherwise>
		  </c:choose>
		  </td>  	
		
		  <td><form:errors path="${_area}Meta[${status.index}].k" /> </td>           
		</tr>  
	</c:if>       
</c:forEach>

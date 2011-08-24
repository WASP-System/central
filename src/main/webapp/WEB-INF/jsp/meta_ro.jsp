<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:forEach items="${_metaList}" var="_meta" varStatus="status">
<tr>

  <c:set var="_myArea">${_area}.</c:set>
  <c:set var="_myCtxArea">${_area}.</c:set>
  <c:if test="${_metaArea != null}">
    <c:set var="_myCtxArea">${_metaArea}.</c:set>
  </c:if>

  <c:set var="labelKey" value="${fn:replace(_meta.k, _myArea, _myCtxArea)}.label" />

  <td><fmt:message key="${labelKey}"/>:</td>
  <td>
  <c:if test="${not empty _meta.property.control}">
  	<c:if test="${_meta.property.control.items != null}">  	
  		<c:set var="selectItems" scope="request" value="${requestScope[_meta.property.control.items]}"/>
  		<c:set var="itemValue" scope="request">${_meta.property.control.itemValue}</c:set>
  		<c:set var="itemLabel" scope="request">${_meta.property.control.itemLabel}</c:set>   	
  	</c:if>
 	<c:if test="${_meta.property.control.items == null}">
 		<c:set var="selectItems" scope="request" value="${_meta.property.control.options}" />
 		<c:set var="itemValue" scope="request">value</c:set>
 		<c:set var="itemLabel" scope="request">label</c:set>  	
 	</c:if>
  	 
  <c:forEach var="option" items="${selectItems}">
    	<c:if test="${option[itemValue] == _meta.v}"><c:out value="${option[itemLabel]}"/></c:if>
  </c:forEach>     	    
	    
  </c:if>
  <c:if test="${empty _meta.property.control}">${_meta.v}</c:if>
  </td>  	 
    
</tr>         
</c:forEach>

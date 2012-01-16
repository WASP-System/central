<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:forEach items="${_metaList}" var="_meta" varStatus="status">
<tr>

  <c:set var="_myArea">${_area}.</c:set>
  <c:set var="_myCtxArea">${_area}.</c:set>
  <c:if test="${_metaArea != null}">
    <c:set var="_myCtxArea">${_metaArea}.</c:set>
  </c:if>

  <c:set var="labelKey" value="${fn:replace(_meta.k, _myArea, _myCtxArea)}.label" />

  <td class="label"><fmt:message key="${labelKey}"/>:</td>
  <td class="value">
  <c:if test="${not empty _meta.property.control}">
		 <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
		 <wasp:metaSelect control="${_meta.property.control}"/>
  	 
  <c:forEach var="option" items="${selectItems}">
    	<c:if test="${option[itemValue] == _meta.v}"><c:out value="${option[itemLabel]}"/></c:if>
  </c:forEach>     	    
	    
  </c:if>
  <c:if test="${empty _meta.property.control}">${_meta.v}</c:if>
  </td>  	 
    
</tr>         
</c:forEach>

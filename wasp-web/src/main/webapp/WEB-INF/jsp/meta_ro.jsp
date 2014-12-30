<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:forEach items="${_metaList}" var="_meta" varStatus="status">
<tr class="FormData">

  <c:set var="_myArea">${_area}.</c:set>
  <c:set var="_myCtxArea">${_area}.</c:set>
  <c:if test="${_metaArea != null}">
    <c:set var="_myCtxArea">${_metaArea}.</c:set>
  </c:if>

  <c:set var="labelKey" value="${_meta.property.label}" />

  <td class="CaptionTD">${labelKey}:</td>
  <td class="DataTD">
  <c:if test="${not empty _meta.property.control}">
		 <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
		 <wasp:metaSelect control="${_meta.property.control}"/>
  	 	
  	 	<c:set var="foundIt" value="0" /><!-- foundIt added 12-30-14 to display items that were previously, but are no longer, in a select list of options [example: peakType == mixed] -->
  		<c:forEach var="option" items="${selectItems}">
    		<c:if test="${option[itemValue] == _meta.v}">
    			<c:out value="${option[itemLabel]}"/>
    			<c:set var="foundIt" value="1" />
    		</c:if>
  		</c:forEach> 
  		<c:if test="${foundIt=='0'}">
  			<c:out value="${_meta.v}"/>
  		</c:if>   	    
	    
  </c:if>
  <c:if test="${empty _meta.property.control}">${_meta.v}</c:if>
  <c:if test="${not empty _meta.property.tooltip}">
	<wasp:tooltip value="${_meta.property.tooltip}" />
  </c:if>
  </td>  	 
    
</tr>         
</c:forEach>

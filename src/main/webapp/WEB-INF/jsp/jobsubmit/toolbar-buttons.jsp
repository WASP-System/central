 <%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 <%-- 
 Renders JavaScript code to create "Add SubtypeName" buttons
 @author Sasha Levchuk
  --%>      
<c:forEach items="${_metaBySubtypeList}" var="_entry" varStatus="_substatus">
<c:set var="_subtype" value="${_entry.key}"/>
<c:set var="_validMetaFields" value="${_entry.value}"/>
navGrid.navButtonAdd("#gridpager",{
       caption:"${_subtype.name}",   
       title: "Add Sample of '${_subtype.name}' subtype",
       onClickButton: function(){                  

           var  _myAddAttr = jQuery.extend({subtypeSampleId:${_subtype.subtypeSampleId}}, _addAttr);
           
           $("#grid_id").jqGrid('editGridRow',"new", _myAddAttr); 
       }, 
       position:"last"
});
</c:forEach> 

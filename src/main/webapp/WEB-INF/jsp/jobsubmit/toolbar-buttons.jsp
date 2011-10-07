 <%@ include file="/WEB-INF/jsp/taglib.jsp" %>
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
/* 
navGrid.navButtonAdd("#gridpager",{
       caption:"Existing Job",   
       title: "Add Sample from Existing Job",
       onClickButton: function(){    
       	   var  _myAddAttr = jQuery.extend({existingJob:true}, _addAttr);                         
           $("#grid_id").jqGrid('editGridRow',"new", _myAddAttr); 
       }, 
       position:"last"
});  
*/  
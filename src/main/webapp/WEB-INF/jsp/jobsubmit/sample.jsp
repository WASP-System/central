<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
</br>
<span style="color:blue;font-size:200%" id='statusMessage'></span>
<span style="color:blue;font-size:200%" id='uploadStatus'></span>
<span style="color:red;font-size:200%;" id='uploadError'></span>

<a href="<c:url value="/jobsubmit/cells/${jobdraftId}.do" />">Cell Assignment</a>
</br>
</br>

<script>

var _validMetaFields={};
<c:forEach items="${_metaBySubtypeList}" var="_entry" varStatus="_substatus">
<c:set var="_subtype" value="${_entry.key}"/>
<c:set var="_validMetaFields" value="${_entry.value}"/>

_validMetaFields.subtypeSampleId_${_subtype.subtypeSampleId}=[];

<c:forEach items="${_validMetaFields}" var="_validMeta">
_validMetaFields.subtypeSampleId_${_subtype.subtypeSampleId}.push('${_validMeta.k}');
</c:forEach>
</c:forEach>


var grid=$("#grid_id");      // your jqGrid (the <table> element)
var orgEditGridRow = grid.jqGrid.editGridRow; // save original function
$.jgrid.extend ({editGridRow : function(rowid, p){
    $.extend(p,
             { // modify some parameters of editGridRow
    	beforeShowForm: function(form) {
                	                	
                	var subtypeSampleId;
                	
                	var _select=document.getElementById(form[0].id).subtypeSampleId;
                	
                	_select.disabled="disabled";
                	
                	if (p.subtypeSampleId) {//add
                		
                		subtypeSampleId=p.subtypeSampleId;
                	
                		for (var i=0; i < _select.length; i++) {
                			if ( _select[i].value == subtypeSampleId) {
                				_select[i].selected = true;
                				break;
                			}
                		}
                		
                	} else {//edit
                		
                		subtypeSampleId = _select.options[_select.selectedIndex].value;                		                		
                		
                	}
               		var _myValidMetaFields=_validMetaFields['subtypeSampleId_'+subtypeSampleId];
                	
               		//alert(_myValidMetaFields);
                	for(var i in colModel) {
                		var k = colModel[i].name;
                		
                		if (k.indexOf('.')==-1) continue;//static field
                		
                		if ($.inArray(k, _myValidMetaFields) == -1) {//element is not valid for given subtype
                			
                			var jqName='#tr_'+k.replace(".","\\.");
                			//alert(k+' does not exist in valid list. will hide '+jqName+"|"+$(jqName, form));
                			$(jqName, form).hide();
                		} else {
                			//alert(k+' exists in valid list. will not hide ');
                		}
                	
					}
                
                 }
             });
    orgEditGridRow.call (this,rowid, p);
}});



</script>

<table id="grid_id"></table> 
<div id="gridpager"></div>


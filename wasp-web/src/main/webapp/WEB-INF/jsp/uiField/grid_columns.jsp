<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="locale" type="select" items="${locales}">
#field.jq['search']=true;
#field.jq['stype']='select';
#field.jq['searchoptions']={ sopt: ['eq','ne'], value:#field.jq['editoptions']['value'] };
</wasp:field>
<wasp:field name="area" type="select" items="${areaNames}" >
#field.jq['search']=true;
#field.jq['stype']='select';
#field.jq['searchoptions']={ sopt: ['eq','ne'], value:#field.jq['editoptions']['value'] };
</wasp:field>
<wasp:field name="name"  type="text">
#field.jq['search']=true;
</wasp:field>

<wasp:field name="attrName"  type="select" items="${attrNames}" >
#field.jq['search']=true;
#field.jq['stype']='select';
#field.jq['searchoptions']={ sopt: ['eq','ne'], value:#field.jq['editoptions']['value'] };
</wasp:field>

<wasp:field name="attrValue"  type="text"/>

<%--   url to delete single field --%>
<wasp:delete url="<c:url value='${_area}/delete.do' />" />


<%-- disable regular search --%>
_navAttr.search=false;

<%-- enable toolbar search --%>
_enableFilterToolbar=true;

var _validateConstaintAttr = function(postdata, formid) {
 
	if ($('#attrName').val() == 'constraint') {			
		if ($('#attrValue').val() != 'NotEmpty') {
			return [false,'Only NotEmpty constraint currently supported. Please correct the value and resubmit the form.'];
		}		
	}
	return [true,''];	
} ;
  
 _editAttr.beforeSubmit = _validateConstaintAttr;
 
 _addAttr.beforeSubmit = _validateConstaintAttr; 
  
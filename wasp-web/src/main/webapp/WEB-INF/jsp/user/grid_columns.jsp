<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="login" type="text" sortable="true" searchable="false"/>

<%-- <wasp:field name="password" type="password" hidden="true" editHidden="true" /> --%>

<wasp:field name="firstName" type="text" sortable="true" searchable="false"/>

<wasp:field name="lastName" type="text" sortable="true" searchable="false"/>

<wasp:field name="roles" type="text" sortable="false" searchable="false"/>

<wasp:field name="email" type="text" sortable="true" searchable="false"/>

<wasp:field name="locale" type="select" items="${locales}" itemValue="key" itemLabel="value"  hidden="true" sortable="false" searchable="false"/> 

<wasp:field name="isActive"  type="checkbox" hidden="false" editHidden="true" sortable="false" searchable="false"/>

_url='/wasp/user/listJSON.do?selId=${param.selId}';

_navAttr={edit:true,view:false,add:true,del:false,search:false,refresh:true,beforeRefresh: 
	function () { 
		var url = window.location.href; 
		if(url.indexOf('selId') != -1){<%-- url contains this string (indicating coming from jobGrid), upon refresh, change url to remove; will cause a COMPLETE refresh of page rather than a JSON call--%> 
			window.location.replace('list.do'); <%-- completely refresh the page, without the userId and labId request parameters --%>
		}
	}
}
	  
// hide the roles attribute on the edit form
_editAttr['afterShowForm'] = function(formId) {
	$('#tr_roles', formId).hide();
};

// hide the roles attribute on the add form
_addAttr['afterShowForm'] = function(formId) {
	$('#tr_roles', formId).hide();
};

// show the locale attribute on the edit form; it is hidden on the main grid
_editAttr['beforeShowForm'] = function(formId) {
	$('#tr_locale', formId).show();
};

// show the locale attribute on the add form; it is hidden on the main grid
_addAttr['beforeShowForm'] = function(formId) {
	$('#tr_locale', formId).show();
};
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="login" type="text"/>

<%-- <wasp:field name="password" type="password" hidden="true" editHidden="true" /> --%>

<wasp:field name="firstName" type="text" sortable="true"/>

<wasp:field name="lastName" type="text" sortable="true"/>

<wasp:field name="roles" type="text" />

<wasp:field name="email" type="text" />

<wasp:field name="locale" type="select" items="${locales}" itemValue="key" itemLabel="value"/>

<wasp:field name="isActive"  type="checkbox" hidden="false" editHidden="true" />
  
// hide the roles attribute on the edit form
_editAttr['afterShowForm'] = function(formId) {
	$('#tr_roles', formId).hide();
};

// hide the roles attribute on the add form
_addAttr['afterShowForm'] = function(formId) {
	$('#tr_roles', formId).hide();
};


<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<wasp:field name="name" type="text" sortable="true"/>

<wasp:field name="machineType" type="text"/>

<wasp:field name="resourceCategoryId" type="select" items="${categoryResources}" itemValue="resourceCategoryId" itemLabel="name" hidden="true" editHidden="true"/>

<wasp:field name="typeResourceId" type="select" items="${typeResources}" itemValue="typeResourceId" editable="false" itemLabel="name"/>

<wasp:field name="isActive" type="checkbox" editable="false"/>

<wasp:field name="barcode" type="text" hidden="true" editHidden="true"/>

// hide the resourceCategory dropdown list on the edit form
_editAttr['afterShowForm'] = function(formId) {
	$('#machineType').attr('disabled', 'disabled');
	$('#tr_resourceCategoryId', formId).hide();
};

// hide resource category text field on the add form, keep the dropdown list with only active resouceCategory in it
_addAttr['afterShowForm'] = function(formId) {
	$('#tr_machineType', formId).hide();
	$('#tr_resource\\.decommission_date').hide();
};

  



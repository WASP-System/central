<%--Doubt this is really used anymore 11/30/12 dubin --%>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text" sortable="true"/>
<wasp:field name="barcode" type="text" hidden="false" editHidden="true"/>
<wasp:field name="subtype"  type="text" editable="false"/>
<wasp:field name="submitter"  type="text" editable="false"/>
<wasp:field name="cellcountForEditBox" type="text" hidden="true" editHidden="true"/>


_navAttr={edit:true,view:true,add:true,del:false};

_navAttr.search=true;

// hide cellcount dropdown list on the edit form and show cellcount text field with pre-selected cell count
_editAttr['afterShowForm'] = function(formId) {
	$('#cellcountForEditBox').attr('disabled', 'disabled');
	$('#tr_platformunitInstance\\.cellcount', formId).hide();
	
	// remove the trailing hyperlink in the flowcell name when showing in grid
	var str=$('input#name').val();
	$('input#name').val(str.replace(/ \(<a href=.*>\)/i, ""));
};

// hide cellcount text field on the add form and show a cellcount dropdown list
_addAttr['afterShowForm'] = function(formId) {
	$('#tr_cellcountForEditBox', formId).hide();
};


// hide cellcount text field on the add form and show a cellcount dropdown list
_viewAttr['afterShowForm'] = function(formId) {
	$('#platformunitInstance\\.cellcount', formId).hide();
};




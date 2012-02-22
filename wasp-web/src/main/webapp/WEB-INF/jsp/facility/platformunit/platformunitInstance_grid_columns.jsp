<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text" sortable="true"/>
<wasp:field name="subtype"  type="text" editable="false"/>
<wasp:field name="submitter"  type="text" editable="false"/>
<wasp:field name="barcode" type="text" hidden="true" editHidden="true"/>
<wasp:field name="lanecount" type="text" hidden="true" editHidden="true"/>


_navAttr={edit:true,view:true,add:true,del:false};

_navAttr.search=true;

// hide lanecount dropdown list on the edit form and show lanecount text field with pre-selected lane count
_editAttr['afterShowForm'] = function(formId) {
	$('#lanecount').attr('disabled', 'disabled');
	$('#tr_platformunitInstance\\.lanecount', formId).hide();
};

// hide lanecount text field on the add form and show a lanecount dropdown list
_addAttr['afterShowForm'] = function(formId) {
	$('#tr_lanecount', formId).hide();
};



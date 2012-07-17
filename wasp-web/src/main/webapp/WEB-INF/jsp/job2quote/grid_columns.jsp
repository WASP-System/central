<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="jobId" type="text" sortable="true" editable="false"/>

<wasp:field name="name" type="text" sortable="true"/>

<wasp:field name="amount" type="currency"/>

<wasp:field name="submitter" type="text" sortable="true" searchable="false" editable="false"/>

<wasp:field name="lab" type="text" sortable="true" searchable="false" editable="false"/>

<wasp:field name="submission_date"  type="text" editable="false"/>

_url='/wasp/job2quote/listJSON.do?showall=${param.showall}';
  
_navAttr={edit:true,view:true,add:false,del:false};

_editAttr['beforeShowForm'] = function(formId) {
	$('input[type="text"][name$="cost"]').val('0');
};

_editAttr['afterShowForm'] = function(formId) {
	$('#name').attr('disabled', 'disabled');
	$('#amount').attr('disabled', 'disabled');
	
	$('input[type="text"][name$="cost"]').keyup( function(evt){
		$('#amount').val(sumCosts().toFixed(2));
	} );
};

function isNumber(str) {
	isPrice = /^\d+(\.\d{0,2})?$/;
	return isPrice.test( str );
} 
function sumCosts() {
	if (isNumber($('#acctQuote\\.library_cost').val())
		&& isNumber($('#acctQuote\\.sample_cost').val())
		&& isNumber($('#acctQuote\\.lane_cost').val())) {
		return window.Number($('#acctQuote\\.library_cost').val()) 
			+ window.Number($('#acctQuote\\.sample_cost').val()) 
			+ window.Number($('#acctQuote\\.lane_cost').val());
	}
};
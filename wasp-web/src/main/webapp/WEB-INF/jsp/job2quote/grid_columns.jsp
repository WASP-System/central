<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="jobId" type="text" sortable="true" searchable="true" editable="false"/>
<wasp:field name="currentStatus" type="text" sortable="false" searchable="false" editable="false"/>

<wasp:field name="name" type="text" sortable="true" searchable="false"/>

<wasp:field name="amount" type="currency" sortable="true" searchable="false"/>

<wasp:field name="grant_code" type="text" sortable="true" searchable="false"/>

<wasp:field name="submitter" type="text" sortable="true" searchable="false" editable="false"/>

<wasp:field name="lab" type="text" sortable="true" searchable="false" editable="false"/>

<wasp:field name="submitted_on"  type="text" sortable="true" searchable="false" editable="false"/>

<wasp:field name="quoteId" type="hidden" />

  
_navAttr={edit:true,view:true,add:false,del:false,search:false,refresh:true,beforeRefresh: 
		function () { 
			<%--http://stackoverflow.com/questions/7089643/programmatically-sorting-the-jqgrid 
			with next line, sortname is set to "" and with that sidx is also set to "" 
			so that with reload grid, sidx is not controlling anything--%>
			$("#grid_id").setGridParam({sortname:''});
		}
};

<%-- 
_editAttr['beforeShowForm'] = function(formId) {
	$('input[type="text"][name$="cost"]').val('0');
};
--%>

function isNonNegNumber(str) {
	isPrice = /^\d+(\.\d{0,2})?$/;
	return isPrice.test( str );
} 
_editAttr['beforeSubmit'] = function(postdata, formid) {

	if (isNonNegNumber($('#acctQuote\\.library_cost').val())
		&& isNonNegNumber($('#acctQuote\\.sample_cost').val())
		&& isNonNegNumber($('#acctQuote\\.cell_cost').val())) {
		return [true,""]; 
	}
	return [false,"Provide numerical value 0 or greater for each cost"];	
}

function sumCosts() {
	if (isNonNegNumber($('#acctQuote\\.library_cost').val())
		&& isNonNegNumber($('#acctQuote\\.sample_cost').val())
		&& isNonNegNumber($('#acctQuote\\.cell_cost').val())) {
		return window.Number($('#acctQuote\\.library_cost').val()) 
			+ window.Number($('#acctQuote\\.sample_cost').val()) 
			+ window.Number($('#acctQuote\\.cell_cost').val());
	}
};
_editAttr['afterShowForm'] = function(formId) {
	$('#name').attr('disabled', 'disabled');
	$('#amount').attr('disabled', 'disabled');
	
	
	$('input[type="text"][name$="cost"]').keyup( function(evt){
		$('#amount').val(sumCosts().toFixed(2));
	} );
};


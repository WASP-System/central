<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name" type="text"/>

<wasp:field name="resourceName" type="text" sortable="true" showLink="true" baseLinkURL="/wasp/resource/list.do" idCol="2"/>
<wasp:field name="machineId" type="hidden" />

<wasp:field name="flow_cell_name" type="text" showLink="true" baseLinkURL="/wasp/facility/platformunit/selid/list.do" idCol="4" />
<wasp:field name="flowcellId" type="hidden" />

<wasp:field name="start_esf_staff" type="select" items="${techs}" itemValue="key" itemLabel="value" />

<wasp:field name="resourceId" type="select" items="${machines}" itemValue="resourceId" defaultSelect="true" itemLabel="name" hidden="true" editHidden="true" />

<wasp:field name="sampleId" type="select" items="${flowcells}" itemValue="sampleId" defaultSelect="true" itemLabel="name" hidden="true" editHidden="true" />

_navAttr={edit:false,view:true,add:true,del:false};

_viewAttr['beforeShowForm'] = function(formId) {
	$('#trv_resourceId').hide();
	$('#trv_sampleId').hide();
};

_addAttr['afterShowForm'] = function(formId) {
	$('#tr_resourceName', formId).hide();
	$('#tr_flow_cell_name', formId).hide();
	
	$('#tr_run\\.end_datetime').hide();
};

_addAttr['beforeShowForm'] = function(formId) {
	$('#resourceId').change(function () {      
		var _val=$("#resourceId option:selected").val();
		var _val2=$("#sampleId option:selected").val();
                		
		if (!_val || !_val2) {
			return;
		} else if (_val2==-1) {
			var options = '<option value="-1"><fmt:message key="wasp.default_select.label" /></option>';
			$.getJSON("/wasp/run/samplesByResourceId.do",{resourceId: _val, ajax: 'true'}, function(data, textStatus, jqXHR){
				$.each(data, function (index, name) {                				    
					options += '<option value="' + index + '">' + name + '</option>';
				});
			})
			
			$("#sampleId").html(options);
		}
	});

	$('#sampleId').change(function () {      
		var _val=$("#sampleId option:selected").val();
		var _val2=$("#resourceId option:selected").val();
                		
		if (!_val || !_val2) {
			return;
		} else if (_val2==-1) {
			var options = '<option value="-1"><fmt:message key="wasp.default_select.label" /></option>';
			$.getJSON("/wasp/run/resourcesBySampleId.do",{sampleId: _val, ajax: 'true'}, function(data, textStatus, jqXHR){
				$.each(data, function (index, name) {                				    
					options += '<option value="' + index + '">' + name + '</option>';
				});
			})
			
			$("#resourceId").html(options);
		}
	});
};

<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text" sortable="true" searchable="false" editable="true" columnWidth="260"/>
<wasp:field name="platformUnitBarcode"  type="text" sortable="true" searchable="false" editable="false" columnWidth="100"/>
<wasp:field name="machine"  type="text" sortable="true" searchable="false" editable="false" columnWidth="170"/>
<wasp:field name="readLength"  type="text" sortable="true" searchable="false" editable="false" columnWidth="65"/>
<wasp:field name="readType"  type="text" sortable="true" searchable="false" editable="false" columnWidth="60"/>
<wasp:field name="dateRunStarted"  type="text" sortable="true" searchable="false" editable="false" columnWidth="70"/>
<wasp:field name="dateRunEnded"  type="text" sortable="true" searchable="false" editable="false" columnWidth="70"/>
<wasp:field name="statusForRun"  type="text" sortable="true" searchable="false" editable="false" columnWidth="90"/>

 _url='<wasp:relativeUrl value="run/listJSON.do?selId=${param.selId}" />';

_navAttr=
	{edit:false,view:false,add:false,del:false,search:false,refresh:true,beforeRefresh: 
		function () { 
			<%--http://stackoverflow.com/questions/5070065/jqgrid-sort-icon-on-columns-are-not-reset-after-triggerreloadgrid-call 
			var myGrid = jQuery("#grid_id");
    		$("span.s-ico",myGrid[0].grid.hDiv).hide();
    		--%>
			<%--http://stackoverflow.com/questions/7089643/programmatically-sorting-the-jqgrid 
			with next line, sortname is set to "date" and with that sidx is also set to "date" 
			so that with reload grid, sidx is date and it is the icon being displayed 
			var myGrid = jQuery("#grid_id");
    		$("span.s-ico",myGrid[0].grid.hDiv).hide();
    		$("#grid_id").setGridParam({sortname:'date'});
    		$("#grid_id").setGridParam({sortorder:'desc'});
			$("span.s-ico",myGrid[0].grid.hDiv).show();--%>
			
			$("#grid_id").setGridParam({sortname:''});
		}
	}

_viewAttr={width:600};




<%-- 
 
<wasp:field name="name" type="text"/>

<wasp:field name="resourceName" type="text" sortable="true" showLink="true" baseLinkURL="resource/list.do" idCol="2"/>
<wasp:field name="machineId" type="hidden" />

<wasp:field name="flow_cell_name" type="text" showLink="true" baseLinkURL="facility/platformunit/selid/list.do" idCol="4" />
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
			$.getJSON("<wasp:relativeUrl value='run/samplesByResourceId.do' />",{resourceId: _val, ajax: 'true'}, function(data, textStatus, jqXHR){
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
			$.getJSON("<wasp:relativeUrl value='run/resourcesBySampleId.do' />",{sampleId: _val, ajax: 'true'}, function(data, textStatus, jqXHR){
				$.each(data, function (index, name) {                				    
					options += '<option value="' + index + '">' + name + '</option>';
				});
			})
			
			$("#resourceId").html(options);
		}
	});
};
--%>
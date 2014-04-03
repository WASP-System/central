<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<%-- <wasp:field name="resourceCategoryName"  type="text" sortable="true" searchable="false" editable="false"/> --%>
<wasp:field name="date"  type="text" sortable="true" searchable="false" editable="false" columnWidth="70"/>
<wasp:field name="name"  type="text" sortable="true" searchable="false" editable="false" columnWidth="100"/>
<wasp:field name="barcode"  type="text" sortable="true" searchable="false" editable="false" columnWidth="100"/>
<wasp:field name="sampleSubtypeName"  type="text" sortable="true" searchable="false" editable="false" columnWidth="190"/>
<wasp:field name="readType"  type="text" sortable="true" searchable="false" editable="false" columnWidth="80"/>
<wasp:field name="readLength"  type="text" sortable="true" searchable="false" editable="false" columnWidth="90"/>
<wasp:field name="cellcount"  type="text" sortable="true" searchable="false" editable="false" columnWidth="65"/>

 _url='<c:url value="facility/platformunit/listJSON_platformUnitGrid.do?selId=${param.selId}" />';

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

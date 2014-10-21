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
			$("#grid_id").setGridParam({sortname:''});
		}
	}

_viewAttr={width:600};

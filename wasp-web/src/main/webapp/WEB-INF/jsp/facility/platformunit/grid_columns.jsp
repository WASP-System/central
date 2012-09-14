<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 
<wasp:field name="name"  type="text" sortable="true" searchable="false"/>
<wasp:field name="barcode"  type="text" sortable="true" searchable="false"/>
<wasp:field name="sampleSubtypeName"  type="text" sortable="true" searchable="false"/>
<wasp:field name="readType"  type="text" sortable="true" searchable="false"/>
<wasp:field name="readlength"  type="text" sortable="true" searchable="false"/>
<wasp:field name="lanecount"  type="text" sortable="true" searchable="false"/>
<wasp:field name="date"  type="text" sortable="true" searchable="false"/>

 _url='/wasp/facility/platformunit/listJSON_DUBINTEST.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}';

_navAttr=
	{edit:false,view:false,add:false,del:false,search:false,refresh:true,beforeRefresh: 
		function () { 
			var url = window.location.href; 
			if(url.indexOf('userId') != -1){<%-- url contains this string (indicating coming from jobGrid), upon refresh, change url to remove; will cause a COMPLETE refresh of page rather than a JSON call--%> 
   				window.location.replace('list.do'); <%-- completely refresh the page, without the userId and labId request parameters --%>
			}
		}
	}

_viewAttr={width:600};

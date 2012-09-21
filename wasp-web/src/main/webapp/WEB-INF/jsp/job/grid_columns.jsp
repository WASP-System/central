<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 
<wasp:field name="jobId" type="text" sortable="true" searchable="true"/>

<wasp:field name="name" type="text" sortable="true" searchable="true"/>

<wasp:field name="submitter" type="text"  sortable="true" searchable="false"/>

<wasp:field name="pi" type="text"  sortable="true"  searchable="false"/>

<wasp:field name="createts"  type="text" sortable="true" searchable="true"/>

<wasp:field name="amount"  type="currency" sortable="false" searchable="false" />
  
<wasp:field name="viewfiles"  type="text"  sortable="false" searchable="false"/>

 _url='/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}';

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


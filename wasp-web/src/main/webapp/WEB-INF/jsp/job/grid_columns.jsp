<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="jobId" type="text" sortable="true" searchable="true"/>

<wasp:field name="name" type="text" sortable="true" searchable="true"/>

<wasp:field name="submitter" type="text"  sortable="true" searchable="true"/>

<%-- <wasp:field name="lab" type="text"  searchable="false"/> --%>

<wasp:field name="pi" type="text"  sortable="true"  searchable="true"/>

<wasp:field name="createts"  type="text" sortable="true" searchable="false"/>
  
<wasp:field name="viewfiles"  type="text"  searchable="false"/>

<%-- _url='/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}'; --%>
_url='/wasp/job/listJSON.do?userId=${param.userId}&labId=${param.labId}';

_navAttr={edit:false,view:true,add:false,del:false};

_viewAttr={width:600};
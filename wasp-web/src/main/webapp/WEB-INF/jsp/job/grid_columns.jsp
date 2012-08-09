<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="jobId" type="text" sortable="false" searchable="false"/>

<wasp:field name="name" type="text" sortable="false" searchable="false"/>

<wasp:field name="submitter" type="text"  sortable="false" searchable="true"/>

<%-- <wasp:field name="lab" type="text"  searchable="false"/> --%>

<wasp:field name="pi" type="text"  sortable="false"  searchable="false"/>

<wasp:field name="createts"  type="text" sortable="false" searchable="false"/>
  
<wasp:field name="viewfiles"  type="text"  searchable="false"/>

 _url='/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}';
<%-- _url='/wasp/job/listJSON.do?userId=${param.userId}&labId=${param.labId}'; --%>

<%-- _navAttr={edit:false,view:true,add:false,del:false}; --%>
_navAttr={edit:false,view:false,add:false,del:false};

_viewAttr={width:600};
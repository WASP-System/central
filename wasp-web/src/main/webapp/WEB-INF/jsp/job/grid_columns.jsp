<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="jobId" type="text" sortable="true"/>

<wasp:field name="name" type="text" sortable="true"/>

<wasp:field name="UserId" type="text" sortable="true" searchable="false"/>

<wasp:field name="lab" type="text" sortable="true" searchable="false"/>

<wasp:field name="submission_date"  type="text" />
  

_url='/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}';

_navAttr={edit:false,view:true,add:false,del:false};

_viewAttr={width:600};
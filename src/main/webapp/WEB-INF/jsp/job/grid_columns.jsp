<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name" type="text"/>

<wasp:field name="submitter" type="text" sortable="true"/>

<wasp:field name="lab" type="text" sortable="true"/>

<wasp:field name="isActive"  type="checkbox" />
  

_url='/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}';

<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name" type="text"/>

<wasp:field name="submitter" type="text" sortable="true"/>

<wasp:field name="lab" type="text" sortable="true"/>

<wasp:field name="status"  type="text" sortable="true" editable="false" />
  
_url='/wasp/jobsubmit/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}';

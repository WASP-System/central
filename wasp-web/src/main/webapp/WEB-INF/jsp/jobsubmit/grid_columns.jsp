<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name" type="text"/>

<wasp:field name="submitter" type="text" sortable="true"/>

<wasp:field name="lab" type="text" sortable="true"/>

<wasp:field name="last_modify_user"  type="text" />

<wasp:field name="last_modify_date"  type="text" />
  
_url='/wasp/jobsubmit/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}';

_navAttr={edit:false,view:true,add:false,del:false};
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name" type="text"/>

<wasp:field name="submitter" type="text" sortable="false"/>

<wasp:field name="lab" type="text" sortable="false"/>

<wasp:field name="last_modify_user"  type="text" />

<wasp:field name="last_modify_date"  type="text" />
  
_url='<wasp:relativeUrl value="jobsubmit/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}" />';

_navAttr={search:false,edit:false,view:false,add:false,del:false,refresh:false};
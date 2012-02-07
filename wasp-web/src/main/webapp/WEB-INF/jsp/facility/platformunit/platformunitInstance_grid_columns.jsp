<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>

_url='/wasp/facility/platformunit/instance/listJSON.do?selId=${param.selId}';



_navAttr={edit:true,view:true,add:true,del:false};

_navAttr.search=true;


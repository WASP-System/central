<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text" />



_url='/wasp/facility/platformunit/listJSON.do?selId=${param.selId}';


_navAttr={edit:false,view:true,add:false,del:false};

_navAttr.search=false;


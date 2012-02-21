<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text" sortable="true"/>
<wasp:field name="submitter"  type="text" editable="false"/>

_url='/wasp/facility/platformunit/selid/listJSON.do';

_navAttr={edit:false,view:true,add:false,del:false};

_navAttr.search=false;


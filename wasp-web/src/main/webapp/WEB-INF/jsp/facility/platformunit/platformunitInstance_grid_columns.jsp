<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>
<wasp:field name="subtype"  type="text"/>
<wasp:field name="submitter"  type="text"/>
<wasp:field name="barcode" type="text" hidden="true" editHidden="true"/>
<wasp:field name="lanecount" type="select" hidden="true" editHidden="true" items="${lanes}"/>

_url='/wasp/facility/platformunit/instance/listJSON.do';

_navAttr={edit:false,view:true,add:true,del:false};

_navAttr.search=true;


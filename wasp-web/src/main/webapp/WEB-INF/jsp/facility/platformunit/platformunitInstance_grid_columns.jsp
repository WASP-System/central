<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text" sortable="true"/>
<wasp:field name="subtype"  type="text" editable="false"/>
<wasp:field name="submitter"  type="text" editable="false"/>
<wasp:field name="barcode" type="text" hidden="true" editHidden="true"/>

_url='/wasp/facility/platformunit/instance/listJSON.do';

_navAttr={edit:false,view:true,add:true,del:false};

_navAttr.search=true;


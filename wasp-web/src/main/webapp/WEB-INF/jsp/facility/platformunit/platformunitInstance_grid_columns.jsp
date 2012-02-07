<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>
<wasp:field name="subtype"  type="text"/>
<wasp:field name="submitter"  type="text"/>
<wasp:field name="barcode" type="text" hidden="true" editHidden="true"/>

_url='/wasp/facility/platformunit/instance/listJSON.do?selId=${param.selId}';

_navAttr={edit:false,view:true,add:true,del:false};

_navAttr.search=true;


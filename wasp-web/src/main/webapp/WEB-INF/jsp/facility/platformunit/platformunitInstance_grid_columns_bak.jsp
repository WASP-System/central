<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>
<wasp:field name="subtypeSample"  type="text"/>
<wasp:field name="submitter"  type="text"/>
<wasp:field name="barcode" type="text" hidden="true" editHidden="true"/>
<wasp:field name="lanecount" type="text" hidden="true" editHidden="true"/>



_navAttr={edit:false,view:true,add:false,del:false};

_navAttr.search=false;


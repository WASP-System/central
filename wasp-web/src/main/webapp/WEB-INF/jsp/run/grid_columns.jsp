<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name" type="text"/>

<wasp:field name="machine_name" type="text" sortable="true" showLink="true" baseLinkURL="/wasp/resource/list.do" idCol="2"/>
<wasp:field name="resourceId" type="hidden" />

<wasp:field name="flow_cell_name" type="text" showLink="true" baseLinkURL="/wasp/facility/platformunit/list.do" idCol="4" />
<wasp:field name="sampleId" type="hidden" />

<wasp:field name="start_esf_staff" type="text"/>

<wasp:field name="isActive"  type="checkbox" />
  
_navAttr={edit:false,view:true,add:false,del:false};
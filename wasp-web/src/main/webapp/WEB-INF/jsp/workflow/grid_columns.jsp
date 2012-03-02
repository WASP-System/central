<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<wasp:field name="workflowId" type="text" searchable="false"/>

<wasp:field name="name" type="text" />

<wasp:field name="isActive"  type="checkbox" />

<wasp:field name="configure" type="text" showLink="true" baseLinkURL="/wasp/workflow/resource/configure.do" searchable="false" >
	#field.jq.editrules.edithidden=true;
</wasp:field>

_navAttr={edit:false,view:true,add:false,del:false};


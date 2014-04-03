<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<wasp:field name="workflowId" type="text" searchable="false"/>

<wasp:field name="name" type="text" sortable="true" />

<wasp:field name="isActive"  type="checkbox" />

<wasp:field name="configure" type="text" showLink="true" baseLinkURL="<wasp:relativeUrl value='workflow/resource/configure.do' />" searchable="false" >
	<%-- #field.jq.editrules.edithidden=true;  dubin:3/28/13, this appears to completely disable the grid, so it was commented out --%>
</wasp:field>

_navAttr={edit:false,view:true,add:false,del:false};

_viewAttr={width:600};
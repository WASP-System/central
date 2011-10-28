<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<wasp:field name="resourceId" type="text" />

<wasp:field name="name" type="text" />

<wasp:field name="typeResourceId" type="select" items="${typeResources}"
	itemValue="typeResourceId" itemLabel="name" />

<wasp:field name="assay_platform" type="text">
#field.jq['editable']=false;
</wasp:field>

<wasp:field name="isActive"  type="checkbox" />
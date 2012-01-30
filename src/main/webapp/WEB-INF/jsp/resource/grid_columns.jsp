<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<wasp:field name="name" type="text" />

<wasp:field name="resourceCategoryId" type="select" items="${categoryResources}" itemValue="resourceCategoryId" itemLabel="name" />

<wasp:field name="typeResourceId" type="select" items="${typeResources}" itemValue="typeResourceId" itemLabel="name" />

<wasp:field name="isActive" type="checkbox" editable="false"/>

<wasp:field name="resourceBarcodeId" type="text" hidden="true" editHidden="true"/>





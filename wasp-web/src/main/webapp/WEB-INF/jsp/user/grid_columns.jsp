<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="login" type="text"/>

<%-- <wasp:field name="password" type="password" hidden="true" editHidden="true" /> --%>

<wasp:field name="firstName" type="text" sortable="true"/>

<wasp:field name="lastName" type="text" sortable="true"/>

<wasp:field name="email" type="text" />

<wasp:field name="locale" type="select" items="${locales}" itemValue="key" itemLabel="value"/>

<wasp:field name="isActive"  type="checkbox" hidden="true" editHidden="true" />
  



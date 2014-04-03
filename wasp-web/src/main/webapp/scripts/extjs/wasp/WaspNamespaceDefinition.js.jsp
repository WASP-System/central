<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
Ext.Loader.setConfig({
	enabled: true,
	paths: {
		'Wasp': '<c:url value="scripts/extjs/wasp" />'
	}
});
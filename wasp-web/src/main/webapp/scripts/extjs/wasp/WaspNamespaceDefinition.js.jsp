<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
Ext.Loader.setConfig({
	enabled: true,
	paths: {
		'Wasp': '<wasp:relativeUrl value="scripts/extjs/wasp" />'
	}
});
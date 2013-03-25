<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<%-- message keys link to internationalized text. Conversions can be specified in the .properties files in /src/main/resources/i18n --%>

<h1>
	<fmt:message key="pageTitle.waspillumina/description.label" />
</h1>
<div class="instructions">
	<fmt:message key="waspIlluminaPlugin.maintext.label" />
</div>

<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<c:if test="${ fn:length(fadingErrorMessage)>0 || fn:length(fadingSuccessMessage)>0}"> 
	
	<div id="fadingmessagediv" >
	 
		<c:if test="${fn:length(fadingErrorMessage)>0}">
			<H2 style="color:red;font-weight:bold;"><c:out value="${fadingErrorMessage}" /></H2>
		</c:if>
	
		<c:if test="${fn:length(fadingSuccessMessage)>0}">
			<H2 style="color:green;font-weight:bold;"><c:out value="${fadingSuccessMessage}" /></H2>
		</c:if>
		
	</div>
	
	<script type="text/javascript">
		//http://papermashup.com/jquery-fading-a-div-after-a-certain-time/ 
		$(document).ready(function(){
			setTimeout(function(){
				$("#fadingmessagediv").fadeOut("slow", function () {
				$("#fadingmessagediv").remove();
			 });
			 }, 3000);
		}); 
	</script>
	
</c:if>

<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<c:if test="${ fn:length(successMessage)>0}"> <%-- not used now     fn:length(errorMessage)>0 || --%>
	
	<div id="fadingmessagediv" >
	<%-- 
		<c:if test="${fn:length(errorMessage)>0}">
			<H2 style="color:red;font-weight:bold;"><c:out value="${errorMessage}" /></H2>
		</c:if>
	--%>
		<c:if test="${fn:length(successMessage)>0}">
			<H2 style="color:green;font-weight:bold;"><c:out value="${successMessage}" /></H2>
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
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 
<script>

$(document).ready(function() {
    $("#accordion").accordion({
		collapsible: true,
		autoHeight: false,
		navigation: true,
		active: false,
		header: 'h4'			
	});
});

</script>

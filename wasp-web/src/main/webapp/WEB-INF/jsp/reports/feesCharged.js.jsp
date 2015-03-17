<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">

$(function() {
  $( "#datepickerStartDate" ).datepicker();
  $( "#datepickerStartDate" ).datepicker( "option", "dateFormat", "yy/mm/dd" );//format will be yyyy/mm/dd
  $( "#datepickerEndDate" ).datepicker();  
  $( "#datepickerEndDate" ).datepicker( "option", "dateFormat", "yy/mm/dd" );//format will be yyyy/mm/dd
  
  $( "#viewAdditionalJobStatsAndMore" ).click(function() {
		if($(this).text()=="View Additional Job Stats"){
			$("#jobStatsAndMoreDiv").css("display", "inline");
			$(this).text("Hide Additional Job Stats");
		}
		else{
			$("#jobStatsAndMoreDiv").css("display", "none");
			$(this).text("View Additional Job Stats"); 
		}
	});
  
});

</script>
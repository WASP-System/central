<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">

$(function() {
  $( "#datepickerStartDate" ).datepicker({  maxDate: new Date });//initialized with {  maxDate: new Date } to prohibit datepicker from selecting date greater than the current date (NOW date); see http://stackoverflow.com/questions/22006328/disable-future-dates-after-today-in-jquery-ui-datepicker
  $( "#datepickerStartDate" ).datepicker( "option", "dateFormat", "yy/mm/dd" );//format will be yyyy/mm/dd
  $( "#datepickerEndDate" ).datepicker({  maxDate: new Date }); 
  $( "#datepickerEndDate" ).datepicker( "option", "dateFormat", "yy/mm/dd" );//format will be yyyy/mm/dd
 
  $( "#viewAdditionalJobStatsAndMore" ).click(function() {
		if($(this).text()=="Click To View Additional Job Stats"){
			$("#jobStatsAndMoreDiv").css("display", "inline");
			$(this).text("Hide Additional Job Stats");
		}
		else{
			$("#jobStatsAndMoreDiv").css("display", "none");
			$(this).text("Click To View Additional Job Stats"); 
		}
	});
  
});

</script>
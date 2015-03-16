<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">

$(function() {
  $( "#datepickerStartDate" ).datepicker();
  $( "#datepickerStartDate" ).datepicker( "option", "dateFormat", "yy/mm/dd" );//format will be yyyy/mm/dd
  $( "#datepickerEndDate" ).datepicker();  
  $( "#datepickerEndDate" ).datepicker( "option", "dateFormat", "yy/mm/dd" );//format will be yyyy/mm/dd
});

</script>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <script src="<c:url value='scripts/jquery/jquery.tablednd_0_5.js' />" type="text/javascript"></script>
  <script>
  $(document).ready(function(){
    $("#cells").tableDnD();
  });
  </script>


  <script>
var maxColumns = 15; 

function adjustcolumns(c) {
  for (var i = 0; i <= maxColumns; i++) {
    var display = "table-cell";
    if (i >  c) {
      display = "none"
    }
    $("[name=column_"+i+"]").css("display", display);
  }
}
  </script>
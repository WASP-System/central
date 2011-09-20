<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
</br>
<span style="color:blue;font-size:200%" id='statusMessage'></span>
<span style="color:blue;font-size:200%" id='uploadStatus'></span>
<span style="color:red;font-size:200%;" id='uploadError'></span>

<a href="<c:url value="/jobsubmit/cells/${jobdraftId}.do" />">Cell Assignment</a>
<table id="grid_id"></table> 
<div id="gridpager"></div>


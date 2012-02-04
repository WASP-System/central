<%@ include file="/WEB-INF/jsp/include.jsp" %>

<script>

function handleform() {

  var found = []; 

  var cbs = document.getElementsByTagName("INPUT"); 
  for (var i = 0; i < cbs.length; i++) {
    if (cbs[i].type != 'checkbox') {continue; }

    if (! cbs[i].checked) { continue; }
    var pair = [ cbs[i].value, cbs[i].parentNode.getElementsByTagName("SPAN")[0].innerHTML];
    found[found.length] = pair; 
   
  }
  var selects = document.getElementsByTagName("INPUT"); 
  for (var i = 0; i < selects.length; i++) {
    // todo, don't cobbler elements that are still there
    for (var j = select[i].options.length;  j >= 0; j-- {
      selects[i].remove(j);
    }

    for (var j = 0; j < found.length; j++) {
      o = new Option(found[j][1], found[j][0]); 
      selects[i].add(o, null);
    }
  }

}

</script>

<form method="POST">

<c:forEach var="s" items="${samples}">
<div>
  <input type="checkbox" name="control" value="<c:out value="${s.sampleDraftId}" />" onclick="handleform()">
  <span><c:out value="${s.name}" /></span>
</c:forEach>
</div>

<div>
<c:forEach var="s" items="${samples}">
<div>
  <c:out value="${s.name}" />
  <select name="sd_<c:out value="${s.sampleDraftId}" />_select">  
    <option></option>
    <c:forEach var="s2" items="${samples}">
      <option value="<c:out value="${s2.sampleDraftId}" />"><c:out value="${s2.name}" /></option>
    </c:forEach>
  </select>  
</div>
</c:forEach>
</div>

<input type="submit">
</form>

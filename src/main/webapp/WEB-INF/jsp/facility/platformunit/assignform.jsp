<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<script src="/wasp/scripts/jquery/jquery-1.6.2.js" type="text/javascript"></script>
<script>
function showAssignForm(e) {
  e.parentNode.getElementsByTagName("A")[0].style.display = "none"; 
  e.parentNode.getElementsByTagName("DIV")[0].style.display = "block"; 
}
</script>

<style>
DIV {margin-left: 10px; }
BODY {margin: 0; padding: 0;}
.assignmentContent {width:900px; height: 400px; }
.sampleSide {float:left; width:465; height: 100%; overflow: auto}
.platformUnitSide {float:left; wldth:465px; height:100%; overflow: auto};
</style>

<font color="blue"><wasp:message /></font>
[<c:out value="${error}" />]


<div class="assignmentContent">
<div class="sampleSide">
  <c:forEach items="${jobs}" var="j">
    <div class="job">
      <label>Job</label>
      <c:out value="${j.name}" /> 
      <c:out value="${j.workflow.name}" /> 
      <c:forEach items="${j.jobCell}" var="jc">
        <div class="jobcell">
          Job Cell: <c:out value="${jc.cellindex}" /> 
          <c:forEach items="${jc.sampleCell}" var="sc">
            <div class="samplecell">
              Sample Cell: <c:out value="${sc.libraryindex}" /> 
               <div class="sample">
                  <label>Sample</label>
                  <c:out value="${sc.sample.name}"/>
                  <c:out value="${sc.sample.typeSample.name}"/>
                  <c:if test="${sc.sample.typeSample.IName == 'library'}">
<div>
<a href="javascript:{}" onclick="showAssignForm(this)">(+)</a>
<div style="display:none">
<form method="POST" action="<c:url value="/facility/platformunit/assignAdd.do" />">
  <input type="hidden" name="librarysampleid" value="${sc.sample.sampleId}"> 
  <select class="selectLane" name="lanesampleid"></select>
  <input type="submit" value="assign">
</form>
</div>
</div>
                  </c:if>
                  <c:forEach items="${sc.sample.sampleSourceViaSourceSampleId}" var="schild">
                    <div class="samplechild">
                    <c:if test="${schild.sample.typeSample.IName == 'library'}">
                        <label>One Deep Libray</label>
                        <c:out value="${schild.sample.name}" />
<div>
<a href="javascript:{}" onclick="showAssignForm(this)">(+)</a>
<div style="display:none">
<form method="POST" action="<c:url value="/facility/platformunit/assignAdd.do" />">
  <input type="hidden" name="librarysampleid" value="${schild.sample.sampleId}"> 
  <select class="selectLane" name="lanesampleid"></select>
  <input type="submit" value="assign">
</form>
</div>
</div>
                    </c:if>
                    <c:if test="${schild.sample.typeSample.IName == 'lane'}">
                        <label>One Deep Lane</label>
                        <c:out value="${schild.sample.name}" />
<div>
<a href="javascript:{}" onclick="showAssignForm(this)">(-)</a>
<div style="display:none">
  <form method="POST" action="<c:url value="/facility/platformunit/assignRemove.do" />">
	
  <input type="hidden" name="samplesourceid" value="<c:out value="${schild.sampleSourceId}" />">
  <input type="submit" value="Remove">
  </form>
</div>
</div>
                    </c:if>
                      <c:forEach items="${schild.sample.sampleSourceViaSourceSampleId}" var="schild2">
                        <c:if test="${schild2.sample.typeSample.IName == 'lane'}">
                          <div class="samplechild2">
                            <label>Two Deep</label>
                              <c:out value="${schild2.sample.name}" />
<div>
<a href="javascript:{}" onclick="showAssignForm(this)">(-)</a>
<div style="display:none">
  <form method="POST" action="<c:url value="/facility/platformunit/assignRemove.do" />">
	
  <input type="hidden" name="samplesourceid" value="<c:out value="${schild.sampleSourceId}" />">
  <input type="submit" value="Remove">
  </form>
</div>
</div>
                          </div>
                        </c:if>
                      </c:forEach>
                    </div>
                  </c:forEach>
               </div>
            </div>
          </c:forEach>
        </div>
      </c:forEach>
    </div>
  </c:forEach>
</div>

<div class="platformUnitSide">
  <c:forEach items="${platformUnitStates}" var="state">
  <c:forEach items="${state.statesample}" var="stateSample">
    <c:set var="pu" value="${stateSample.sample}" />
    <div class="platformunit">
      <label>PU</label>
      <c:out value="${pu.name}" /> 
      <c:forEach items="${pu.sampleSource}" var="puparent">
         <div class="cell">
           <label>Cells</label>
           <c:out value="${puparent.sampleSourceId}" /> 
           <c:out value="${puparent.sampleViaSource.name}" /> 

           <c:forEach items="${puparent.sampleViaSource.sampleSource}" var="lib">
             <div class="library">
               <label>Library</label><c:out value="${lib.sampleViaSource.name}" /> 
             </div>
           </c:forEach>
         </div>
      </c:forEach>
    </div>
  </c:forEach>
  </c:forEach>
</div>

<div style="clear:both" />
</div><!-- /assignmentContent -->


<script>
  var lanes = $(".selectLane");
  <c:forEach items="${platformUnitStates}" var="state">
  <c:forEach items="${state.statesample}" var="stateSample">
    <c:set var="pu" value="${stateSample.sample}" />
lanes.append($('<option></option>').val('<c:out value="0" />').html('<b><c:out value="${pu.name}" /></b>'));
      <c:forEach items="${pu.sampleSource}" var="puparent">
         lanes.append($('<option></option>').val('<c:out value="${puparent.sampleViaSource.sampleId}" />').html(' - <c:out value="${puparent.sampleViaSource.name}" />'));
      </c:forEach>
  </c:forEach>
  </c:forEach>
</script>

<% /* TODO  javascript to read selected SampleId and scroll to it */ %>

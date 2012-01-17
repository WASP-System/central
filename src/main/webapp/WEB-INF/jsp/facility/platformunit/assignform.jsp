<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

HELLO WORLD

<style>
DIV {margin-left: 10px; }

</style>

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
                  <c:forEach items="${sc.sample.sampleSourceViaSourceSampleId}" var="schild">
                    <div class="samplechild">
                      <label>One Deep</label>
                      <c:forEach items="${schild.sample.sampleSourceViaSourceSampleId}" var="schild2">
                        <div class="samplechild2">
                          <label>Two Deep</label>
                        </div>
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

<hr>

<div class="platformUnitSide">
  <c:forEach items="${platformunits}" var="pu">
    <div class="platformunit">
      <label>PU</label>
      <c:out value="${pu.name}" /> 
      <c:forEach items="${pu.sampleSource}" var="puparent">
         <div class="cell">
           <label>Cells</label>
           <c:out value="${puparent.sampleSourceId}" /> 
           <c:out value="${puparent.sampleViaSource.name}" /> 

[           <c:out value="${puparent.sample.sampleId}" /> 
           <c:out value="${puparent.sample.typeSample.name}" />  |
           <c:out value="${puparent.sampleViaSource.sampleId}" /> 
           <c:out value="${puparent.sampleViaSource.typeSample.name}" />  ]

           <c:forEach items="${puparent.sampleViaSource.sampleSource}" var="lib">
             <div class="library">
               <c:out value="${lib.sample.name}" /> 
             </div>
           </c:forEach>
         </div>
      </c:forEach>
    </div>
  </c:forEach>
</div>

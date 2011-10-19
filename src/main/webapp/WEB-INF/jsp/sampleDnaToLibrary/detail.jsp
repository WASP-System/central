<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

hello 


[ <c:out value="${sampleDnaToLibSampleMeta}" /> ]

   <table>
   <c:set var="_area" value = "${parentarea}" scope="request"/>
   <c:set var="_metaArea" value = "${area}" scope="request"/>

   <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />

   <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
   </table>

    <form:form command="sample">
       <table>
   <c:set var="_area" value = "${parentarea}" scope="request"/>
   <c:set var="_metaArea" value = "${area}" scope="request"/>

   <c:set var="_metaList" value = "${sampleDnaToLibSampleMeta}" scope="request" />
   <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr>
              <td colspan="2" align=right>
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>

       </table>

    </form:form>



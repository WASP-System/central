<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%@ page import="edu.yu.einstein.wasp.model.Job" %>
<%@ page import="edu.yu.einstein.wasp.model.JobMeta" %>
<%@ page import="edu.yu.einstein.wasp.model.JobResourcecategory" %>
<%@ page import="edu.yu.einstein.wasp.model.ResourceCategory" %>
<%@ page import="edu.yu.einstein.wasp.model.JobSoftware" %>
<%@ page import="edu.yu.einstein.wasp.model.Software" %>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>

<wasp:message />

<c:if test="${baseMetas.size()> 0}" >
<section>
<h1>Base</h1>
  <c:set var="_area" value = "${resourceCategory.IName}" scope="request"/>
  <c:set var="_metaArea" value = "${resourceCategory.IName}" scope="request"/>
  <c:set var="_metaList" value = "${baseMetas}" scope="request" />

  <form:form>
  <table class="data">
    <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
  </table>
  </form:form>
</section> 
</c:if>

<section>
<h1>Resource</h1>
<c:forEach items="${resourceMap}" var="entry">
  <c:set var="resourceCategory" value="${entry.key}" />
  <c:set var="jobMetas" value="${entry.value}" />

  <h2><c:out value="${resourceCategory.name}" /></h2>
  <c:if test="${jobMetas.size()> 0}" >
    <c:set var="_area" value = "${resourceCategory.IName}" scope="request"/>
    <c:set var="_metaArea" value = "${resourceCategory.IName}" scope="request"/>
    <c:set var="_metaList" value = "${jobMetas}" scope="request" />
    <c:set var="resourceOptions" value = "${resourceOptionsMap[entry.key]}" scope="request" />

    <form:form>
    <table class="data">
      <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
    </table>
    </form:form>
  </c:if>
</c:forEach>
</section>


<section>
<h1>Software</h1>
<c:forEach items="${softwareMap}" var="entry">
  <c:set var="software" value="${entry.key}" />
  <c:set var="jobMetas" value="${entry.value}" />

  <h2><c:out value="${software.name}" /></h2>
  <c:if test="${jobMetas.size()> 0}" >
    <c:set var="_area" value = "${software.IName}" scope="request"/>
    <c:set var="_metaArea" value = "${software.IName}" scope="request"/>
    <c:set var="_metaList" value = "${jobMetas}" scope="request" />

    <form:form>
    <table class="data">
      <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
    </table>
    </form:form>
  </c:if>
</c:forEach>
</section>


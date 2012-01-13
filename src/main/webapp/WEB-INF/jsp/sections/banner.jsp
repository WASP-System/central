<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <!-- top -->
  <sec:authorize access="isAuthenticated()">
    <header id="pageHeader">
      <nav>
        <a href="/wasp/j_spring_security_logout" class="button right">Logout</a>
        <a href="/wasp/dashboard.do"><img src="/wasp/css/wasp_logo.jpg" alt="WASP" width="154" height="107" /></a>
      </nav>
    </header>
  </sec:authorize>
  <!-- /top -->


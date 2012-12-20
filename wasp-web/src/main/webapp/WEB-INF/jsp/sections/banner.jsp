<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <!-- top -->
 
  <sec:authorize access="isAuthenticated()">
    <header id="pageHeaderLoggedIn">
      <nav>
      	<div class="header_nav">
      		<a href="/wasp/dashboard.do" ><fmt:message key="sections.banner_dashboard.label" /></a> | 
      		<a href="http://waspsystem.org/documentation" >Documentation</a> | 
        	<a href="/wasp/j_spring_security_logout" ><fmt:message key="sections.banner_logout.label" /></a>
        </div>
        <a href="/wasp/dashboard.do"><img src="/wasp/images/waspSystemLogo_108x80.png" alt="WASP System" /></a>
      </nav>
    </header>
  </sec:authorize>
  <sec:authorize access="! isAuthenticated()">
    <header id="pageHeaderNotLoggedIn">
      <nav>
      	<div class="header_nav">
      		<a href="http://waspsystem.org/documentation" >Documentation</a>
        </div>
        <a href="/wasp/"><img src="/wasp/images/waspSystemLogo_108x80.png" alt="WASP System" /></a>
      </nav>
    </header>
  </sec:authorize>

  <!-- /top -->


<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <!-- top -->
 
  <sec:authorize access="isAuthenticated()">
    <header id="pageHeaderLoggedIn">
      <nav>
      	<div class="header_panel_right">
      		<div class="waspIcon"><a href="http://waspsystem.org"><img src="<c:url value='images/waspSystemLogoLong_200x60.png' />" alt="Wasp System Website" height="40" /></a></div>
        	<div class="header_nav">
        		<wasp:displayInDemo><a href="http://waspsystem.org/documentation" >Documentation</a> | </wasp:displayInDemo>
        		<a href="<c:url value='j_spring_security_logout' />" ><fmt:message key="sections.banner_logout.label" /></a>
        	</div>
        </div>
        <a href="<c:url value='dashboard.do' />"><img src='/wasp<spring:eval expression="@siteProperties.getProperty('wasp.customimage.logo')" />' alt="Home Page" height="80"/></a>
      </nav>
    </header>
  </sec:authorize>
  <sec:authorize access="! isAuthenticated()">
    <header id="pageHeaderNotLoggedIn">
      <nav>
      	<div class="header_panel_right">
      		<div class="waspIcon"><a href="http://waspsystem.org"><img src="<c:url value='images/waspSystemLogoLong_200x60.png' />" alt="Wasp System Website" height="40" /></a></div>
        	<div class="header_nav">
      			<wasp:displayInDemo><a href="http://waspsystem.org/documentation" >Documentation</a></wasp:displayInDemo>
        	</div>
        </div>
        <a href="<c:url value='dashboard.do' />"><img src='/wasp<spring:eval expression="@siteProperties.getProperty('wasp.customimage.logo')" />' alt="Home Page" height="80"/></a>
      </nav>
    </header>
  </sec:authorize>

  <!-- /top -->


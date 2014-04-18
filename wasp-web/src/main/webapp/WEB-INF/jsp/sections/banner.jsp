<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <!-- top -->
  <c:set var="customLogoImage"><spring:eval expression="@siteProperties.getProperty('wasp.customimage.logo')" /></c:set>
  <sec:authorize access="isAuthenticated()">
    <header id="pageHeaderLoggedIn">
      <nav>
      	<div class="header_panel_right">
      		<div class="waspIcon"><a href="http://waspsystem.org"><img src="<wasp:relativeUrl value='images/waspSystemLogoLong_200x60.png' />" alt="Wasp System Website" height="40" /></a></div>
        	<div class="header_nav">
        		<wasp:displayInDemo><a href="http://waspsystem.org/documentation" >Documentation</a> | </wasp:displayInDemo>
        		<a href="<wasp:relativeUrl value='j_spring_security_logout' />" ><fmt:message key="sections.banner_logout.label" /></a>
        	</div>
        </div>
        <a href="<wasp:relativeUrl value='dashboard.do' />"><img src='<wasp:relativeUrl value='${customLogoImage}' />' alt="Home Page" height="80"/></a>
      </nav>
    </header>
  </sec:authorize>
  <sec:authorize access="! isAuthenticated()">
    <header id="pageHeaderNotLoggedIn">
      <nav>
      	<div class="header_panel_right">
      		<div class="waspIcon"><a href="http://waspsystem.org"><img src="<wasp:relativeUrl value='images/waspSystemLogoLong_200x60.png' />" alt="Wasp System Website" height="40" /></a></div>
        	<div class="header_nav">
      			<wasp:displayInDemo><a href="http://waspsystem.org/documentation" >Documentation</a></wasp:displayInDemo>
        	</div>
        </div>
        <a href="<wasp:relativeUrl value='dashboard.do' />"><img src='<wasp:relativeUrl value='${customLogoImage}' />' alt="Home Page" height="80"/></a>
      </nav>
    </header>
  </sec:authorize>

  <!-- /top -->


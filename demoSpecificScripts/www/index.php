<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>WASP System Developer Documentation</title>
  <link rel="stylesheet" type="text/css" media="screen" href="css/base.css" />
</head>

<body>
	<div id="container">
  		<div id="header">
		    <header id="pageHeaderLoggedIn">
		      <div>
		        <a href="http://waspsystem.org/wasp/dashboard.do"><img src="images/waspSystemLogo_108x80.png" alt="WASP System" /></a>
		        <div class="demo_warning">
		        	A work in progress - this documentation is not by any means complete. 
		        </div>
		      </div>
		    </header>
		</div>
  		<div id="content">

			<h1>Developer Documentation</h1>
			<div>
				<ul>
					<li><a href="/documentation/docbkx/pdf/wasp-dev.pdf">PDF version</a></li>
					
					<li><a href="/documentation/docbkx/html/wasp-dev.html">HTML version</a></li>
				</ul>
			</div>
			<h1>Javadoc</h1>
			<div>
				<ul>
					<?php
						if ($handle = opendir('/var/www/html/documentation')) {
    						while(false !== ($componentName = readdir($handle))) {
								if (preg_match("/^wasp-/", $componentName) && file_exists("/var/www/html/documentation/$componentName/apidocs/index.html")){
	        							print "<li><a href='/documentation/$componentName/apidocs/index.html'>$componentName</a></li>";
								}
    						}
    						closedir($handle);
						}
					?>
					<li>Plugins
						<ul>
							<?php
								if ($handle = opendir('/var/www/html/documentation/wasp-plugins')) {
		    						while(false !== ($pluginName = readdir($handle))) {
										if (! preg_match("/^\./", $pluginName) && file_exists("/var/www/html/documentation/wasp-plugins/$pluginName/apidocs/index.html")){
			        							print "<li><a href='/documentation/wasp-plugins/$pluginName/apidocs/index.html'>$pluginName</a></li>";
										}
		    						}
		    						closedir($handle);
								}
							?>
						</ul>
					</li>
				</ul>
			</div>
		</div>
  		<div id="footer">
			<center>
	&copy; Albert Einstein College of Medicine (2012). Distributed freely under the terms of the <a href="http://www.gnu.org/licenses/agpl-3.0.html" target="_blank">GNU AFFERO General Public License</a>.
	<br>
	Core WASP System and plugin repository maintained by Albert Einstein College of Medicine Computational Epigenomics &amp; Genomics Cores (<a href="http://waspsystem.org/"  target="_blank">waspsystem.org</a>).
			</center> 
		</div>
	</div>
</body>
</html>
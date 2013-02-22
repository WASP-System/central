



 











<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title> 	 	
    Illumina OLB Stats: Focus Quality Charts 
  </title>
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/reset.css" />
  <link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
  <link rel="stylesheet" type="text/css" href="/wasp/css/tree-interactive.css" />
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/menu.css" />
  <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script type="text/javascript" src="http://code.jquery.com/ui/1.10.0/jquery-ui.js"></script> 
  
  <script type="text/javascript">
  
  function waspTooltip(){
		$( ".tooltip" ).tooltip({
	  	      position: {
	  	        my: "center bottom-20",
	  	        at: "center top",
	  	        using: function( position, feedback ) {
	  	          $( this ).css( position );
	  	          $( "<div>" )
	  	            .addClass( "arrow" )
	  	            .addClass( feedback.vertical )
	  	            .addClass( feedback.horizontal )
	  	            .appendTo( this );
	  	        }
	  	      }
	  	    });
	}

	$( document ).ready( function(){
		waspTooltip();
  		waspFade("waspErrorMessage");
  		waspFade("waspMessage");
  		waspOnLoad();
  	});
  
  	function waspFade(el, msg) {
		if (msg != null && msg != ""){
			$('#'+el).html(msg);
		}
		if ($('#'+el).html() == ''){
			$('#'+el).hide();
		} else {
			$('#'+el).show();
			setTimeout(function() {
				$('#'+el).fadeOut('slow',
					function() {
						// after fadeout do the following
						$('#'+el).html('');
					});
			},7500);
		}
	}
  
    var waspOnLoad=function() {
      // re-define the waspOnLoad var 
      // in head-js if you need custom body 
      // onLoad function
      
    }
  </script>
  











<style>
	
	
	.preloadHidden {
		display:none;
	}
	
	.errorHeader{
		background-color:red;
		background:red;
	}
	
	
	#error_dialog-modal .ui-icon
	{
	    background-image: url(/wasp/css/jquery/images/ui-icons_c91313_256x240.png);
	}
	
	.center
	{
		margin-left:auto;
		margin-right:auto;
		display: block;
	}

	td.formLabel {
		text-align: right;
		color: #191975;
		font-size: 14px;
	}
	

	#slider_frame {
		font-family: Arial, Verdana, "Times New Roman", Times, serif;
		font-size: 12px;
		color: #525252;
		margin-left: 15px;
		margin-right: 15px;
		margin-top: 15px;
		
	}
	#slider .ui-slider-range { background: #CCCCCC; }
	#slider .ui-slider-handle { border-color: #525252; }
	
	#slider {
		width: 800px;
		float:left;
		margin-top:10px;
		margin-right:10px;
	}
	
	#cycle_number{
		float: left;
		width: 150px;
		margin-top:8px;
		
	}
	
	#intA, #intC, #intG, #intT {
		float: left;
		width: 260px;
		height: 695px;
		margin: 15px;
	}
	
	#intA{
		clear: left;
		background-image:url(http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_a.png);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intC{
		background-image:url(http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_c.png);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intT{
		background-image:url(http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_t.png);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intG{
		background-image:url(http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_g.png);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#loading_dialog-modal{
		padding:15px;
		font-size: 14px;
	}
	
	#error_dialog-modal
	{
		padding:15px;
		font-size: 14px;
	}

	
</style>
<script>
	
	function validate(){
		TOTAL_LANES = 8;
		incomplete = false;
		message = "You must choose either 'Pass' or 'Fail' for ALL lanes. You have not yet scored lanes ";
		for (i=1; i <= TOTAL_LANES; i++){
			laneResult = $("input[name=radioL" + i + "]:checked").val();
			if (laneResult == null){
				message += i + ", ";
				incomplete = true;
			}
		}
		if (incomplete){
			message = message.substring(0, message.length-2); // trim trailing comma and space 
			message += ".";
			$( "#warningText" ).html(message);
			$( "#error_dialog-modal" ).dialog("open");
		}
		else{
			$( "#selectionWindow").hide();
			$( "#qualityForm").submit();
		}
	}

	function updateOnSlide( value_update ){
		$( "#amount" ).val( value_update );
		$( "#intA" ).css("background-image", "url(http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_" + value_update + "_a.png)");
		$( "#intC" ).css("background-image", "url(http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_" + value_update + "_c.png)");
		$( "#intT" ).css("background-image", "url(http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_" + value_update + "_t.png)");
		$( "#intG" ).css("background-image", "url(http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_" + value_update + "_g.png)");
	}	

	$( document ).load( function(){
		$( "#main" ).show();
		$( '#loading_dialog-modal' ).dialog( 'close' );
	});
		
	$(function() {
		$( "#main" ).hide();
		$( "#selectionWindow" ).hide();
		
		$( "#radioL1" ).buttonset();
		$( "#radioL2" ).buttonset();
		$( "#radioL3" ).buttonset();
		$( "#radioL4" ).buttonset();
		$( "#radioL5" ).buttonset();
		$( "#radioL6" ).buttonset();
		$( "#radioL7" ).buttonset();
		$( "#radioL8" ).buttonset();

		$( "#submitForm" ).button()
					.click(function(){
						validate();
					});
		
		$( "#cancelForm" ).button()
					.click(function(){
						$( "#selectionWindow").hide();
					});
	
		$( "#showForm" ).button()
					.click(function(){
						$( "#selectionWindow").show();
					});

		$( "#selectionWindow" ).draggable({ handle: ".ui-widget-header" });
		
		$( "#slider" ).slider({
			range: "min",
			min: 1,
			max: 120,
			value: 1,
			slide: function( event, ui ) {
				updateOnSlide( ui.value );
			}
		});
		
		$( "#amount" ).val( $( "#slider" ).slider( "value" ) );
		$( "#loading_dialog-modal" ).dialog({
			height: 170,
			modal: true
			});

		$( "#error_dialog-modal" ).dialog({
			modal: true,
			height: 250,
			width: 350,
			autoOpen: false,
			buttons: {
				Ok: function() {
					$( this ).dialog( "close" ); // close modal when 'Ok' button pressed 
				}
			},
			open: function(){
				$(this).parents(".ui-dialog:first").find(".ui-dialog-titlebar").addClass("errorHeader"); // change header bar background color 
			}
		});
				
	});

	
</script>
</head>

<body >
	<div id="container">
  		<div id="header">
			











  <!-- top -->
 
  
    <header id="pageHeaderLoggedIn">
      <nav>
      	<div class="header_nav">
      		
        	<a href="/wasp/j_spring_security_logout" >Logout</a>
        </div>
        <a href="/wasp/dashboard.do"><img src="/wasp/images/waspSystemLogo_108x80.png" alt="WASP System" /></a>
      </nav>
    </header>
  
  

  <!-- /top -->


		</div>
		
			<div id="menu">
				









     

		<ul class="main_menu">
			<li class="main_menu-root">
				<a href='/wasp/dashboard.do'>Home</a>
			</li>
			<li class="main_menu-root">
				<a href="#">User</a>
				<ul>
					<li><a href='/wasp/user/me_ro.do'>My Profile</a></li>
					
						<li><a href='/wasp/user/mypassword.do'>Change Password</a></li>
					
					
				</ul>
			</li>
			
			
				<li class="main_menu-root">
					<a href="#">Jobs</a>
					<ul>
						
							<li><a href='/wasp/job/list.do'>All Jobs</a></li>
						
						
						
							<li><a href='/wasp/jobsubmit/list.do'>My Job Drafts</a></li>
						
						
							<li><a href='/wasp/jobsubmit/create.do'>Submit A New Job</a></li>
						
					</ul>
				</li>
			
			
				<li class="main_menu-root">
					<a href="#">Admin</a>
					<ul>
						
							<li><a href='/wasp/department/list.do'>Department Admin</a></li>
						
						<li><a href='/wasp/lab/list.do'>Labs</a></li>
						<li><a href='/wasp/job2quote/list_all.do'>Quotes</a></li>
						
							<li class="has-children">
								<a href="#">Tasks For Others</a>
								<ul>
									<li><a href='/wasp/task/daapprove/list.do'>Dept. Admin. Tasks</a></li>
									<li><a href='/wasp/task/piapprove/list.do'>PI/Manager Tasks</a></li>
								</ul>
							</li>
						
						
							<li class="has-children">
								<a href="#">Users</a>
								<ul>
									<li><a href='/wasp/user/list.do'>Regular Users</a></li>
									<li><a href='/wasp/sysrole/list.do'>System Users</a></li>
								</ul>
							</li>
							
								<li><a href='/wasp/plugin/listAll.do'>Web Plugins</a></li>
							
							<li><a href='/wasp/workflow/list.do'>Workflows</a></li>
						
					</ul>
				</li>
			
			
			
				<li class="main_menu-root">
					<a href="#">Facility</a>
					<ul>
						<li><a href="/wasp/resource/list.do">Machines</a></li>
						<li class="has-children">
							<a href="#">Platform Units</a>
							<ul>
								<li><a href='/wasp/facility/platformunit/list.do'>Platform Units</a></li>
								<li><a href='/wasp/facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=0&sampleId=0'>New Platform Unit</a></li>
								<li><a href='/wasp/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=0'>Assign Libraries</a></li>
							</ul>
						</li>
						<li class="has-children">
							<a href="#">Samples</a>
							<ul>
								<li><a href='/wasp/sample/list.do'>All Samples</a></li>
								<li><a href='/wasp/sample/listControlLibraries.do'>Control Libraries</a></li>
								<li><a href='/wasp/task/samplereceive/list.do'>Sample Receiver</a></li>
							</ul>
						</li>
						<li><a href='/wasp/run/list.do'>Sequence Runs</a></li>
					</ul>
				</li>
			
			
			
			
			<li class="main_menu-root">
				<a href='/wasp/task/myTaskList.do'>Tasks</a>
			</li>
		</ul>
			</div>
		
  		<div id="content"> 
  			<a href='/wasp/dashboard.do'>Home</a> &gt;&gt; <a href='/wasp/facility/platformunit/list.do'>Platform Unit List</a> &gt;&gt; <a href='/wasp/sample/listControlLibraries.do'>Control Libraries</a> &gt;&gt; <a href='/wasp/sample/createUpdateLibraryControl/0.do'>Create/Update Control Libraries</a> &gt;&gt; <a href='/wasp/sample/listControlLibraries.do'>Control Libraries</a>
  			<div id='waspErrorMessage' class='waspErrorMessage'></div>

  			<div id='waspMessage' class='waspMessage'></div>

			













<div id="loading_dialog-modal" title="Page Loading"  >
	<table border="0" cellpadding="5">
	<tr>
	<td><img src="/wasp/images/wasp-illumina/postRunQC/spinner.gif" align="left" border="0" ></td>
	<td>Please be patient while this page loads. All Focus Quality Charts are being pre-loaded so this may take a few seconds.</td>
	</tr>
	</table>
</div>

<div id="main" class="center">
	<p class="ui-state-default ui-corner-all ui-helper-clearfix" style="padding:4px; font-size: 14px">
		<span class="ui-icon ui-icon-info" style="float:left; margin:-2px 5px 0 0;"></span>
		Illumina OLB Stats: Focus Quality
	</p>
	<div id="slider_frame">
		<div id="cycle_number">
			<label for="amount">Cycle Number: </label>
			<input type="text" id="amount" style="border:0;font-weight:bold;" size="4" readonly="readonly"/>
		</div>
		<div id="slider" ></div>
		<div id="displayWindow" style="float:left; margin-left: 20px;"><button id="showForm">Continue</button></div>
	</div>
	<div id="intA" class="ui-widget-content ui-corner-all"></div>
	<div id="intC" class="ui-widget-content ui-corner-all"></div>
	<div id="intT" class="ui-widget-content ui-corner-all"></div>
	<div id="intG" class="ui-widget-content ui-corner-all"></div>
	
</div>	

<div id="error_dialog-modal" title="Warning" >
	<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;" ></span><span id="warningText"></span></p>
</div>


<div id="selectionWindow">
	<div class="ui-overlay" >
		<div class="ui-widget-shadow ui-corner-all" style="width: 302px; height: 542px; position: absolute; left:50%; margin-left: -148px; top:100px" ></div>
	</div>
	<div style="position: absolute; left:50%; margin-left: -150px; top:100px; width: 300px; height: 540px; padding: 2px;" class="ui-widget-content ui-corner-all">
		<div class="ui-widget-header ui-corner-all" style="font-size: 14px; padding:4px">Assessment of Lane Focus Quality</div>
		<div style="padding: 10px" class="verifyQualityForm">		
			<p>Please click either 'Pass' or 'Fail' for each lane based on your interpretation of the LANE FOCUS QUALITY charts only, then click the 'Continue' button.</p>
			<form id="qualityForm"  method="post">
			<table align="center">

					<tr>
						<td class="formLabel">Comments: </td>
						<td><textarea style="resize: none;" name="comments" id="comments" rows="5" cols="13" maxlength="100"></textarea></td>
					</tr>
				</table>
				</form>
				<br />
				<center><button id="submitForm">Continue</button><button id="cancelForm">Cancel</button></center>
		</div>
	</div>
</div>

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_a.png' alt='Cycle 1 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_c.png' alt='Cycle 2 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_t.png' alt='Cycle 3 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_g.png' alt='Cycle 4 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_1_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_2_a.png' alt='Cycle 5 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_2_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_2_c.png' alt='Cycle 6 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_2_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_2_t.png' alt='Cycle 7 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_2_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_2_g.png' alt='Cycle 8 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_2_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_3_a.png' alt='Cycle 9 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_3_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_3_c.png' alt='Cycle 10 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_3_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_3_t.png' alt='Cycle 11 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_3_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_3_g.png' alt='Cycle 12 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_3_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_4_a.png' alt='Cycle 13 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_4_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_4_c.png' alt='Cycle 14 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_4_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_4_t.png' alt='Cycle 15 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_4_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_4_g.png' alt='Cycle 16 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_4_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_5_a.png' alt='Cycle 17 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_5_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_5_c.png' alt='Cycle 18 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_5_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_5_t.png' alt='Cycle 19 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_5_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_5_g.png' alt='Cycle 20 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_5_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_6_a.png' alt='Cycle 21 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_6_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_6_c.png' alt='Cycle 22 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_6_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_6_t.png' alt='Cycle 23 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_6_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_6_g.png' alt='Cycle 24 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_6_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_7_a.png' alt='Cycle 25 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_7_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_7_c.png' alt='Cycle 26 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_7_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_7_t.png' alt='Cycle 27 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_7_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_7_g.png' alt='Cycle 28 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_7_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_8_a.png' alt='Cycle 29 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_8_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_8_c.png' alt='Cycle 30 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_8_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_8_t.png' alt='Cycle 31 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_8_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_8_g.png' alt='Cycle 32 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_8_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_9_a.png' alt='Cycle 33 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_9_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_9_c.png' alt='Cycle 34 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_9_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_9_t.png' alt='Cycle 35 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_9_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_9_g.png' alt='Cycle 36 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_9_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_10_a.png' alt='Cycle 37 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_10_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_10_c.png' alt='Cycle 38 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_10_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_10_t.png' alt='Cycle 39 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_10_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_10_g.png' alt='Cycle 40 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_10_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_11_a.png' alt='Cycle 41 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_11_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_11_c.png' alt='Cycle 42 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_11_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_11_t.png' alt='Cycle 43 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_11_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_11_g.png' alt='Cycle 44 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_11_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_12_a.png' alt='Cycle 45 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_12_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_12_c.png' alt='Cycle 46 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_12_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_12_t.png' alt='Cycle 47 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_12_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_12_g.png' alt='Cycle 48 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_12_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_13_a.png' alt='Cycle 49 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_13_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_13_c.png' alt='Cycle 50 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_13_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_13_t.png' alt='Cycle 51 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_13_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_13_g.png' alt='Cycle 52 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_13_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_14_a.png' alt='Cycle 53 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_14_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_14_c.png' alt='Cycle 54 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_14_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_14_t.png' alt='Cycle 55 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_14_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_14_g.png' alt='Cycle 56 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_14_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_15_a.png' alt='Cycle 57 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_15_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_15_c.png' alt='Cycle 58 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_15_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_15_t.png' alt='Cycle 59 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_15_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_15_g.png' alt='Cycle 60 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_15_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_16_a.png' alt='Cycle 61 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_16_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_16_c.png' alt='Cycle 62 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_16_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_16_t.png' alt='Cycle 63 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_16_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_16_g.png' alt='Cycle 64 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_16_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_17_a.png' alt='Cycle 65 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_17_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_17_c.png' alt='Cycle 66 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_17_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_17_t.png' alt='Cycle 67 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_17_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_17_g.png' alt='Cycle 68 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_17_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_18_a.png' alt='Cycle 69 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_18_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_18_c.png' alt='Cycle 70 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_18_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_18_t.png' alt='Cycle 71 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_18_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_18_g.png' alt='Cycle 72 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_18_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_19_a.png' alt='Cycle 73 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_19_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_19_c.png' alt='Cycle 74 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_19_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_19_t.png' alt='Cycle 75 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_19_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_19_g.png' alt='Cycle 76 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_19_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_20_a.png' alt='Cycle 77 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_20_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_20_c.png' alt='Cycle 78 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_20_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_20_t.png' alt='Cycle 79 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_20_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_20_g.png' alt='Cycle 80 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_20_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_21_a.png' alt='Cycle 81 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_21_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_21_c.png' alt='Cycle 82 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_21_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_21_t.png' alt='Cycle 83 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_21_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_21_g.png' alt='Cycle 84 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_21_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_22_a.png' alt='Cycle 85 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_22_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_22_c.png' alt='Cycle 86 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_22_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_22_t.png' alt='Cycle 87 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_22_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_22_g.png' alt='Cycle 88 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_22_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_23_a.png' alt='Cycle 89 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_23_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_23_c.png' alt='Cycle 90 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_23_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_23_t.png' alt='Cycle 91 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_23_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_23_g.png' alt='Cycle 92 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_23_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_24_a.png' alt='Cycle 93 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_24_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_24_c.png' alt='Cycle 94 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_24_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_24_t.png' alt='Cycle 95 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_24_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_24_g.png' alt='Cycle 96 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_24_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_25_a.png' alt='Cycle 97 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_25_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_25_c.png' alt='Cycle 98 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_25_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_25_t.png' alt='Cycle 99 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_25_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_25_g.png' alt='Cycle 100 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_25_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_26_a.png' alt='Cycle 101 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_26_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_26_c.png' alt='Cycle 102 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_26_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_26_t.png' alt='Cycle 103 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_26_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_26_g.png' alt='Cycle 104 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_26_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_27_a.png' alt='Cycle 105 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_27_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_27_c.png' alt='Cycle 106 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_27_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_27_t.png' alt='Cycle 107 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_27_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_27_g.png' alt='Cycle 108 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_27_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_28_a.png' alt='Cycle 109 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_28_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_28_c.png' alt='Cycle 110 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_28_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_28_t.png' alt='Cycle 111 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_28_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_28_g.png' alt='Cycle 112 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_28_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_29_a.png' alt='Cycle 113 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_29_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_29_c.png' alt='Cycle 114 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_29_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_29_t.png' alt='Cycle 115 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_29_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_29_g.png' alt='Cycle 116 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_29_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_30_a.png' alt='Cycle 117 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_30_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_30_c.png' alt='Cycle 118 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_30_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_30_t.png' alt='Cycle 119 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_30_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_30_g.png' alt='Cycle 120 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_30_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_31_a.png' alt='Cycle 121 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_31_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_31_c.png' alt='Cycle 122 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_31_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_31_t.png' alt='Cycle 123 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_31_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_31_g.png' alt='Cycle 124 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_31_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_32_a.png' alt='Cycle 125 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_32_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_32_c.png' alt='Cycle 126 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_32_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_32_t.png' alt='Cycle 127 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_32_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_32_g.png' alt='Cycle 128 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_32_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_33_a.png' alt='Cycle 129 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_33_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_33_c.png' alt='Cycle 130 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_33_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_33_t.png' alt='Cycle 131 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_33_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_33_g.png' alt='Cycle 132 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_33_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_34_a.png' alt='Cycle 133 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_34_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_34_c.png' alt='Cycle 134 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_34_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_34_t.png' alt='Cycle 135 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_34_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_34_g.png' alt='Cycle 136 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_34_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_35_a.png' alt='Cycle 137 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_35_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_35_c.png' alt='Cycle 138 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_35_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_35_t.png' alt='Cycle 139 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_35_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_35_g.png' alt='Cycle 140 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_35_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_36_a.png' alt='Cycle 141 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_36_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_36_c.png' alt='Cycle 142 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_36_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_36_t.png' alt='Cycle 143 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_36_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_36_g.png' alt='Cycle 144 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_36_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_37_a.png' alt='Cycle 145 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_37_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_37_c.png' alt='Cycle 146 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_37_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_37_t.png' alt='Cycle 147 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_37_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_37_g.png' alt='Cycle 148 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_37_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_38_a.png' alt='Cycle 149 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_38_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_38_c.png' alt='Cycle 150 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_38_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_38_t.png' alt='Cycle 151 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_38_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_38_g.png' alt='Cycle 152 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_38_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_39_a.png' alt='Cycle 153 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_39_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_39_c.png' alt='Cycle 154 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_39_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_39_t.png' alt='Cycle 155 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_39_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_39_g.png' alt='Cycle 156 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_39_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_40_a.png' alt='Cycle 157 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_40_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_40_c.png' alt='Cycle 158 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_40_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_40_t.png' alt='Cycle 159 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_40_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_40_g.png' alt='Cycle 160 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_40_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_41_a.png' alt='Cycle 161 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_41_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_41_c.png' alt='Cycle 162 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_41_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_41_t.png' alt='Cycle 163 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_41_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_41_g.png' alt='Cycle 164 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_41_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_42_a.png' alt='Cycle 165 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_42_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_42_c.png' alt='Cycle 166 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_42_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_42_t.png' alt='Cycle 167 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_42_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_42_g.png' alt='Cycle 168 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_42_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_43_a.png' alt='Cycle 169 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_43_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_43_c.png' alt='Cycle 170 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_43_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_43_t.png' alt='Cycle 171 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_43_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_43_g.png' alt='Cycle 172 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_43_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_44_a.png' alt='Cycle 173 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_44_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_44_c.png' alt='Cycle 174 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_44_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_44_t.png' alt='Cycle 175 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_44_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_44_g.png' alt='Cycle 176 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_44_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_45_a.png' alt='Cycle 177 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_45_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_45_c.png' alt='Cycle 178 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_45_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_45_t.png' alt='Cycle 179 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_45_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_45_g.png' alt='Cycle 180 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_45_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_46_a.png' alt='Cycle 181 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_46_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_46_c.png' alt='Cycle 182 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_46_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_46_t.png' alt='Cycle 183 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_46_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_46_g.png' alt='Cycle 184 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_46_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_47_a.png' alt='Cycle 185 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_47_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_47_c.png' alt='Cycle 186 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_47_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_47_t.png' alt='Cycle 187 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_47_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_47_g.png' alt='Cycle 188 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_47_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_48_a.png' alt='Cycle 189 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_48_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_48_c.png' alt='Cycle 190 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_48_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_48_t.png' alt='Cycle 191 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_48_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_48_g.png' alt='Cycle 192 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_48_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_49_a.png' alt='Cycle 193 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_49_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_49_c.png' alt='Cycle 194 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_49_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_49_t.png' alt='Cycle 195 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_49_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_49_g.png' alt='Cycle 196 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_49_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_50_a.png' alt='Cycle 197 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_50_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_50_c.png' alt='Cycle 198 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_50_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_50_t.png' alt='Cycle 199 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_50_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_50_g.png' alt='Cycle 200 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_50_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_51_a.png' alt='Cycle 201 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_51_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_51_c.png' alt='Cycle 202 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_51_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_51_t.png' alt='Cycle 203 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_51_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_51_g.png' alt='Cycle 204 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_51_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_52_a.png' alt='Cycle 205 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_52_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_52_c.png' alt='Cycle 206 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_52_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_52_t.png' alt='Cycle 207 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_52_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_52_g.png' alt='Cycle 208 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_52_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_53_a.png' alt='Cycle 209 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_53_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_53_c.png' alt='Cycle 210 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_53_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_53_t.png' alt='Cycle 211 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_53_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_53_g.png' alt='Cycle 212 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_53_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_54_a.png' alt='Cycle 213 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_54_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_54_c.png' alt='Cycle 214 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_54_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_54_t.png' alt='Cycle 215 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_54_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_54_g.png' alt='Cycle 216 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_54_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_55_a.png' alt='Cycle 217 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_55_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_55_c.png' alt='Cycle 218 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_55_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_55_t.png' alt='Cycle 219 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_55_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_55_g.png' alt='Cycle 220 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_55_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_56_a.png' alt='Cycle 221 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_56_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_56_c.png' alt='Cycle 222 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_56_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_56_t.png' alt='Cycle 223 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_56_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_56_g.png' alt='Cycle 224 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_56_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_57_a.png' alt='Cycle 225 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_57_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_57_c.png' alt='Cycle 226 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_57_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_57_t.png' alt='Cycle 227 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_57_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_57_g.png' alt='Cycle 228 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_57_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_58_a.png' alt='Cycle 229 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_58_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_58_c.png' alt='Cycle 230 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_58_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_58_t.png' alt='Cycle 231 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_58_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_58_g.png' alt='Cycle 232 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_58_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_59_a.png' alt='Cycle 233 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_59_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_59_c.png' alt='Cycle 234 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_59_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_59_t.png' alt='Cycle 235 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_59_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_59_g.png' alt='Cycle 236 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_59_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_60_a.png' alt='Cycle 237 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_60_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_60_c.png' alt='Cycle 238 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_60_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_60_t.png' alt='Cycle 239 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_60_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_60_g.png' alt='Cycle 240 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_60_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_61_a.png' alt='Cycle 241 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_61_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_61_c.png' alt='Cycle 242 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_61_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_61_t.png' alt='Cycle 243 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_61_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_61_g.png' alt='Cycle 244 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_61_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_62_a.png' alt='Cycle 245 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_62_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_62_c.png' alt='Cycle 246 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_62_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_62_t.png' alt='Cycle 247 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_62_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_62_g.png' alt='Cycle 248 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_62_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_63_a.png' alt='Cycle 249 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_63_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_63_c.png' alt='Cycle 250 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_63_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_63_t.png' alt='Cycle 251 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_63_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_63_g.png' alt='Cycle 252 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_63_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_64_a.png' alt='Cycle 253 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_64_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_64_c.png' alt='Cycle 254 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_64_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_64_t.png' alt='Cycle 255 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_64_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_64_g.png' alt='Cycle 256 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_64_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_65_a.png' alt='Cycle 257 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_65_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_65_c.png' alt='Cycle 258 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_65_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_65_t.png' alt='Cycle 259 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_65_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_65_g.png' alt='Cycle 260 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_65_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_66_a.png' alt='Cycle 261 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_66_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_66_c.png' alt='Cycle 262 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_66_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_66_t.png' alt='Cycle 263 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_66_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_66_g.png' alt='Cycle 264 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_66_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_67_a.png' alt='Cycle 265 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_67_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_67_c.png' alt='Cycle 266 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_67_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_67_t.png' alt='Cycle 267 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_67_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_67_g.png' alt='Cycle 268 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_67_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_68_a.png' alt='Cycle 269 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_68_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_68_c.png' alt='Cycle 270 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_68_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_68_t.png' alt='Cycle 271 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_68_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_68_g.png' alt='Cycle 272 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_68_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_69_a.png' alt='Cycle 273 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_69_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_69_c.png' alt='Cycle 274 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_69_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_69_t.png' alt='Cycle 275 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_69_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_69_g.png' alt='Cycle 276 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_69_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_70_a.png' alt='Cycle 277 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_70_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_70_c.png' alt='Cycle 278 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_70_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_70_t.png' alt='Cycle 279 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_70_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_70_g.png' alt='Cycle 280 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_70_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_71_a.png' alt='Cycle 281 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_71_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_71_c.png' alt='Cycle 282 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_71_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_71_t.png' alt='Cycle 283 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_71_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_71_g.png' alt='Cycle 284 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_71_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_72_a.png' alt='Cycle 285 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_72_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_72_c.png' alt='Cycle 286 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_72_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_72_t.png' alt='Cycle 287 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_72_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_72_g.png' alt='Cycle 288 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_72_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_73_a.png' alt='Cycle 289 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_73_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_73_c.png' alt='Cycle 290 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_73_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_73_t.png' alt='Cycle 291 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_73_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_73_g.png' alt='Cycle 292 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_73_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_74_a.png' alt='Cycle 293 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_74_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_74_c.png' alt='Cycle 294 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_74_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_74_t.png' alt='Cycle 295 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_74_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_74_g.png' alt='Cycle 296 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_74_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_75_a.png' alt='Cycle 297 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_75_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_75_c.png' alt='Cycle 298 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_75_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_75_t.png' alt='Cycle 299 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_75_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_75_g.png' alt='Cycle 300 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_75_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_76_a.png' alt='Cycle 301 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_76_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_76_c.png' alt='Cycle 302 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_76_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_76_t.png' alt='Cycle 303 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_76_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_76_g.png' alt='Cycle 304 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_76_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_77_a.png' alt='Cycle 305 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_77_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_77_c.png' alt='Cycle 306 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_77_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_77_t.png' alt='Cycle 307 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_77_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_77_g.png' alt='Cycle 308 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_77_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_78_a.png' alt='Cycle 309 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_78_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_78_c.png' alt='Cycle 310 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_78_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_78_t.png' alt='Cycle 311 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_78_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_78_g.png' alt='Cycle 312 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_78_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_79_a.png' alt='Cycle 313 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_79_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_79_c.png' alt='Cycle 314 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_79_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_79_t.png' alt='Cycle 315 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_79_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_79_g.png' alt='Cycle 316 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_79_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_80_a.png' alt='Cycle 317 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_80_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_80_c.png' alt='Cycle 318 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_80_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_80_t.png' alt='Cycle 319 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_80_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_80_g.png' alt='Cycle 320 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_80_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_81_a.png' alt='Cycle 321 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_81_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_81_c.png' alt='Cycle 322 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_81_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_81_t.png' alt='Cycle 323 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_81_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_81_g.png' alt='Cycle 324 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_81_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_82_a.png' alt='Cycle 325 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_82_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_82_c.png' alt='Cycle 326 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_82_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_82_t.png' alt='Cycle 327 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_82_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_82_g.png' alt='Cycle 328 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_82_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_83_a.png' alt='Cycle 329 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_83_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_83_c.png' alt='Cycle 330 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_83_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_83_t.png' alt='Cycle 331 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_83_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_83_g.png' alt='Cycle 332 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_83_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_84_a.png' alt='Cycle 333 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_84_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_84_c.png' alt='Cycle 334 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_84_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_84_t.png' alt='Cycle 335 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_84_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_84_g.png' alt='Cycle 336 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_84_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_85_a.png' alt='Cycle 337 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_85_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_85_c.png' alt='Cycle 338 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_85_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_85_t.png' alt='Cycle 339 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_85_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_85_g.png' alt='Cycle 340 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_85_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_86_a.png' alt='Cycle 341 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_86_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_86_c.png' alt='Cycle 342 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_86_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_86_t.png' alt='Cycle 343 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_86_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_86_g.png' alt='Cycle 344 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_86_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_87_a.png' alt='Cycle 345 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_87_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_87_c.png' alt='Cycle 346 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_87_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_87_t.png' alt='Cycle 347 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_87_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_87_g.png' alt='Cycle 348 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_87_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_88_a.png' alt='Cycle 349 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_88_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_88_c.png' alt='Cycle 350 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_88_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_88_t.png' alt='Cycle 351 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_88_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_88_g.png' alt='Cycle 352 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_88_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_89_a.png' alt='Cycle 353 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_89_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_89_c.png' alt='Cycle 354 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_89_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_89_t.png' alt='Cycle 355 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_89_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_89_g.png' alt='Cycle 356 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_89_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_90_a.png' alt='Cycle 357 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_90_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_90_c.png' alt='Cycle 358 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_90_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_90_t.png' alt='Cycle 359 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_90_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_90_g.png' alt='Cycle 360 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_90_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_91_a.png' alt='Cycle 361 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_91_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_91_c.png' alt='Cycle 362 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_91_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_91_t.png' alt='Cycle 363 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_91_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_91_g.png' alt='Cycle 364 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_91_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_92_a.png' alt='Cycle 365 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_92_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_92_c.png' alt='Cycle 366 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_92_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_92_t.png' alt='Cycle 367 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_92_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_92_g.png' alt='Cycle 368 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_92_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_93_a.png' alt='Cycle 369 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_93_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_93_c.png' alt='Cycle 370 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_93_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_93_t.png' alt='Cycle 371 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_93_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_93_g.png' alt='Cycle 372 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_93_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_94_a.png' alt='Cycle 373 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_94_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_94_c.png' alt='Cycle 374 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_94_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_94_t.png' alt='Cycle 375 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_94_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_94_g.png' alt='Cycle 376 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_94_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_95_a.png' alt='Cycle 377 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_95_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_95_c.png' alt='Cycle 378 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_95_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_95_t.png' alt='Cycle 379 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_95_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_95_g.png' alt='Cycle 380 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_95_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_96_a.png' alt='Cycle 381 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_96_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_96_c.png' alt='Cycle 382 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_96_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_96_t.png' alt='Cycle 383 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_96_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_96_g.png' alt='Cycle 384 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_96_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_97_a.png' alt='Cycle 385 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_97_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_97_c.png' alt='Cycle 386 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_97_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_97_t.png' alt='Cycle 387 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_97_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_97_g.png' alt='Cycle 388 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_97_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_98_a.png' alt='Cycle 389 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_98_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_98_c.png' alt='Cycle 390 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_98_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_98_t.png' alt='Cycle 391 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_98_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_98_g.png' alt='Cycle 392 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_98_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_99_a.png' alt='Cycle 393 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_99_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_99_c.png' alt='Cycle 394 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_99_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_99_t.png' alt='Cycle 395 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_99_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_99_g.png' alt='Cycle 396 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_99_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_100_a.png' alt='Cycle 397 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_100_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_100_c.png' alt='Cycle 398 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_100_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_100_t.png' alt='Cycle 399 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_100_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_100_g.png' alt='Cycle 400 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_100_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_101_a.png' alt='Cycle 401 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_101_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_101_c.png' alt='Cycle 402 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_101_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_101_t.png' alt='Cycle 403 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_101_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_101_g.png' alt='Cycle 404 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_101_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_102_a.png' alt='Cycle 405 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_102_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_102_c.png' alt='Cycle 406 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_102_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_102_t.png' alt='Cycle 407 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_102_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_102_g.png' alt='Cycle 408 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_102_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_103_a.png' alt='Cycle 409 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_103_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_103_c.png' alt='Cycle 410 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_103_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_103_t.png' alt='Cycle 411 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_103_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_103_g.png' alt='Cycle 412 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_103_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_104_a.png' alt='Cycle 413 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_104_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_104_c.png' alt='Cycle 414 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_104_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_104_t.png' alt='Cycle 415 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_104_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_104_g.png' alt='Cycle 416 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_104_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_105_a.png' alt='Cycle 417 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_105_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_105_c.png' alt='Cycle 418 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_105_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_105_t.png' alt='Cycle 419 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_105_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_105_g.png' alt='Cycle 420 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_105_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_106_a.png' alt='Cycle 421 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_106_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_106_c.png' alt='Cycle 422 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_106_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_106_t.png' alt='Cycle 423 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_106_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_106_g.png' alt='Cycle 424 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_106_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_107_a.png' alt='Cycle 425 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_107_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_107_c.png' alt='Cycle 426 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_107_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_107_t.png' alt='Cycle 427 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_107_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_107_g.png' alt='Cycle 428 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_107_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_108_a.png' alt='Cycle 429 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_108_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_108_c.png' alt='Cycle 430 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_108_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_108_t.png' alt='Cycle 431 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_108_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_108_g.png' alt='Cycle 432 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_108_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_109_a.png' alt='Cycle 433 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_109_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_109_c.png' alt='Cycle 434 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_109_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_109_t.png' alt='Cycle 435 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_109_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_109_g.png' alt='Cycle 436 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_109_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_110_a.png' alt='Cycle 437 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_110_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_110_c.png' alt='Cycle 438 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_110_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_110_t.png' alt='Cycle 439 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_110_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_110_g.png' alt='Cycle 440 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_110_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_111_a.png' alt='Cycle 441 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_111_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_111_c.png' alt='Cycle 442 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_111_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_111_t.png' alt='Cycle 443 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_111_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_111_g.png' alt='Cycle 444 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_111_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_112_a.png' alt='Cycle 445 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_112_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_112_c.png' alt='Cycle 446 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_112_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_112_t.png' alt='Cycle 447 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_112_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_112_g.png' alt='Cycle 448 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_112_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_113_a.png' alt='Cycle 449 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_113_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_113_c.png' alt='Cycle 450 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_113_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_113_t.png' alt='Cycle 451 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_113_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_113_g.png' alt='Cycle 452 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_113_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_114_a.png' alt='Cycle 453 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_114_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_114_c.png' alt='Cycle 454 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_114_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_114_t.png' alt='Cycle 455 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_114_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_114_g.png' alt='Cycle 456 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_114_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_115_a.png' alt='Cycle 457 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_115_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_115_c.png' alt='Cycle 458 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_115_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_115_t.png' alt='Cycle 459 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_115_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_115_g.png' alt='Cycle 460 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_115_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_116_a.png' alt='Cycle 461 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_116_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_116_c.png' alt='Cycle 462 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_116_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_116_t.png' alt='Cycle 463 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_116_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_116_g.png' alt='Cycle 464 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_116_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_117_a.png' alt='Cycle 465 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_117_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_117_c.png' alt='Cycle 466 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_117_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_117_t.png' alt='Cycle 467 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_117_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_117_g.png' alt='Cycle 468 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_117_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_118_a.png' alt='Cycle 469 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_118_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_118_c.png' alt='Cycle 470 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_118_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_118_t.png' alt='Cycle 471 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_118_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_118_g.png' alt='Cycle 472 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_118_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_119_a.png' alt='Cycle 473 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_119_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_119_c.png' alt='Cycle 474 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_119_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_119_t.png' alt='Cycle 475 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_119_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_119_g.png' alt='Cycle 476 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_119_g.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_120_a.png' alt='Cycle 477 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_120_a.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_120_c.png' alt='Cycle 478 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_120_c.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_120_t.png' alt='Cycle 479 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_120_t.png' class='preloadHidden' />

	<img src='http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_120_g.png' alt='Cycle 480 FWHM Plot http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_120_g.png' class='preloadHidden' />


		</div>
  		<div id="footer">
			










<center>
	&copy; Albert Einstein College of Medicine (2012). Distributed freely under the terms of the <a href="http://www.gnu.org/licenses/agpl-3.0.html" target="_blank">GNU AFFERO General Public License</a>.
	<br>
	Core WASP System and plugin repository maintained by Albert Einstein College of Medicine Computational Epigenomics and Genomics Cores (<a href="http://waspsystem.org/"  target="_blank">waspsystem.org</a>).
</center> 
		</div>
	</div>
</body>
</html>




<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

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
		background-image:url(<c:out value="${runReportBaseImagePath}/Chart_1_a.png" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intC{
		background-image:url(<c:out value="${runReportBaseImagePath}/Chart_1_c.png" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intT{
		background-image:url(<c:out value="${runReportBaseImagePath}/Chart_1_t.png" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intG{
		background-image:url(<c:out value="${runReportBaseImagePath}/Chart_1_g.png" />);
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
		$( "#intA" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}"/>/Chart_" + value_update + "_a.png)");
		$( "#intC" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}"/>/Chart_" + value_update + "_c.png)");
		$( "#intT" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}"/>/Chart_" + value_update + "_t.png)");
		$( "#intG" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}"/>/Chart_" + value_update + "_g.png)");
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
			max: <c:out value="${numCycles}" />,
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
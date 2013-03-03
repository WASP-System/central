<%@ include file="/WEB-INF/jsp/taglib.jsp"%>


<script>
	
	function validate(){
		TOTAL_LANES = 8;
		messagePassFailRoot = "You must choose either 'Pass' or 'Fail' for ALL lanes. You have not yet scored lanes ";
		messagePassFailLanes = "";
		messageCommentsRoot = "You must write a comment for all failed lanes: ";
		messageCommentsLanes = "";
		for (i=1; i <= TOTAL_LANES; i++){
			laneResult = $("input[name='radioL" + i + "']:checked").val();
			laneComment = $("textarea[name='commentsL" + i + "']").val();
			if (laneResult == null){
				if (messagePassFailLanes.length > 0)
					messagePassFailLanes += ", ";
				messagePassFailLanes += i;
			} else if (laneResult == 0 && (laneComment == null || laneComment == "")){
				if (messageCommentsLanes.length > 0)
					messageCommentsLanes += ", ";
				messageCommentsLanes += i;
			}
		}
		message = "";
		if (messagePassFailLanes.length > 0 )
			message += messagePassFailRoot + messagePassFailLanes + ". ";
		if (messageCommentsLanes.length > 0 )
			message += messageCommentsRoot + messageCommentsLanes + ". ";
		if (message.length > 0){
			$( "#warningText" ).html(message);
			$( "#error_dialog-modal" ).dialog("open");
		}
		else{
			$( "#selectionWindow").hide();
			$( "#qualityForm").submit();
		}
	}
	

	function updateOnSlideH( value_update ){
		$( "#amountH" ).val( value_update );
		$( "#intA" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}/${chartSubFolder}"/>/Chart_" + value_update + "_a.png)");
		$( "#intC" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}/${chartSubFolder}"/>/Chart_" + value_update + "_c.png)");
		$( "#intT" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}/${chartSubFolder}"/>/Chart_" + value_update + "_t.png)");
		$( "#intG" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}/${chartSubFolder}"/>/Chart_" + value_update + "_g.png)");
	}	
	
	function updateOnSlideV( value_update ){
		$( "#amountV" ).val( value_update );
		$( "#int" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}/${chartSubFolder}"/>/Chart_" + value_update + ".png)");
	}	

	$( window ).load( function(){
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
		
		$( "#qscoreSelector" ).buttonset();
		$( "#qscoreSelector" ).change(function(){
				newLane = $("input[name=qscoreRadio]:checked").val();
				$( "#qscoreChart" ).attr("src", "<c:out value="${runReportBaseImagePath}/${qscoreSubFolder}"/>/QScore_L" + newLane + ".png");
			});

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
		
		$( "#sliderH" ).slider({
			range: "min",
			min: 1,
			max: <c:out value="${numCycles}" />,
			value: 1,
			slide: function( event, ui ) {
				updateOnSlideH( ui.value );
			}
		});
		
		$( "#sliderV" ).slider({
			orientation: "vertical",
			range: "min",
			min: 1,
			max: <c:out value="${numCycles}" />,
			value: 1,
			slide: function( event, ui ) {
				updateOnSlideV( ui.value );
			}
		});
		
		$( "#sliderVValue" ).val( $( "#sliderV" ).slider( "value" ) );
		
		$( "#sliderHValue" ).val( $( "#sliderH" ).slider( "value" ) );
		
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
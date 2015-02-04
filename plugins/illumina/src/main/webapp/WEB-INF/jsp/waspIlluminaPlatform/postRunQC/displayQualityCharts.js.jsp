<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<script>
	
	function validate(){
		TOTAL_LANES = <c:out value="${cellIndexList.size()}" />;
		messagePassFailRoot = '<fmt:message key="waspIlluminaPlugin.displayQc_noChoose.error" />';
		messagePassFailLanes = "";
		messageCommentsRoot = '<fmt:message key="waspIlluminaPlugin.displayQc_noComment.error" />';
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
	
	var urlArray = [];
	
	function populateUrlArray(){
	<c:forEach items="${fileHandlesByName.keySet()}" var="fileName" varStatus="status">
		urlArray['<c:out value="${fileName}" />'] = '<wasp:url fileAccessor = "${fileHandlesByName.get(fileName)}" />';
	</c:forEach>
	}
	

	function updateOnSlideH( value_update ){
		$( "#amountH" ).val( value_update );
		$( "#intA" ).css("background-image", "url(" + urlArray['<c:out value="${chartSubFolder}" />/Chart_' + value_update + '_a.png'] + ")");
		$( "#intC" ).css("background-image", "url(" + urlArray['<c:out value="${chartSubFolder}" />/Chart_' + value_update + '_c.png'] + ")");
		$( "#intT" ).css("background-image", "url(" + urlArray['<c:out value="${chartSubFolder}" />/Chart_' + value_update + '_t.png'] + ")");
		$( "#intG" ).css("background-image", "url(" + urlArray['<c:out value="${chartSubFolder}" />/Chart_' + value_update + '_g.png'] + ")");
	}	
	
	function updateOnSlideV( value_update ){
		$( "#amountV" ).val( value_update );
		$( "#int" ).css("background-image", "url(" + urlArray['<c:out value="${chartSubFolder}" />/Chart_' + value_update + '.png'] + ")");
	}	

	$( window ).load( function(){
		$( "#main" ).css("visibility", "visible");
		$( "#selectionWindow" ).css("visibility", "visible");
		$( "#error_dialog-modal" ).css("visibility", "visible");
		$( "#main" ).show();
		$( '#loading_dialog-modal' ).dialog( 'close' );
	});
		
	$(function() {
		$( "#selectionWindow" ).hide();
		
		$( ".radio-jquery-ui" ).buttonset();
		
		$( "#qscoreSelector" ).change(function(){
				newLane = $("input[name=qscoreRadio]:checked").val();
				$( "#qscoreChart" ).attr("src", urlArray['<c:out value="${qscoreSubFolder}" />/QScore_L' + newLane + '.png']);
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
		$("#loading_dialog-modal").css("visibility", "visible");

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
		
		populateUrlArray();
	});

	
</script>
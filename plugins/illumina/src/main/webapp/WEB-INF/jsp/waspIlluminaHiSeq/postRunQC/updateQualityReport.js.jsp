<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>

	function validate(){
		TOTAL_LANES = <c:out value="${cellIndexList.size()}" />;
		reviewsIncomplete = false;
		commentsIncomplete = false;
		acceptMessage = '<fmt:message key="waspIlluminaPlugin.updateQc_noChoose.error" />';
		commentMessage = '<fmt:message key="waspIlluminaPlugin.updateQc_noComment.error" />';
		for (i=1; i <= TOTAL_LANES; i++){
			laneResult = $("input[name=radioL" + i + "]:checked").val();
			comments = $("#commentsL" + i).val();
			if (laneResult == null){
				acceptMessage += i + ", ";
				reviewsIncomplete = true;
			}
			else if (laneResult == 0 && comments == ""){
				commentMessage += i + ", ";
				commentsIncomplete = true;
			} 
		}
		if (reviewsIncomplete || commentsIncomplete){
			message = "";
			if (reviewsIncomplete){
				message = acceptMessage.substring(0, acceptMessage.length-2); // trim trailing comma and space 
				message += ".</p>";
			}
			if (commentsIncomplete){
				message += commentMessage.substring(0, commentMessage.length-2); // trim trailing comma and space 
				message += ".</p>";
			}
			$( "#warningText" ).html(message);
			$( "#error_dialog-modal" ).dialog("open");
		}
		else{
			$( "#qualityForm").submit();
			openWaitDialog();
		}
	}
		
	$(function() {
		$( "#main" ).css("visibility", "visible");
		$( "#error_dialog-modal" ).css("visibility", "visible");
		$( ".radio-jquery-ui" ).buttonset();

		$( "#submitForm" ).button()
					.click(function(){
						validate();
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
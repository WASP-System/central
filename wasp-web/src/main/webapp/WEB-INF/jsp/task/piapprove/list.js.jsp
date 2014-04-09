<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<script>
	
	$(document).ready(function() {
	    $("#accordion").accordion({
			collapsible: true,
			autoHeight: false,
			navigation: true,
			active: false,
			header: 'h4'			
		});
	    $("#accordion2").accordion({
			collapsible: true,
			autoHeight: false,
			navigation: true,
			active: false,
			header: 'h4'			
		});

	 $('button[id^="robbutton"]').click(function() {

		  var id = parseInt(this.id.replace("robbutton", ""));
	  	  $("#robdiv" + id).fadeToggle("fast", "linear");
	  	  if( $(this).html() == "show"){ $(this).html("hide"); }
		  else{ $(this).html("show"); }
	  	  
	  	});
	  //$("#robbutton8").click(function() {
	  //	  $("#robdiv8").fadeToggle("slow", "linear");
	  //	});   
	    
	    
	  });
	
	function show_data(counter){
				
		var button_name = 'robbutton' + counter;
		var div_name = 'robdiv' + counter;
		
		var but = document.getElementById(button_name);
		if(but.value == 'show'){
			but.value = 'hide';
		}
		else{
			but.value = 'show';
		}
		
		name = 'robdiv' + counter; 
		
		var obj = document.getElementById(div_name);
		if(obj.style.display == 'none'){
			obj.style.display = 'block';
		}
		else{
			obj.style.display = 'none';
		}
	}
	
	function validate(theform){
		if(!theform.action[0].checked && !theform.action[1].checked){
			alert("<fmt:message key="jobapprovetask.approveRejectAlert.label" />");
			return false;
		}
		var commentObj = theform.comment;
		var commentValue = commentObj.value; 
		var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
		if(theform.action[1].checked && trimmedCommentValue.length==0){
			alert("<fmt:message key="jobapprovetask.validateCommentAlert.label" />");
			if(commentValue.length>0){
				commentObj.value = "";
			}
			commentObj.focus();
			return false;
		}
		openWaitDialog();
		return true;
	}
	function selectedFail(formId){
		var commentObj = document.getElementById(formId).comment;
		var commentValue = commentObj.value; 
		var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
		if(trimmedCommentValue.length==0){
			alert("<fmt:message key="jobapprovetask.validateCommentAlert.label" />");
			if(commentValue.length>0){
				commentObj.value = "";
			}
			commentObj.focus();
		}
	}
	
</script>

<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<head>

    <link type="text/css" href="../../themes/base/ui.all.css" rel="stylesheet" />

    <script type="text/javascript" src="../../jquery-1.3.2.js"></script>
    <script type="text/javascript" src="../../ui/ui.core.js"></script>
    <script type="text/javascript" src="../../ui/ui.dialog.js"></script>
 </head>
<br />
<br />

<script type="text/javascript">
function toggleViewerFrame(toggleButton){
	var viewerFrame = document.getElementById('viewerFrame');
	if(toggleButton.value == "Show Window"){
		toggleButton.value = "Hide Window";
		viewerFrame.style.display = "block";
	}
	else if(toggleButton.value == "Hide Window"){
		toggleButton.value = "Show Window";
		viewerFrame.style.display = "none";		
	}	
}
$(document).ready(function() {
	$("#modalDialog").dialog({
        autoOpen: false,
        modal: true,
        height: 800,
        width: 1200,
        position: { my: "right top", at: "right top", of: window } <%--could user "#container" too, which is set by wasp css --%>
    });
	$("#modalessDialog").dialog({
        autoOpen: false,
        modal: false,
        height: 800,
        width: 1200,
        position: { my: "right top", at: "right top", of: window }
    });
	
 // $("#robanchor").click(function (e) {
	  //$("#modalIframeId").attr("src","http://www.w3schools.com/"); 
	  //$("#modalIframeId").attr("src","<c:url value="/sampleDnaToLibrary/showplay.do" />"); 
	//  $( "#robdiv" ).dialog("open");
	  //return false;
	 /* 
	  $( "#robdiv" ).dialog({
		  
			width: 600,
			height: 400,
			open: function(event, ui)
			{
				var textarea = $('<textarea style="height: 276px;">');
				$(this).html(textarea);
				$(textarea).redactor({ autoresize: false });
				$(textarea).setCode('<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>');
			}
		});*/
	  //alert("dubin");
    //e.preventDefault();
    //$("#robdiv").load("http://webdesign.about.com/od/iframes/a/aaiframe.htm#abt").dialog({modal:true}); 
    // load the html file using ajax 
    //$.get("http://webdesign.about.com/od/iframes/a/aaiframe.htm#abt", function(resp){
    //    var data = $('#robdiv').append(resp);
    //    data.modal();
   // }); 
  //}); 
 
  /*
  $("#robanchor").click(function (e) {
	    e.preventDefault();

	    // change height, width and modal options as required
	    $.modal('<iframe src="http://webdesign.about.com/od/iframes/a/aaiframe.htm#abt" height="450" width="830" style="border:0">', {
	        closeHTML:"",
	        containerCss:{
	            backgroundColor:"#fff",
	            borderColor:"#fff",
	            height:450,
	            padding:0,
	            width:830
	        },
	        overlayClose:true
	    });
	});
 */
});
function showModalDialog(url){
	//alert("robert's test alert");
	//var iframe = document.getElementById("modalIframeId");
	$("#modalIframeId").attr("src", url);
	$( "#modalDialog" ).dialog("open");
}
function showModalessDialog(url){
	$("#modalessIframeId").attr("src", url);
	$( "#modalessDialog" ).dialog("open");
}
</script>

<style>
.pageContainer {width:100%; overflow:hidden;}

.selectionLeft {float:left; width:15%; }
.viewerRight {float:left; width:80%; padding-left:0.3cm; overflow:hidden;};
</style>

<div class="pageContainer">
	<div class="selectionLeft">
	   <div id="modalDialog" <%--style="display: none;"--%>>
	   		<%-- this is my dialog box saying --%>
	   			<iframe id="modalIframeId" name="modalIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
	   	</div>
	   	<div id="modalessDialog" <%--style="display: none;"--%>>
	   		<%-- this is my dialog box saying --%>
	   			<iframe id="modalessIframeId" name="modalessIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
	   	</div>
		<br />
		<input id="toggleButton" class="fm-button" type="button" value="Hide Window"  onClick="toggleViewerFrame(this)" />
		<br /><br /> <br />
		<p>
		<a href="http://webdesign.about.com/od/iframes/a/aaiframe.htm#abt" target="myIframe">Right Frame: View A Web-design Page</a>
		</p>
		<p>
		<a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">Right Frame: View Fastqc report from /results/production_wiki</a>
		</p>
		<p>
		<a href="<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">Right Frame: Wasp job 87's home page</a>
		</p>		
		<p>
		<a href="<c:url value="/sampleDnaToLibrary/showplay.do" />" target="myIframe">Right Frame: view a FILE stored on Chiam</a>
		</p>		
		<br />
		<p>		
		<a href="javascript:void(0);" title=""  onclick="window.open('<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">CHILD WINDOW: Wasp job 87's home page</a>
		</p>
		<p>		
		<a href="javascript:void(0);" title=""  onclick="window.open('<c:url value="/sampleDnaToLibrary/showplay.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">CHILD WINDOW: view a FILE stored on Chiam</a>
		</p>
		<br />
<%--		<a id="robanchor" href="javascript:void(0);"  >  Click here to open model dialog with html</a>
		<br />
 		
		<p>
		w3schools.com as model but must click other<a href="http://www.w3schools.com/" target="modalIframeId" > by clicking this link</a>
		</p>
		<p>
		Circular RNAs as model<a href="http://www.nature.com/news/circular-rnas-throw-genetics-for-a-loop-1.12513" target="modalIframeId" > by clicking this link</a>
		</p>
--%>
		<p>
		<a href="javascript:void(0);" onclick='showModalDialog("http://en.wikipedia.org/wiki/Andromeda_Galaxy");' >Modal Dialog: Andromeda</a>
		</p>
		<p>
		<a href="javascript:void(0);" onclick='showModalDialog("http://www.amnh.org/");' >Modal Dialog: AMNH</a>
		</p>
		<p>
		<a href="javascript:void(0);" onclick='showModalDialog("http://www.wqxr.org/#!/");' >Modal Dialog: WQXR</a>
		</p>		
		<p>
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modal Dialog: Wasp job 87's home page</a>
		</p>
		<p>
		<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/showplay.do" />");' >Modal Dialog: view a FILE stored on Chiam</a>
		</p>
		<br />
		
		<p>
		<a href="javascript:void(0);" onclick='showModalessDialog("http://www.wqxr.org/#!/");' >Modaless Dialog: WQXR</a>
		</p>		
		<p>
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modaless Dialog: Wasp job 87's home page</a>
		</p>
		<p>
		<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/showplay.do" />");' >Modaless Dialog: view a FILE stored on Chiam</a>
		</p>
		<br />
		
 	</div>
	<div class="viewerRight">
		<div id="viewerFrame" style="display:block;">
  			<iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="600" width="99%"><p>iframes not supported</p></iframe>
  		</div>
	</div>
	
	<div style="clear:both;"></div>
	
</div>



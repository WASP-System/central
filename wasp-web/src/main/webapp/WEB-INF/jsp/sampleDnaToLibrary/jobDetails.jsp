<%@ include file="/WEB-INF/jsp/taglib.jsp" %>







<script type="text/javascript">

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
});
/*
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
*/
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
function showPopupWindow(url) 
{//from http://stackoverflow.com/questions/10728207/position-a-window-on-screen 
 var width  = 1200;
 var height = 800;
 var left   = screen.width - width;
 var top    = 0;
 var params = 'width='+width+', height='+height;
 params += ', top='+top+', left='+left;
 params += ', directories=no';
 params += ', location=no';
 params += ', menubar=no';
 params += ', resizable=no';
 params += ', scrollbars=yes';
 params += ', status=no';
 params += ', toolbar=no';
 newwin=window.open(url,'customWindow', params);
 if (window.focus) {newwin.focus();}
 return false;
}
</script>






 <%-- 
<style>
	.pageContainer {width:100%; overflow:hidden;}
	.selectionLeft {float:left; width:35%; }
	.viewerRight {float:left; width:55%; padding-left:0.3cm; border-left:2px solid black; overflow:hidden;}
	.selectionLeft div {margin:5px 0px 5px 10px;}
	.rob div {margin:5px 0px 5px 20px;}
</style>
--%>
<%--
<style>
	.pageContainer {width:850px; overflow:hidden;}
	.selectionLeft {float:left; width:400px; border-right:2px solid black;}
	.viewerRight {float:left; width:400px; padding-left:0.3cm; overflow:hidden;}
	.selectionLeft div {margin:5px 0px 5px 10px;}
	.rob div {margin:5px 0px 5px 10px;}
</style>
--%>
<%--these dialogs are not displayed until called; don't know where is best to put them--%>
<div id="modalDialog">
	<iframe id="modalIframeId" name="modalIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>
<div id="modalessDialog">
	<iframe id="modalessIframeId" name="modalessIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>
<br />
<br />
This is a test of the new jobDetails view<br />
this job is J<c:out value="${job.jobId}" />: <c:out value="${job.getName()}" />
This is a test of the new jobDetails view<br />
this job is J<c:out value="${job.jobId}" />: <c:out value="${job.getName()}" />
This is a test of the new jobDetails view<br />
this job is J<c:out value="${job.jobId}" />: <c:out value="${job.getName()}" />
This is a test of the new jobDetails view<br />
this job is J<c:out value="${job.jobId}" />: <c:out value="${job.getName()}" />
<br />
<a href="/wasp/file/fileHandle/28/download.do" >Download</a>
<br />

<a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">Right Frame: View Fastqc report from /results/production_wiki</a>
<br />
<a href="<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">Right Frame: Wasp job 87's home page</a>
<br />
<a href="<c:url value="/sampleDnaToLibrary/showplay.do" />" target="myIframe">Right Frame: view a FILE stored on Chiam</a>
<br />	
<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html');">Aligned Popup WINDOW: fastqc</a>
<br />	
<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />');">Aligned Popup WINDOW: Wasp job 87's home page</a>
<br />	
<a href="javascript:void(0);" title="popup"  onclick="window.open('<c:url value="/sampleDnaToLibrary/showplay.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">Popup WINDOW (left): view a FILE stored on Chiam</a>
<br />	
<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<c:url value="/sampleDnaToLibrary/showplay.do" />');">Aligned Popup WINDOW: File on Chiam</a>
<br />
<a href="javascript:void(0);" onclick='showModalDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modal Dialog: fastqc</a>
<br />
<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modal Dialog: Wasp job 87's home page</a>
<br />
<a href="javascript:void(0);" onclick='showModalDialog("<c:url value="/sampleDnaToLibrary/showplay.do" />");' >Modal Dialog: view a FILE stored on Chiam</a>
<br />
<a href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modaless Dialog: fastqc</a>
<br />
<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modaless Dialog: Wasp job 87's home page</a>
<br />
<a href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/sampleDnaToLibrary/showplay.do" />");' >Modaless Dialog: view a FILE stored on Chiam</a>

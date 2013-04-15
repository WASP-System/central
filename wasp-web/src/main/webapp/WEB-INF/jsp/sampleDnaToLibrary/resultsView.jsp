<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<br />
<div style="float:right;">
  <div id="viewerFrame" style="display:block;">
  	<iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" height="600" width="600"><p>iframes not supported</p></iframe>
  </div>
</div>
  
<br /><br /> <br />
<input id="toggleButton" class="fm-button" type="button" value="Hide Window"  onClick="toggleViewerFrame(this)" />
<br /><br /> <br />
<p>
When you click <a href="http://webdesign.about.com/od/iframes/a/aaiframe.htm#abt" target="myIframe">this link</a> it will open a new document inside the above window.
</p>
<p>
But by clicking on <a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">this link</a> it will open a new document inside the above window.
</p>
<p>
However, When you click here <a href="<c:url value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">this link</a> be ready for a big surprise.
</p>

<p>
However, When you click <a href="<c:url value="/sampleDnaToLibrary/showplay.do" />" target="myIframe">showplay</a> you're gonna smile.
</p>

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
</script>
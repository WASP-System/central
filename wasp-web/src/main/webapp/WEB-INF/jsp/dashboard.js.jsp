<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/ext-all-dev.js"></script>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/packages/ext-theme-neptune/build/ext-theme-neptune.js"></script>
<script type="text/javascript"	src="<wasp:relativeUrl value='scripts/extjs/wasp/WaspNamespaceDefinition.js.jsp' />"></script>
<!-- bxSlider Javascript file -->
<script src="<wasp:relativeUrl value='scripts/bxslider/jquery.bxslider.min.js' />"></script>
<!-- bxSlider CSS file -->
<link href="<wasp:relativeUrl value='scripts/bxslider/jquery.bxslider.css' />" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<wasp:relativeUrl value='css/ext-theme-neptune-all-wasp.css' />" />
<script>
	Ext.require(['Ext.layout.container.Border']);
	
	Ext.onReady(function() {
		var borderPanel = new Ext.Panel({
			width: $('#content').width(),
	        height: $('#content').height()-40,
		    layout: 'border',
		    renderTo: "viewpanel",
		    /*listeners: {
		    	afterrender: function(){
		    		slider = $('.bxslider').bxSlider({
		    			captions: true,
		    			auto: true,
		    			speed: 500,
		    			pause: 8000,
		    			controls: false,
		    			slideMargin: 2,
		    			randomStart: true,
		    			collapsible: false,
		    		});
		    		$('.bxslider').append('<li><img src="<wasp:relativeUrl value='images/carousel/desktop1-1030x330-blurred.jpg' />" title="The Wasp System for the Computational Genomics Researcher: The only complete metadata capture system from sample submission through LIMS and analytical pipelines." /></li>');
		    		$('.bxslider').append('<li><img src="<wasp:relativeUrl value='images/carousel/DSC_0111-resized.jpg' />" title="The Wasp System for the Systems Administrator: Open source, enterprise programming, with intelligent job provisioning to cloud or grid resources." /></li>');
		    		$('.bxslider').append('<li><img src="<wasp:relativeUrl value='images/carousel/illumina2.png' />" title="The Wasp for the Sequencing Core Facility Director: A complete LIMS, developed for Illumina but adaptable to other platforms, includes quality metrics and real time communication tools." /></li>');
		    		$('.bxslider').append('<li><img src="<wasp:relativeUrl value='images/carousel/photo-5-resized-blurred-1030x330.jpg' />" title="The Wasp System for the Clinician or Biologist: Automates the analytical process to allow a focus on the question being asked." /></li>');
		    		slider.reloadSlider();
		    	}
		    },*/
		    items: [
				{
				xtype: 'panel',
				itemId: 'northregion',
				collapsible:true,
				region: 'north',
				title: '<fmt:message key="home.portalHeader.label" />',
				//html: '<div style="width:1040px; margin: auto;"><div style="margin: 10px"><ul class="bxslider"></ul></div></div>',
				
				html: '<div style="margin:10px;"><fmt:message key="home.welcomeBack.label" /> ${me.firstName} ${me.lastName}<c:if test="${isTasks == true}"><span style="color:red">: <fmt:message key="home.tasksAwaiting.label" /></span></c:if></div><div style="display: block;margin-right:auto;margin-left:auto;width:100%"><img height="200" alt="Wasp System Button" src="<wasp:relativeUrl value='images/waspSystemLogoFancy_270x270.png' />" id="mainLogo"></div>',
				height: 300
				},
				/*{
				xtype: 'panel',
				region: 'west',
				html: 'Western Region',
				width: 200,
				title: 'Western Region',
				collapseDirection: 'left',
				collapsible: true,
				split: false
				},*/
				{
				xtype: 'panel',
				itemId: 'centerregion',
				region: 'center',
				autoScroll: true,
				title: '<fmt:message key="home.newsHeader.label" />',
				html: '<div style="margin:10px;><table style="margin-top: 5px"><tr><th style="text-align: right">09/28/2014:</th><td>Software Available for sample submissions for ChIP-seq, HELP-tagging, Exome-seq and WGS</td></tr></table></div>',
				},
				/*{
				xtype: 'panel',
    			collapsible:true,
    			autoScroll: true,
				region: 'south',
				html: 'Southern Region',
				height: 120
				}*/
			] 
		});
		jQuery(window).bind('resize', function () {
			borderPanel.setWidth($('#content').width());
			borderPanel.setHeight($('#content').height()-40);
			}).trigger('resize');
	});
	
</script>
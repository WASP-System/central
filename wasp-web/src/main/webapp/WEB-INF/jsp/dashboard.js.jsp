<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/ext-all-dev.js"></script>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/packages/ext-theme-neptune/build/ext-theme-neptune.js"></script>
<script type="text/javascript"	src="/wasp/scripts/extjs/wasp/WaspNamespaceDefinition.js"></script>
<!-- bxSlider Javascript file -->
<script src="/wasp/scripts/bxslider/jquery.bxslider.min.js"></script>
<!-- bxSlider CSS file -->
<link href="/wasp/scripts/bxslider/jquery.bxslider.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="/wasp/css/ext-theme-neptune-all-wasp.css" />
<script>
	Ext.require(['Ext.layout.container.Border']);
	
	Ext.onReady(function() {
		var borderPanel = new Ext.Panel({
			width: $('#content').width(),
	        height: $('#content').height()-40,
		    layout: 'border',
		    renderTo: "viewpanel",
		    listeners: {
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
		    		$('.bxslider').append('<li><img src="/wasp/images/carousel/desktop1-1030x330-blurred.jpg" title="The Wasp System for the Computational Genomics Researcher: The only complete metadata capture system from sample submission through LIMS and analytical pipelines." /></li>');
		    		$('.bxslider').append('<li><img src="/wasp/images/carousel/DSC_0111-resized.jpg" title="The Wasp System for the Systems Administrator: Open source, enterprise programming, with intelligent job provisioning to cloud or grid resources." /></li>');
		    		$('.bxslider').append('<li><img src="/wasp/images/carousel/illumina2.png" title="The Wasp for the Sequencing Core Facility Director: A complete LIMS, developed for Illumina but adaptable to other platforms, includes quality metrics and real time communication tools." /></li>');
		    		$('.bxslider').append('<li><img src="/wasp/images/carousel/photo-5-resized-blurred-1030x330.jpg" title="The Wasp System for the Clinician or Biologist: Automates the analytical process to allow a focus on the question being asked." /></li>');
		    		slider.reloadSlider();
		    	}
		    },
		    items: [
				{
				xtype: 'panel',
				itemId: 'northregion',
				collapsible:true,
				region: 'north',
				title: 'Welcome to The Wasp System! Foundational software in modern biology.',
				html: '<div style="width:1040px; margin: auto;"><div style="margin: 10px"><ul class="bxslider"></ul></div></div>',
				height: 400
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
				title: 'Wasp System News & Events ',
				html: '<div><h2>Software Demo Session at The 15th Annual AGBT Meeting, Marco Island, Florida, February 13 5:15 p.m. - 7:15 p.m.</h2>\
<table style="margin-top: 5px"><tr><th style="text-align: right">Title:</th><td>THE WASP SYSTEM: AN OPEN SOURCE SOFTWARE ECOSYSTEM</td></tr>\
<tr><th style="text-align: right">Presenting Author:</th><td>Andrew McLellan</td></tr>\
<tr><th style="text-align: right">Authors:</th><td>Andrew McLellan, Robert A. Dubin, Qiang Jing, Natalia Volnova, R. Brent Calder, Jinlu Cai, Joseph Hargitai, Aaron Golden, John M. Greally</td></tr></table></div>',
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
/**
 * @class Ext.wasp.Portal
 * @extends Object
 * A sample portal layout application class.
 */

Ext.define('Ext.wasp.Portal', {

    extend: 'Ext.container.Container',
    requires: ['Ext.wasp.PortalPanel', 'Ext.wasp.PortalColumn', 'Ext.wasp.GridPortlet', 'Ext.wasp.ChartPortlet'],

    getTools: function(){
        return [/*{
            xtype: 'tool',
            type: 'gear',
            handler: function(e, target, header, tool){
                var portlet = header.ownerCt;
                portlet.setLoading('Loading...');
                Ext.defer(function() {
                    portlet.setLoading(false);
                }, 2000);
            }
        },*/{
        	xtype: 'tool',
        	type: 'maximize',
        	handler: function(e, target, header, tool){
                var portlet = header.ownerCt;
                var portal = portlet.up('portalpanel');
                if(portal===undefined) { // if the portlet is in the max panel
                	var pmax = portlet.ownerCt;
                	pmax.removeAll();
                	var tabpanel = pmax.ownerCt;
                	portal = tabpanel.items.last();
	                tabpanel.getLayout().setActiveItem(portal);
                } else {  // if the portlet is in the normal portal panel
	                var tabpanel = portal.ownerCt;
	                var pmax = tabpanel.items.first();
	                var portletClone = portlet.cloneConfig();
	                portletClone.closable = false;
	                portletClone.collapsible = false;
	                pmax.add(portletClone);
	                pmax.doLayout();
	                tabpanel.getLayout().setActiveItem(pmax);
                }
            }
        }];
    },

    initComponent: function(){
        var content = '<div class="portlet-content">content</div>';

        Ext.apply(this, {
            id: 'wasp-viewport',
            renderTo: 'resultpanel',
            width: 1400,
            height: 800,
            layout: {
                type: 'border',
                padding: '0 5 5 5' // pad the layout from the window edges
            },
            items: [{
                id: 'wasp-header',
                xtype: 'box',
                region: 'north',
                height: 20,
                html: 'Job Result Portal'
            },{
                xtype: 'container',
                region: 'center',
                layout: 'border',
                items: [{
                    id: 'wasp-options',
                    title: 'D3 Treeview',
                    region: 'west',
                    animCollapse: true,
                    width: 400,
                    minWidth: 250,
                    maxWidth: 700,
                    split: true,
                    collapsible: true,
                    layout:{
                        type: 'accordion',
                        animate: true
                    },
                    items: [{
                        html: '<div id="treeview"></div>',
                        title:'Navigation',
                        autoScroll: true,
                        border: false,
                        iconCls: 'nav'
                    },{
                        title:'Settings',
                        html: content,
                        border: false,
                        autoScroll: true,
                        iconCls: 'settings'
                    }]
                },{
                	id: 'wasp-tabpanel',
                    xtype: 'tabpanel',
                    region: 'center',
                    activeTab: 0,
                    items: [{
                    	//id: 'wasp-tab1',
        	            xtype: 'panel',
                        title: 'Tab 1',
                        layout:'card',
						activeItem: 1,
                        items: [{
                        	layout: 'fit'
                        },{
                        	//id: 'wasp-portal1',
    	                	xtype: 'portalpanel',
		                    items: [{
		                       // id: 'col-1',
		                        items: [{
		                            //id: 'portlet-2',
		                            title: 'Portlet 2',
		                            tools: this.getTools(),
		                            closable: false,
		                            html: content,
		                            listeners: {
		                                'close': Ext.bind(this.onPortletClose, this)
		                            }
		                        }]
		                    },{
		                        //id: 'col-2',
		                        items: [{
		                            //id: 'portlet-3',
		                            title: 'Portlet 3',
		                            tools: this.getTools(),
		                            html: '<div id=\'highChartContainer_089d7b82-c5b5-49f9-9dca-f514931f394b\'></div>',
		                            listeners: {
//		                                'close': Ext.bind(this.onPortletClose, this),
//		                                'render': Ext.bind(this.onPortletRender, this),
//		                                'resize': Ext.bind(this.onPortletRender, this)
		                            }
		                        }]
		                    }]
                        }]
                    },{
                    	//id: 'wasp-tab2',
                    	xtype: 'panel',
                    	title: 'Tab 2',
                    	layout: 'card',
                    	activeItem: 1,
                    	items: [{
                        	layout: 'fit'
                        },{
                        	xtype: 'portalpanel',
		                    items: [{
		                        //id: 'col-4',
		                        items: [{
		                            //id: 'portlet-4',
		                            title: 'Portlet 4',
		                            tools: this.getTools(),
		                            closable: false,
		                            html: content,
		                            listeners: {
		                                'close': Ext.bind(this.onPortletClose, this)
		                            }
		                        }]
		                    },{
		                    	//id: 'col-5'
		                    }]
                    	}]
                    }]
                }]
            }]
        });
        this.callParent(arguments);
    },

    onPortletClose: function(portlet) {
        this.showMsg('"' + portlet.title + '" was removed');
    },
    
//    onPortletRender: function(portlet) {
//		$('#highChartContainer_089d7b82-c5b5-49f9-9dca-f514931f394b').highcharts({
//			chart: { type: 'spline' },
//			title: { text: 'Sequence Duplication Level >= 14.0' },
//			legend: { enabled: false },
//			xAxis: { categories: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10++'],
//			title: { text: 'Sequence Duplication Level' }},
//			yAxis: { title: { text: '% Duplicate Relative to Unique' }
//		},series: [{ 
//			name: '% Duplication', 
//			color: '#ff0000', 
//			animation:false, 
//			marker: { enabled: false }, 
//			data: [[100],[6.339399637364851],[1.6139502607839187],[0.8148098403957648],[0.5433931009782195],[0.37326796946701585],[0.29380162514270364],[0.2383990329729367],[0.19083115080697513],[1.5165760078794799]]
//			}]
//		});
//    },

    showMsg: function(msg) {
        var el = Ext.get('wasp-msg'),
            msgId = Ext.id();

        this.msgId = msgId;
        el.update(msg).show();

        Ext.defer(this.clearMsg, 3000, this, [msgId]);
    },

    clearMsg: function(msgId) {
        if (msgId === this.msgId) {
            Ext.get('wasp-msg').hide();
        }
    }
});

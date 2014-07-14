/**
 * @class Wasp.Portal
 * @extends Object
 * A sample portal layout application class.
 */

Ext.define('Wasp.Portal', {

    extend: 'Ext.container.Container',
    
    requires: [	'Wasp.PortalPanel', 
    			'Wasp.PortalColumn', 
    			'Wasp.PluginSummaryGridPortlet', 
    			'Wasp.FileDownloadGridPortlet', 
    			'Wasp.ChartPortlet',
    			'Wasp.GridPortlet'],
    
    width: 1500,
    height: 800,
    headerheight: 20,

    getTools: function(isMaximizable){
    	if (isMaximizable) {
	        return [{
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
    	}
    },

    initComponent: function(){
        var content = '<div class="portlet-content"></div>';

        Ext.apply(this, {
            id: 'wasp-viewport',
            renderTo: 'resultpanel',
            width: this.width,
            height: this.height,
            layout: {
                type: 'border',
                padding: '0 5 5 5' // pad the layout from the window edges
            },
            items: [{
                id: 'wasp-header',
                xtype: 'box',
                region: 'north',
                //html: 'Job Result Portal',
                height: this.headerheight
            },{
                xtype: 'container',
                region: 'center',
                layout: 'border',
                items: [{
                    id: 'wasp-treeview',
                    title: 'Treeview',
                    region: 'west',
                    animCollapse: true,
                    width: this.width*0.3,
                    minWidth: 250,
                    maxWidth: 1000,
                    split: true,
                    collapsible: true,
                    layout:{
                        type: 'accordion',
                        animate: true
                    },
                    items: [{
                        html: '<div id="treeview"></div>',
                        title:'Job View',
                        autoScroll: true,
                        border: false,
                        iconCls: 'nav'
                    },{
                        title:'Other View',
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
                    tabBar: {
				        defaults: {
				            //flex: 1, // if you want them to stretch all the way
				            //height: 20, // set the height
				        	border: 2,
				            padding: 6 // set the padding
				         },
				        dock: 'top'
				    },
                    items: []
                }]
            }]
        });
        this.callParent(arguments);
    },

    onPortletClose: function(portlet) {
        this.showMsg('"' + portlet.title + '" was removed');
    },
    
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

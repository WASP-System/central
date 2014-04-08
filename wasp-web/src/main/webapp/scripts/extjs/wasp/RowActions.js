/**
 * @class Ext.ux.grid.RowActions
 * @extends Ext.grid.column.Column
 *
 * RowActions plugin for Ext grid. Contains renderer for icons and fires events when an icon is clicked.
 * CSS rules from Ext.ux.RowActions.css are mandatory
 *
 * Important general information: Actions are identified by iconCls. Wherever an <i>action</i>
 * is referenced (event argument, callback argument), the iconCls of clicked icon is used.
 * In other words, action identifier === iconCls.
 *
 * @author    Ing. Jozef Saki
 * @author    Christian King (port to Ext 4.x)
 * @copyright (c) 2008, by Ing. Jozef Saki
 * @date      21 June 2011
 * @version   2.0.1
 *
 * @license Ext.ux.grid.RowActions is licensed under the terms of
 * the Open Source LGPL 3.0 license.  Commercial use is permitted to the extent
 * that the code/component(s) do NOT become part of another Open Source or Commercially
 * licensed development library or toolkit without explicit permission.
 * 
 * <p>License details: <a href="http://www.gnu.org/licenses/lgpl.html"
 * target="_blank">http://www.gnu.org/licenses/lgpl.html</a></p>
 *
 * @donate
 * <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_blank">
 * <input type="hidden" name="cmd" value="_s-xclick">
 * <input type="hidden" name="hosted_button_id" value="3430419">
 * <input type="image" src="https://www.paypal.com/en_US/i/btn/x-click-butcc-donate.gif" 
 * border="0" name="submit" alt="PayPal - The safer, easier way to pay online.">
 * <img alt="" border="0" src="https://www.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
 * </form>
 */

// add RegExp.escape if it has not been already added
if('function' !== typeof RegExp.escape) {
	RegExp.escape = function(s) {
		if('string' !== typeof s) {
			return s;
		}
		// Note: if pasting from forum, precede ]/\ with backslash manually
		return s.replace(/([.*+?\^=!:${}()|\[\]\/\\])/g, '\\$1');
	};
}

/**
 * Creates new RowActions plugin
 * @constructor
 * @param {Object} config A config object
 */
Ext.define('Wasp.RowActions',{
	extend: 'Ext.grid.column.Column',
	alias: 'widget.rowactions',
	constructor: function(config) {
		var me = this;

		me.addEvents(
			/**
			 * @event beforeaction
			 * Fires before action event. Return false to cancel the subsequent action event.
			 * @param {Ext.grid.GridPanel} grid
			 * @param {Ext.data.Record} record Record corresponding to row clicked
			 * @param {String} action Identifies the action icon clicked. Equals to icon css class name.
			 * @param {Integer} rowIndex Index of clicked grid row
			 * @param {Integer} colIndex Index of clicked grid column that contains all action icons
			 */
			 'beforeaction'
			/**
			 * @event action
			 * Fires when icon is clicked
			 * @param {Ext.grid.GridPanel} grid
			 * @param {Ext.data.Record} record Record corresponding to row clicked
			 * @param {String} action Identifies the action icon clicked. Equals to icon css class name.
			 * @param {Integer} rowIndex Index of clicked grid row
			 * @param {Integer} colIndex Index of clicked grid column that contains all action icons
			 */
			,'action'
			/**
			 * @event beforegroupaction
			 * Fires before group action event. Return false to cancel the subsequent groupaction event.
			 * @param {Ext.grid.GridPanel} grid
			 * @param {Array} records Array of records in this group
			 * @param {String} action Identifies the action icon clicked. Equals to icon css class name.
			 * @param {String} groupId Identifies the group clicked
			 */
			,'beforegroupaction'
			/**
			 * @event groupaction
			 * Fires when icon in a group header is clicked
			 * @param {Ext.grid.GridPanel} grid
			 * @param {Array} records Array of records in this group
			 * @param {String} action Identifies the action icon clicked. Equals to icon css class name.
			 * @param {String} groupId Identifies the group clicked
			 */
			,'groupaction'
		);

		me.callParent(arguments);
		
		// calculate width after state handling if set to autoWidth
		if(me.autoWidth) {
			// Width as defined by either our size or the minimum size as defined by the header resizer
			me.width = me.minWidth = Math.max(me.widthSlope * me.actions.length + me.widthIntercept,
					Ext.grid.plugin.HeaderResizer.prototype.minColWidth);
			me.fixed = true;
			//Ext.Msg.alert('minColWidth : ' + Ext.grid.plugin.HeaderResizer.prototype.minColWidth);
		}
		
		
	},

	/**
	 * @cfg {Array} actions Mandatory. Array of action configuration objects. The action
	 * configuration object recognizes the following options:
	 * <ul class="list">
	 * <li style="list-style-position:outside">
	 *   {Function} <b>callback</b> (optional). Function to call if the action icon is clicked.
	 *   This function is called with same signature as action event and in its original scope.
	 *   If you need to call it in different scope or with another signature use 
	 *   createCallback or createDelegate functions. Works for statically defined actions. Use
	 *   callbacks configuration options for store bound actions.
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {Function} <b>cb</b> Shortcut for callback.
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {String} <b>iconIndex</b> Optional, however either iconIndex or iconCls must be
	 *   configured. Field name of the field of the grid store record that contains
	 *   css class of the icon to show. If configured, shown icons can vary depending
	 *   of the value of this field.
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {String} <b>iconCls</b> CSS class of the icon to show. It is ignored if iconIndex is
	 *   configured. Use this if you want static icons that are not base on the values in the record.
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {Boolean} <b>hide</b> Optional. True to hide this action while still have a space in 
	 *   the grid column allocated to it. IMO, it doesn't make too much sense, use hideIndex instead.
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {String} <b>hideIndex</b> Optional. Field name of the field of the grid store record that
	 *   contains hide flag (falsie [null, '', 0, false, undefined] to show, anything else to hide).
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {String} <b>qtipIndex</b> Optional. Field name of the field of the grid store record that 
	 *   contains tooltip text. If configured, the tooltip texts are taken from the store.
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {String} <b>tooltip</b> Optional. Tooltip text to use as icon tooltip. It is ignored if 
	 *   qtipIndex is configured. Use this if you want static tooltips that are not taken from the store.
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {String} <b>qtip</b> Synonym for tooltip
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {String} <b>textIndex</b> Optional. Field name of the field of the grids store record
	 *   that contains text to display on the right side of the icon. If configured, the text
	 *   shown is taken from record.
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {String} <b>text</b> Optional. Text to display on the right side of the icon. Use this
	 *   if you want static text that are not taken from record. Ignored if textIndex is set.
	 * </li>
	 * <li style="list-style-position:outside">
	 *   {String} <b>style</b> Optional. Style to apply to action icon container.
	 * </li>
	 * </ul>
	 */

	/**
	 * @cfg {String} actionEvent Event to trigger actions, e.g. click, dblclick, mouseover (defaults to 'click')
	 */
	 actionEvent:'click'

	/**
	 * @cfg {Boolean} autoWidth true to calculate field width for iconic actions only (defaults to true).
	 * If true, the width is calculated as {@link #widthSlope} * number of actions + {@link #widthIntercept}.
	 */
	,autoWidth:true
	
	/**
	 * @cfg {Function} the renderer to use, we need to default this to null
	 */
	,renderer:null

	/**
	 * @cfg {String} dataIndex - Do not touch!
	 * @private
	 */
	,dataIndex:''

	/**
	 * @cfg {Array} groupActions Array of action to use for group headers of grouping grids.
	 * These actions support static icons, texts and tooltips same way as {@link #actions}. There is one
	 * more action config option recognized:
	 * <ul class="list">
	 * <li style="list-style-position:outside">
	 *   {String} <b>align</b> Set it to 'left' to place action icon next to the group header text.
	 *   (defaults to undefined = icons are placed at the right side of the group header.
	 * </li>
	 * </ul>
	 */

	/**
	 * @cfg {Object} callbacks iconCls keyed object that contains callback functions. For example:
	 * <pre>
	 * callbacks:{
	 * &nbsp;    'icon-open':function(...) {...}
	 * &nbsp;   ,'icon-save':function(...) {...}
	 * }
	 * </pre>
	 */

	/**
	 * @cfg {String} header Actions column header
	 */
	,header:''

	/**
	 * @cfg {Boolean} isColumn
	 * Tell ColumnModel that we are column. Do not touch!
	 * @private
	 */
	,isColumn:true

	/**
	 * @cfg {Boolean} keepSelection
	 * Set it to true if you do not want action clicks to affect selected row(s) (defaults to false).
	 * By default, when user clicks an action icon the clicked row is selected and the action events are fired.
	 * If this option is true then the current selection is not affected, only the action events are fired.
	 */
	,keepSelection:false

	/**
	 * @cfg {Boolean} menuDisabled No sense to display header menu for this column
	 * @private
	 */
	,menuDisabled:true

	/**
	 * @cfg {Boolean} sortable Usually it has no sense to sort by this column
	 * @private
	 */
	,sortable:false
	
	/**
	 * @cfg {String} tplGroup Template for group actions
	 * @private
	 */
	,tplGroup:
		 '<tpl for="actions">'
		+'<div class="ux-grow-action-item<tpl if="\'right\'===align"> ux-action-right</tpl> '
		+'{cls} qtip-target" style="{style}" data-qtip="{qtip}">{text}</div>'
		+'</tpl>'

	/**
	 * @cfg {String} tplRow Template for row actions
	 * @private
	 */
	,tplRow:
		 '<div class="ux-row-action">'
		+'<tpl for="actions">'
		+'<div class="ux-row-action-item {cls} <tpl if="text">'
		+'ux-row-action-text</tpl>" style="{hide}{style}" data-qtip="{qtip}">'
		+'<tpl if="text"><span data-qtip="{qtip}">{text}</span></tpl></div>'
		+'</tpl>'
		+'</div>'

	/**
	 * @cfg {String} hideMode How to hide hidden icons. Valid values are: 'visibility' and 'display' 
	 * (defaluts to 'visibility'). If the mode is visibility the hidden icon is not visible but there
	 * is still blank space occupied by the icon. In display mode, the visible icons are shifted taking
	 * the space of the hidden icon.
	 */
	,hideMode:'visiblity'

	/**
	 * @cfg {Number} widthIntercept Constant used for auto-width calculation (defaults to 4).
	 * See {@link #autoWidth} for explanation.
	 */
	,widthIntercept:6

	/**
	 * @cfg {Number} widthSlope Constant used for auto-width calculation (defaults to 21).
	 * See {@link #autoWidth} for explanation.
	 */
	,widthSlope:28

	/**
	 * Initialize the references to grid, generate renderer and attach to events on the grid
	 */
	,initRenderData:function() {
		
		var me = this;

		me.grid = me.up('gridpanel');
		me.tpl = me.tpl || me.processActions(me.actions);
		var groupFeature = me.getGroupingFeature(me.grid);

		me.grid.on('destroy', me.purgeListeners, me);

		// setup renderer
		if(!me.renderer) {
			me.renderer = Ext.bind(function(value, cell, record, row, col, store) {
				cell.tdCls += (cell.tdCls ? ' ' : '') + 'ux-row-action-cell';
				return this.tpl.apply(this.getData(value, cell, record, row, col, store));
			}, me);
		}

		if(me.groupActions && groupFeature) {
			// Don't trigger collapse / expand when clicking on the group header
			me.grid.view.on('groupclick', 
				function(view,group,idx,e,options){
					//return !e.getTarget('.ux-grow-action-item');
					if (e.getTarget('.ux-grow-action-item')) {
						me.onContainerEvent(view,group,idx,e,options);
						return false;
					} else {
						return true;
					}
				});
			//me.grid.view.on('container' + me.actionEvent, me.onContainerEvent, me);

			groupFeature.groupHeaderTpl = 
				'<div class="ux-grow-action-text">' + groupFeature.groupHeaderTpl + '</div>'
				+me.processActions(me.groupActions, me.tplGroup).apply({});
		}

		// cancel click
		if(true === me.keepSelection) {
			me.grid.on('beforeitemmousedown', function(view, record, item, index, e, options) {
				return !this.getAction(e);
			}, me);
		}
		
		return this.callParent(arguments);

		
	} 
	
	/**
	 * Gets the grouping feature from the grid features list
	 * @private
	 */
	,getGroupingFeature: function(grid) {
		return Ext.ComponentQuery.query("[alias='feature.grouping']", grid.features || [])[0] || null;
	}

	/**
	 * Returns data to apply to template. Override this if needed.
	 * @param {Mixed} value 
	 * @param {Object} cell object to set some attributes of the grid cell
	 * @param {Ext.data.Record} record from which the data is extracted
	 * @param {Number} row row index
	 * @param {Number} col col index
	 * @param {Ext.data.Store} store object from which the record is extracted
	 * @return {Object} data to apply to template
	 */
	,getData:function(value, cell, record, row, col, store) {
		return record.data || {};
	} 
	
	/**
	 * Processes actions configs and returns template.
	 * @param {Array} actions
	 * @param {String} template Optional. Template to use for one action item.
	 * @return {String}
	 * @private
	 */
	,processActions:function(actions, template) {
		var acts = [], me = this;

		// actions loop
		Ext.each(actions, function(a, i) {
			// save callback
			if(a.iconCls && 'function' === typeof (a.callback || a.cb)) {
				me.callbacks = me.callbacks || {};
				me.callbacks[a.iconCls] = a.callback || a.cb;
			}

			// data for intermediate template
			var o = {
				 cls:a.iconIndex ? '{' + a.iconIndex + '}' : (a.iconCls ? a.iconCls : '')
				,qtip:a.qtipIndex ? '{' + a.qtipIndex + '}' : (a.tooltip || a.qtip ? a.tooltip || a.qtip : '')
				,text:a.textIndex ? '{' + a.textIndex + '}' : (a.text ? a.text : '')
				,hide:a.hideIndex 
					? '<tpl if="' + a.hideIndex + '">' 
						+ ('display' === me.hideMode ? 'display:none' :'visibility:hidden') + ';</tpl>' 
					: (a.hide ? ('display' === me.hideMode ? 'display:none' :'visibility:hidden;') : '')
				,align:a.align || 'right'
				,style:a.style ? a.style : ''
			};
			acts.push(o);

		}, me);

		var xt = new Ext.XTemplate(template || me.tplRow);
		return new Ext.XTemplate(xt.apply({actions:acts}));

	}
	,getAction:function(e) {
		var action = false;
		try{
			var t = e.getTarget('.ux-row-action-item');
		}catch(ex){
		
		}
		if(t) {
			action = t.className.replace(/ux-row-action-item /, '');
			if(action) {
				action = action.replace(/ ux-row-action-text/, '');
				action = Ext.String.trim(action);
			}
		}
		return action;
	} 
	/**
	 * Grid body actionEvent event handler
	 * @private
	 */
	,processEvent : function(type, view, cell, recordIndex, cellIndex, e){
		var me   = this,
	    	grid = me.grid,
	    	col = (cell)?cell.cellIndex:false,
	    	action = me.getAction(e),
	    	target = e.getTarget(".ux-row-action-item");
		
		if(false !== recordIndex && false !== col && false !== action && type === me.actionEvent) {
			var record = grid.store.getAt(recordIndex);

			// fire events
			if(true !== me.eventsSuspended && false === me.fireEvent('beforeaction', grid, record, action, recordIndex, col, e, target)) {
				return;
			}
			
			// call callback if any
			if(me.callbacks && 'function' === typeof me.callbacks[action]) {
				me.callbacks[action](grid, record, action, recordIndex, col);
			}

			me.fireEvent('action', grid, record, action, recordIndex, col, e, target);

		}
		
	}
	/**
	 * Container event attaches to the view in the case of group actions
	 * @private
	 */
	,onContainerEvent:function(view,group,idx,e,options) {
		var me    = this,
			grid  = me.grid,
			view  = grid.getView(),
			groupField = grid.store.groupField;
	
		var t = e.getTarget('.ux-grow-action-item');
		
		// If this event isn't on a group actions then continue
		if (!t) return true;

		//var currentVal = grid.store.getAt(view.indexOf(Ext.fly(t).up('.x-grid-group-hd').next().down('.x-grid-row'))).get(groupField);
		var currentVal = idx;

		// get matching records
		var records = grid.store.queryBy(function(r) {
			return r.get(groupField).match(currentVal);
		});
		
		records = records ? records.items : [];
		
		action = t.className.replace(/ux-grow-action-item (ux-action-right )*/, '');
		
		if(action && action.indexOf(' ') > -1){
			action = action.substring(0, action.indexOf(' '));
		}

		// fire events
		if(true !== me.eventsSuspended && false === me.fireEvent('beforegroupaction', grid, records, action, currentVal)) {
			return false;
		}
		
		// call callback if any
		if('function' === typeof me.callbacks[action]) {
			me.callbacks[action](grid, records, action, currentVal);
		}

		return this.fireEvent('groupaction', grid, records, action, currentVal);
		
	}
});

/**
 * We need to override the ColumnLayout because it does not respect fixed sizes for columns 
 * (we don't want our width to change if it is auto width) override to enforce the forceFit config.
 */
Ext.override(Ext.grid.ColumnLayout, {
	calculateChildBoxes: function(visibleItems, targetSize) {
	    var me = this,
	        calculations = me.callParent(arguments),
	        boxes = calculations.boxes,
	        metaData = calculations.meta,
	        len = boxes.length, i = 0, box, item;
	
	    if (targetSize.width && !me.isColumn) {
	        // If configured forceFit then all columns will be flexed if they are not defined as fixed
	        if (me.owner.forceFit) {
	
	            for (; i < len; i++) {
	                box = boxes[i];
	                item = box.component;
	
	                // Set a sane minWidth for the Box layout to be able to squeeze flexed Headers down to.
	                item.minWidth = Ext.grid.plugin.HeaderResizer.prototype.minColWidth;
	
	                // For forceFit, just use allocated width as the flex value, and the proportions
	                // will end up the same whatever HeaderContainer width they are being forced into.
	                // UPDATE - Do not changed fixed columns to flex
	                if (!item.fixed) {
	                	item.flex = box.width;
	                }
	            }
	
	            // Recalculate based upon all columns now being flexed instead of sized.
	            calculations = me.callParent(arguments);
	        }
	        else if (metaData.tooNarrow) {
	            targetSize.width = metaData.desiredSize;
	        }
	    }
	
	    return calculations;
	}
});

/**
 * Override for the header resizer, there is a change where now if you make a column smaller the
 * rest is given to the next column (which looks nice) but it's not checking if it's not fixed.  On
 * top of that, when we added not([fixed]) we were getting random columns from another grid in the card
 * panel that should not have been returned.  Sanity check on the container fixes it but maybe there is
 * a root cause that is bigger?
 */
Ext.override(Ext.grid.plugin.HeaderResizer, {
    doResize: function() {
        if (this.dragHd) {
            var dragHd = this.dragHd,
                nextHd,
                offset = this.tracker.getOffset('point');

            
            if (dragHd.flex) {
                delete dragHd.flex;
            }
            
            if (this.headerCt.forceFit) {
            	
            	// UPDATE - Added :not([fixed]) to the component query and changed the method
            	// from nextNode to next to prevent it from traversing the component tree and getting
            	// a gridcolumn from another (possibly not rendered yet) grid
                nextHd = dragHd.next('gridcolumn:not([hidden]):not([isGroupHeader]):not([fixed])');
                                
                if (nextHd) {
                    this.headerCt.componentLayout.layoutBusy = true;
                }
            }
            
            dragHd.minWidth = this.origWidth + offset[0];
            dragHd.setWidth(dragHd.minWidth);
            
            if (nextHd) {
                delete nextHd.flex;
                nextHd.setWidth(nextHd.getWidth() - offset[0]);
                this.headerCt.componentLayout.layoutBusy = false;
                this.headerCt.doComponentLayout();
            }
        }
    }
});


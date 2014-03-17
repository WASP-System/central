/* Wasp.store.TreeGridStore
 * 
 * TreeStore is not designed for paging yet but this implementation of a TreeGridStore works nicely. It adds the paging functions from Ext.data.Store
 * to Ext.data.TreeStore.Code sourced from here: 
 * http://www.sencha.com/forum/showthread.php?205428-Extjs-tree-grid-paging-toolbar-with-remote-store&p=888643&viewfull=1#post888643
 * Code author: Lim Euncheon 
 */

Ext.define("Wasp.store.TreeGridStore", {    
	extend : "Ext.data.TreeStore",
    getTotalCount : function() {
        if(!this.proxy.reader.rawData) return 0;
        this.totalCount = this.proxy.reader.rawData.totalCount;
        return this.totalCount;
    },
    getStore : function() {
        return this;
    },
    currentPage : 1,
    clearRemovedOnLoad: true,
    clearOnPageLoad : true,
    addRecordsOptions: {
        addRecords: true
    },
    loadPage: function(page, options) {
        var me = this;


        me.currentPage = page;


        // Copy options into a new object so as not to mutate passed in objects
        options = Ext.apply({
            page: page,
            start: (page - 1) * me.pageSize,
            limit: me.pageSize,
            addRecords: !me.clearOnPageLoad
        }, options);


        if (me.buffered) {
            return me.loadToPrefetch(options);
        }
        me.read(options);
    },
    nextPage: function(options) {
        this.loadPage(this.currentPage + 1, options);
    },
    previousPage: function(options) {
        this.loadPage(this.currentPage - 1, options);
    },
    loadData: function(data, append) {
        var me = this,
            model = me.model,
            length = data.length,
            newData = [],
            i,
            record;


        for (i = 0; i < length; i++) {
            record = data[i];


            if (!(record.isModel)) {
                record = Ext.ModelManager.create(record, model);
            }
            newData.push(record);
        }


        me.loadRecords(newData, append ? me.addRecordsOptions : undefined);
    },
    loadRecords: function(records, options) {
        var me     = this,
            i      = 0,
            length = records.length,
            start,
            addRecords,
            snapshot = me.snapshot;


        if (options) {
            start = options.start;
            addRecords = options.addRecords;
        }


        if (!addRecords) {
            delete me.snapshot;
            me.clearData(true);
        } else if (snapshot) {
            snapshot.addAll(records);
        }


        me.data.addAll(records);


        if (start !== undefined) {
            for (; i < length; i++) {
                records[i].index = start + i;
                records[i].join(me);
            }
        } else {
            for (; i < length; i++) {
                records[i].join(me);
            }
        }


        /*
         * this rather inelegant suspension and resumption of events is required because both the filter and sort functions
         * fire an additional datachanged event, which is not wanted. Ideally we would do this a different way. The first
         * datachanged event is fired by the call to this.add, above.
         */
        me.suspendEvents();


        if (me.filterOnLoad && !me.remoteFilter) {
            me.filter();
        }


        if (me.sortOnLoad && !me.remoteSort) {
            me.sort(undefined, undefined, undefined, true);
        }


        me.resumeEvents();
        me.fireEvent('datachanged', me);
        me.fireEvent('refresh', me);
    },
    clearData: function(isLoad) {
        var me = this,
            records = me.data.items,
            i = records.length;


        while (i--) {
            records[i].unjoin(me);
        }
        me.data.clear();
        if (isLoad !== true || me.clearRemovedOnLoad) {
            me.removed.length = 0;
        }
    },
});
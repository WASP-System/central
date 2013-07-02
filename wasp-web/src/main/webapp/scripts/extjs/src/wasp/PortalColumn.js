/**
 * @class Ext.wasp.PortalColumn
 * @extends Ext.container.Container
 * A layout column class used internally be {@link Ext.wasp.PortalPanel}.
 */
Ext.define('Ext.wasp.PortalColumn', {
    extend: 'Ext.container.Container',
    alias: 'widget.portalcolumn',

    requires: [
        'Ext.layout.container.Anchor',
        'Ext.wasp.Portlet'
    ],

    layout: 'anchor',
    defaultType: 'portlet',
    cls: 'x-portal-column'

    // This is a class so that it could be easily extended
    // if necessary to provide additional behavior.
});
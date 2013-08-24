/**
 * @class Wasp.PortalColumn
 * @extends Ext.container.Container
 * A layout column class used internally be {@link Wasp.PortalPanel}.
 */
Ext.define('Wasp.PortalColumn', {
    extend: 'Ext.container.Container',
    alias: 'widget.portalcolumn',

    requires: [
        'Ext.layout.container.Anchor',
        'Wasp.Portlet'
    ],

    layout: 'anchor',
    defaultType: 'portlet',
    cls: 'x-portal-column'

    // This is a class so that it could be easily extended
    // if necessary to provide additional behavior.
});
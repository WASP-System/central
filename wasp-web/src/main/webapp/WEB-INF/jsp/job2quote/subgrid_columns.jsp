<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
subGrid: true,
subGridUrl:'<wasp:relativeUrl value="job2quote/subgridJSON.do" />',
subGridModel: [ 
    {
	    name  : [
	    	'<fmt:message key="job2quoteSubgrid.libraryConstructionCost.label"/>', 
	    	'<fmt:message key="job2quoteSubgrid.sequenceRunCost.label"/>', 
	    	'<fmt:message key="job2quoteSubgrid.additionalCost.label"/>', 
	    	'<fmt:message key="job2quoteSubgrid.initialFacilityCost.label"/>',
	    	'<fmt:message key="job2quoteSubgrid.discountCost.label"/>',
	    	'<fmt:message key="job2quoteSubgrid.disountedFailityCost.label"/>',
	    	'<fmt:message key="job2quoteSubgrid.computationalCost.label"/>',
	    	'<fmt:message key="job2quoteSubgrid.finalCost.label"/>'
	    ],
	    width : ['auto', 'auto', 'auto', 'auto', 'auto', 'auto', 'auto', 'auto'],
	    align : ['center', 'center', 'center', 'center', 'center', 'center', 'center', 'center']                
    }
],
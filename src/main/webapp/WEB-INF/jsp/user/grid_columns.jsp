<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="login" type="text"/>

<wasp:field name="password" type="password">
//Password is required on Add screen and is optional on Edit screen
//We cannot require password on Edit screen because we cannot pre-populate it (it's hashed) 
_beforeShowAddForm = function (formId) {
    var myPass=jQuery("#grid_id").jqGrid ('getGridParam', 'colModel')[1];//TODO: get password field dynammicly
    myPass['editrules']['custom_func']=_validate_required;
	myPass['editrules']['custom']=true;
	myPass['formoptions']={elmsuffix:'<font color=red>*</font>'};//TODO: make it work		
};
  
_beforeShowEditForm = function (formId) {
    var myPass=jQuery("#grid_id").jqGrid ('getGridParam', 'colModel')[1];//TODO: get password field dynammicly    
    myPass['editrules']['custom']=false;
	myPass['formoptions']={};	
};
</wasp:field>

<wasp:field name="firstName" type="text"/>

<wasp:field name="lastName" type="text"/>

<wasp:field name="email" type="text">
#field.jq['editrules']={custom:true,custom_func:_validate_email};
</wasp:field>

<wasp:field name="locale" type="select" items="${locales}" itemValue="key" itemLabel="value"/>

<wasp:field name="isActive"  type="checkbox" />
  



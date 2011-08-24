<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="login" object="user" type="text"/>

<wasp:field name="password" object="user" type="password">
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

<wasp:field name="firstName" object="user" type="text"/>

<wasp:field name="lastName" object="user" type="text"/>

<wasp:field name="email" object="user" type="text">
#field.jq['editrules']={custom:true,custom_func:_validate_email};
</wasp:field>

<wasp:field name="locale" object="user" type="select" items="${locales}" itemValue="key" itemLabel="value"/>

<wasp:field name="isActive" object="user" type="text">
#field.jq['edittype']='checkbox';   
#field.jq['editoptions']={value:"1:0"}; 
#field.jq['formatter']='checkbox';
#field.jq['formatoptions']={disabled : true};
#field.jq['align']='center';
#field.jq['search']=false;
</wasp:field>
  




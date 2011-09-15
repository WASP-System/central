</br>
<span style="color:blue;font-size:200%" id='statusMessage'></span>
<table id="grid_id"></table> 
<div id="gridpager"></div>
<script>
$("#grid_id").jqGrid('setGridParam', { rowNum: 50,height: '300'}).trigger("reloadGrid");    
</script>
    
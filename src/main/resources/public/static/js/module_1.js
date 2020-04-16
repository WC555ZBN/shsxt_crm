function formatterGrade(grade) {
    if(grade ==0){
        return "一级菜单";
    }
    if(grade ==1){
        return "二级菜单";
    }
    if(grade ==2){
        return "三级菜单";
    }
}

function formatterOp(value,rowData) {
    var title=rowData.moduleName+"_二级菜单";
    var href='javascript:openSecondModule("'+title+'",'+rowData.id+')';
    return "<a href='"+href+"'>二级菜单</a>";

}

function openSecondModule(title,mid) {
    window.parent.openTab(title,ctx+"/module/index/2?mid="+mid);
}

function searchModule() {
    $("#dg").datagrid("load",{
        moduleName:$("#s_moduleName").val(),
        code:$("#s_code").val()
    })
}

function openModuleAddDialog() {
    openDialog("dlg","菜单添加");
}

function closeModuleDialog() {
    closeDialog("dlg");
}

function saveOrUpdateModule() {
    saveOrUpdateRecode(ctx+"/module/save",ctx+"/module/update","dlg",searchModule,clearForm);

}

function clearForm() {
    $("#moduleName").val("");
    $("#moduleStyle").val("");
    $("#grade").val("");
    $("#optValue").val("");
    $("#orders").val("");
    $("#input[name='id']").val("");
}

function openModuleModifyDialog() {
    openModifyDialog("dg","fm","dlg","菜单更新");
}

function deleteModule() {
    deleteRecode("dg",ctx+"/module/delete",searchModule);
}
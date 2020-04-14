function searchRoles() {
   $("#dg").datagrid("load",{
       roleName:$("#s_roleName").val()
   })

}

function openRoleAddDialog() {
    openDialog("dlg","角色的添加")
}

function closeRoleDialog() {
    closeDialog("dlg")
}

function clearForm() {
    $("#roleName").val("");
    $("#roleRemark").val("");
    $("#input[name='id']").val("");
}
function saveOrUpdateRole() {
    saveOrUpdateRecode(ctx+"/role/save",ctx+"/role/update","dlg",searchRoles,clearForm)
}

function openRoleModifyDialog() {
    openModifyDialog("dg","fm","dlg","角色更新");
}

function deleteRole() {
    deleteRecode("dg",ctx+"/role/delete",searchRoles)
}

var zTreeObj;
var roleId;
function addModule() {

    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待授权的数据!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量授权!","error");
        return;
    }


    roleId=rows[0].id;

    $.ajax({
        url:ctx+"/module/queryAllModules",
        data:{
          roleId:roleId
        },
        type:"post",
        dataType:"json",
        success:function (data) {

            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                check: {
                    enable: true
                },
                callback:{
                    onCheck:zTreeOnCheck
                }
            };
            zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, data);
            openDialog("module","授权")
        }
    });


}


function zTreeOnCheck(event, treeId, treeNode) {
    /*alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);*/
    /**
     * 获取ztree所有选中得节点
     */
    var nodes=zTreeObj.getCheckedNodes(true);
    console.log(nodes);
    var mids="mids=";
    for(var i=0;i<nodes.length;i++){
        if(i<nodes.length-1){
            mids=mids+nodes[i].id+"&mids=";
        }else{
            mids=mids+nodes[i].id;
        }
    }

    console.log(mids);

    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mids+"&roleId="+roleId,
        dateType:"json",
        success:function (data) {
            console.log(data);
        }
    })

};


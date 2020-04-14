/**
 * 打开对话框
 * @param digId  对话框节点id
 * @param title   对话框标题
 */
function openDialog(digId,title) {
    $("#"+digId).dialog("open").dialog("setTitle",title);
}

function closeDialog(digId) {
    $("#"+digId).dialog("close")
}

/**
 * 添加与更新记录
 * @param saveUrl 添加记录后端的url地址
 * @param updateUrl 更新记录后端的url地址
 * @param dlgId     对话框的id
 * @param search    多条件搜索的方法名字
 * @param clearTab  清除表单的方法名字
 */
function saveOrUpdateRecode(saveUrl,updateUrl,dlgId,search,clearTab) {
    var url = saveUrl;
    if(!(isEmpty($("input[name='id']").val()))){
        url = updateUrl;
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);
            if(data.code==200){
               closeDialog(dlgId);
                search();
                clearTab();
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    });
}

/**
 *
 * @param dataGridId 表格的id
 * @param formId    表单的id
 * @param dlgId     对话框的id
 * @param title     对话框的标题
 */

function deleteRecode(dataGridId,deleteUrl,search) {
    var rows=$("#"+dataGridId).datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待删除的数据!!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量删除!","error");
        return;
    }

    $.messager.confirm("来自crm","请确认选中的删除记录!",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url:deleteUrl,
                data:{
                    id:rows[0].id
                },
                dataType:"json",
                success:function (data) {
                    if(data.code ==200){
                        search();
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            })
        }
    })
}

/**
 *
 * @param dataGridId  表格id
 * @param formId      表单id
 * @param dlgId      对话框id
 * @param title       对话框标题
 */
function openModifyDialog(dataGridId,formId,dlgId,title) {
    var rows=$("#"+dataGridId).datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待修改的数据!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量修改!","error");
        return;
    }

    $("#"+formId).form("load",rows[0]);
    openDialog(dlgId,title);
}

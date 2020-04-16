package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.service.ModuleService;
import com.shsxt.crm.service.PermissionService;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.vo.User;
import com.shsxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @Resource
    private ModuleService moduleService;
    /**
     * 登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        /*if(1==1){
            throw  new ParamsException("参数异常");
        }*/
        return "index";
    }

    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        List<String> permissions=permissionService.queryUserHasRolesHasPermissions(userId);
        request.getSession().setAttribute("permissions",permissions);

        request.getSession().setAttribute("modules",moduleService.queryUserHasRoleHasModuleDtos(userId));
        request.setAttribute("user",userService.selectByPrimaryKey(userId)); ;
        return "main";
    }



}

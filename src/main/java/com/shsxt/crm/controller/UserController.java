package com.shsxt.crm.controller;

import com.github.pagehelper.PageException;
import com.shsxt.base.BaseController;
import com.shsxt.crm.annotaions.RequirePermission;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.vo.User;
import com.shsxt.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    @RequestMapping("user/index")
    public String index(){
        return "user";
    }

    @GetMapping("user/queryUserByUserId")
    @ResponseBody
    public User queryUserByUserId(Integer userId){
        return userService.selectByPrimaryKey(userId);

    }

    @RequestMapping("user/login")
    @ResponseBody
    public ResultInfo login(String userName,String userPwd){

            UserModel userModel=userService.login(userName,userPwd);
          return success("用户登陆成功",userModel);
    }

    @RequestMapping("user/updatePassword")
    @ResponseBody
    public ResultInfo updatePassword(HttpServletRequest request,String oldPassword,String newPassword,String configPassword){
        userService.updateUserPassword(LoginUserUtil.releaseUserIdFromCookie(request),oldPassword,newPassword,configPassword);
        return success("密码更新成功");
    }


    @RequestMapping("user/save")
    @ResponseBody
    @RequirePermission(code = "601001")
    public ResultInfo saveUser(User user){
        user.getRoleIds().forEach(roleIds->{
            System.out.println(roleIds);
        });
        userService.saveUser(user);
        return success("用户记录添加成功");

    }

    @RequestMapping("user/list")
    @ResponseBody
    @RequirePermission(code = "601002")
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        return userService.queryByParamsForDataGrid(userQuery);
    }

    @RequestMapping("user/update")
    @ResponseBody
    @RequirePermission(code = "601003")
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户记录更新成功");
    }

    @RequestMapping("user/delete")
    @ResponseBody
    @RequirePermission(code = "601004")
    public ResultInfo deleteUser(@RequestParam(name="id") Integer userId){
        userService.deleteUser(userId);
        return success("用户记录删除成功");
    }
}

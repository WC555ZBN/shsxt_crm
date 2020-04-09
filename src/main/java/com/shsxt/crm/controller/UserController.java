package com.shsxt.crm.controller;

import com.github.pagehelper.PageException;
import com.shsxt.base.BaseController;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.vo.User;
import com.shsxt.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController extends BaseController {
    @Resource
    private UserService userService;

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

}

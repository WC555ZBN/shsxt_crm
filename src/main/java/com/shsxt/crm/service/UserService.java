package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.vo.User;
import com.shsxt.utils.AssertUtil;
import com.shsxt.utils.MD5Util;
import com.shsxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends BaseService<User,Integer> {

    @Autowired
    private UserMapper userMapper;

    public UserModel login(String userName, String userPwd){
        /**
         *
         * 1.参数检验
         *      用户名
         *      密码
         * 2.根据用户名查询用户记录
         * 3.校验用户所存在性
         *      不存在  -->记录不存在  方法结束
         * 4.用户存在
         *      校验密码
         *      密码错误 -->提示密码不正确 方法结束
         * 5.密码正确
         *      用户登录成功  -->返回用户相关信息
         *
         */
        checkLoginParams(userName,userPwd);
        User user=userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(null==user,"用户不存在或已注销");
        AssertUtil.isTrue(!(user.getUserPwd().equals(MD5Util.encode(userPwd))),"密码错误!");
        return buildUserModelInfo(user);


    }

    private UserModel buildUserModelInfo(User user) {

        return new UserModel(UserIDBase64.encoderUserID(user.getId()),user.getUserName(),user.getTrueName());
    }

    /**
     * 参数校验
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");

    }

    /**
     *
     */
    /*注解实现事务*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String configPassword){

        /**
         * 1.参数校验
         *      userId 非空 -->记录必须存在
         *      oldPassword 非空
         *      newPassword 非空  新密码不能与old密码相同
         *      configPassword 非空
         *2. 设置用户新密码
         *      新密码加密
         * 3.执行更新
         *
         */

        checkParams(userId,oldPassword,newPassword,configPassword);
        /*设置新密码*/
        User user = selectByPrimaryKey(userId);
        user.setUserPwd(MD5Util.encode(newPassword));
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"密码更新失败");


    }

    /**
     * 参数校验
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param configPassword
     */
    private void checkParams(Integer userId, String oldPassword, String newPassword, String configPassword) {
        User user =selectByPrimaryKey(userId);
        AssertUtil.isTrue(null==userId || null==user ,"用户不存在或用户未登录");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码!!");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        AssertUtil.isTrue(StringUtils.isBlank(configPassword),"请输入确认密码");
        AssertUtil.isTrue(!user.getUserPwd().equals(MD5Util.encode(oldPassword)),"原始密码不正确");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"旧密码与新密码不能相同");
        AssertUtil.isTrue(!(newPassword.equals(configPassword)),"新密码输入不一致");



    }
}

package com.shsxt.crm;

import com.alibaba.fastjson.JSON;
import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class GlobaExceptionResolver implements HandlerExceptionResolver {


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        /**
         * 首先异常的类型，如果异常为未登陆异常,执行视图装发
         *
         */
        ModelAndView mv = new ModelAndView();
        if(ex instanceof NoLoginException){
            NoLoginException ne = (NoLoginException) ex;
            mv.setViewName("no_login");
            mv.addObject("msg",ne.getMsg());
            mv.addObject("ctx",request.getContextPath());
            return mv;
        }

        /**方法返回值的类型判断
         *  如果方法级别存在ResponseBody的注解 响应内容为json否则为视图
         *  Handler类型参数
         * 返回值
         *      视图；默认的错误页面
         *
         *      json:错误的json信息
         */

        mv.setViewName("error");
        mv.addObject("code",400);
        mv.addObject("msg","系统异常,请稍后再试.....");
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if(null==responseBody){
                /**
                 * 方法返回视图
                 */
                if(ex instanceof ParamsException){
                     ParamsException pe = (ParamsException) ex;
                     mv.addObject("msg",pe.getMsg());
                     mv.addObject("code",pe.getCode());
                }
                return mv;

            }else{
                /**
                 * 方法返回json
                 */
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统错误请稍后再试!!");

                if(ex instanceof ParamsException){
                    ParamsException pe = (ParamsException) ex;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }

                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter pw =null;
                try {
                    pw=response.getWriter();
                    pw.write(JSON.toJSONString(resultInfo));
                    pw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(pw!=null){
                        pw.close();
                    }
                }

                return null;

            }
        }else{
            return mv;
        }


    }
}

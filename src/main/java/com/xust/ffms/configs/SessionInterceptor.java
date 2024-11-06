package com.xust.ffms.configs;


import com.xust.ffms.controller.UserInfoController;
import com.xust.ffms.entity.UserInfo;
import com.xust.ffms.utils.Config;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();


        //若session中没有用户信息 但cookie中存在用户信息，
        //则通过cookie中的信息重新初始化该用户信息，达到免登录的效果
        if (session.getAttribute(Config.CURRENT_USERNAME) != null && getCookieUser(request) != null) {
            if (HandlerMethod.class.equals(handler.getClass())) {
                Object controller = ((HandlerMethod) handler).getBean();
                if (controller instanceof UserInfoController) {
                    UserInfoController userInfoController = (UserInfoController) controller;
                    String userinfoStr = getCookieUser(request);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(Integer.parseInt(userinfoStr.split("_")[1]));
                    userInfo.setUsername(userinfoStr.split("_")[0]);
                    userInfo = userInfoController.getUserInfo(userInfo);
                    if (userInfo == null) {
                        return false;
                    }
                    userInfoController.setSessionUserInfo(userInfo, session);
                    return true;
                }
            }
        }

        //如果是移动端登录，则跳过登录验证
        if ("client".equals(request.getHeader("token"))) {
            return true;
        }
        String uri = request.getRequestURI();
        //是登录页面或者静态资源，不拦截
        if ("/".equals(uri) || "/login.html".equals(uri) || "/register.html".equals(uri) || "/login.do".equals(uri) || "/register.do".equals(uri) || uri.contains("/static/")) {
            return true;
        } else {
            //不是登录页面，则验证是否有session，没有则跳转到登录页面
            if (session.getAttribute(Config.CURRENT_USERNAME) == null) {
                response.sendRedirect("/login.html");
                return false;
            }
        }
        return true;
    }

    /**
     * 获取cookie中的用户信息
     *
     * @param request
     * @return
     */
    private String getCookieUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        } else {
            for (Cookie cookie : cookies) {
                if (Config.CURRENT_USERNAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
            return null;
        }
    }
}

package com.lhamster.interceptor;

import com.lhamster.response.exception.ServerException;
import com.lhamster.util.Audience;
import com.lhamster.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    // 指定放行的url地址（发送短信、注册、登录和密码重置）
    private List<String> urlList = new ArrayList<>(Arrays.asList("/organizations/web/user/message"
            , "/organizations/web/user/register"
            , "/organizations/web/user/login"
            , "/organizations/web/user/reset"));

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String servletPath = request.getServletPath();// 获取Servlet路径
        log.info("servletPath路径：{}", servletPath);
        // 获取请求头信息lhamster_identity_info信息
        final String authHeader = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        log.info("lhamster_identity_info= " + authHeader);
        // 放行发送验证码、注册、登录和手机号短信验证修改验证码接口
        if (urlList.contains(servletPath)) {
            return true;
        }
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            throw new ServerException(Boolean.FALSE, "用户未登录");
        }
        // 获取token
        final String token = authHeader.substring(9);
        // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
        JwtTokenUtil.parseJWT(token, Audience.base64Secret);
        return true;
    }
}

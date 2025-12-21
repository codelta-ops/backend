package com.give_hand.interceptor;

import com.give_hand.properties.JwtProperties; // 你项目里如果叫别的名字，用你自己的
import com.give_hand.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties; // 确保你的 properties 类里有 userSecretKey + userTokenName

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tokenName = jwtProperties.getUserTokenName(); // 一般是 token
        String token = request.getHeader(tokenName);

        // 1) 没带 token：直接 401，不要抛异常
        if (!StringUtils.hasText(token)) {
            log.warn("JWT校验失败：token为空");
            response.setStatus(401);
            return false;
        }

        try {
            // 2) 关键：parseJWT(token, secretKey)
            Claims claims = JwtUtil.parseJWT(token, jwtProperties.getUserSecretKey());
            // 你如果有 ThreadLocal 放用户信息，可在这里处理 claims
            return true;
        } catch (Exception e) {
            log.error("JWT验证失败，token={}", token, e);
            response.setStatus(401);
            return false;
        }
    }
}

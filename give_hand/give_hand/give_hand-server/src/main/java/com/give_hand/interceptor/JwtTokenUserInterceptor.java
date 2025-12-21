package com.give_hand.interceptor;

import com.give_hand.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 预检请求直接放行（CORS 常见）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            // 有些前端用 Authorization: Bearer xxx
            String auth = request.getHeader("Authorization");
            if (StringUtils.hasText(auth) && auth.startsWith("Bearer ")) {
                token = auth.substring(7);
            }
        }

        log.info("jwt校验:{}", token);

        if (!StringUtils.hasText(token)) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"Missing token\"}");
            return false;
        }

        try {
            Claims claims = JwtUtil.parseJWT(token);
            // 你如果需要把用户信息放到 request 里，可以在这里做
            // request.setAttribute("userId", claims.get("userId"));
            return true;
        } catch (Exception e) {
            log.error("JWT验证失败，token={}", token, e);
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"Invalid token\"}");
            return false;
        }
    }
}

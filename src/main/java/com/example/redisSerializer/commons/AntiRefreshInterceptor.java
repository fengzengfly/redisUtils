package com.example.redisSerializer.commons;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.redisSerializer.redisUtils.RedisUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author fengzeng
 * @create 2021/7/20 15:38
 */
@Component
public class AntiRefreshInterceptor implements HandlerInterceptor {

  private final static String ANTI_REFRESH_BLACKLIST = "anti:refresh:blacklist";

  @Resource
  private RedisUtil redisUtil;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    System.out.println("preHandler");
    response.setContentType("text/html;charset=utf-8");
    String clientIP = request.getRemoteAddr();
    String userAgent = request.getHeader("User-Agent");
    String key = "anti:refresh:"+DigestUtil.md5Hex(clientIP + "_" + userAgent);

    if (redisUtil.hasKey(ANTI_REFRESH_BLACKLIST)) {
      if (redisUtil.setIsNumber(ANTI_REFRESH_BLACKLIST, clientIP)) {
        response.getWriter().println("检测到您的IP异常，已加入黑名单");
        return false;
      }
    }
    Integer num = (Integer) redisUtil.getCacheObject(key);
    if (null == num) {
      redisUtil.setCacheObject(key, 1, 60, TimeUnit.SECONDS);
    } else {

      if (num > 20 && num <= 60) {
        response.getWriter().println("请求过于频繁,请稍后再试");
        redisUtil.increment(key, 1);
        return false;
      } else if (num > 60) {
        response.getWriter().println("检测到您的IP异常，已加入黑名单");
        Set set = new HashSet<>();
        set.add(clientIP);
        redisUtil.setCacheSet(ANTI_REFRESH_BLACKLIST, set);
        redisUtil.expire(ANTI_REFRESH_BLACKLIST, 24, TimeUnit.HOURS);
        return false;
      }
      redisUtil.increment(key, 1);
    }

    return true;
  }
}

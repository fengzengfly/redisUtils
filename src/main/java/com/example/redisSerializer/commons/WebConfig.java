package com.example.redisSerializer.commons;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author fengzeng
 * @create 2021/7/20 15:40
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Resource
  private AntiRefreshInterceptor antiRefreshInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(antiRefreshInterceptor).addPathPatterns("/test/","/**");
  }
}

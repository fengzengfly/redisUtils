package com.example.redisSerializer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengzeng
 * @create 2021/7/20 15:33
 */
@RestController
public class Test {

  @GetMapping("/test")
  public String test() {
    return "success";
  }
}

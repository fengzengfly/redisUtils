package com.example.redisSerializer;

import com.example.redisSerializer.redisUtils.Employee;
import com.example.redisSerializer.redisUtils.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class GsonSerializerApplicationTests {

  @Resource
  private RedisUtil redisUtil;

  @Test
  void contextLoads() {
  }

  @Test
  public void testRedis() {
    Employee employee = new Employee("6666", "liweishen", 20);
    ValueOperations<Object, Object> operations =
            redisUtil.setCacheObject("liweishen", employee, 30, TimeUnit.MINUTES);
    System.out.println(operations);
  }

  @Test
  public void getObjectTest() throws JsonProcessingException {
    Object employee = redisUtil.getCacheObject("liweishen");
    ObjectMapper objectMapper = new ObjectMapper();
    Employee employee1 = objectMapper.convertValue(employee, Employee.class);

    if (employee1!= null) {
      System.out.println("执行方法....");
      System.out.println(employee1.getName());
    }
    else{ System.out.println("数据库查询");}
  }

  @Test
  public void setRedisTest() {
    Gson gson = new Gson();
    String jsonString = gson.toJson(new Employee("3306", "fengzeng", 22));
    System.out.println(jsonString);
    Employee employee = gson.fromJson(jsonString, Employee.class);
    System.out.println(employee);
  }

  @Test
  public void test() {
    redisUtil.setCacheObject("key1", 1);
    Integer key1 = (Integer) redisUtil.getCacheObject("key1");
    System.out.println(key1);
  }

}

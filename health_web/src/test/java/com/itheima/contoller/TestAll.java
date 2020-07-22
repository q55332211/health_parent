package com.itheima.contoller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.itheima.controller.CheckgroupController;
import com.itheima.controller.SetmealControlle;
import com.itheima.entity.Result;

import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 8:17 2020/7/18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springmvc.xml")
public class TestAll {


    @Reference
    private SetmealService setmealService;

    @Test
    public void tset() {
        List<Setmeal> al1 = setmealService.findAll();
      //  Setmeal all = (Setmeal) all1;
        String json = JSONObject.toJSONString(al1);
        System.out.println(json);
    }


}

package com.itheima.controller;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 14:56 2020/7/10
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标记controlle
 * 方法绑定
 * 参数绑定
 * 添加参数
 */
@Controller()
@RequestMapping("/setmeal")
public class SetmealControlle {

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/findPage")
    @ResponseBody
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult page = this.setmealService.findPage(queryPageBean);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
    }


    @RequestMapping(value = "add" ,method = RequestMethod.POST)
    @ResponseBody
    public Result addSetmeal(Integer[] checkgroupIds,@RequestBody Setmeal setmeal){
        try {
          //  System.out.println(checkgroupIds);
            this.setmealService.addSetmeal(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new Result(false,MessageConstant.ADD_MEMBER_FAIL);
    }

}

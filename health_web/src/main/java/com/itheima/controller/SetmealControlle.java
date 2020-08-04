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
import com.itheima.health.exception.HealthException;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;

import com.itheima.untis.CosUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        PageResult page = this.setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, page);
    }


    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result addSetmeal(Integer[] checkgroupIds, @RequestBody Setmeal setmeal) {
        //  System.out.println(checkgroupIds);
        this.setmealService.addSetmeal(setmeal, checkgroupIds);
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);

    }


    /**
     * 文件上传到腾讯cos对象存储
     *
     * @param imgFile 前端对应文件
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            String imgInfo = CosUtils.uploadImgVuserInfo(imgFile);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS, imgInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    @ResponseBody
    @RequestMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(Integer id) {
        try {
            List<Integer> data = this.setmealService.findCheckgroupIdsBySetmealId(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @ResponseBody
    @RequestMapping("/update")
    public Result update(@RequestParam("checkgroupIds") Integer[] checkgroupIds, @RequestBody Setmeal setmeal) {
        try {
            this.setmealService.update(checkgroupIds, setmeal);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
    }

    @ResponseBody
    @RequestMapping("/getSetmealById")
    public Result getSetmaelById(Integer id) {
        if (null != id) {
            Setmeal setmeal = this.setmealService.findById(id);
            if (null != setmeal) {
                return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
            }
        }
        throw new HealthException(MessageConstant.QUERY_SETMEAL_FAIL);
    }

}

package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.junit.Test;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 17:08 2019/7/11
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckgroupController {


    @Reference
    private CheckGroupService checkgroupService;


    @RequestMapping("/findPage")
    @ResponseBody
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = checkgroupService.findPage(queryPageBean);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, pageResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }


    /**
     * 添加检查组
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addCheckgroup(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup) {
        try {
            checkgroupService.add(checkitemIds, checkGroup);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }

    }

    /**
     * 根据id查找检查组
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @ResponseBody
    public Result findById(Integer id) {
        try {
            CheckGroup checkGroup = checkgroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    //查询检查组对应检查项id
    @RequestMapping(value = "findCheckItemsById", method = RequestMethod.GET)
    @ResponseBody
    public Result findCheckItemsById(Integer id) {
        try {
            Integer[] checkItems = checkgroupService.findCheckItemsById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItems);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 修改检查组
     *
     * @param checkitemIds
     * @param checkGroup
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Result update(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup) {
        checkgroupService.update(checkitemIds, checkGroup);
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }


    @ResponseBody
    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public Result deleteById(Integer id) {
        checkgroupService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public Result findAll() {
        List<CheckGroup> data = this.checkgroupService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, data);
    }


}

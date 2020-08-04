package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 20:22 2019/7/9
 */

/**
 * 检查项控制器
 */
@RequestMapping("/checkitem")
@RestController
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestBody CheckItem checkItem) {
        checkItemService.add(checkItem);
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPage) throws HealthException {
        PageResult page = checkItemService.findPage(queryPage);
        if (page == null) {
            throw new HealthException(MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/update")
    @ResponseBody
    public Result update(@RequestBody CheckItem checkItem) {
        checkItemService.update(checkItem);
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody CheckItem checkItem) {

        Integer count = this.checkGroupService.findCheckGroupIdBycheckItemId(checkItem.getId());
        if (null != count && count > 0) {
            //查询是否已经被检查组使用。如果被使用 返回已经被使用
            checkItemService.delete(checkItem);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        }
        return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public Result findAll() {
        List<CheckItem> list = checkItemService.findAll();
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS, list);
    }
}

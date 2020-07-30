package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@Controller
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
          //  System.out.println("save");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage")
    @ResponseBody
    public Result findPage(@RequestBody QueryPageBean queryPage) {
        try {

            PageResult page = checkItemService.findPage(queryPage);
            if (page != null) {
                return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, page);
            }
        } catch (Exception e) {
            System.out.println("数据查询异常");
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_FAIL);
    }

    @RequestMapping("/update")
    @ResponseBody
    public Result update(@RequestBody CheckItem checkItem) {
        try {

            checkItemService.update(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据查询异常");
        }
        return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody CheckItem checkItem) {
        try {
            Integer count = this.checkGroupService.findCheckGroupIdBycheckItemId(checkItem.getId());
            if (null != count && count > 0) {
                //查询是否已经被检查组使用。如果被使用 返回已经被使用
                checkItemService.delete(checkItem);
                return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
           // System.out.println("数据查询异常");
        }
        return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public Result findAll() {
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
           // System.out.println("数据查询异常");
        }
        return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
    }
}

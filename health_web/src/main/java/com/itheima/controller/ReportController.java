package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @Date: Create in 14:42 2020/7/27
 */
@RestController
@RequestMapping("report")
public class ReportController {


    @Reference
    private ReportService reportService;

    /***
     * 查询一年内的会员数据
     * @return
     */
    @RequestMapping("getMemberReport")
    public Result getMemberReport() {
        //封装12个月数据
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        //将日历设置到12个月之前
        calendar.add(Calendar.MONTH, -12);
        //通过12个月数据调用servce去获取每个月的用户数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //使用for循环去获取对应数据
        for (int i = 0; i < 12; i++) {
            //每次添加一个月
            calendar.add(Calendar.MONTH, 1);
            //格式化日期
            String month = sdf.format(calendar.getTime());
            //封装到list
            months.add(month);
        }
        List<Integer> mbmberCount = this.reportService.getMemberReport(months);
        Map<String, Object> data = new HashMap<>();
        //将数据使用map封装返回给前端
        data.put("months", months);
        data.put("memberCount", mbmberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, data);
    }

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            Map<String, Object> data = new HashMap<>();
            //获取所有id
            List<Map<String, Object>> setmealCount = this.setmealService.findSetmealCount();
            //遍历list 取出对应setmealName
            List<String> setmealNames = new ArrayList<>();
            if (null != setmealCount) {
                for (Map<String, Object> map : setmealCount) {
                    setmealNames.add(map.get("name").toString());
                }
                data.put("setmealNames", setmealNames);
                data.put("setmealCount", setmealCount);
                return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
    }


}

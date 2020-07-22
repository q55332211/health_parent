package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.utlis.DateFormatUtil;
import com.itcast.utlis.POIUtils;
import com.itheima.entity.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 16:28 2020/7/15
 */
@Controller
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量添加
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        List<OrderSetting> orderSettingList = new ArrayList<>();

        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            for (String[] strings : list) {
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                orderSettingList.add(orderSetting);
            }
            this.orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }


    @RequestMapping(value = "/getOrderSettingByMothe", method = RequestMethod.POST)
    @ResponseBody
    public Result getOrderSettingByMothe(@RequestParam("date") String date) {
        try {
            List<OrderSetting> list = this.orderSettingService.findOrderSettingByMothe(date);
            List<Map<String, Object>> data = new ArrayList<>();
            for (OrderSetting orderSetting : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("date", DateFormatUtil.dateFormat("dd", orderSetting.getOrderDate()));
                map.put("number", orderSetting.getNumber());
                map.put("reservations", orderSetting.getReservations());
                data.add(map);
            }
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.QUERY_ORDER_FAIL);

    }


    @RequestMapping("/exitNumberByDate")
    @ResponseBody
    public Result exitNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            this.orderSettingService.exitNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.ORDERSETTING_FAIL);
    }

}

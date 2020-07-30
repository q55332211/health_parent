package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;

import com.itheima.pojo.Order;
import com.itheima.untis.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @Date: Create in 15:38 2020/7/27
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据月份查询会员数
     *
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {

        List<Integer> membercounts = new ArrayList<>();
        for (String month : months) {
            // + "-31" 组装日期类型
            Integer count = this.memberService.getCountByMoth(month + "-31");
            if (count != null) {
                membercounts.add(count);
            }
        }
        return membercounts;
    }

    /***
     * 运维数据统计
     * @return
     */
    @Override
    public Map<String, Object> getBusinessData() {

        /**  --------------------------------日期数据初始化------------------------------------------------- **/
        Map<String, Object> data = new HashMap<>();
        //运维日期 reporeDate
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String todayString = sdf.format(today);
        //星期一
        String monday = sdf.format(DateUtils.getFirstDayOfWeek(today));
        //星期天
        String sunday = sdf.format(DateUtils.getLastDayOfWeek(today));
        //1号
        String firsDayofThisMonth = sdf.format(DateUtils.getFirstDay4ThisMonth());
        //本月最后一日
        String lastDayofThisMonth = sdf.format(DateUtils.getLastDay4ThisMonth());
        /**  --------------------------------会员数据处理------------------------------------------------- **/
        //今日新会员 todayNewMember
        Integer todayNewMember = memberService.getNewMemeberByDate(today);
        //总会员数           totalMember :0,
        Integer totalMember = memberService.getTotalMember();
        //本月新会员  使用两个数字相减？            thisMonthNewMember :0,
        Integer thisMonthNewMember = this.memberService.getThisMothMember(firsDayofThisMonth, lastDayofThisMonth);
        //本周新会员数     thisWeekNewMember :0, 用in 好像是比用符号好点  用 weeki 最好
        Integer thisWeekNewMember = this.memberService.getMemberByWeek(monday, sunday);

        /**  --------------------------------订单数据处理------------------------------------------------- **/
        //今日预约数             todayOrderNumber :0,
        Integer todayOrderNumber = this.orderService.findByDate(today);
        // 今日就诊数            todayVisitsNumber :0,
        Integer todayVisitsNumber = this.orderService.findOrderInfoByCondition(todayString, todayString, Order.ORDERSTATUS_YES);
        // 本月预约数              thisMonthOrderNumber :0,
        Integer thisMonthOrderNumber = this.orderService.findOrderInfoByCondition(firsDayofThisMonth, lastDayofThisMonth, null);
        //本月就诊数                thisMonthVisitsNumber :0,
        Integer thisMonthVisitsNumber = this.orderService.findOrderInfoByCondition(firsDayofThisMonth, todayString, Order.ORDERSTATUS_YES);
        //本周预约数             thisWeekOrderNumber :0,
        Integer thisWeekOrderNumber = this.orderService.findOrderInfoByCondition(monday, sunday, null);
        //本周就诊数                thisWeekVisitsNumber :0,
        Integer thisWeekVisitsNumber = this.orderService.findOrderInfoByCondition(monday, todayString, Order.ORDERSTATUS_YES);
        //热门套餐           hotSetmeal
        List<Map<String, Object>> hotSetmeal = this.setmealService.getHotSetmeal();

        /**  --------------------------------数据封装------------------------------------------------- **/
        data.put("reporeDate", todayString);
        data.put("todayNewMember", todayNewMember);
        data.put("totalMember", totalMember);
        data.put("thisMonthNewMember", thisMonthNewMember);
        data.put("thisWeekNewMember", thisWeekNewMember);
        data.put("todayOrderNumber", todayOrderNumber);
        data.put("todayVisitsNumber", todayVisitsNumber);
        data.put("thisMonthOrderNumber", thisMonthOrderNumber);
        data.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        data.put("thisWeekOrderNumber", thisWeekOrderNumber);
        data.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        data.put("hotSetmeal", hotSetmeal);
        return data;
    }




}

package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
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

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> data = this.reportService.getBusinessData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
    }

    /**
     * 导出运营统计数据报表
     */
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res) {
        //获得模板文件
        String templatePath = req.getSession().getServletContext().getRealPath("/static/template/report_template.xlsx");
        try {
            //从res中获取输出流
            OutputStream os = res.getOutputStream();
            //创建工作文件
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(templatePath);
            //获得工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            //获得后台运维数据
            Map<String, Object> businessData = this.reportService.getBusinessData();
            sheet.getRow(2).getCell(5).setCellValue(businessData.get("reporeDate").toString());

            /**-------------------------会员--------------------------------------*/
            sheet.getRow(4).getCell(5).setCellValue((Integer) businessData.get("todayNewMember"));
            sheet.getRow(4).getCell(7).setCellValue((Integer) businessData.get("totalMember"));
            sheet.getRow(5).getCell(5).setCellValue((Integer) businessData.get("thisWeekNewMember"));
            sheet.getRow(5).getCell(7).setCellValue((Integer) businessData.get("thisMonthNewMember"));
            /**-------------------------预约--------------------------------------*/
            sheet.getRow(7).getCell(5).setCellValue((Integer) businessData.get("todayOrderNumber"));
            sheet.getRow(7).getCell(7).setCellValue((Integer) businessData.get("todayVisitsNumber"));
            sheet.getRow(8).getCell(5).setCellValue((Integer) businessData.get("thisWeekOrderNumber"));
            sheet.getRow(8).getCell(7).setCellValue((Integer) businessData.get("thisWeekVisitsNumber"));
            sheet.getRow(9).getCell(5).setCellValue((Integer) businessData.get("thisMonthOrderNumber"));
            sheet.getRow(9).getCell(7).setCellValue((Integer) businessData.get("thisMonthVisitsNumber"));
            /**-------------------------热门套餐-----------------------------------*/
            // 热门套餐
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) businessData.get("hotSetmeal");
            int row = 12;
            for (Map<String, Object> setmealMap : hotSetmeal) {
                sheet.getRow(row).getCell(4).setCellValue((String) setmealMap.get("name"));
                sheet.getRow(row).getCell(5).setCellValue((Double) setmealMap.get("setmeal_count"));
                Double proportion = (Double) setmealMap.get("proportion");
                sheet.getRow(row).getCell(6).setCellValue(proportion.doubleValue());
                sheet.getRow(row).getCell(7).setCellValue((String) setmealMap.get("remark"));
                row++;
            }
            /**-------------------------写入到reponse-----------------------------------*/
            // 工作簿写给reponse输出流
            res.setContentType("application/vnd.ms-excel");
            String filename = "运营统计数据报表.xlsx";
            // 设置UTF-8编码Strng 解决下载的文件名 中文乱码
            filename = new String(filename.getBytes(), "ISO-8859-1");
            // 设置头信息，告诉浏览器，是带附件的，文件下载
            res.setHeader("Content-Disposition", "attachement;filename=" + filename);
            xssfWorkbook.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

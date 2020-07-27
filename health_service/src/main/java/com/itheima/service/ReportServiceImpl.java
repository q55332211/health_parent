package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;

import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Date: Create in 15:38 2020/7/27
 */
@Service(interfaceClass = ReportService.class)

public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberService memberService;

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
}

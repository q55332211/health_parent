package com.itheima.service;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Date: Create in 15:37 2020/7/27
 */
public interface ReportService {

    List<Integer> getMemberReport(List<String> months);

    Map<String, Object> getBusinessData();

}

package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Date: Create in 14:24 2020/7/22
 */
public interface MemberService {

    Member findByPhone(String phone);

    void add(Member member);

    Member findByid(int parseInt);

    Integer getCountByMoth(String month);

    Integer getNewMemeberByDate(Date date);

    Integer getTotalMember();

    Integer getThisMothMember(String stratDate, String endDate);

    Integer getMemberByWeek(String monday, String sunday);
}


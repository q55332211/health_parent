package com.itheima.dao;

import com.itheima.pojo.Member;

import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Date: Create in 14:29 2020/7/22
 */
public interface MemberDao {

    Member findByPhone(String phone);

    void add(Member member);

    Member findById(int id);

    Integer getCountByMoth(String month);
    /*根据日期查询当日新会员*/
    Integer getNewMemeberByDate(Date date);
    /*查询会员总数*/
    Integer getTotalMember();
    //查询当月会员
    Integer getThisMothMember(Map<String,String> map);
    //查询当月新会员
    Integer getMemberByWeek(Map<String,String> map);
}

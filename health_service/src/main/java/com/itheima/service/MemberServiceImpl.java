package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;


import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Date: Create in 14:26 2020/7/22
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberdao;

    @Override
    public Member findByPhone(String phone) {
        if (phone != null) {
            return memberdao.findByPhone(phone);
        }
        return null;
    }

    /**
     * 添加一个用户
     *
     * @param
     * @return
     */
    @Override
    public void add(Member member) {
        this.memberdao.add(member);
    }

    @Override
    public Member findByid(int id) {
        return this.memberdao.findById(id);
    }

    @Override
    public Integer getCountByMoth(String month) {
        return this.memberdao.getCountByMoth(month);
    }

    /**
     * 查询当天新会员
     *
     * @param date
     * @return
     */
    @Override
    public Integer getNewMemeberByDate(Date date) {
        return this.memberdao.getNewMemeberByDate(date);
    }

    @Override
    public Integer getTotalMember() {
        return this.memberdao.getTotalMember();
    }

    @Override
    public Integer getThisMothMember(String stratDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("stratDate", stratDate);
        map.put("endDate", endDate);
        return this.memberdao.getThisMothMember(map);
    }

    @Override
    public Integer getMemberByWeek(String monday, String sunday) {
        Map<String, String> map = new HashMap<>();
        map.put("monday", monday);
        map.put("sunday", sunday);
        return this.memberdao.getMemberByWeek(map);
    }
}

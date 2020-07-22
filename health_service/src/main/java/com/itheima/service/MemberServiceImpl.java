package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;


import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;

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
     * @param map
     * @return
     */
    @Override
    public Integer add(Map map) {
        return this.memberdao.add(map);
    }
}

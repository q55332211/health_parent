package com.itheima.dao;

import com.itheima.pojo.Member;

import java.util.Map;

/**
 * @Description:
 * @Date: Create in 14:29 2020/7/22
 */
public interface MemberDao {

    Member findByPhone(String phone);

    void add(Member member);

    Member findById(int id);
}

package com.itheima.dao;

import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Date: Create in 17:49 2020/7/26
 */
public interface UserDao {

    User findUserByName(String username);

    Set<Role> findRolesById(Integer id);

    Set<Permission> findPermissionsInRoles(List<Integer> rids);
}

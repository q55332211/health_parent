package com.itheima.service;

import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.pojo.Permission;


import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Date: Create in 16:26 2020/7/26
 */
public interface UserService {


    User findUserByName(String username);

   Set<Role> findRolesById(Integer id);

    Set<Permission> findPermissionsInRoles(List<Integer> roles);
}

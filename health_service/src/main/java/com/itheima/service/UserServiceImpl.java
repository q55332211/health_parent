package com.itheima.service;

import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Date: Create in 17:47 2020/7/26
 */
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findUserByName(String username) {
        return this.userDao.findUserByName(username);
    }

    @Override
    public Set<Role> findRolesById(Integer id) {
        return this.userDao.findRolesById(id);
    }

    @Override
    public Set<Permission> findPermissionsInRoles(List<Integer> roles) {
        return this.userDao.findPermissionsInRoles(roles);
    }
}

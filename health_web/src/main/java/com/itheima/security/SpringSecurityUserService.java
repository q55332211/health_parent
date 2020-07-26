package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Date: Create in 16:22 2020/7/26
 */
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.itheima.pojo.User dbUser = userService.findUserByName(username);
        if (dbUser == null) {
            return null;
        }
        //创建权限集合
        List<GrantedAuthority> list = new ArrayList<>();
        //根据用户id查询用户角色
        Set<Role> roles = userService.findRolesById(dbUser.getId());
        /*取得所有角色role的id*/
        List<Integer> rids = new ArrayList<>();
        for (Role role : roles) {
            rids.add(role.getId());
        }
        //根据用户角色查询用户权限
        Set<Permission> permissions = this.userService.findPermissionsInRoles(rids);
        for (Permission permission : permissions) {
            list.add(new SimpleGrantedAuthority(permission.getKeyword()));        }
        System.out.println("success");
        return new User(username, dbUser.getPassword(), list);
    }
}

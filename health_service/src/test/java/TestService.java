import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.dao.SetmealDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.Setmeal;
import com.itheima.pojo.User;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealServiceImpl;
import com.itheima.vo.SetmealVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Date: Create in 20:07 2020/7/26
 */
@ContextConfiguration("classpath:applicationContext-tx.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestService {
    @Autowired
    private UserDao userdao;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
   // private SetmealServiceImpl setmealService;

    @Test
    public void testfindUserByName() {
        User user = userdao.findUserByName("admin");
    }


    @Test
    public void testFindRolesById() {
        Set<Role> roles = userdao.findRolesById(2);

    }

    @Test
    public void testFindPermissionsInRoles() {
        //  Integer[] rids = {2, 3};
        List<Integer> rids = new ArrayList<>();
        rids.add(2);
        rids.add(3);
        Set<Permission> roles = userdao.findPermissionsInRoles(rids);


        System.out.println(roles);
    }

    @Test
    public void createPassword() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String admin = bCryptPasswordEncoder.encode("admin");
        System.out.println(admin);
    }


    @Test
    public void testSetmealVo() {
        // SetmealVo setmealVo = setmealDao.getSetmealVoById(12);
      //  Setmeal setmeal = setmealService.getSetmealVoById(12);
    }


}

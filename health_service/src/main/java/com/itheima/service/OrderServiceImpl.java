package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderDao;
import com.itheima.entity.MessageConstant;
import com.itheima.entity.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;
import com.itheima.untis.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Date: Create in 16:37 2020/7/21
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingService orderSettingService;
    @Autowired
    private MemberService memberService;

    @Autowired
    private SetmealService setmealService;

    /***
     * 提交预约/ 好坑爹的业务
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Result sumbit(Map map) throws Exception {
        ///查询日期是否可以约  查询之前要进行日期转换
        String dateString = map.get("orderDate").toString();
        Date date = DateUtils.parseString2Date(dateString);
        OrderSetting orderSetting = this.orderSettingService.getOrderSettingByDate(date);
        //查询是否可以预约
        if (orderSetting == null) {
            System.out.println(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //查询是否已经约满
        if (orderSetting.getNumber() <= orderSetting.getReservations()) {
            System.out.println(MessageConstant.ORDER_FULL);
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //查询用户是否已经注册
        Member member = this.memberService.findByPhone((String) map.get("telephone"));
        //yo
        if (member == null) {
            // 未注册 自动注册添加信息到用户表 然后创建 order //todo sql未写
            member = new Member();
            member.setIdCard((String) map.get("idCard"));
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(DateUtils.parseString2Date(map.get("orderDate").toString()));
            member.setPhoneNumber((String) map.get("telephone"));
            this.memberService.add(member);
            System.out.println("用户未注册，自动注册完成");
        }
        //防止重复提交
        map.put("memberId", member.getId());
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setSetmealId(Integer.parseInt(map.get("setmealId").toString()));
        order.setOrderDate(date);
        long count = this.orderDao.getOrder(order);
        if (count >= 1) {
            System.out.println(MessageConstant.HAS_ORDERED);
            return new Result(false, MessageConstant.HAS_ORDERED);
        }
        //已经注册直接创建订单order   //在订单表添加一条预约信息
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        this.orderDao.add(order);
        //添加预约人数
        this.orderSettingService.exitReservationsByDate(date);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @Override
    public boolean findByDate(String date) {
        long count = this.orderDao.findByDate(date);
        return 1 == count;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getOrderById(Integer id) {
        Map<String, Object> map = this.orderDao.getOrderById(id);
        Integer setmealId = Integer.parseInt(map.get("setmeal_id").toString());
        Setmeal setmeal = this.setmealService.findById(setmealId);
        map.put("setmeal", setmeal.getName());
        String memberId = map.get("member_id").toString();
        Member member = this.memberService.findByid(Integer.parseInt(memberId));
        map.put("member", member.getName());
        return map;
    }

}

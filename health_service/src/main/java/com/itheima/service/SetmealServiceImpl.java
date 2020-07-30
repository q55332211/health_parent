package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 15:08 2020/7/10
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Reference
    private CheckGroupService checkGroupService;

    @Reference
    private CheckItemService checkItemService;

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void addSetmeal(Setmeal setmeal, Integer[] checkgroupIds) {
        //插入套餐数据
        this.setmealDao.addSetmeal(setmeal);
        //获得套餐id
        Integer id = setmeal.getId();
        //根据套餐id 插入多条 检查组记录
        //
        setSetmealAndCheckGroup(id, checkgroupIds);
        return;

    }

    @Override
    public List<Setmeal> findAll() {
        return this.setmealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        return toFor(id);
    }

    @Override
    public Map<String, Object> queryById(Integer sid) {
        //通过套餐id查询检查组
        Map<String, Object> data = new HashMap();
        List<Integer> checkGroupIds = this.checkGroupService.getCheckGroupIds(sid);

        List<CheckItem> checkItems = this.checkItemService.queryByCheckGroupIds(checkGroupIds);
        Setmeal setmeal = this.setmealDao.findById(sid);
        data.put("setmeal", setmeal);
        data.put("checkItems", checkItems);
        data.put("total", checkItems.size());
        return data;
    }

    @Override
    public Setmeal findBaseSemealById(Integer id) {
        return this.setmealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return this.setmealDao.findSetmealCount();
    }

    @Autowired
    private OrderService orderService;

    /**
     * 查询热门套餐
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getHotSetmeal() {
        List<Map<String, Object>> list = new ArrayList<>();
        //查询热门前五条
        List<Map<String, Object>> hotSetmeal = this.setmealDao.getHotSetmeal();
        //     List<Map<String,Object>> setmeals = this.setmealDao.getHotSetmeal();
        //查询订单总数
        Integer total = orderService.getTotalCount();
        // Map<String, Object> map = new HashMap();
        if (null == total || total <= 0) {
            return null;
        }
        //查询每个订单预约数
        for (Map<String, Object> setmeal : hotSetmeal) {
            Map<String, Object> map = new HashMap();
            float count = Integer.parseInt(setmeal.get("count").toString());
            float proportion = count / total;
            map.put("proportion", proportion);
            map.put("name", setmeal.get("name").toString());
            map.put("setmeal_count", count);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(Integer id) {
        return this.setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    @Override
    public void update(Integer[] checkgroupIds, Setmeal setmeal) {
        //根据sid删除原来的关联
        this.setmealDao.deleteCheckGroupAndSetmealBySid(setmeal.getId());
        //批量插入新的关联值
        Map<String, Object> params = new HashMap<>();
        List<Integer> ids = Arrays.asList(checkgroupIds);
        params.put("sid", setmeal.getId());
        params.put("ids", ids);
        this.setmealDao.insertCheckGroups(params);
        //更新套餐数据
        this.setmealDao.updateSetmeal(setmeal);
    }


    public Setmeal toFor(Integer id) {
        //通过套餐id查询检查组
        List<CheckGroup> checkGroups = checkGroupService.queryCheckGroupsBySid(id);
        for (CheckGroup checkGroup : checkGroups) {
            //根据checkGroupId查询检查项
            List<CheckItem> checkItems = this.checkItemService.quertByGid(checkGroup.getId());
            checkGroup.setCheckItems(checkItems);
        }
        Setmeal setmeal = this.setmealDao.findById(id);
        setmeal.setCheckGroups(checkGroups);
       // System.out.println(JSONObject.toJSONString(setmeal));
        return setmeal;
    }


    //插入id对应的chechgroups 这里为什么要用map 不用怎么写
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {

        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmealid", id);
            map.put("checkgroupId", checkgroupId);
            this.setmealDao.setSetmealAndCheckGroup(map);
        }
    }


}

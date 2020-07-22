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
        System.out.println(JSONObject.toJSONString(setmeal));
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

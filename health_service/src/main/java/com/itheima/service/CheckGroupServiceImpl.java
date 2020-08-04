package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.vo.CheckGroupVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 17:21 2019/7/11
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加检查组
     *
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        //添加检测组
        checkGroupDao.add(checkGroup);
        //保存检查组检查项中间表
        Integer groupId = checkGroup.getId();
        System.out.println(groupId);
        setCheckGroupAndCheckItem(groupId, checkitemIds);
    }

    /**
     * 编辑检查组
     *
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public void update(Integer[] checkItems, CheckGroup checkGroup) {
        //更新检查组
        checkGroupDao.update(checkGroup);
        //删除原有关联数据
        checkGroupDao.deleteCheckItemById(checkGroup.getId());
        //插入新数据
        this.setCheckGroupAndCheckItem(checkGroup.getId(), checkItems);
    }

    @Override
    public Integer[] findCheckItemsById(Integer id) {
        return checkGroupDao.findCheckItemsById(id);
    }

    /**
     * 根据检查项删除检查组
     *
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //删除关联检查项
        checkGroupDao.deleteCheckItemById(id);
        //删除关联检查组
        checkGroupDao.deleteCheckGroupById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    @Override
    public List<CheckGroup> queryCheckGroupsBySid(Integer sid) {
        return this.checkGroupDao.queryCheckGroupsBySid(sid);
    }

    @Override
    public List<Integer> getCheckGroupIds(Integer id) {
        return this.checkGroupDao.queryCheckGroupIds(id);
    }

    @Override
    public Integer findCheckGroupIdBycheckItemId(Integer id) {
        return this.checkGroupDao.findCheckGroupIdBycheckItemId(id);
    }

    @Override
    public List<CheckGroup> getCheckGroupsById(Integer id) {
        return this.checkGroupDao.getCheckGroupsById(id);
    }


    /**
     * 设置检查项
     *
     * @param groupId
     * @param checkitemIds
     */
    private void setCheckGroupAndCheckItem(Integer groupId, Integer[] checkitemIds) {
        for (Integer checkitemId : checkitemIds) {
            //将两个参数放入map，为后续配置文件使用map方便
            Map<String, Integer> map = new HashMap<>();
            map.put("groupId", groupId);
            map.put("checkitemId", checkitemId);
            checkGroupDao.setCheckGroupAndCheckItem(map);
        }

    }


}

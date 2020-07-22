package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 17:27 2019/7/11
 */
public interface CheckGroupDao {

    Page<CheckGroup> findPage(String queryString);

    void add(CheckGroup checkGroup);

    //插入一对多数据
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    CheckGroup findById(Integer id);

    void update(CheckGroup checkGroup);
    //根据检查组id查询所选检查项
    Integer[] findCheckItemsById(Integer id);

    void deleteCheckItemById(Integer id);

    void deleteCheckGroupById(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> queryCheckGroupsBySid(Integer sid);

    List<Integer> queryCheckGroupIds(Integer sid);

}

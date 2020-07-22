package com.itheima.service;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 17:17 2019/7/11
 */
public interface CheckGroupService {

    PageResult findPage(QueryPageBean queryPageBean);

    void add(Integer[] checkitemIds, CheckGroup checkGroup);

    CheckGroup findById(Integer id);

    void update(Integer[] checkItems, CheckGroup checkGroup);

    Integer[] findCheckItemsById(Integer id);

    void deleteById(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> queryCheckGroupsBySid(Integer sid);


    List<Integer> getCheckGroupIds(Integer id);
}

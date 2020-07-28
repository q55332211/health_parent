package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 15:05 2020/7/10
 */
public interface SetmealService {

    //分页查询
    PageResult findPage(QueryPageBean queryPageBean);

    void addSetmeal(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    Map<String, Object> queryById(Integer id);

    Setmeal findBaseSemealById(Integer id);

    List<Map<String, Object>> findSetmealCount();

    List<Map<String, Object>>  getHotSetmeal();
}

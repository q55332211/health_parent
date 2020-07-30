package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 15:09 2020/7/10
 */
public interface SetmealDao {

    //分页查询
    Page<Setmeal> selectByCondition(String queryString);

    void addSetmeal(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetmealCount();

    List<Map<String,Object>> getHotSetmeal();

    List<Integer> findCheckgroupIdsBySetmealId(Integer id);

    void deleteCheckGroupAndSetmealBySid(Integer id);

    void insertCheckGroups(Map<String, Object> params);

    void updateSetmeal(Setmeal setmeal);

}

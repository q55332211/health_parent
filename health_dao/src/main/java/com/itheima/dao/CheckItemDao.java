package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.vo.CheckItemVo;

import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 20:36 2019/7/9
 */
public interface CheckItemDao {

    //添加检查项
    void save(CheckItem checkItem);
    //分页查询
    Page<CheckItem> selectByCondition(String queryString);

    void update(CheckItem checkItem);

    void deleteById(Integer id);

    List<CheckItem> findAll();

    List<CheckItem> queryByGid(Integer gid);

    //根据id数组查询
    List<CheckItem> queryByGids(List<Integer>  checkGroupIds);

    List<CheckItemVo> getCheckItemVoInCheckGroupIds(List<Integer> groupIds);
}

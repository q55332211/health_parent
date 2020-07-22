package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 20:32 2019/7/9
 */
public interface CheckItemService {

    void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPage);

    void update(CheckItem checkItem);

    void delete(CheckItem checkItem);

    List<CheckItem> findAll();

   List<CheckItem> quertByGid(Integer gid);

    List<CheckItem> queryByCheckGroupIds(List<Integer>  checkGroupIds);
}

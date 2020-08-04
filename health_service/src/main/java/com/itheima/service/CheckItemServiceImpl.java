package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.pojo.CheckItem;
import com.itheima.vo.CheckItemVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 20:40 2019/7/9
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.save(checkItem);
        //System.out.println(checkItem.toString());
    }

    /**
     * 使用分页助手分页查询
     *
     * @param queryPage
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPage) {
        /* throw new HealthException(MessageConstant.QUERY_CHECKITEM_FAIL);*/
        PageHelper.startPage(queryPage.getCurrentPage(), queryPage.getPageSize());
        Page<CheckItem> page = checkItemDao.selectByCondition(queryPage.getQueryString());
        if (page.getResult() == null) {
            throw new HealthException(MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    @Override
    public void delete(CheckItem checkItem) {
        checkItemDao.deleteById(checkItem.getId());
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


    public List<CheckItem> quertByGid(Integer gid) {
        if (gid == null) {
            throw new HealthException("id不存在");
        }
        List<CheckItem> itemList = this.checkItemDao.queryByGid(gid);
        if (null == itemList || itemList.size() <= 0) {
            throw new HealthException(MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return itemList;
    }

    @Override
    public List<CheckItem> queryByCheckGroupIds(List<Integer> checkGroupIds) {
        return this.checkItemDao.queryByGids(checkGroupIds);
    }

    @Override
    public List<CheckItemVo> getCheckItemVoInCheckGroupIds(List<Integer> groupIds) {
        return this.checkItemDao.getCheckItemVoInCheckGroupIds(groupIds);
    }
}

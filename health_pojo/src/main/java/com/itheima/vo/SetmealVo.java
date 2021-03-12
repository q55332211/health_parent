package com.itheima.vo;

import com.itheima.pojo.Setmeal;

import java.util.List;

/**
 * @Description:
 * @Date: Create in 9:02 2020/8/2
 */
public class SetmealVo extends Setmeal {
    private List<Integer> checkGroupIds;

    public List<Integer> getCheckGroupIds() {
        return checkGroupIds;
    }

    public void setCheckGroupIds(List<Integer> checkGroupIds) {
        this.checkGroupIds = checkGroupIds;
    }
}

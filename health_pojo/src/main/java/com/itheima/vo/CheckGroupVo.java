package com.itheima.vo;

import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * @Description:
 * @Date: Create in 9:09 2020/8/2
 */
public class CheckGroupVo extends CheckGroup {

    private List<Integer> checkItesIds;

    public List<Integer> getCheckItesIds() {
        return checkItesIds;
    }

    public void setCheckItesIds(List<Integer> checkItesIds) {
        this.checkItesIds = checkItesIds;
    }
}

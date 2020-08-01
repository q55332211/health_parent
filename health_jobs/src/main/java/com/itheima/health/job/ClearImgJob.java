package com.itheima.health.job;

import com.github.pagehelper.StringUtil;
import com.itheima.dao.SetmealDao;
import com.itheima.untis.CosUtils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Description: 定时任务
 * @Date: Create in 19:57 2020/7/30
 */
@Component
public class ClearImgJob {


    @Autowired
    private SetmealDao setmealDao;

    /**
     * 定时任务，用于删除图片
     */
    public void clearImg() {

        List<String> imgs = this.setmealDao.getImgs();

        if (null == imgs || imgs.size() <= 0) {
            return;
        }
        for (int i = 0; i < imgs.size() - 1; i++) {
            String img = imgs.get(i);
            if (StringUtil.isNotEmpty(img) && img.contains("uploads")) {
                imgs.set(i, img.substring(img.indexOf("uploads")));
            }
        }
        List<String> cosList = CosUtils.listAllObjects(null, null, null, null);
        cosList.removeAll(imgs);
        CosUtils.batchDelFile(null, null, null, null, cosList);
    }

}

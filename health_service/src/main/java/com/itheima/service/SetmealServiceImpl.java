package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.vo.CheckGroupVo;
import com.itheima.vo.CheckItemVo;
import com.itheima.vo.SetmealVo;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.*;


/**
 * @Author: Nice
 * @Description:
 * @Date: Create in 15:08 2020/7/10
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Reference
    private CheckGroupService checkGroupService;

    @Reference
    private CheckItemService checkItemService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String out_put_path;


    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.selectByCondition(queryPageBean.getQueryString());
        generateMobileStaticHtml();
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void addSetmeal(Setmeal setmeal, Integer[] checkgroupIds) {
        //插入套餐数据
        this.setmealDao.addSetmeal(setmeal);
        //获得套餐id
        Integer id = setmeal.getId();
        //根据套餐id 插入多条 检查组记录
        setSetmealAndCheckGroup(id, checkgroupIds);

        generateMobileStaticHtml();

    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        generateMobileStaticHtml();
        return this.setmealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = this.getSetmealVoById(id);
        return setmeal;
    }

    /**
     * 优化性能值对id查询套餐所有信息
     *
     * @param sid
     * @return
     */
    @Override
    public Map<String, Object> queryById(Integer sid) {

        //根据使用
        //    Setmeal setmeal = this.setmealDao.findByIdPuls(sid);
        Setmeal setmeal = getSetmealVoById(sid);
        //通过套餐id查询检查组
        Map<String, Object> data = new HashMap();
        List<Integer> checkGroupIds = this.checkGroupService.getCheckGroupIds(sid);
        List<CheckItem> checkItems = this.checkItemService.queryByCheckGroupIds(checkGroupIds);
        //   Setmeal setmeal = this.setmealDao.findById(sid);
        data.put("setmeal", setmeal);
        data.put("checkItems", checkItems);
        data.put("total", checkItems.size());
        return data;
    }

    @Override
    public Setmeal findBaseSemealById(Integer id) {
        return this.setmealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return this.setmealDao.findSetmealCount();
    }



    /**
     * 查询热门套餐
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getHotSetmeal() {
        List<Map<String, Object>> list = new ArrayList<>();
        //查询热门前五条
        List<Map<String, Object>> hotSetmeal = this.setmealDao.getHotSetmeal();
        //     List<Map<String,Object>> setmeals = this.setmealDao.getHotSetmeal();
        //查询订单总数
        Integer total = orderService.getTotalCount();
        // Map<String, Object> map = new HashMap();
        if (null == total || total <= 0) {
            return null;
        }
        //查询每个订单预约数
        for (Map<String, Object> setmeal : hotSetmeal) {
            Map<String, Object> map = new HashMap();
            float count = Integer.parseInt(setmeal.get("count").toString());
            float proportion = count / total;
            map.put("proportion", proportion);
            map.put("name", setmeal.get("name").toString());
            map.put("setmeal_count", count);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(Integer id) {
        return this.setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    @Override
    public void update(Integer[] checkgroupIds, Setmeal setmeal) {
        //根据sid删除原来的关联
        this.setmealDao.deleteCheckGroupAndSetmealBySid(setmeal.getId());
        //批量插入新的关联值
        Map<String, Object> params = new HashMap<>();
        List<Integer> ids = Arrays.asList(checkgroupIds);
        params.put("sid", setmeal.getId());
        params.put("ids", ids);
        this.setmealDao.insertCheckGroups(params);
        //更新套餐数据
        this.setmealDao.updateSetmeal(setmeal);
    }


    /**
     * 使用for 查询 效率最低
     *
     * @param id
     * @return
     */
    public Setmeal toFor(Integer id) {
        //通过套餐id查询检查组
        List<CheckGroup> checkGroups = checkGroupService.queryCheckGroupsBySid(id);
        for (CheckGroup checkGroup : checkGroups) {
            //根据checkGroupId查询检查项
            List<CheckItem> checkItems = this.checkItemService.quertByGid(checkGroup.getId());
            checkGroup.setCheckItems(checkItems);
        }
        Setmeal setmeal = this.setmealDao.findById(id);
        setmeal.setCheckGroups(checkGroups);
        // System.out.println(JSONObject.toJSONString(setmeal));
        return setmeal;
    }

    /***
     * 增强+++
     * @param id
     * @return
     */
    private Setmeal getSetmealVoById(Integer id) {
        Setmeal setmeal = this.setmealDao.findById(id);
        List<Integer> groupIds = this.setmealDao.findCheckgroupIdsBySetmealId(id);
        List<CheckGroup> checkGroups = this.checkGroupService.queryCheckGroupsBySid(id);
        List<CheckItemVo> checkItemVos = this.checkItemService.getCheckItemVoInCheckGroupIds(groupIds);
        for (CheckGroup checkGroup : checkGroups) {
            ArrayList<CheckItem> items = new ArrayList<>();
            for (CheckItemVo itemVo : checkItemVos) {
                if (itemVo.getCheckGroupId() == checkGroup.getId()) {
                    items.add(itemVo);
                }
                checkGroup.setCheckItems(items);
            }
        }
        setmeal.setCheckGroups(checkGroups);
        return setmeal;
    }

    //插入id对应的chechgroups 这里为什么要用map 不用怎么写
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {

        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmealid", id);
            map.put("checkgroupId", checkgroupId);
            this.setmealDao.setSetmealAndCheckGroup(map);
        }
    }


    /**
     * 生成 列表及详情静态页面
     */
    private void generateMobileStaticHtml() {
        try {
            // 套餐列表静态页面
            generateSetmealListHtml();
            // 套餐详情静态页面 生成单就行了，为了测试方便，生成所有的
            generateSetmealDetailHtml();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成套餐详情
     */
    private void generateSetmealDetailHtml() throws Exception {
        List<Setmeal> setmealList = setmealDao.findAll();
        for (Setmeal setmeal : setmealList) {
            Setmeal setmealDetail = this.getSetmealVoById(setmeal.getId());
            generateDetailHtml(setmealDetail);
        }
    }


    private void generateDetailHtml(Setmeal setmealDetail) throws Exception {
        // 获取模板 套餐列表的模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal_detail.ftl");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmeal", setmealDetail);
        File setmealDetailFile = new File(out_put_path, "setmeal_" + setmealDetail.getId() + ".html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealDetailFile), "utf-8")));
    }

    /**
     * 生成套餐列表
     */
    private void generateSetmealListHtml() throws Exception {
        // 获取模板 套餐列表的模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal.ftl");
        // 获取数据模型
        List<Setmeal> setmealList = setmealDao.findAll();
        Map<String, Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmealList", setmealList);
        // 给模板填充数据 new OutputStreamWriter要指定编码格式，否则中文乱码
        // 生成的文件 c:/sz89/health_parent/health_mobile/src/main/webapp/pages/m_setmeal.html
        File setmealListFile = new File(out_put_path, "m_setmeal.html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealListFile),"utf-8")));

    }


}

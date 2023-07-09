package com.vinoxm.infoc.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vinoxm.infoc.dao.RssDao;
import com.vinoxm.infoc.model.RssSubscribe;
import com.vinoxm.infoc.result.BaseResult;
import com.vinoxm.infoc.result.DataResult;
import com.vinoxm.infoc.vo.PagerVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@Service
public class RssService extends BaseService<RssDao> {

    public BaseResult addOneRssSubs(RssSubscribe rssSubscribe) {
        baseDao.insertOne(rssSubscribe);
        return BaseResult.Success();
    }

    public BaseResult addManyRssSubs(RssSubscribe[] rssArray) {
        List<RssSubscribe> list = Arrays.asList(rssArray);
        baseDao.insertMany(list);
        return BaseResult.Success();
    }

    public BaseResult delOneRssSubs(RssSubscribe rssSubscribe) {
        baseDao.deleteOne(rssSubscribe);
        return BaseResult.Success();
    }

    public BaseResult delManyRssSubs(RssSubscribe[] rssArray) {
        List<RssSubscribe> list = Arrays.asList(rssArray);
        baseDao.deleteMany(list);
        return BaseResult.Success();
    }

    public BaseResult selOneRssSubs(long id) {
        return DataResult.Success(baseDao.selectOneById(id));
    }

    public BaseResult selAllRssSubsBySearch(PagerVo<RssSubscribe> pager, HashMap<String, Object> params) {
        Page<Object> page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        List<RssSubscribe> list = baseDao.selectAll(params);
        pager.setData(list);
        pager.setTotal(page.getTotal());
        return DataResult.Success(pager);
    }

    public BaseResult editOneRssSubsById(RssSubscribe rssSubscribe) {
        RssSubscribe old = baseDao.selectOneById(rssSubscribe.getId());
        if (!Objects.equals(old.getUrl(), rssSubscribe.getUrl()) || !Objects.equals(old.getRegex(), rssSubscribe.getRegex())) {
            baseDao.deleteManyRssResultByPid(old.getId());
        }
        baseDao.updateOneById(rssSubscribe);
        return BaseResult.Success();
    }
}

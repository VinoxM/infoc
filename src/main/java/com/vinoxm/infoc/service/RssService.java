package com.vinoxm.infoc.service;

import com.vinoxm.infoc.dao.RssDao;
import com.vinoxm.infoc.model.RssSubscribe;
import com.vinoxm.infoc.result.BaseResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


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
}

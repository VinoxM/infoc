package com.vinoxm.infoc.service;

import com.vinoxm.infoc.dao.BaseDao;
import com.vinoxm.infoc.model.User;
import com.vinoxm.infoc.result.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService <Base extends BaseDao>{
    @Autowired
    protected Base baseDao;
}

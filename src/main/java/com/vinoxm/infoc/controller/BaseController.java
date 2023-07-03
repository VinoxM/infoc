package com.vinoxm.infoc.controller;

import com.vinoxm.infoc.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController <Base extends BaseService> {
    @Autowired
    protected Base baseService;

}

package com.vinoxm.infoc.controller;

import com.vinoxm.infoc.model.RssSubscribe;
import com.vinoxm.infoc.result.BaseResult;
import com.vinoxm.infoc.service.RssService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rss")
@Log4j2
public class RssController extends BaseController<RssService>{

    @PostMapping("addOne")
    public BaseResult addOne(@RequestBody RssSubscribe json) {
        return baseService.addOneRssSubs(json);
    }

    @PostMapping("addMany")
    public BaseResult addMany(@RequestBody RssSubscribe[] array) {
        return baseService.addManyRssSubs(array);
    }
}

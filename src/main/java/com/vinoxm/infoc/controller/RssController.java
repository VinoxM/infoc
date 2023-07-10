package com.vinoxm.infoc.controller;

import com.vinoxm.infoc.annotions.NeedSecret;
import com.vinoxm.infoc.model.RssSubscribe;
import com.vinoxm.infoc.result.BaseResult;
import com.vinoxm.infoc.service.RssService;
import com.vinoxm.infoc.vo.PagerVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("rss")
@Log4j2
public class RssController extends BaseController<RssService>{

    @PostMapping("addOne")
    @NeedSecret
    public BaseResult addOne(@RequestBody RssSubscribe json) {
        return baseService.addOneRssSubs(json);
    }

    @PostMapping("addMany")
    @NeedSecret
    public BaseResult addMany(@RequestBody RssSubscribe[] array) {
        return baseService.addManyRssSubs(array);
    }

    @PostMapping("delOne")
    @NeedSecret
    public BaseResult delOne(@RequestBody RssSubscribe json) {
        return baseService.delOneRssSubs(json);
    }

    @PostMapping("delMany")
    @NeedSecret
    public BaseResult delMany(@RequestBody RssSubscribe[] array) {
        return baseService.delManyRssSubs(array);
    }

    @GetMapping("getOne")
    @NeedSecret
    public BaseResult selOne(long id) {
        return baseService.selOneRssSubs(id);
    }

    @PostMapping("getSearch.page")
    @NeedSecret
    public BaseResult selSearchWithPage(@RequestBody PagerVo<RssSubscribe> pager,@RequestParam HashMap<String, Object> params) {
        return baseService.selSearchRssSubsWithPage(pager, params);
    }

    @GetMapping("getSearch")
    @NeedSecret
    public BaseResult selSearch(@RequestParam HashMap<String, Object> params) {
        return baseService.selSearchRssSubs(params);
    }

    @PostMapping("editOne")
    @NeedSecret
    public BaseResult editOne(@RequestBody RssSubscribe rssSubscribe) {
        return baseService.editOneRssSubsById(rssSubscribe);
    }

    @GetMapping("getSeason")
    @NeedSecret
    public BaseResult selSeason() {
        return baseService.selSeason();
    }
}

package com.vinoxm.infoc.schedules;

import com.vinoxm.infoc.model.RssSubscribe;
import com.vinoxm.infoc.service.RssService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class RssSubscribeTask {

    private RssService rssService;

    @Autowired(required = false)
    private void setRssDao(RssService rssService) {
        this.rssService = rssService;
    }

    @Scheduled(cron = "${schedule.corn}")
//    @Scheduled(fixedDelay = 20000)
    public void updateRssSubscribe() {
        log.info("[Schedule start] Update Rss Subscribe");
        List<RssSubscribe> list = rssService.getRssSubscribe();
        rssService.subscribeRss(list);
    }
}

package com.vinoxm.infoc.vo;

import com.vinoxm.infoc.model.RssResult;
import com.vinoxm.infoc.utils.StringUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class RssVo extends RssBase {
    @Getter
    @Setter
    private Rss rss;

    public List<RssResult> getRssItems(String regexStr, long pid) {
        List<RssResult> list = new ArrayList<>();
        Channel channel = rss.getChannel();
        if (channel != null) {
            List<Item> items = channel.getItem();
            if (items != null) {
                if (StringUtils.isEmpty(regexStr)) {
                    for (Item item : items) {
                        list.add(new RssResult(pid, item.getTitle(), item.getEnclosure().getUrl(), item.getPubDate()));
                    }
                } else {
                    String[] regex = regexStr.split(",");
                    for (Item item : items) {
                        int flag = 0;
                        for (String reg : regex) {
                            if (Pattern.matches(reg.toUpperCase(), item.getTitle().toUpperCase())) {
                                flag++;
                            }
                        }
                        if (flag == regex.length) {
                            list.add(new RssResult(pid, item.getTitle(), item.getEnclosure().getUrl(), item.getPubDate()));
                        }
                    }
                }
            }
        }
        return list;
    }

    @Data
    protected static class Rss {
        private Channel channel;
    }

    @Data
    protected static class Channel {
        private List<Item> item;
        private String link;
        private String description;
        private String language;
        private String title;
    }

    @Data
    protected static class Item {
        @Getter
        @Setter
        private RssSingleVo.Enclosure enclosure;
        @Getter
        @Setter
        private String author;
        @Getter
        @Setter
        private String link;
        @Getter
        @Setter
        private String description;
        @Getter
        @Setter
        private String title;

        @Setter
        private String pubDate;


        public Date getPubDate() {
            Date result;
            try {
                result = format.parse(pubDate);
            } catch (ParseException e) {
                result = null;
            }
            return result;
        }
    }

    @Data
    protected static class Enclosure {
        private Integer length;
        private String type;
        private String url;
    }
}

package com.vinoxm.infoc.vo;

import com.vinoxm.infoc.model.RssResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Data
public class RssVo {
    private Rss rss;

    public List<RssResult> getRssItems(String regexStr, long pid) {
        List<RssResult> list = new ArrayList<>();
        Channel channel = rss.getChannel();
        if (channel != null) {
            List<Item> items = channel.getItem();
            if (items != null) {
                String[] regex = regexStr.split(",");
                for (Item item : items) {
                    int flag = 0;
                    for (String reg : regex) {
                        if (Pattern.matches(reg.toUpperCase(), item.getTitle().toUpperCase())) {
                            flag++;
                        }
                    }
                    if (flag == regex.length) {
                        list.add(new RssResult(pid, item.getTitle(), item.getEnclosure().getUrl()));
                    }
                }
            }
        }
        return list;
    }

    @Data
    static class Rss {
        private Channel channel;
    }

    @Data
    static class Channel {
        private List<Item> item;
        private String link;
        private String description;
        private String language;
        private String title;
        private String pubDate;
    }

    @Data
    static class Item {
        private Enclosure enclosure;
        private String author;
        private String link;
        private String description;
        private String title;
        private String pubDate;
    }

    @Data
    static class Enclosure {
        private Integer length;
        private String type;
        private String url;
    }
}

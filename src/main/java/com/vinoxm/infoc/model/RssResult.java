package com.vinoxm.infoc.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
public class RssResult {

    private long id;
    private long pid;
    private String title;
    private String torrent;
    private Date pubDate;

    public RssResult(long pid, String title, String torrent, Date pubDate) {
        this.pid = pid;
        this.title = title;
        this.torrent = torrent;
        this.pubDate = pubDate;
    }

    public RssResult() {
    }

    @Override
    public String toString() {
        return "RssResult{" +
                "id=" + id +
                ", pid=" + pid +
                ", title='" + title + '\'' +
                ", torrent='" + torrent + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RssResult rssResult = (RssResult) o;
        return id == rssResult.id && pid == rssResult.pid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pid);
    }
}

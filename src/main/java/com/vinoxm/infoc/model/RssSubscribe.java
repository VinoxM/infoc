package com.vinoxm.infoc.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class RssSubscribe {

    private long id;
    private String name;
    private String url;
    private String regex;
    private String season;
    private Date startTime;
    private String cover;
    private List<RssResult> result;

    @Override
    public String toString() {
        return "Rss{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", regex=" + regex +
                ", season='" + season + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RssSubscribe that = (RssSubscribe) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(season, that.season);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, season);
    }
}

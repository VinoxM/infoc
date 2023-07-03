package com.vinoxm.infoc.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "rss_subscribe")
@Data
public class RssSubscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String regex;

    @Column(nullable = false)
    private String season;

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
}

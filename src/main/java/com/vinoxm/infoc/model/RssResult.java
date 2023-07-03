package com.vinoxm.infoc.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "rss_result")
@Data
public class RssResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long pid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String torrent;

    @Override
    public String toString() {
        return "RssResult{" +
                "id=" + id +
                ", pid=" + pid +
                ", title='" + title + '\'' +
                ", torrent='" + torrent + '\'' +
                '}';
    }
}

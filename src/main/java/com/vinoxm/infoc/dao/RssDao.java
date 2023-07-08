package com.vinoxm.infoc.dao;

import com.vinoxm.infoc.model.RssResult;
import com.vinoxm.infoc.model.RssSubscribe;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RssDao extends BaseDao{

    @Insert("INSERT INTO rss_subscribe(name, url, regex, season) VALUE(#{name}, #{url}, #{regex}, #{season})")
    void insertOne(RssSubscribe rssSubscribe);

    @Insert({"<script>" +
            "INSERT INTO rss_subscribe" +
            "(name, url, regex, season) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.name}, #{item.url}, #{item.regex}, #{item.season})" +
            "</foreach>" +
            "</script>"})
    void insertMany(@Param(value = "list") List<RssSubscribe> rssSubscribeList);

    @Insert("INSERT INTO rss_result(pid, title, torrent) VALUE(#{pid}, #{title}, #{torrent})")
    void insertOneResult(RssResult rss);

    @Insert({"<script>" +
            "INSERT IGNORE INTO rss_result" +
            "(pid, title, torrent) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.pid}, #{item.title}, #{item.torrent})" +
            "</foreach>" +
            "</script>"})
    void insertManyResult(@Param(value = "list") List<RssResult> rssResultsList);

    @Select("SELECT id,name,regex,url,season FROM rss_subscribe WHERE season=#{season}")
    List<RssSubscribe> getRssSubscribeBySeason(String season);
}

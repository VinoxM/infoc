package com.vinoxm.infoc.dao;

import com.vinoxm.infoc.model.RssResult;
import com.vinoxm.infoc.model.RssSubscribe;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface RssDao extends BaseDao{

    @Insert("INSERT INTO rss_subscribe(name, url, regex, season, start_time, cover) VALUE(#{name}, #{url}, #{regex}, #{season}, #{startTime}, #{cover}) ON DUPLICATE KEY UPDATE url=#{url},regex=#{regex}")
    void insertOne(RssSubscribe rssSubscribe);

    @Insert({"<script>" +
            "INSERT INTO rss_subscribe" +
            "(name, url, regex, season, start_time, cover) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.name}, #{item.url}, #{item.regex}, #{item.season}, #{item.startTime}, #{item.cover})" +
            "</foreach>" +
            " ON DUPLICATE KEY UPDATE url=VALUES(url),regex=VALUES(regex)" +
            "</script>"})
    void insertMany(@Param(value = "list") List<RssSubscribe> rssSubscribeList);

    @Insert("INSERT INTO rss_result(pid, title, torrent, pub_date) VALUE(#{pid}, #{title}, #{torrent}, #{pubDate})")
    void insertOneResult(RssResult rss);

    @Insert({"<script>" +
            "INSERT IGNORE INTO rss_result" +
            "(pid, title, torrent, pub_date) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.pid}, #{item.title}, #{item.torrent}, #{item.pubDate})" +
            "</foreach>" +
            "</script>"})
    int insertManyResult(@Param(value = "list") List<RssResult> rssResultsList);

    @Select("SELECT id,name,regex,url,season,start_time startTime,cover FROM rss_subscribe WHERE season=#{season}")
    @Results(value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "id", property = "result", javaType = ArrayList.class, many = @Many(select = "com.vinoxm.infoc.dao.RssDao.selectResultByPid"))
    })
    List<RssSubscribe> selectRssSubscribeBySeason(String season);

    @Delete("DELETE rs,rr FROM rss_subscribe rs LEFT JOIN rss_result rr ON rs.id=rr.pid WHERE rs.id=#{id}")
    void deleteOne(RssSubscribe rssSubscribe);

    @Delete("<script>" +
            "DELETE rs,rr FROM rss_subscribe rs LEFT JOIN rss_result rr ON rs.id=rr.pid WHERE rs.id IN (" +
            "<foreach collection='list' item='item' separator=','>" +
            "#{item.id}" +
            "</foreach>" +
            ")" +
            "</script>")
    void deleteMany(@Param(value = "list") List<RssSubscribe> rssSubscribe);

    @Select("<script>" +
            "SELECT id,name,url,regex,season,start_time startTime,cover FROM rss_subscribe" +
            "<where>" +
            "<if test='season!=null'>" +
            "season=#{season} " +
            "</if>" +
            "<if test='name!=null'>" +
            "AND name LIKE CONCAT('%',#{name},'%') " +
            "</if>" +
            "</where>" +
            "</script>")
    @Results(value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "id", property = "result", javaType = ArrayList.class, many = @Many(select = "com.vinoxm.infoc.dao.RssDao.selectResultByPid"))
    })
    List<RssSubscribe> selectAll(HashMap<String, Object> params);

    @Select("SELECT id,name,url,regex,season,start_time startTime,cover FROM rss_subscribe WHERE id=#{id}")
    @Results(value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "id", property = "result", javaType = ArrayList.class, many = @Many(select = "com.vinoxm.infoc.dao.RssDao.selectResultByPid"))
    })
    RssSubscribe selectOneById(long id);

    @Select("SELECT id,pid,title,torrent,pub_date pubDate FROM rss_result WHERE pid=#{pid} ORDER BY pub_date DESC")
    List<RssResult> selectResultByPid(long pid);

    @Update("UPDATE rss_subscribe SET name=#{name},url=#{url},regex=#{regex},season=#{season},cover=#{cover},start_time=#{startTime} WHERE id=#{id}")
    void updateOneById(RssSubscribe rssSubscribe);

    @Delete("DELETE FROM rss_result WHERE pid=#{pid}")
    void deleteManyRssResultByPid(long pid);

    @Select("SELECT season FROM rss_subscribe GROUP BY season")
    List<String> selectRssSubscribeSeason();
}

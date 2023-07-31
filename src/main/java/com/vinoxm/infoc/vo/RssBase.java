package com.vinoxm.infoc.vo;

import com.vinoxm.infoc.model.RssResult;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public abstract class RssBase {

    static SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    public abstract List<RssResult> getRssItems(String regexStr, long pid);
}

package com.trimph.everything.log.okHttpLog;

import com.trimph.everything.log.LogUtil;

/**
 * author: baiiu
 * date: on 16/8/31 19:40
 * description:
 */
public class HttpLogInterceptor implements HttpLoggingInterceptorM.Logger {

    public static String INTERCEPTOR_TAG_STR = "OkHttp";

    public HttpLogInterceptor() {
    }

    public HttpLogInterceptor(String tag) {
        INTERCEPTOR_TAG_STR = tag;
    }

    @Override public void log(String message, @LogUtil.LogType int type) {
        LogUtil.printLog(false, type, INTERCEPTOR_TAG_STR, message);
    }
}

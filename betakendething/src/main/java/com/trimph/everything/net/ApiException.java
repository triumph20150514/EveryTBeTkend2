package com.trimph.everything.net;

/**
 * author: Trimph
 * data: 2016/11/30.
 * description:
 */

public class ApiException extends Exception {
    public ApiException(String detailMessage) {
        super(detailMessage);
    }
}

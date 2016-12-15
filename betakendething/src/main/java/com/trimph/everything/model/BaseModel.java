package com.trimph.everything.model;

/**
 * author: Trimph
 * data: 2016/12/15.
 * description:
 */

public class BaseModel {
    public int type;

    public BaseModel(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

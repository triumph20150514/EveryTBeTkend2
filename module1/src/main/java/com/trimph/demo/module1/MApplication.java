package com.trimph.demo.module1;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * author: Trimph
 * data: 2016/11/22.
 * description:
 */

public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/msjh.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
//        //....

//        FontUtils.setDefaultFont(this, "DEFAULT", "fonts/msjh.ttf");
//		FontUtils.setDefaultFont(this, "MONOSPACE", "fonts/msjh.ttf");
//		FontUtils.setDefaultFont(this, "SERIF", "fonts/msjh.ttf");
//		FontUtils.setDefaultFont(this, "SANS_SERIF", "fonts/msjh.ttf");
    }
}

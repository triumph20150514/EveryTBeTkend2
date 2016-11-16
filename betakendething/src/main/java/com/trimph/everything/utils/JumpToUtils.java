package com.trimph.everything.utils;

import android.content.Context;
import android.content.Intent;

import com.trimph.everything.ui.activity.MainActivity;

/**
 * author: Trimph
 * data: 2016/11/10.
 * description:
 */

public class JumpToUtils {
    public static void JumpToMain(Context context){
        Intent intent=new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}

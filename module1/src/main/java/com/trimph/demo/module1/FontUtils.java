package com.trimph.demo.module1;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

public class FontUtils {

    public static void setFont(Context context, TextView view) {
        AssetManager assetManager = context.getAssets();// 得到AssetManager
        Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/msjh.ttf");// 根据路径得到Typeface
//        Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/msjh.ttf");// 根据路径得到Typeface
        view.setTypeface(typeface);// 设置字体
    }

    public static void setFont(Context context, Button view) {
        AssetManager assetManager = context.getAssets();// 得到AssetManager
        Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/msjh.ttf");// 根据路径得到Typeface
        view.setTypeface(typeface);// 设置字体
    }


    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

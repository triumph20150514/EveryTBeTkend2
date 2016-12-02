package com.trimph.everything.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.trimph.everything.base.RootView;
import com.trimph.everything.ui.contact.CollectionContact;

/**
 * author: Trimph
 * data: 2016/11/24.
 * description:
 */

public class CollectionViewImpl extends RootView implements CollectionContact.View {


    public CollectionViewImpl(Context context) {
        super(context);
    }

    public CollectionViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void setPersenter(CollectionContact.Persenter persenter) {

    }
}

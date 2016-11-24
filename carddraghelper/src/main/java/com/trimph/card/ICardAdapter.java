package com.trimph.card;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Trimph
 * data: 2016/11/18.
 * description:
 */

public abstract class ICardAdapter {
    public List<View> viewList = new ArrayList<>();

    public abstract int getItemNum();

    public abstract View createView(ViewGroup viewGroup);

    public abstract void BindView(View view, int position);
}

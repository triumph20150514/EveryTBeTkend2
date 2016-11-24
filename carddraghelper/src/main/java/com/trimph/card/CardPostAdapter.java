package com.trimph.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Trimph
 * data: 2016/11/18.
 * description:
 */

public class CardPostAdapter extends ICardAdapter {

    public Context mContext;

    public List<Bean> list = new ArrayList<>();

    public List<Bean> getList() {
        return list;
    }

    public void setList(List<Bean> list) {
        this.list = list;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public CardPostAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemNum() {
        if (list != null) {
            return list.size() > 0 ? list.size() : 0;
        }
        return 0;
    }

    @Override
    public View createView(ViewGroup viewGroup) {
        return LayoutInflater.from(mContext).inflate(R.layout.card_item, viewGroup, false);
    }

    @Override
    public void BindView(View view, int position) {
        if (view != null) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder == null) {
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            if (position == 0) {
                holder.imageView.setBackgroundResource(R.mipmap.p4);
            } else {
                holder.imageView.setBackgroundResource(R.mipmap.p5);
            }
        }
    }

    public class ViewHolder {

        public SquareImageView imageView;

        public ViewHolder(View contentView) {
            imageView = (SquareImageView) contentView.findViewById(R.id.imageview);
        }
    }
}

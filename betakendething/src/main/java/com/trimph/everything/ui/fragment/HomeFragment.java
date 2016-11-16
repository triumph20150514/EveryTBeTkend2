package com.trimph.everything.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trimph.everything.R;
import com.trimph.everything.base.BaseFragment;
import com.trimph.everything.ui.presenter.HomePresenter;
import com.trimph.everything.ui.view.HomeViewImpl;

/**
 * author: Trimph
 * data: 2016/11/11.
 * description:
 */

public class HomeFragment extends BaseFragment {

    public HomeViewImpl homeView;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeView = (HomeViewImpl) view.findViewById(R.id.home_view);
        presenter = new HomePresenter(homeView);
    }

}

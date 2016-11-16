package com.trimph.everything.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.trimph.everything.R;
import com.trimph.everything.base.BaseActivity;
import com.trimph.everything.ui.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;


public class MainActivity extends BaseActivity {

    @BindView(R.id.slide_bar)
    RelativeLayout slideBar;
    @BindView(R.id.mian_frame)
    FrameLayout mianFrame;
    @BindView(R.id.bottom_navigation)
    BottomNavigation bottomNavigation;
    @BindView(R.id.main_layout)
    CoordinatorLayout mainLayout;
    @BindView(R.id.ResideLayout)
    com.trimph.everything.weight.ResideLayout resideLayout;
    public HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initData() {

        if (homeFragment != null) {
            replace(homeFragment);
        }
        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int i1) {
//                replaceContent(i1);
                replace(homeFragment);
            }

            @Override
            public void onMenuItemReselect(@IdRes int i, int i1) {

            }
        });

    }

    private void replaceContent(int i) {
        switch (i) {
            case 0:
                replace(homeFragment);
                break;
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    private void replace(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if (!fragment.isAdded()) {
        fragmentTransaction.replace(R.id.mian_frame, fragment, fragment.getTag());
//        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initView() {
        homeFragment = new HomeFragment();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

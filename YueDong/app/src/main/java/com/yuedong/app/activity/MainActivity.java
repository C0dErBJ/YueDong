package com.yuedong.app.activity;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yuedong.app.Base.BaseActivity;
import com.yuedong.app.Base.BaseApplication;
import com.yuedong.app.R;
import android.support.v4.app.Fragment;
import com.yuedong.app.adapter.FragmentListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    //mainContent的fragment
    public SportFragment sportFragment;
    public WeathFragment weathFragment;
    public NearByFragment nearByFragment;
    public MessageFragment messageFragment;
    public PersonalFragment personalFragment;

    public LocationClient mLocClient;


    public ViewPager mViewPager;
    public int mCurrentView = 2;
    public List<Fragment> mFragementList = new ArrayList<Fragment>();

    public android.support.v4.app.FragmentManager mFragmentManager;
    public android.support.v4.app.FragmentTransaction mFragmentTransaction;
    public FragmentListAdapter mFragmentAdapter;

    //region textview
    private TextView mSport;
    private TextView mSportIcon;
    private TextView mNearBy;
    private TextView mNearByIcon;
    private TextView mWeath;
    private TextView mWeathIcon;
    private TextView mMessage;
    private TextView mMessageIcon;
    private TextView mPersonal;
    private TextView mPersonalIcon;

    private RelativeLayout tb_weath;
    private RelativeLayout tb_nearby;
    private RelativeLayout tb_sport;
    private RelativeLayout tb_message;
    private RelativeLayout tb_personal;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();
    }

    private void init() {
        mLocClient = new LocationClient(this);

        mLocClient.start();
        mViewPager = $(R.id.mainContent);

        //region banding
        tb_weath = $(R.id.tb_weath);
        tb_nearby = $(R.id.tb_nearby);
        tb_sport = $(R.id.tb_Sport);
        tb_message = $(R.id.tb_Message);
        tb_personal = $(R.id.tb_Personal);

        mNearBy = $(R.id.tv_neayby);
        mNearByIcon = $(R.id.ic_neayby);
        mSport = $(R.id.tv_sport);
        mSportIcon = $(R.id.ic_sport);
        mWeath = $(R.id.tv_weath);
        mWeathIcon = $(R.id.ic_weath);
        mMessage = $(R.id.tv_message);
        mMessageIcon = $(R.id.ic_message);
        mPersonal = $(R.id.tv_person);
        mPersonalIcon = $(R.id.ico_person);
        //endregion

        sportFragment = new SportFragment(this, mLocClient);
        messageFragment = new MessageFragment(this);
        weathFragment = new WeathFragment(this, mLocClient,BaseApplication.getInstance());
        personalFragment = new PersonalFragment(this);
        nearByFragment = new NearByFragment(this, onKeyListener,BaseApplication.getInstance());

        mFragementList.add(weathFragment);
        mFragementList.add(nearByFragment);
        mFragementList.add(sportFragment);
        mFragementList.add(messageFragment);
        mFragementList.add(personalFragment);
        mFragmentManager = getSupportFragmentManager();

        mFragmentAdapter = new FragmentListAdapter(mFragmentManager, mFragementList);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(2);

//        mFragmentTransaction = mFragmentManager.beginTransaction();
//        mFragmentTransaction.add(R.id.mainContent, sportFragment);
//        mFragmentTransaction.commit();
    }

    private void setListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {


            }

            @Override
            public void onPageSelected(int i) {
                resetView();
                switch (i) {
                    case 0:
                        mWeath.setTextColor(getResources().getColor(R.color.darkPrimaryColor));
                        mWeathIcon.setBackground(getResources().getDrawable(R.drawable.ic_cloud_circle_black_36dp));
                        break;
                    case 1:
                        mNearBy.setTextColor(getResources().getColor(R.color.darkPrimaryColor));
                        mNearByIcon.setBackground(getResources().getDrawable(R.drawable.ic_room_black_36dp));
                        break;
                    case 2:
                        mSport.setTextColor(getResources().getColor(R.color.darkPrimaryColor));
                        mSportIcon.setBackground(getResources().getDrawable(R.drawable.ic_directions_walk_black_36dp));
                        break;
                    case 3:
                        mMessage.setTextColor(getResources().getColor(R.color.darkPrimaryColor));
                        mMessageIcon.setBackground(getResources().getDrawable(R.drawable.ic_textsms_black_36dp));
                        break;
                    case 4:
                        mPersonal.setTextColor(getResources().getColor(R.color.darkPrimaryColor));
                        mPersonalIcon.setBackground(getResources().getDrawable(R.drawable.ic_perm_contact_calendar_black_36dp));
                        break;
                }
                mCurrentView = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tb_weath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        tb_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });
        tb_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });
        tb_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(3);
            }
        });
        tb_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(4);
            }
        });

    }

    private void resetView() {
        mSport.setTextColor(getResources().getColor(R.color.dividColor));
        mSportIcon.setBackground(getResources().getDrawable(R.drawable.ic_directions_walk_black_36dp_blank));
        mWeath.setTextColor(getResources().getColor(R.color.dividColor));
        mWeathIcon.setBackground(getResources().getDrawable(R.drawable.ic_cloud_circle_black_36dp_blank));
        mMessage.setTextColor(getResources().getColor(R.color.dividColor));
        mMessageIcon.setBackground(getResources().getDrawable(R.drawable.ic_textsms_black_36dp_blank));
        mPersonal.setTextColor(getResources().getColor(R.color.dividColor));
        mPersonalIcon.setBackground(getResources().getDrawable(R.drawable.ic_perm_contact_calendar_black_36dp_blank));
        mNearBy.setTextColor(getResources().getColor(R.color.dividColor));
        mNearByIcon.setBackground(getResources().getDrawable(R.drawable.ic_room_black_36dp_blank));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
    }

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            nearByFragment.OnKeyDown(keyCode);
            return false;
        }
    };
}

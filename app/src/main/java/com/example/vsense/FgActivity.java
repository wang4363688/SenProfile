package com.example.vsense;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class FgActivity extends FragmentActivity implements OnClickListener{

    private ViewPager viewpager;
    private PagerAdapter adapter;
    private CarFragment carFragment = new CarFragment();;
    private MapFragment mapFragment = new MapFragment();

    private ImageView button1;
    private ImageView button3;

    ArrayList<Fragment> list = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viewpager);
        Log.d("onCreate", "onCreate");
        viewpager = (ViewPager) findViewById(R.id.viewPager);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                clearSel();
                viewpager.setCurrentItem(arg0);
                switch (arg0) {
                    case 0:
                        button1.setBackgroundResource(R.drawable.bt_car1);
                        break;
                    case 1:
                        button3.setBackgroundResource(R.drawable.map1);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        initFragment();

        initViewPager();

        button1 = (ImageView) findViewById(R.id.bt_car);
        button3 = (ImageView) findViewById(R.id.bt_map);
        button1.setOnClickListener(this);
        button3.setOnClickListener(this);
        button1.setBackgroundResource(R.drawable.bt_car1);
    }



    private void initViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);
    }

    private void initFragment() {
        list.add(carFragment);
        list.add(mapFragment);
    }


    private void clearSel(){
        button1.setBackgroundResource(R.drawable.bt_car2);
        button3.setBackgroundResource(R.drawable.map2);
    }

    @Override
    public void onClick(View v) {
        clearSel();
        switch (v.getId()) {
            case R.id.bt_car:
                this.viewpager.setCurrentItem(0);
                button1.setBackgroundResource(R.drawable.bt_car1);
                break;
            case R.id.bt_map:
                this.viewpager.setCurrentItem(1);
                button3.setBackgroundResource(R.drawable.map1);
                break;
        }

    }
}

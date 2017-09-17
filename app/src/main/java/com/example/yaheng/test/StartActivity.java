package com.example.yaheng.test;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import me.relex.circleindicator.CircleIndicator;

public class StartActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int[] ref_screenLayouts;
    private Button skipButton, nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);


        if(true) {                //set as true to test the isFirstRun sequence
            setContentView(R.layout.activity_main);

            viewPager =  findViewById(R.id.view_pager);
            skipButton = findViewById(R.id.button_skip);
            nextButton = findViewById(R.id.button_next);



            ref_screenLayouts = new int[]{
                    R.layout.intro_screen,
                    R.layout.signup_screen
            };



            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
            viewPager.setAdapter(viewPagerAdapter);


            CircleIndicator scroll = findViewById(R.id.scroll_dots);
            scroll.setViewPager(viewPager);
            viewPager.addOnPageChangeListener(viewScrollListener);


            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), SplashScreen.class);
                    startActivity(i);
                    finish();
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextItem = getItem();
                    if(nextItem < ref_screenLayouts.length){
                        viewPager.setCurrentItem(nextItem);
                    } else {
                        Intent i = new Intent(v.getContext(), SplashScreen.class);
                        startActivity(i);
                        finish();
                    }
                }
            });

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).apply();

        } else {
            finish();
        }
    }

    private int getItem(){
        return viewPager.getCurrentItem() + 1; //Returns the next item's integer`
    }

    ViewPager.OnPageChangeListener viewScrollListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(position == ref_screenLayouts.length - 1){
                nextButton.setText("SIGN UP");
                skipButton.setVisibility(View.GONE);
            } else {
                nextButton.setText("NEXT");
                skipButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(ref_screenLayouts[position], container, false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {

            return ref_screenLayouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();

    }
}

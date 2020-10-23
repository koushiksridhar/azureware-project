package com.theparkcorp.azureware3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private IntroManager introManager;
    private int[] layouts = new int[6];
    private TextView[] dots;
    private LinearLayout dotsLayout;
    Button next, skip;

    private ViewPagerAdapter vpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        introManager = new IntroManager(this);
        if (!introManager.Check()){
            introManager.setFirst(false);
            Intent intent = new Intent(this, Main2.class);
            startActivity(intent);
            finish();
        }

        if (Build.VERSION.SDK_INT >= 21){

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        }

        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        skip = (Button) findViewById(R.id.btn_skip);
        next = (Button) findViewById(R.id.btn_next);

        layouts = new int[]{R.layout.activity_intro, R.layout.activity_screen1, R.layout.activity_screen2,
                R.layout.activity_screen4, R.layout.activity_screen6, R.layout.activity_screen7};

        addBottomDots(0);
        changeStatusBarColor();
        vpa = new ViewPagerAdapter();
        viewPager.setAdapter(vpa);
        viewPager.addOnPageChangeListener(viewListener);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , Main2.class);
                startActivity(intent);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = getItem(+1);
                if (current <layouts.length){
                    viewPager.setCurrentItem(current);
                }else{
                    Intent i = new Intent(MainActivity.this, Main2.class);
                    startActivity(i);
                    finish();
                }
            }

        });
    }

    private void addBottomDots(int position){

        dots = new TextView[layouts.length];
        int[] coloractive = getResources().getIntArray(R.array.dot_active);
        int[] colorinactive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for (int i = 0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(coloractive[position]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0){

            dots[position].setTextColor(coloractive[position]);
        }

    }

    private int getItem (int i){

        return viewPager.getCurrentItem();
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
            if (position==layouts.length-1){
                skip.setText("PROCEED");
                next.setText("PROCEED");
            }else {
                next.setText("NEXT");
                skip.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position], container, false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }
    }
}

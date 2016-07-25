package com.example.user.myviewpagerdem;

import android.app.Activity;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * ViewPager中ImageView的容器
     */
    private List<ImageView> imageViewContainer = null;
    /**
     * 上一个被选中的小圆点的索引，默认值为0
     */
    private int preDotPosition = 0;

    private boolean isStop = false;

    private ViewPager viewPager;

    private LinearLayout llDocGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        startViewPager();
    }

    private void initView() {
        llDocGroup = (LinearLayout) findViewById(R.id.ll_dot_group);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        imageViewContainer = new ArrayList<>();
        int[] imageIDs = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d};

        LinearLayout.LayoutParams params = null;
        ImageView imageView = null;
        View doc = null;
        for (int i = 0; i < imageIDs.length; i++) {
            imageView = new ImageView(this);
            imageView.setImageResource(imageIDs[i]);
            imageViewContainer.add(imageView);
            //每次循环添加一个点到线性布局中
            doc = new View(this);
            doc.setBackgroundResource(R.drawable.doc_bg_selector);
            params = new LinearLayout.LayoutParams(10, 10);
            params.setMargins(15, 0, 0, 0);
            doc.setEnabled(false);
            doc.setLayoutParams(params);
            llDocGroup.addView(doc);

        }
        viewPager.setAdapter(new BannerAdapter());
        llDocGroup.getChildAt(0).setEnabled(true);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int newPosition = position % imageViewContainer.size();
                llDocGroup.getChildAt(preDotPosition).setEnabled(false);
                llDocGroup.getChildAt(newPosition).setEnabled(true);
                preDotPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void startViewPager() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    SystemClock.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int newIndex = viewPager.getCurrentItem() + 1;
                            viewPager.setCurrentItem(newIndex);
                        }
                    });
                }

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        isStop = true;
        super.onDestroy();
    }

    private class BannerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = imageViewContainer.get(position % imageViewContainer.size());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewContainer.get(position % imageViewContainer.size()));
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}

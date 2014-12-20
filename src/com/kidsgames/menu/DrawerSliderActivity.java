package com.kidsgames.menu;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;


import com.kidsgames.gamebase.DrawerActivity;
import com.kidsgames.utils.ResourceId;
import com.vikinc.coloring.R;

public class DrawerSliderActivity extends Activity implements OnClickListener {

    DetailOnPageChangeListener pageListener;
    FixedSpeedViewPager mPager;
    SlideAdapter mAdapter;
    private String packName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.slider_layout);

        findViewById(R.id.left_arrow).setOnClickListener(this);
        findViewById(R.id.left_arrow).setVisibility(View.GONE);
        findViewById(R.id.right_arrow).setVisibility(View.VISIBLE);
        findViewById(R.id.right_arrow).setOnClickListener(this);
        findViewById(R.id.home_button).setOnClickListener(this);
        packName = getIntent().getStringExtra("pack");

        mPager = (FixedSpeedViewPager) findViewById(R.id.pager);

        String json = ResourceId.getJson(this, packName);

        mAdapter = new SlideAdapter(this, packName);

        pageListener = new DetailOnPageChangeListener(this, mAdapter);
        mPager.setOnPageChangeListener(pageListener);

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("slider_images");
            int ids[] = new int[(int) (jsonObject.getInt("count"))];
            for (int i = 0; i < jsonObject.getInt("count"); i++) {
                ids[i] = ResourceId.getResId(array.getString(i), ResourceId.DRAWABLE, this);
            }
            mAdapter.setIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPager.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.refresh();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int startIndex = pageListener.currentPage * 2;

        switch (v.getId()) {

            case R.id.image_first:
                Intent intent = new Intent(this, DrawerActivity.class);
                intent.putExtra("pack", packName);
                intent.putExtra("level", startIndex + 1);
                startActivity(intent);
                break;
            case R.id.image_second:
                intent = new Intent(this, DrawerActivity.class);
                intent.putExtra("level", startIndex + 2);
                intent.putExtra("pack", packName);
                startActivity(intent);
                break;
            case R.id.home_button:
                onBackPressed();
                break;
            case R.id.left_arrow:
                int page = pageListener.getCurrentPage();
                if (page - 1 >= 0)
                    mPager.setCurrentItem(page - 1, true);
                break;
            case R.id.right_arrow:
                page = pageListener.getCurrentPage();
                if (mAdapter.getCount() > page + 1)
                    mPager.setCurrentItem(page + 1, true);
                break;

        }
    }


    public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;
        private Activity activity;
        private SlideAdapter mPager;

        public DetailOnPageChangeListener(Activity activity, SlideAdapter mPager) {
            this.activity = activity;
            this.mPager = mPager;
        }

        @Override
        public void onPageSelected(int position) {
            currentPage = position;

            if (position == 0) {
                activity.findViewById(R.id.left_arrow).setVisibility(View.GONE);
            } else {
                activity.findViewById(R.id.left_arrow).setVisibility(View.VISIBLE);
            }

            if (mPager.getCount() - 1 == currentPage) {
                activity.findViewById(R.id.right_arrow).setVisibility(View.GONE);
            } else {
                activity.findViewById(R.id.right_arrow).setVisibility(View.VISIBLE);
            }
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }

}

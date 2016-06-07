package com.example.chiakimayuzumi.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.chiakimayuzumi.myapplication.R;
import com.example.chiakimayuzumi.myapplication.adapter.CircleMenuAdapter;
import com.example.chiakimayuzumi.myapplication.bean.CircleMenuItem;
import com.example.chiakimayuzumi.myapplication.view.CircleMenuLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private CircleMenuLayout mCircleMenuLayout;
  private List<CircleMenuItem> mMenuItems = new ArrayList<CircleMenuItem>();
  private String[] mItemTexts = new String[]{"美国队长", "黑寡妇", "绿巨人",
          "雷神", "钢铁侠"};
  private int[] mItemImgs = new int[]{R.drawable.mgdz,
          R.drawable.hgf, R.drawable.ljr,
          R.drawable.ls, R.drawable.gtx,};

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initData(mItemTexts, mItemImgs);
    initView();

  }


  private void initData(String[] mItemTexts, int[] mItemImgs) {
    if (mItemImgs == null && mItemTexts == null) {
      throw new IllegalArgumentException("文本和图片不能为空");
    }
    int count = mItemImgs == null ? mItemTexts.length : mItemImgs.length;
    if (mItemImgs != null && mItemTexts != null) {
      count = Math.min(mItemImgs.length, mItemTexts.length);
    }

    for (int i = 0; i < count; i++) {
      mMenuItems.add(new CircleMenuItem(mItemTexts[i], mItemImgs[i]));
    }
  }

  private void initView() {
    //中心视图
    View centerView = LayoutInflater.from(this).inflate(R.layout.circle_menu_item_center, null, false);

    mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.circlemenu);
    mCircleMenuLayout.setAdapter(new CircleMenuAdapter(mMenuItems));
    mCircleMenuLayout.setCenterView(centerView);
    mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {

      @Override
      public void itemClick(View view, int pos) {
        Toast.makeText(MainActivity.this, mItemTexts[pos],
                Toast.LENGTH_SHORT).show();
      }

      @Override
      public void centerClick(View view) {
        Toast.makeText(MainActivity.this, "你点击了中心图标",
                Toast.LENGTH_SHORT).show();
      }
    });
  }
}

package com.example.chiakimayuzumi.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chiakimayuzumi.myapplication.bean.CircleMenuItem;
import com.example.chiakimayuzumi.myapplication.R;

import java.util.List;

/**
 * Created by chiakimayuzumi on 16/6/5.
 */
public class CircleMenuAdapter extends BaseAdapter {

  private List<CircleMenuItem> mCircleMenuItemsList;

  public CircleMenuAdapter(List<CircleMenuItem> mCircleMenuItemsList) {

    this.mCircleMenuItemsList = mCircleMenuItemsList;
  }

  @Override
  public int getCount() {
    return mCircleMenuItemsList.size();
  }

  @Override
  public Object getItem(int position) {
    return mCircleMenuItemsList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_menu_item, parent, false);
    //获得 layout 中的布局
    ImageView imageView = (ImageView) view.findViewById(R.id.id_circle_menu_item_image);
    TextView textView = (TextView) view.findViewById(R.id.id_circle_menu_item_text);

    //给布局设定参数
    CircleMenuItem mCircleMenuItem = mCircleMenuItemsList.get(position);
    imageView.setImageResource(mCircleMenuItem.imageId);
    textView.setText(mCircleMenuItem.title);
    return view;
  }
}

package com.example.chiakimayuzumi.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;

import com.example.chiakimayuzumi.myapplication.R;


/**
 * Created by wanghao on 2015/11/25.
 */
public class CircleMenuLayout extends ViewGroup {



  private final float radioChildDimen; //子视图尺寸，相对于直径的比例
  private final float radioCenterDimen;//中心视图尺寸，相对于直径的比例
  private final float radioPadding;//子视图尺寸，相对于直径的比例

  private ListAdapter mAdapter;

  private View centerView;//中央视图

  private int cl ;//中心按钮的左边或上边离边界的距离

  private int cr ;//中心按钮的右边或下边离边界的距离


  /**
   * 该容器的内边距,无视padding属性，如需边距请用该变量
   */
  private float mPadding;

  private OnMenuItemClickListener mOnMenuItemClickListener;
  private int mRadius; //圆形直径

  /**
   * 布局时的开始角度
   */
  private double mStartAngle = 0;

  public CircleMenuLayout(Context context,AttributeSet attrs) {
    super(context,attrs);
    // 自定义属性需要用到 后面的值是默认的属性
    TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CircleMenu);
    radioChildDimen = typedArray.getFloat(R.styleable.CircleMenu_radioChildDimen, 1/4f);
    radioCenterDimen = typedArray.getFloat(R.styleable.CircleMenu_radioCenterDimen, 1/3f);
    radioPadding = typedArray.getFloat(R.styleable.CircleMenu_radioPadding, 1/12f);
    // 回收TypedArray，以便后面重用
    typedArray.recycle();
    setPadding(0,0,0,0);
  }



  public void setAdapter(ListAdapter mAdapter){
    this.mAdapter = mAdapter;
  }

  public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener){

    this.mOnMenuItemClickListener = mOnMenuItemClickListener;

  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (mAdapter!=null) buildMenuItems();
    // 给 ViewGroup 添加子视图
    if (centerView!=null) addView(centerView);
    // 给 centerview 设置点击事件
    centerView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnMenuItemClickListener!=null) mOnMenuItemClickListener.centerClick(v);
      }
    });
  }

  /**
   * 构建菜单项
   */
  private void buildMenuItems() {

    //根据用户设置的参数初始化 menu
    for (int i=0;i<mAdapter.getCount();i++){
      final View itemView = mAdapter.getView(i,null,this);
      final int position = i;
      // 给 每个 itemview 设置点击事件
      itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mOnMenuItemClickListener!=null) mOnMenuItemClickListener.itemClick(v, position);
        }
      });
      // 给 ViewGroup 添加子视图
      addView(itemView);
    }
  }

  /**
   * 重写 onMeasure 测量布局的宽高，并测量 menu item 宽高
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //测量自身尺寸
    measureMyself(widthMeasureSpec, heightMeasureSpec);
    //测试子视图尺寸
    measureChild();
    //内边距根据直径的大小改变
    mPadding = radioPadding * mRadius;
  }

  // 测量自身尺寸
  private void measureMyself(int widthMeasureSpec, int heightMeasureSpec) {
    int resWidth, resHeight;

    /**
     * 根据传入的参数，分别获取测量模式和测量值
     */
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);

    int height = MeasureSpec.getSize(heightMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);

    /**
     * 如果宽或者高的测量模式非精确值
     */
    if (widthMode != MeasureSpec.EXACTLY
            || heightMode != MeasureSpec.EXACTLY)
    {
      // 主要设置为背景图的高度
      resWidth = getSuggestedMinimumWidth();
      // 如果未设置背景图片，则设置为屏幕宽高的默认值
      resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

      resHeight = getSuggestedMinimumHeight();
      // 如果未设置背景图片，则设置为屏幕宽高的默认值
      resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
    } else
    {
      // 如果都设置为精确值，则直接取小值；
      resWidth = resHeight = Math.min(width, height);
    }

    //然后将尺寸设置给 viewgroup
    setMeasuredDimension(resWidth, resHeight);
  }

  private void measureChild() {
    // 获得直径
    mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());

    // menu item数量
    final int count = getChildCount();
    // menu item尺寸
    int childSize = (int) (mRadius * radioChildDimen);
    // menu item测量模式
    int childMode = MeasureSpec.EXACTLY;

    // 迭代测量
    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);

      if (child.getVisibility() == GONE) {
        continue;
      }

      // 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
      int makeMeasureSpec = -1;

      if (child == centerView) {
        makeMeasureSpec = MeasureSpec.makeMeasureSpec(
                (int) (mRadius * radioCenterDimen),
                childMode);
      } else {
        makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
                childMode);
      }
      child.measure(makeMeasureSpec, makeMeasureSpec);
    }
  }

  /**
   * 获得默认该layout的尺寸
   *
   * @return
   */
  private int getDefaultWidth()
  {
    WindowManager wm = (WindowManager) getContext().getSystemService(
            Context.WINDOW_SERVICE);
    DisplayMetrics outMetrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(outMetrics);
    return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {

    int layoutRadius = mRadius;

    int left, top;
    // menu item 的尺寸
    int cWidth = (int) (layoutRadius * radioChildDimen);

    // 根据menu item的个数，计算角度
    float angleDelay = 360/mAdapter.getCount();

    // 遍历去设置menuitem的位置
    for (int i = 0; i < mAdapter.getCount(); i++)
    {
      final View child = getChildAt(i);

      if (child.getVisibility() == GONE)
      {
        continue;
      }

      //相当于 mStartAngle = mStartAngle % 360
      mStartAngle %= 360;

      // 计算，中心点到menu item中心的距离
      float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;

      // tmp cosa 即menu item中心点的横坐标
      left = layoutRadius/2 + (int) Math.round(tmp
              * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
              * cWidth);
      // tmp sina 即menu item的纵坐标
      top = layoutRadius/2 + (int) Math.round(tmp
              * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f
              * cWidth);

      child.layout(left, top, left + cWidth, top + cWidth);
      // 叠加尺寸
      mStartAngle += angleDelay;
    }

    if (centerView != null)
    {
      // 设置center item位置
      cl = layoutRadius / 2 - centerView.getMeasuredWidth() / 2;
      cr = cl + centerView.getMeasuredWidth();
      centerView.layout(cl, cl, cr, cr);
    }

  }

  public interface OnMenuItemClickListener
  {
    void itemClick(View view, int pos);
    void centerClick(View view);

  }

  public void setCenterView(View v){
    this.centerView = v;
  }
}
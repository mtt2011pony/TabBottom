package com.ynet.android.flame.tabmanager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 *<p>TabSignView：自定义的角标控件，用于Flame开发平台的TabManager组件显示角标的原生组件，在Flame开发平台中使用。<br>
 *TabSignView仅作为角标参考，样式为圆形背景+文字显示。<br>
 *各位小伙伴在项目实施过程中，请反馈其中的不足，我们共同改进。
 *
= * @version v1.0.0
 */

public class TabSignView extends TextView{

	private final int VIEW_WIDTH_DIP=35;
	
	public TabSignView(Context context) {
		this(context, null);
	}
	
	@SuppressLint("NewApi")
	public TabSignView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackground(getBackgroundDrawable());
		setWidth(VIEW_WIDTH_DIP);
		setHeight(VIEW_WIDTH_DIP);
		setTextSize(8);
		setTextColor(Color.WHITE);
		setGravity(Gravity.CENTER);
	}
	
	private GradientDrawable getBackgroundDrawable(){
		int strokeWidth = 2; 
	    int strokeColor = Color.parseColor("#FF0000");
	    int fillColor = Color.parseColor("#FF0000");
	    GradientDrawable gd = new GradientDrawable();
	    gd.setShape(GradientDrawable.OVAL);
	    gd.setColor(fillColor);
	    gd.setStroke(strokeWidth, strokeColor);	    
	    gd.setDither(true);
	    return gd;
	}

	public TabSignView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}
	
	/**
	 * 设置圆形背景的直径
	 * @param pixels：像素值 
	 */
	public void setDiameter(int pixels)
	{
		this.setWidth(pixels);
	}
	
	/**
	 * 设置圆形背景的半径
	 * @param pixels：像素值
	 */
	public void setRadius(int pixels)
	{
		setDiameter(pixels*2);
	}
	
	/**
	 * 设置背景颜色
	 * @param color：颜色值
	 */
	@SuppressLint("NewApi")
	public void setBackgroundColors(int color)
	{
		GradientDrawable gd=getBackgroundDrawable();
	    gd.setColor(color);	    
	    gd.setStroke(2, color);
	    setBackground(gd);
	}
}

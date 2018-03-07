package com.ynet.android.flame.tabmanager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *<p>TabManager:自定义的Tab管理控件，用于Flame开发平台的首页底部管理的原生组件，在Flame开发平台中使用。<br>
 *因开发平台因混合方式进行，故该组件仅针对WebView的加载进行管理，若后续需要添加Fragment管理，可再进行扩展。<br>
 *TabManager提供默认的布局方式，即上方图标、下方文字的景点样式，使用该样式，仅需要对assert中config.xml进行配置即可。<br>
 *TabManager作为开发平台组件，也提供自定义布局样式的方式，该方式调用类似SimpleAdapter，可根据实际需要，斟酌使用。<br>
 *TabManger作为混合框架下的组件，默认会在组件所在的界面搜索第一个被添加WebView，并作为载体，若后续界面包含多个WebView，可通过setWeb方法设置载体WebView。<br>
 *TabManager提供添加自定义角标，角标理论上可以是任何View。本包内提供了TabSignView，为较为经典的（圆形背景+文字）自定义角标组件，可供使用。<br>
 *为Flame开发平台快速实施的要求，TabMamager在使用经典样式时，不需要修改代码，仅需要通过配置，即可满足基本需求。<br>
 *各位小伙伴在项目实施过程中，请反馈其中的不足，我们共同改进。
 *
= * @version v1.0.0
 */

@SuppressLint("DefaultLocale")
public class TabMananger extends FrameLayout {
	// 私有常量
	private final int IMG_ID = 1001;
	private final int TV_ID = 1002;
	private final int SIGN_VIEW_ID = 1003;
	
	private final int SIGN_PADDING_DIP=5;
	
	private final int PADDING_TYPE_TOP=1;
	private final int PADDING_TYPE_BOTTOM=2;
	
	// 公共常量 
	/**
	 * 角标位置，左上，调用addSignView()方法时的参数常量
	 */
	public final static int SIGN_VIEW_ALIGN_LT = 0x9A;
	/**
	 * 角标位置，左下，调用addSignView()方法时的参数常量
	 */
	public final static int SIGN_VIEW_ALIGN_LB = 0x9C;
	/**
	 * 角标位置，右上，调用addSignView()方法时的参数常量
	 */
	public final static int SIGN_VIEW_ALIGN_RT = 0xBA;
	/**
	 * 角标位置，右下，调用addSignView()方法时的参数常量
	 */
	public final static int SIGN_VIEW_ALIGN_RB = 0xBC;
	/**
	 * 角标位置类型，添加到item中实际View的边界，调用addSignView()方法时的参数常量
	 */
	public final static int SIGN_VIEW_ALIGN_TYPE_INSIDE=1;
	/**
	 * 角标位置类型，添加到item中布局View的边界，调用addSignView()方法时的参数常量
	 */
	public final static int SIGN_VIEW_ALIGN_TYPE_OUTSIDE=2;
	

	// 成员
	private int count = 0;
	private List<TabItem> items = null;
	private OnTabClickListener mClickListener;
	private OnTabSelectedChangedListener mChangedListener;
	private WebView web;
	private boolean enable = true;
	private CustomItems customItems = null;
	private int paddingTop=0;
	private int paddingBottom=0;

	private Context context;
	private String textSize="12sp";
	private boolean isCustomView;
	private List<View> views;
	private LinearLayout container;
	private LinearLayout.LayoutParams lp;
	private RelativeLayout.LayoutParams lpRe;
	private int currentSelected = 0;

	// 初始化
	public TabMananger(Context context) {
		this(context, null);
	}

	public TabMananger(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;		
		readConfig(); 
		if (!isCustomView)
		{
			initLayout();
			initView();
			setListener();
		}
	}

	// dip转px
	private int dip2px(Context context, float dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpValue, context.getResources().getDisplayMetrics());
	}

	// 遍历当前界面寻找webView，找到的第一个WebView作为返回值
	private void findWebView(ViewGroup group) {
		for (int i = 0; i < group.getChildCount(); i++) {
			View v = group.getChildAt(i);
			if (v instanceof WebView) {
				web = (WebView) v;
				return;
			}
			if (v instanceof ViewGroup) {
				findWebView((ViewGroup) v);
			}
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		initWebView();
	}

	// 初始化WebView，加载第一个Tab的内容
	private void initWebView() {
		if (web != null)
			return;
		findWebView((ViewGroup) ((Activity) context)
				.findViewById(android.R.id.content));
		if (null!=items && items.size() > 0)
			loadUrl(items.get(0));
	}

	// 设置所有Tab的点击事件
	private void setListener() {
		for (int i = 0; i < count; i++) {
			views.get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!enable) return;
					int index=views.indexOf(v);
					int oldIndex=currentSelected;
					if (null != mClickListener)
						changeView(index,!mClickListener.onTabClick(v, index));
					else 
						changeView(index);
					if (index!=oldIndex && null!=mChangedListener)
						mChangedListener.onTabSelectedChanged(v, index);
					
				}
			});
		}
	}

	public TabMananger(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	// 布局初始化
	private void initLayout() {
		container = new LinearLayout(context);
		container.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		container.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LinearLayout.LayoutParams(0,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER;

		lpRe = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		lpRe.addRule(RelativeLayout.CENTER_IN_PARENT);

		views = new ArrayList<View>();
		views.clear();
	}

	private View getItemViewClassical(TabItem item) {
		LinearLayout.LayoutParams lpTv = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lpTv.gravity = Gravity.CENTER_HORIZONTAL;
		LinearLayout.LayoutParams lpImg = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lpImg.gravity = Gravity.CENTER_HORIZONTAL;
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(lp);
		layout.setOrientation(LinearLayout.VERTICAL);		
		layout.setPadding(0, paddingTop, 0, paddingBottom);

		ClassicalTabItem tab = (ClassicalTabItem) item;
		ImageView img = new ImageView(context);
		img.setId(IMG_ID);
		TextView tv = new TextView(context);
		tv.setText(tab.getName());
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setId(TV_ID);
		layout.addView(img, lpImg);
		layout.addView(tv, lpTv);
		layout.setOrientation(LinearLayout.VERTICAL);
		return layout;
	}

	private void setTextSize() {
		if (isNullString(textSize))
			textSize="12sp";
		String[] src=new String[]{"px","dip","sp","pt","dp"};
        int i=0;
        for (;i<src.length;i++)
        	if (textSize.endsWith(src[i]))
        		break;
        if (i>=src.length)
        	i=0;
        try{
        	float size=Float.valueOf(textSize.replace(src[i], ""));
        	if (i>3)
        		i=1;
        	setTextSize(i,size);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}

	@SuppressWarnings("rawtypes")
	private void bindView(int position, View view) {
		final Map dataSet = customItems.data.get(position);
		if (dataSet == null) {
			return;
		}
		final String[] from = customItems.from;
		final int[] to = customItems.to;
		final int len = to.length;

		for (int i = 0; i < len; i++) {
			final View v = view.findViewById(to[i]);
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}
				if (v instanceof TextView) {
					((TextView) v).setText(text);
				} else if (v instanceof ImageView) {
					if (data instanceof Integer) {
						((ImageView) v).setImageResource((Integer) data);
					}
				} else {
					throw new IllegalStateException(v.getClass().getName()
							+ " is not a "
							+ " view that can be bounds by TabManager");
				}

			}
		}
	}

	private View getItemViewCustom(TabItem item) {
		if (null == customItems || null == customItems.data)
			return null;
		View v = LayoutInflater.from(context).inflate(customItems.resource,
				this, false);
		if (null == v)
			return null;
		bindView(items.indexOf(item),v);
		return v;
	}

	private View addItemView(TabItem item) {
		if (isCustomView)
			return getItemViewCustom(item);
		else
			return getItemViewClassical(item);
	}

	// 添加Tab项目，代码可优化
	private View addItemView(TabItem item, boolean b) {

		RelativeLayout itemLayout = new RelativeLayout(context);
		itemLayout.addView(addItemView(item), lpRe);

		RelativeLayout.LayoutParams lpSign = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout layoutSign = new LinearLayout(context);
		layoutSign.setId(SIGN_VIEW_ID);
		int padding=dip2px(context, SIGN_PADDING_DIP);
		layoutSign.setPadding(padding, padding, padding, padding);
		itemLayout.addView(layoutSign, lpSign);
		setViewStyle(itemLayout, item, b);

		return itemLayout;
	}

	// 初始化默认布局
	private void initView() {
		for (int i = 0; i < count; i++) {
		
			View itemLayout = addItemView(items.get(i), i == 0);
			views.add(itemLayout);
			container.addView(itemLayout, lp);
		}
		this.removeAllViews();
		this.addView(container);
		setTextSize();
	}

	// 获取tab中图片的资源id
	private int getDrawableResourceId(String name) {
		String packName = context.getPackageName();
		return context.getResources().getIdentifier(name, "drawable", packName);
	}
	
	// 获取tab中颜色的资源id
	private int getColorResourceId(String name) {
		String packName = context.getPackageName();
		return context.getResources().getIdentifier(name, "color", packName);
	}

	// 设置tab中图片显示
	private void setImageRes(ImageView img, String src) {
		int id=getDrawableResourceId(src);
		if (0!=id)
			img.setImageResource(id);
	}


	private void setTextColor(TextView tv, String textColor) {
		int color=Color.BLACK;
		boolean b=true;
		try{
			color=Color.parseColor(textColor);
			b=false;
		}
		catch(Exception e) 
		{
			;
		}
		if (b)
		{
			int id=getColorResourceId(textColor);
			if (0!=id)
				color=context.getResources().getColor(0);
		}
		tv.setTextColor(color);
	}
	
	private void setViewStyleClassical(View currentView, TabItem item,
			boolean selected) {
		ClassicalTabItem tab = (ClassicalTabItem) item;
		String backColor = selected ? tab.getSelectedColor() : tab.getColor();
		String backImage = selected ? tab.getSelectedIcon() : tab.getIcon();
		String textColor = selected ? tab.getTextSelectedColor() : tab
				.getTextColor();
		currentView.setBackgroundColor(Color.parseColor(backColor));
		ImageView img=(ImageView) currentView.findViewById(IMG_ID);
		TextView tv=(TextView) currentView.findViewById(TV_ID);
		if (!item.isCustomIcon())
			setImageRes(img, backImage);
		setTextColor(tv,textColor);
		img.setSelected(selected);
		tv.setSelected(selected);		
	}


	private void setViewStyleCustom(View currentView, TabItem item,
			boolean selected) {
		int len=((ViewGroup)currentView).getChildCount();
		for (int i=0;i<len;i++)
		{
			View v=((ViewGroup)currentView).getChildAt(i);
			if (v instanceof ViewGroup)
				setViewStyleCustom(v,item,selected);
			else
				v.setSelected(selected);
		}
	}

	// 设置tab颜色风格
	private void setViewStyle(View currentView, TabItem item, boolean pressed) {
		if (isCustomView)
			setViewStyleCustom(currentView, item, pressed);
		else
			setViewStyleClassical(currentView, item, pressed);
	}

	private void setPaddingFromConfig(int type,String size)
	{
		if (isNullString(size)) return;
		String paddingPxStr="0";
		if (size.endsWith("dip") || size.endsWith("dp"))
		{
			size=size.replace("i", "");
			size=size.replace("dp", "");
			try
			{
				paddingPxStr=dip2px(context,Float.valueOf(size))+"";
			}catch(Exception e)
			{;}
		}
		else if (size.endsWith("px"))
		{
			size=size.replace("px", "");
			paddingPxStr=size;
			
		}
		else
			paddingPxStr=size;
		try
		{
			int paddingPx=(int) Float.parseFloat(paddingPxStr);
			if (PADDING_TYPE_TOP==type)
				paddingTop=paddingPx;
			if (PADDING_TYPE_BOTTOM==type)
				paddingBottom=paddingPx;
		}catch(Exception e)
		{;}
	}
	
	// 读取配置文件，初始化项目
	@SuppressLint("DefaultLocale")
	private void readConfig() {

		if (items == null) {

			items = new ArrayList<TabItem>();
			// 读取配置文件config
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			try {
				InputStream inStream = context.getAssets().open("config.xml");
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(inStream);
				Element root = dom.getDocumentElement();
				Element common = (Element) root.getElementsByTagName("Common").item(0);
				textSize=common.getAttribute("textSize");
				setPaddingFromConfig(PADDING_TYPE_TOP,common.getAttribute("paddingTop"));
				setPaddingFromConfig(PADDING_TYPE_BOTTOM,common.getAttribute("paddingBottom"));
				setPaddingFromConfig(PADDING_TYPE_TOP,common.getAttribute("paddingTopBottom"));
				setPaddingFromConfig(PADDING_TYPE_BOTTOM,common.getAttribute("paddingTopBottom"));

				if ("true".equalsIgnoreCase(common.getAttribute("useCustomView")))
				{
					isCustomView=true;
					inStream.close();
					return;
				}
				else
					isCustomView=false;
				NodeList tabs = root.getElementsByTagName("Tab");// 解析tab项
				for (int i = 0; i < tabs.getLength(); i++) {
					ClassicalTabItem tab = new ClassicalTabItem();
					Element personNode = (Element) tabs.item(i);
					tab.setName(personNode.getAttribute("name"));
					//tab.setClassPath(personNode.getAttribute("class"));
					tab.setUrl(personNode.getAttribute("url"));
					tab.setIcon(personNode.getAttribute("icon"));
					tab.setSelectedIcon(personNode.getAttribute("selectedIcon"));
					tab.setColor(personNode.getAttribute("color"));
					tab.setSelectedColor(personNode.getAttribute("selectedColor"));
					tab.setTextColor(personNode.getAttribute("textColor"));
					tab.setTextSelectedColor(personNode
							.getAttribute("textSelectedColor"));
					tab.setTags(personNode.getAttribute("tags"));					
					items.add(tab);
				}
				
				inStream.close();
 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		count = items.size();

	}

	// 方法
	private boolean isNullString(String src)
	{
		return (null==src || "".equals(src))?true:false;
	}
	
	// 判断是否越界
	private boolean inRange(int index) {
		return (index >= 0 && index < count);
	}

	private void loadUrl(TabItem item) {
		if (null == item || null == web)
			return;
		String url = item.getUrl();
		if (!isNullString(url))
			web.loadUrl(url);
	}

	
	/**
	 * 设置是否可用，当设置不可用时，item点击无效
	 * @param b:是否可用
	 */
	public void setEnable(boolean b) {
		enable = b;
	}

	/**
	 * 获取项目数量
	 * @return 当前包含的item的数量
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 * 设置文字大小
	 * @param unit：度量单位，取值为TypedValue中的常量
	 * @param size: 度量值
	 */
	public void setTextSize(int unit,float size)
	{
		for (int i=0;i<views.size();i++)
			((TextView) views.get(i).findViewById(TV_ID)).setTextSize(unit, size);	
	}
	
	private StateListDrawable getStateDrawable(Drawable normal, Drawable selected) {
	    StateListDrawable list = new StateListDrawable();
	    list.addState(new int[]{android.R.attr.state_focused}, selected);
	    list.addState(new int[]{android.R.attr.state_pressed}, selected);
	    list.addState(new int[]{android.R.attr.state_selected}, selected);
	    list.addState(new int[]{android.R.attr.state_enabled}, normal);
	    list.addState(new int[]{}, normal);
	    return list;
	}
	
	/**
	 * 设置项目图标，针对使用默认tab样式的情况下使用
	 * @param index：项目索引
	 * @param drawable: 默认状态下显示的Drawable
	 * @param selectedDrawable: 选中状态下显示的Drawable
	 */
	public void setIcon(int index,Drawable drawable,Drawable selectedDrawable)
	{
		setIcon(index,IMG_ID,drawable,selectedDrawable);
		
	}
	
	/**
	 * 设置项目图标，针对使用自定义样式的情况下使用
	 * @param index：项目索引
	 * @param imgId：需要改变的ImageView的id
	 * @param drawable: 默认状态下显示的Drawable
	 * @param selectedDrawable: 选中状态下显示的Drawable
	 */
	public void setIcon(int index,int imgId,Drawable drawable,Drawable selectedDrawable)
	{
		if (!inRange(index)) return;
		ImageView img=(ImageView) views.get(index).findViewById(imgId);
		if (null==img) return;
		items.get(index).setCustomIcon(true);
		img.setImageDrawable(getStateDrawable(drawable, selectedDrawable));
		
	}

	/**
	 * 设置数据源
	 * @param items：数据源
	 */
	public void setItems(List<TabItem> items) {
		this.items = items;
		count = items.size();
		container.removeAllViews();
		views.clear();
		initView();
		setListener();
	}

	/**
	 * 获取数据源
	 * @return 数据源
	 */
	public List<TabItem> getItems() {
		return this.items;
	}

	/**
	 * 设置自定义的布局样式，调用此方法，将覆盖config.xml下的useCustomView属性，
	 * 改方式目前调用较为繁琐，可以后续优化
	 * @param items：数据源
	 * @param data：界面相关的数据源
	 * @param resource: 布局资源id
	 * @param from: 数据来源数组
	 * @param to: 控件id数组
	 */
	public void setCustom(List<TabItem> items,List<? extends Map<String, ?>> data, int resource,
			String[] from, int[] to) {
		customItems = new CustomItems();
		customItems.data = data;
		customItems.resource = resource;
		customItems.from = from;
		customItems.to = to;
		this.isCustomView = true;
		this.items=items;
		count=items.size();
		initLayout();
		initView();
		setListener();
	}

	/**
	 * 获取单个View
	 * @param index：索引
	 * @return 对应index的View
	 */
	public View getItemView(int index) {
		return inRange(index) ? views.get(index) : null;
	}

	// 切换tab
	
	private boolean changeView(int index)
	{
		return changeView(index,true);
	}
	
	private boolean changeView(int index,boolean hasOper) {
		if (!inRange(index))
			return false;
		if (currentSelected == index)
			return false;
		setViewStyle(views.get(currentSelected), items.get(currentSelected),
				false);
		currentSelected = index;
		setViewStyle(views.get(currentSelected), items.get(currentSelected),
				true);
		if (hasOper)
			loadUrl(items.get(index));
		return true;
	}

	/**
	 * 获取宿主webView
	 * @return WebView
	 */
	public WebView getWeb() {
		return web;
	}

	/**
	 * 设置宿主webView
	 * @param web
	 */
	public void setWeb(WebView web) {
		this.web = web;
	}

	/**
	 * 设置选中，并进行对应如WebView.load的操作
	 * @param index：需要被选中的索引
	 */
	public void setSelected(int index) {

		if (changeView(index) && mChangedListener != null)
			mChangedListener.onTabSelectedChanged(views.get(currentSelected),
					currentSelected);
	}

	/**
	 * 获取被选中项目的索引
	 * @return 被选中项目的索引
	 */
	public int getSelected() {
		return currentSelected;
	}

	/**
	 * 设置选中，仅进行tab状态的切换
	 * @param index：需要被选中的索引
	 */
	public void setSelectedWithOutOperat(int index) {
		if (changeView(index,false) && mChangedListener != null)
			mChangedListener.onTabSelectedChanged(views.get(currentSelected),
					currentSelected);
	}

	/**
	 * 添加项目，项目将添加至最后
	 * @param item：需要被添加的项目
	 */
	public void addItem(TabItem item) {
		if (items == null)
			return;
		addItem(item, items.size());
	}

	/**
	 * 添加项目
	 * @param item：需要被添加的项目
	 * @param index：被添加的位置
	 */
	public void addItem(TabItem item, int index) {
		if (items == null)
			return;
		items.add(index, item);
		count = items.size();
		container.removeAllViews();
		views.clear();
		initView();
		setListener();
	}

	/**
	 * 移除项目
	 * @param index：被移除项目的索引
	 */
	public void removeItem(int index) {
		if (!inRange(index))
			return;
		items.remove(index);
		views.remove(index);
		count = items.size();
		container.removeViewAt(0);
	}

	/**
	 * 移除项目
	 * @param item：被移除的项目
	 */
	public void removeItem(TabItem item) {
		removeItem(items.indexOf(item));
	}

	/**
	 * 添加自定义角标View
	 * @param index：被添加角标的项目索引
	 * @param view：被添加的角标View
	 * @param algin：添加的位置，共包含左上、左下、右上、右下四种
	 * @param type：位置类型， 
	 * SIGN_VIEW_ALIGN_TYPE_INSIDE：添加到item中实际View的边界，
	 * SIGN_VIEW_ALIGN_TYPE_OUTSIDE：添加到item中布局View的边界
	 */
	public void addSignView(int index, View view, int algin,int type) {
		if (!inRange(index))
			return;
		View v = views.get(index);
		LinearLayout ll = (LinearLayout) v.findViewById(SIGN_VIEW_ID);
		RelativeLayout.LayoutParams lpr = (RelativeLayout.LayoutParams) ll
				.getLayoutParams();
		int typeH=0xF & (algin >> 4);
		int typeV=0xF & algin;
		lpr.addRule(typeH);
		lpr.addRule(typeV);
		if (SIGN_VIEW_ALIGN_TYPE_INSIDE==type)
		{
			int[] paddings={0,0,0,0};
			if (RelativeLayout.ALIGN_PARENT_LEFT==typeH)
				paddings[0]=((ViewGroup)v).getChildAt(0).getLeft();
			if (RelativeLayout.ALIGN_PARENT_RIGHT==typeH)
				paddings[2]=((ViewGroup)v).getChildAt(0).getLeft();
			if (RelativeLayout.ALIGN_PARENT_TOP==typeV)
				paddings[1]=paddingTop;
			if (RelativeLayout.ALIGN_PARENT_BOTTOM==typeV)
				paddings[3]=paddingBottom;
			ll.setPadding(paddings[0], paddings[1], paddings[2], paddings[3]);
		}
		else
		{
			int padding=dip2px(context, SIGN_PADDING_DIP);
			ll.setPadding(padding, padding, padding, padding);
		}

		
		ll.setLayoutParams(lpr);
		ll.addView(view);
	}

	/**
	 * 移除自定义角标View
	 * @param index：被移除角标的项目索引
	 */
	public void removeSignView(int index) {
		if (!inRange(index))
			return;
		View v = views.get(index);
		LinearLayout ll = (LinearLayout) v.findViewById(SIGN_VIEW_ID);
		ll.removeAllViews();
	}

	// 事件
	// 点击
	
	public interface OnTabClickListener {
		/**
		 * tab项目点击监听器
		 * @param v：被点击的控件
		 * @param index：被点击项目的索引
		 * @return 是否阻止WebView.load操作，默认为false，不阻止load操作
		 */
		boolean onTabClick(View v, int index);
	}
	
	/**
	 * 设置tab项目点击监听器
	 * @param listener：监听器
	 */
	public void setOnTabClickListener(OnTabClickListener listener) {
		mClickListener = listener;
	}
	
	
	public interface OnTabSelectedChangedListener {
		/**
		 * tab项目点击监听器
		 * @param v：最新被选择的控件
		 * @param index：最新被选择项目的索引
		 */
		void onTabSelectedChanged(View v, int index);
	}

	// 选中项改变
	/**
	 * 设置tab选中项切换监听器
	 * @param listener：监听器
	 */
	public void setOnTabSelectedChangedListener(OnTabSelectedChangedListener listener) {
		mChangedListener = listener;
	}

	class CustomItems {
		public List<? extends Map<String, ?>> data;
		public int resource;
		public String[] from;
		public int[] to;
	}
}

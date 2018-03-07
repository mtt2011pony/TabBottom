package com.ynet.android.flame.tabmanager;

/**
 *<p>TabItem:TabManager的数据实体类基类，用于Flame开发平台的首页底部管理的数据源管理，在Flame开发平台中使用。<br>
 *使用TabManager控件时，数据管理部分应为TabItem类的子类。<br>
 *各位小伙伴在项目实施过程中，请反馈其中的不足，我们共同改进。<br>
 *实际应用中，应使用List<? extends TabItem>类型的变量存储数据源。
 *
= * @version v1.0.0
 */
public class TabItem {


	private String url;
	
	private boolean	isCustomIcon=false;

	/**
	 * 获取url属性，url属性决定tab点击或切换时的操作
	 * @return url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * 设置url属性，url属性决定tab点击或切换时的操作
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取isCustomIcon属性，isCustomIcon属性决定当前item是否使用自定义图片
	 * @return isCustomIcon
	 */
	public boolean isCustomIcon() {
		return isCustomIcon;
	}

	/**
	 * 设置isCustomIcon属性，isCustomIcon属性决定当前item是否使用自定义图片
	 * @param isCustomIcon
	 */
	public void setCustomIcon(boolean isCustomIcon) {
		this.isCustomIcon = isCustomIcon;
	}
	
	
}

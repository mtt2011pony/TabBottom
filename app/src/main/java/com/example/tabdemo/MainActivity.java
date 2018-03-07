package com.example.tabdemo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ynet.android.flame.tabmanager.TabItem;
import com.ynet.android.flame.tabmanager.TabMananger;
import com.ynet.android.flame.tabmanager.TabMananger.OnTabClickListener;
import com.ynet.android.flame.tabmanager.TabMananger.OnTabSelectedChangedListener;
import com.ynet.android.flame.tabmanager.TabSignView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Context mContext;
    private TabMananger mTab;
    private boolean b = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        mTab = (TabMananger) findViewById(R.id.tab);
        mTab.setOnTabClickListener(new OnTabClickListener() {

            @Override
            public boolean onTabClick(View v, int index) {
                if (null == v)
                    Toast.makeText(mContext, "这是第" + index + "被点击了", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "这是第" + index + "被点击了", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mTab.setOnTabSelectedChangedListener(new OnTabSelectedChangedListener() {

            @Override
            public void onTabSelectedChanged(View v, int index) {
                if (null == v)
                    Toast.makeText(mContext, "这是第" + index + "被选中了", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "这是第" + index + "被选中了", Toast.LENGTH_SHORT).show();

            }
        });
        ((Button) findViewById(R.id.btn)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                TabSignView btn = new TabSignView(mContext);

                //	btn.setBackgroundColors(Color.BLACK);
                btn.setText("99+");
                if (b)
                    mTab.addSignView(1, btn, TabMananger.SIGN_VIEW_ALIGN_RT, 2);
                else
                    mTab.removeSignView(1);
                b = !b;
            }
        });
        ((Button) findViewById(R.id.btn1)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                initCustom();
//				Drawable d1=mContext.getResources().getDrawable(R.drawable.nav_pressed_ranking);
//				Drawable d2=mContext.getResources().getDrawable(R.drawable.nav_pressed_mine);
//				mTab.setIcon(1, R.id.bb, d1, d2);
                //mTab.setSelectedWithOutOperat(3);
                //	mTab.setTextSize(TypedValue.COMPLEX_UNIT_SP , 12);
//				ClassicalTabItem item=(ClassicalTabItem)mTab.getItems().get(0);
//				mTab.removeItem(item);
            }
        });
        //	initCustom();
    }

    private String[] str = {"http://www.baidu1.com", "http://www.baidu2.com", "http://www.baidu3.com", "http://www.baidu4.com"};

    private void initCustom() {
        List<TabItem> list = new ArrayList<TabItem>();
        for (int i = 0; i < 4; i++) {
            TabItem item = new TabItem();
            item.setUrl(str[i]);
            list.add(item);
        }

        mTab.setCustom(list, getData(),
                R.layout.tab_item_classical, new String[]{"aa", "bb", "cc"},
                new int[]{R.id.aa, R.id.bb, R.id.cc});
    }

    //测试数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", "1111");
        map.put("bb", R.drawable.common_btn_selector);
        map.put("cc", "aaaa");
        list.add(map);

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("aa", "2222");
        map1.put("bb", R.drawable.common_btn_selector);
        map1.put("cc", "bbbb");
        list.add(map1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("aa", "3333");
        map2.put("bb", R.drawable.nav_normal_product_recommend);
        map2.put("cc", "cccc");
        list.add(map2);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("aa", "4444");
        map3.put("bb", R.drawable.nav_normal_ranking);
        map3.put("cc", "dddd");
        list.add(map3);

        return list;
    }

}

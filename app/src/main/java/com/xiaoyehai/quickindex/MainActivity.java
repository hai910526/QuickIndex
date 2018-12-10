package com.xiaoyehai.quickindex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.xiaoyehai.quickindex.util.PinYinUtil;
import com.xiaoyehai.quickindex.widget.QuickIndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 快速检索:
 * 1.应用场景:联系人，好友列表，商品等列表的快速定位和搜索
 * <p/>
 * 2.实现逻辑:
 * a.右边是自定义QuickIndexBar,它能获取触摸它的时候当前所触摸到的字母;
 * 绘制文本x坐标: width/2;
 * 绘制文本y坐标: 格子高度的一半 + 文本高度的一半 + position*格子高度
 * 计算触摸点对应的字母:根据触摸点的y坐标除以cellHeight,得到的值就是字母对应的索引;
 * <p/>
 * b.左边是listview，它根据当前触摸的字母，去自己列表找首字母和触摸字母相同的那个
 * item，然后让item放置到屏幕顶端(setSelection(position));
 * <p/>
 * c.需要用到获取汉字的拼音,借助类库pinyin4j.jar实现;
 */
public class MainActivity extends AppCompatActivity {

    private QuickIndexBar mQuickIndexBar;
    private ListView mListView;

    private List<Friend> list;
    private TextView mCurrentWord;

    private Handler handler = new Handler();
    private EditText mEditText;
    private MyAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        findViews();
        initListView();

        //设置字母触摸监听，获取触摸的字母
        mQuickIndexBar.setonTouchLetterListener(new QuickIndexBar.onTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                Log.i("info", "onTouchLetter: " + letter);
                //根据当前触摸的字母，去集合中找那个item的首字母和letter一样，然后将对应的item放到屏幕顶端
                for (int i = 0; i < list.size(); i++) {
                    String firstWord = list.get(i).getPinyin().charAt(0) + "";
                    if (letter.equals(firstWord)) {
                        //说明找到了，那么应该讲当前的item放到屏幕顶端
                        mListView.setSelection(i);
                        break; //只需要找到第一个就行
                    }
                }

                //屏幕中间显示当前触摸的字母
                showCurrentWord(letter);
            }
        });
        //Log.e("info", "onCreate: " + PinYinUtil.getPinYin("中国")); //  ZHONGGUO

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(s)) {
                    mMyAdapter.refresh(list);
                }
                String pinYin = PinYinUtil.getPinYin(s.toString());
                if (TextUtils.isEmpty(pinYin)) {
                    return;
                }
                String letter = pinYin.charAt(0) + "";

                List<Friend> newList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    String firstWord = list.get(i).getPinyin().charAt(0) + "";
                    if (letter.equals(firstWord)) {
                        newList.add(list.get(i));
                    }
                }
                mMyAdapter.refresh(newList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 屏幕中间显示当前触摸的字母
     *
     * @param letter
     */
    private void showCurrentWord(String letter) {
        mCurrentWord.setVisibility(View.VISIBLE);
        mCurrentWord.setText(letter);

        //先移除之前的任务
        handler.removeCallbacksAndMessages(null);

        //延时隐藏mCurrentWord
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //主线程执行
                mCurrentWord.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }

    /**
     * 初始化listView
     */
    private void initListView() {
        list = new ArrayList<>();
        initData();
        //对数据进行排序
        Collections.sort(list);
        mMyAdapter = new MyAdapter(this, list);
        mListView.setAdapter(mMyAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 虚拟数据
        list.add(new Friend("李伟"));
        list.add(new Friend("张三"));
        list.add(new Friend("张三"));
        list.add(new Friend("步惊云"));
        list.add(new Friend("步惊云"));
        list.add(new Friend("张三"));
        list.add(new Friend("阿三"));
        list.add(new Friend("阿四"));
        list.add(new Friend("段誉"));
        list.add(new Friend("段正淳"));
        list.add(new Friend("张三丰"));
        list.add(new Friend("陈坤"));
        list.add(new Friend("林俊杰1"));
        list.add(new Friend("陈坤2"));
        list.add(new Friend("王二a"));
        list.add(new Friend("林俊杰a"));
        list.add(new Friend("张四"));
        list.add(new Friend("林俊杰"));
        list.add(new Friend("王二"));
        list.add(new Friend("王二b"));
        list.add(new Friend("赵四"));
        list.add(new Friend("杨坤"));
        list.add(new Friend("赵子龙"));
        list.add(new Friend("杨坤1"));
        list.add(new Friend("李伟1"));
        list.add(new Friend("宋江"));
        list.add(new Friend("宋江1"));
        list.add(new Friend("李伟3"));
    }

    private void findViews() {
        mQuickIndexBar = (QuickIndexBar) findViewById(R.id.quickindexbar);
        mListView = (ListView) findViewById(R.id.listview);
        mCurrentWord = (TextView) findViewById(R.id.currentword);
        mEditText = (EditText) findViewById(R.id.et_search);
    }
}

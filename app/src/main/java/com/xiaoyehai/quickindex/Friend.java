package com.xiaoyehai.quickindex;

import com.xiaoyehai.quickindex.util.PinYinUtil;

/**
 * Created by xiaoyehai on 2016/11/30.
 */
public class Friend implements Comparable<Friend> {

    private String name;
    private String pinyin;

    //构造方法
    public Friend(String name) {
        this.name = name;
        //一开始就转好拼音
        setPinyin(PinYinUtil.getPinYin(name));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public int compareTo(Friend another) {
//        String pinyin = PinYinUtil.getPinYin(name);
//        String anotherPinyin = PinYinUtil.getPinYin(another.getName());
//        return pinyin.compareTo(anotherPinyin);

        return getPinyin().compareTo(another.getPinyin());
    }
}

package com.xiaoyehai.quickindex;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xiaoyehai on 2016/11/30.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;

    private List<Friend> list;

    public MyAdapter(Context context, List<Friend> list) {
        this.context = context;
        this.list = list;
    }

    public void refresh(List<Friend> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);

        //设置数据
        Friend friend = list.get(position);
        holder.tv_name.setText(friend.getName());

        String currentWord = friend.getPinyin().charAt(0) + "";
        //获取上一个item的首字母
        if (position > 0) {
            String lastWord = list.get(position - 1).getPinyin().charAt(0) + "";
            if (currentWord.equals(lastWord)) {
                //首字母相同
                holder.tv_first_world.setVisibility(View.GONE);
            } else {
                //首字母不同
                //由于布局是复用的，所以需要设置可见
                holder.tv_first_world.setVisibility(View.VISIBLE);
                holder.tv_first_world.setText(currentWord);
            }
        } else {
            //第一个
            holder.tv_first_world.setVisibility(View.VISIBLE);
            holder.tv_first_world.setText(currentWord);
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView tv_first_world, tv_name;

        //构造方法
        public ViewHolder(View convertView) {
            tv_first_world = (TextView) convertView.findViewById(R.id.tv_first_word);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        }

        //对ViewHolder的封装
        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}

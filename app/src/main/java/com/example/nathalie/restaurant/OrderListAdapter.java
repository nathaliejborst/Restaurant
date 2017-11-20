package com.example.nathalie.restaurant;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nathalie on 18-11-2017.
 */

public class OrderListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Order> mOrderlist;

    public OrderListAdapter(Context mContext, List<Order> mOrderlist) {
        this.mContext = mContext;
        this.mOrderlist = mOrderlist;
    }

    @Override
    public int getCount() {
        return mOrderlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mOrderlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.item_order_list, null);

        TextView tvAmount = (TextView)v.findViewById(R.id.tv_amount);
        TextView tvName = (TextView)v.findViewById(R.id.tv_item);
        TextView tvPrice = (TextView)v.findViewById(R.id.tv_total);


//        TextView tvName = (TextView)v.findViewById(R.id.tv_item);
//        TextView tvPrice = (TextView)v.findViewById(R.id.tv_total);
//        TextView tvDescription = (TextView)v.findViewById(R.id.tv_description);
//        ImageView ivIcon = (ImageView)v.findViewById(R.id.iv_icon);
//
//        tvName.setText(mProductlist.get(i).getName());
//        tvPrice.setText(String.valueOf(mProductlist.get(i).getPrice()) + "$");
//        tvDescription.setText(mProductlist.get(i).getDescription());
//
//        String imageUri = mProductlist.get(i).getIcon();
//        Log.d("hallo_img", imageUri);
//        Picasso.with(mContext).load(imageUri).into(ivIcon);
//

//        ivIcon.setImageResource(mProductlist.get(i).getIcon());




        v.setTag(mOrderlist.get(i).getName());

        return v;
    }

}

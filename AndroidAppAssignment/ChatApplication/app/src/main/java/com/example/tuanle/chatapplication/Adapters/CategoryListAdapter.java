package com.example.tuanle.chatapplication.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tuanle.chatapplication.R;

public class CategoryListAdapter  {
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mCategoryName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mCategoryName = itemView.findViewById(R.id.tv_category);
        }
    }
}

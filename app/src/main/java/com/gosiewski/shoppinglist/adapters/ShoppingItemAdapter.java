package com.gosiewski.shoppinglist.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gosiewski.shoppinglist.R;
import com.gosiewski.shoppinglist.model.ShoppingItem;

import java.util.List;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> {
    private List<ShoppingItem> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView checkView;
        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout v) {
            super(v);

            nameView = (TextView) v.findViewById(R.id.item_name);
            checkView = (TextView) v.findViewById(R.id.item_check);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.item_layout);
        }
    }

    public ShoppingItemAdapter(List<ShoppingItem> items) {
        this.items = items;
    }

    @Override
    public ShoppingItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameView.setText(getItemAt(position).name);
        if (items.get(position).alreadyBought) {
            holder.checkView.setText(R.string.item_second_touch_effect);
            holder.relativeLayout.setBackgroundColor(Color.GRAY);
        } else {
            holder.checkView.setText(R.string.item_touch_effect);
            holder.relativeLayout.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ShoppingItem getItemAt(int index) {
        return items.get(index);
    }

}

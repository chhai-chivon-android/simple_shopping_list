package com.gosiewski.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gosiewski.shoppinglist.R;
import com.gosiewski.shoppinglist.model.ShoppingItem;

import java.util.List;

public class NewShoppingItemAdapter extends RecyclerView.Adapter<NewShoppingItemAdapter.ViewHolder> {
    private List<ShoppingItem> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;

        public ViewHolder(RelativeLayout v) {
            super(v);
            nameView = (TextView) v.findViewById(R.id.item_name);
        }
    }

    public NewShoppingItemAdapter(List<ShoppingItem> items) {
        this.items = items;
    }

    @Override
    public NewShoppingItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_shopping_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).name);
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public ShoppingItem getItemAt(int index){
        return items.get(index);
    }
}

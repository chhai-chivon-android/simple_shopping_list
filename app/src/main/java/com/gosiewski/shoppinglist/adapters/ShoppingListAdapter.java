package com.gosiewski.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gosiewski.shoppinglist.R;
import com.gosiewski.shoppinglist.model.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private List<ShoppingList> lists;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView dateView;

        public ViewHolder(View v) {
            super(v);

            this.nameView = (TextView) v.findViewById(R.id.list_name);
            this.dateView = (TextView) v.findViewById(R.id.list_date);
        }

        public void setList(String name, Date date){
            nameView.setText(name);
            dateView.setText(SimpleDateFormat.getDateInstance().format(date));
        }
    }

    public ShoppingListAdapter(List<ShoppingList> lists) {
        this.lists = lists;
    }

    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shoppping_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setList(getItemAt(position).getName(), getItemAt(position).getDate());
    }

    @Override
    public int getItemCount(){
        return lists.size();
    }

    public ShoppingList getItemAt(int index){
        return lists.get(index);
    }
}

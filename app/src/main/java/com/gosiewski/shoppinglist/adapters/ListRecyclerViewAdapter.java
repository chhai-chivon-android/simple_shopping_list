package com.gosiewski.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gosiewski.shoppinglist.R;
import com.gosiewski.shoppinglist.model.ShoppingItem;
import com.gosiewski.shoppinglist.model.ShoppingList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {
    private List<ShoppingList> lists;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView dateView;

        public ViewHolder(RelativeLayout v){
            super(v);
            this.nameView = (TextView) v.findViewById(R.id.list_name);
            this.dateView = (TextView) v.findViewById(R.id.list_date);
        }

        public void setListName(String name){
            nameView.setText(name);
        }

        public void setListDate(Date date){
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
            dateView.setText(dateFormat.format(date));
        }
    }

    public ListRecyclerViewAdapter(List<ShoppingList> lists) {
        this.lists = lists;
    }

    @Override
    public ListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setListName(lists.get(position).getName());
        holder.setListDate(lists.get(position).getDate());
    }

    @Override
    public int getItemCount(){
        return lists.size();
    }

    public ShoppingList getItemAt(int index){
        return lists.get(index);
    }
}

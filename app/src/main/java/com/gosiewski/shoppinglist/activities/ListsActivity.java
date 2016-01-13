package com.gosiewski.shoppinglist.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.gosiewski.shoppinglist.R;
import com.gosiewski.shoppinglist.adapters.ShoppingListAdapter;
import com.gosiewski.shoppinglist.model.ShoppingList;

import java.util.List;

public class ListsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ShoppingList> lists;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle("Lists");


    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_lists);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                if(e.getAction() == MotionEvent.ACTION_UP) {
                    View view = rv.findChildViewUnder(e.getX(), e.getY());
                    if(view != null){
                        ShoppingList list = ((ShoppingListAdapter) rv.getAdapter()).getItemAt(rv.getChildAdapterPosition(view));
                        showList(list);
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        lists = ShoppingList.getAll();

        adapter = new ShoppingListAdapter(lists);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lists_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.lists_action_bar_add :
                addNewList();
                break;
            default :
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewList(){
        Intent intent = new Intent(this, AddNewListActivity.class);
        startActivity(intent);
    }

    private void showList(ShoppingList list){

    }
}

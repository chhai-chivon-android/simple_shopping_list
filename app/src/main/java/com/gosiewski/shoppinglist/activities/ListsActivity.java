package com.gosiewski.shoppinglist.activities;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
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
import com.gosiewski.shoppinglist.listeners.RecyclerViewOnGestureListener;
import com.gosiewski.shoppinglist.model.ShoppingList;

import java.util.List;

public class ListsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ShoppingList> lists;
    private RecyclerView.LayoutManager layoutManager;
    public final static String EXTRA_LIST_ID = "com.gosiewski.shoppinglist.LISTID";

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
        lists = ShoppingList.getAll();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_lists);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(this,
                new RecyclerViewOnGestureListener() {

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if(view != null){
                            showList(((ShoppingListAdapter)recyclerView.getAdapter()).getItemAt(recyclerView.getChildAdapterPosition(view)));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {

                    }
                }
        );

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return gestureDetector.onTouchEvent(e);
            }
            @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
            @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

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
        Intent intent = new Intent(this, ListDetailsActivity.class);
        intent.putExtra(EXTRA_LIST_ID, list.getId());
        startActivity(intent);
    }
}

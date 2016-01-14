package com.gosiewski.shoppinglist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.gosiewski.shoppinglist.R;
import com.gosiewski.shoppinglist.adapters.ShoppingItemAdapter;
import com.gosiewski.shoppinglist.listeners.RecyclerViewOnGestureListener;
import com.gosiewski.shoppinglist.model.ShoppingItem;
import com.gosiewski.shoppinglist.model.ShoppingList;

import java.text.SimpleDateFormat;

public class ListDetailsActivity extends AppCompatActivity {
    private ShoppingList list;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public final static String EXTRA_EDIT_LIST_ID = "com.gosiewski.shoppinglist.EDITLISTID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        this.list = ShoppingList.load(ShoppingList.class, intent.getLongExtra(ListsActivity.EXTRA_LIST_ID, 0));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(list.getName());
            actionBar.setSubtitle(SimpleDateFormat.getDateInstance().format(list.getDate()));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_shopping_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(this,
                new RecyclerViewOnGestureListener() {

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if(view != null){
                            ShoppingItem item = ((ShoppingItemAdapter) recyclerView.getAdapter()).getItemAt(recyclerView.getChildAdapterPosition(view));

                            item.alreadyBought = !item.alreadyBought;
                            item.save();
                            adapter.notifyDataSetChanged();
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

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
            @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

        adapter = new ShoppingItemAdapter(list.items());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_details_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.list_details_action_bar_edit :
                editList();
                break;
            case R.id.list_details_action_bar_delete :
                deleteList();
                break;
            default :
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void editList(){
        Intent intent = new Intent(this, AddNewListActivity.class);
        intent.putExtra(EXTRA_EDIT_LIST_ID, list.getId());
        startActivity(intent);
    }

    public void deleteList(){
        list.delete();
        finish();
    }
}

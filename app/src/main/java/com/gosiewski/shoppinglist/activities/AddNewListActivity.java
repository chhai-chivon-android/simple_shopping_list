package com.gosiewski.shoppinglist.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gosiewski.shoppinglist.R;
import com.gosiewski.shoppinglist.adapters.NewShoppingItemAdapter;
import com.gosiewski.shoppinglist.fragments.DatePickerFragment;
import com.gosiewski.shoppinglist.listeners.RecyclerViewOnGestureListener;
import com.gosiewski.shoppinglist.model.ShoppingItem;
import com.gosiewski.shoppinglist.model.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private ShoppingList shoppingList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<ShoppingItem> items;
    private RecyclerView.Adapter adapter;
    private Button showDatePickerButton;
    private EditText nameEdit;
    public final static String EXTRA_EDIT_LIST_ID = "com.gosiewski.shoppinglist.EDITLISTID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);

        showDatePickerButton = (Button) findViewById(R.id.show_date_picker_button);
        nameEdit = (EditText) findViewById(R.id.list_name);

        long listId = getIntent().getLongExtra(EXTRA_EDIT_LIST_ID, 0);

        if(listId == 0)
            initEmptyList();
        else
            initSelectedList(listId);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_new_shopping_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(this,
                new RecyclerViewOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if(view != null){
                            ShoppingItem item = ((NewShoppingItemAdapter) recyclerView.getAdapter())
                                    .getItemAt(recyclerView.getChildAdapterPosition(view));

                            if (item.getId() != null)
                                item.delete();

                            items.remove(item);
                            adapter.notifyDataSetChanged();

                            Toast.makeText(AddNewListActivity.this, "Item deleted",
                                    Toast.LENGTH_SHORT).show();

                            return true;
                        }

                        return false;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {

                    }
                }
        );

        recyclerView.addOnItemTouchListener(
                new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        return gestureDetector.onTouchEvent(e);
                    }
                    @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
                    @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
                }
        );

        adapter = new NewShoppingItemAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_list_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_new_list_action_bar_edit :
                saveList();
                break;
            default :
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        shoppingList.setDate(date);

        showDatePickerButton.setText(SimpleDateFormat.getDateInstance().format(date));
    }

    public void addItem(View view) {
        EditText itemNameEdit = (EditText) findViewById(R.id.new_item_name_edit);

        if (itemNameEdit.getText().length() == 0) {
            Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
            return;
        }

        String itemName = itemNameEdit.getText().toString();

        items.add(new ShoppingItem(itemName, shoppingList));
        adapter.notifyDataSetChanged();

        itemNameEdit.setText("");
        Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
    }

    public void saveList() {
        if (nameEdit.getText().length() == 0) {
            Toast.makeText(this, "Please enter list name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (shoppingList.getDate() == null) {
            Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
            return;
        }

        shoppingList.setName(nameEdit.getText().toString());
        long listId = shoppingList.save();

        for(ShoppingItem item : items)
            shoppingList.addItem(item);

        Intent intent = new Intent(this, ListDetailsActivity.class);
        intent.putExtra(ListDetailsActivity.EXTRA_LIST_ID, listId);
        startActivity(intent);
        finish();
    }

    public void showDataPickerDialog(View view) {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void initEmptyList() {
        shoppingList = new ShoppingList();
        items = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("New list");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initSelectedList(long listID){
        shoppingList = ShoppingList.load(ShoppingList.class, listID);
        items = shoppingList.items();

        showDatePickerButton.setText(SimpleDateFormat.getDateInstance().format(shoppingList.getDate()));
        nameEdit.setText(shoppingList.getName());

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Edit list");
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        }
    }

}

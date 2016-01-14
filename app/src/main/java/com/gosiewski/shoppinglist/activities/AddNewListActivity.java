package com.gosiewski.shoppinglist.activities;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);

        showDatePickerButton = (Button) findViewById(R.id.show_date_picker_button);
        nameEdit = (EditText) findViewById(R.id.list_name);

        long listId = getIntent().getLongExtra(ListDetailsActivity.EXTRA_EDIT_LIST_ID, -1);

        if(listId < 0)
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
                            ShoppingItem item = ((NewShoppingItemAdapter) recyclerView.getAdapter()).getItemAt(recyclerView.getChildAdapterPosition(view));
                            items.remove(item);
                            item.delete();

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

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        shoppingList.setDate(date);

        showDatePickerButton.setText(SimpleDateFormat.getDateInstance().format(date));
    }

    public void addItem(View view) {
        EditText itemNameEdit = (EditText) findViewById(R.id.new_item_name_edit);
        if (itemNameEdit.getText().toString().equals("")) {
            Toast.makeText(this, "Enter item name!", Toast.LENGTH_SHORT).show();
            return;
        }
        String itemName = (itemNameEdit.getText().toString());
        itemNameEdit.setText("");
        items.add(new ShoppingItem(itemName, shoppingList));
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
    }

    public void saveList(View view) {
        if (nameEdit.getText().toString().equals("")) {
            Toast.makeText(this, "Enter list name!", Toast.LENGTH_SHORT).show();
            return;
        }
        shoppingList.setName(nameEdit.getText().toString());
        shoppingList.save();
        for(ShoppingItem item : items){
            shoppingList.addItem(item);
        }

        finish();
    }

    public void showDataPickerDialog(View view) {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void initEmptyList(){
        shoppingList = new ShoppingList();
        items = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle("New list");
    }

    private void initSelectedList(long listID){
        shoppingList = ShoppingList.load(ShoppingList.class, listID);
        items = shoppingList.items();

        showDatePickerButton.setText(SimpleDateFormat.getDateInstance().format(shoppingList.getDate()));
        nameEdit.setText(shoppingList.getName());

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle("Edit list");
    }

}

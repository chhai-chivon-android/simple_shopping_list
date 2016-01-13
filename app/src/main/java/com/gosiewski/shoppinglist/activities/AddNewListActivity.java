package com.gosiewski.shoppinglist.activities;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);

        shoppingList = new ShoppingList();
        items = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle("New list");
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = (RecyclerView) findViewById(R.id.item_recycler_view);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    View view = rv.findChildViewUnder(e.getX(), e.getY());
                    if (view != null) {
                        items.remove(rv.getChildAdapterPosition(view));
                        adapter.notifyDataSetChanged();
                        //TODO: Change it for working listener, like in ShoppingListAdapter
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

        adapter = new NewShoppingItemAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        shoppingList.setDate(date);

        Button showDatePickerButton = (Button) findViewById(R.id.show_date_picker_button);
        showDatePickerButton.setText(SimpleDateFormat.getDateInstance().format(date));
    }

    public void addItem(View view) {
        EditText itemNameEdit = (EditText) findViewById(R.id.new_item_name);
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
        EditText nameEdit = (EditText) findViewById(R.id.list_name);
        if (nameEdit.getText().toString().equals("")) {
            Toast.makeText(this, "Enter list name!", Toast.LENGTH_SHORT).show();
            return;
        }
        shoppingList.setName(nameEdit.getText().toString());
        for(ShoppingItem item : items){
            shoppingList.addItem(item);
        }
        shoppingList.save();

        finish();
    }

    public void showDataPickerDialog(View view) {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}

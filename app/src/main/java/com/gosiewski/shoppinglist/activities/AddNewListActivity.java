package com.gosiewski.shoppinglist.activities;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.gosiewski.shoppinglist.R;
import com.gosiewski.shoppinglist.fragments.DatePickerFragment;
import com.gosiewski.shoppinglist.model.ShoppingList;

import java.util.Calendar;

public class AddNewListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private ShoppingList shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);
        shoppingList = new ShoppingList();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle("New list");
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        shoppingList.setDate(calendar.getTime());
    }

    public void addItem(View view){
        EditText itemNameEdit = (EditText) findViewById(R.id.new_item_name);
        String itemName = (itemNameEdit.getText().toString());
        shoppingList.addItem(itemName);
    }

    public void saveList(View view){
        EditText nameEdit = (EditText) findViewById(R.id.list_name);
        shoppingList.setName(nameEdit.getText().toString());
        shoppingList.save();

        finish();
    }

    public void showDataPickerDialog(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}

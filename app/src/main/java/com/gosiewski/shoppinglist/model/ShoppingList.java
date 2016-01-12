package com.gosiewski.shoppinglist.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

@Table(name = "lists")
public class ShoppingList extends Model {
    @Column(name = "name") public String name;
    @Column(name = "timestamp") public long timestamp;

    public ShoppingList() {
        super();
    }

    public ShoppingList(String name, Date date) {
        super();
        this.name = name;
        setDate(date);
    }

    public List<ShoppingItem> items() {
        return getMany(ShoppingItem.class, "List");
    }

    public Date getDate() {
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime();
    }

    public void setDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        this.timestamp = calendar.getTimeInMillis();
    }
}

package com.gosiewski.shoppinglist.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "items")
public class ShoppingItem extends Model {
    @Column(name = "name") public String name;
    @Column(name = "bought") public boolean alreadyBought;
    @Column(name = "list", onDelete = Column.ForeignKeyAction.CASCADE) public ShoppingList list;

    public ShoppingItem() {
        super();
    }

    public ShoppingItem(String name, ShoppingList list) {
        this.name = name;
        this.alreadyBought = false;
        this.list = list;
    }


}

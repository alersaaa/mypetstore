package org.csu.mypetstorebyssm.domain;

import org.csu.mypetstorebyssm.persistence.ItemMapper;
import org.csu.mypetstorebyssm.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class CartItem {
    private String username;
    private String  itemId;
    private int quantity;
    private boolean inStock;
    private BigDecimal totalcost;
    private boolean pay;
    private Item item;

    public CartItem(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String  getItemId() {
        return itemId;
    }

    public void setItemId(String  itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public BigDecimal getTotalcost() {
        return totalcost;
    }

    public void setTotalcost(BigDecimal totalcost) {
        this.totalcost = totalcost;
    }

    public boolean isPay() {
        return pay;
    }

    public void setPay(boolean pay) {
        this.pay = pay;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

package org.yearup.models.contracts;

import org.yearup.models.ShoppingCartItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Contract {
    private String customerName;
    private List<ShoppingCartItem> items;
    private LocalDateTime date;

    public Contract(String customerName, List<ShoppingCartItem> items) {
        this.customerName = customerName;
        this.items = items;
        this.date = LocalDateTime.now();
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public abstract BigDecimal getSubtotal();
    public abstract BigDecimal getTax();
    public abstract BigDecimal getTotal();
}

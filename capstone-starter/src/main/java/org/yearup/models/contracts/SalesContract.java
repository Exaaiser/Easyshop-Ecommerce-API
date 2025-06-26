package org.yearup.models.contracts;

import org.yearup.models.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.List;

public class SalesContract extends Contract {

    private final BigDecimal TAX_RATE = new BigDecimal("0.06"); // %6 vergi

    public SalesContract(String customerName, List<ShoppingCartItem> items) {
        super(customerName, items);
    }

    @Override
    public BigDecimal getSubtotal() {
        return getItems().stream()
                .map(ShoppingCartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTax() {
        return getSubtotal().multiply(TAX_RATE);
    }

    @Override
    public BigDecimal getTotal() {
        return getSubtotal().add(getTax());
    }
}

package org.yearup.models;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    // productId'ye göre ShoppingCartItem'ları tutan bir Map
    private Map<Integer, ShoppingCartItem> items;
    private BigDecimal total;

    public ShoppingCart() {
        this.items = new HashMap<>();
        this.total = BigDecimal.ZERO;
    }

    public void addItem(ShoppingCartItem newItem) {
        int productId = newItem.getProduct().getProductId();
        if (this.items.containsKey(productId)) {
            // Ürün sepette zaten varsa, miktarını artır
            ShoppingCartItem existingItem = this.items.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
        } else {
            // Ürün sepette yoksa, yeni olarak ekle
            this.items.put(productId, newItem);
        }
        calculateTotal(); // Toplamı her eklemede yeniden hesapla
    }

    public void removeItem(int productId) {
        this.items.remove(productId);
        calculateTotal(); // Toplamı her çıkarmada yeniden hesapla
    }

    // JSON serileştirme için Map'i döndürür
    public Map<Integer, ShoppingCartItem> getItems() {
        return items;
    }

    // Bu metodun sadece iç kullanım için olduğundan ve doğrudan JSON'a serileştirilmediğinden emin olun
    public Collection<ShoppingCartItem> getItemsAsCollection() {
        return items.values();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) { // Set metodu da olmalı, gerekirse
        this.total = total;
    }

    public void calculateTotal() {
        this.total = BigDecimal.ZERO;
        for (ShoppingCartItem item : items.values()) {
            this.total = this.total.add(item.getLineTotal());
        }
    }
}
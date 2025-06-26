package org.yearup.data;

import org.springframework.stereotype.Repository;
import org.yearup.models.ShoppingCartItem;

import java.util.*;

@Repository
public class InMemoryCartDao implements ShoppingCartDao {
    private final Map<String, List<ShoppingCartItem>> carts = new HashMap<>();

    @Override
    public void addToCart(String username, ShoppingCartItem item) {
        List<ShoppingCartItem> cart = carts.getOrDefault(username, new ArrayList<>());

        for (ShoppingCartItem existingItem : cart) {
            if (existingItem.getProductId() == item.getProductId()) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                carts.put(username, cart);
                return;
            }
        }

        cart.add(item);
        carts.put(username, cart);
    }

    @Override
    public void removeFromCart(String username, int productId) {
        List<ShoppingCartItem> cart = carts.get(username);
        if (cart != null) {
            cart.removeIf(item -> item.getProductId() == productId);
        }
    }

    @Override
    public List<ShoppingCartItem> getCartItems(String username) {
        return carts.getOrDefault(username, new ArrayList<>());
    }

    @Override
    public void clearCart(String username) {
        carts.remove(username);
    }
}
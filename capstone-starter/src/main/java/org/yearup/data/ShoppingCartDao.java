package org.yearup.data;

import org.yearup.models.ShoppingCartItem;
import java.util.List;

public interface ShoppingCartDao {
    void addToCart(String username, ShoppingCartItem item);
    void removeFromCart(String username, int productId);
    List<ShoppingCartItem> getCartItems(String username);
    void clearCart(String username);
}
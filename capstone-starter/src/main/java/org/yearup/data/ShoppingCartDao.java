package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartDao {
    void addToCart(String username, int item);
    ShoppingCart removeFromCart(String username, int productId);
    List<ShoppingCartItem> getCartItems(String username);
    void clearCart(String username); // Checkout sırasında lazım


}

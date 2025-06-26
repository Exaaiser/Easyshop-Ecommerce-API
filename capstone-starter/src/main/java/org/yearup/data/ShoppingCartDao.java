package org.yearup.data;

import org.yearup.models.ShoppingCart; // <-- Bu satırı ekleyin
import org.yearup.models.ShoppingCartItem;
import java.util.List;

public interface ShoppingCartDao {
    void addToCart(String username, ShoppingCartItem item);
    void removeFromCart(String username, int productId);
    List<ShoppingCartItem> getCartItems(String username); // Bu metod da muhtemelen ShoppingCart döndürmeli ama mevcut haliyle bırakıyorum
    void clearCart(String username);

    // Kullanıcının tam sepetini döndürür
    ShoppingCart getShoppingCartByUsername(String username); // <-- Bu satırı ekleyin
}
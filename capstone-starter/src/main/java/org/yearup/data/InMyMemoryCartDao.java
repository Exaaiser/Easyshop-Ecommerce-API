package org.yearup.data;

import org.springframework.stereotype.Repository;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Repository // Spring'in bu sınıfı bir bean olarak bulmasını sağlar
public class InMyMemoryCartDao implements ShoppingCartDao {

    // Kullanıcı adlarına göre sepetleri saklayan Map
    private Map<String, ShoppingCart> carts = new HashMap<>();

    @Override
    public void addToCart(String username, ShoppingCartItem newItem) {

        ShoppingCart userCart = getShoppingCartByUsername(username);


        userCart.addItem(newItem);


        carts.put(username, userCart);
    }

    @Override
    public void removeFromCart(String username, int productId) {
        ShoppingCart userCart = carts.get(username);
        if (userCart != null) {

            userCart.removeItem(productId);
        }
    }

    @Override
    public void clearCart(String username) {
        carts.remove(username);
    }

    @Override
    public ShoppingCart getShoppingCartByUsername(String username) {
        ShoppingCart userCart = carts.getOrDefault(username, new ShoppingCart());
        userCart.calculateTotal();
        return userCart;
    }
    //
    @Override
    public List<ShoppingCartItem> getCartItems(String username) {
        // Bu metot ShoppingCartController tarafından doğrudan kullanılmasa da,
        // ShoppingCartDao arayüzünde tanımlı olduğu için implement edilmelidir.
        ShoppingCart userCart = carts.get(username);
        return userCart != null ? new ArrayList<>(userCart.getItems().values()) : new ArrayList<>();
        // Not: ShoppingCart.getItems() artık bir Map döndürüyor olabilir,
        // bu yüzden .values() ile öğe Collection'ını alıp List'e dönüştürüyoruz.
    }
}
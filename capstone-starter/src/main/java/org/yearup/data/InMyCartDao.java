package org.yearup.data;

import org.springframework.stereotype.Repository;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.*;

// @Repository anotasyonu, Spring'in bu sınıfı otomatik olarak tanımasını sağlar.
// Bu sınıf CartDao arayüzünü (interface) implement eder.
@Repository
public class InMyCartDao implements ShoppingCartDao
{
    // Kullanıcı adını (username) anahtar (key) olarak kullanıyoruz.
    // Her kullanıcıya ait bir CartItem listesi (sepeti) olacak.
    private final Map<String, List<ShoppingCartItem>> carts = new HashMap<>();

    @Override
    public void addToCart(String username, int item)
    {
        // Kullanıcının sepetini getir, yoksa yeni oluştur
        List<ShoppingCartItem> cart = carts.getOrDefault(username, new ArrayList<>());

        // Eğer ürün zaten sepette varsa, sadece miktarını artır
        for (ShoppingCartItem existingItem : cart)
        {
            if (existingItem.getProductId() == item.getProductId())
            {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                carts.put(username, cart); // Güncellenmiş listeyi geri koy
                return;
            }
        }

        // Ürün daha önce sepete eklenmemişse, doğrudan listeye ekle
        cart.add(item);
        carts.put(username, cart);
    }

    @Override
    public ShoppingCart removeFromCart(String username, int productId)
    {
        // Kullanıcının sepetini al
        List<ShoppingCartItem> cart = carts.get(username);
        if (cart == null) return null;

        // Ürünü productId'ye göre filtrele ve çıkar
        cart.removeIf(item -> item.getProductId() == productId);
        return null;
    }

    @Override
    public List<ShoppingCartItem> getCartItems(String username)
    {
        // Sepette ürün yoksa boş liste döndür
        return carts.getOrDefault(username, new ArrayList<>());
    }

    @Override
    public void clearCart(String username)
    {
        // Kullanıcının sepetini tamamen sil
        carts.remove(username);
    }
}

package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin // Cross-Origin isteklerine izin verir
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // Sadece yetkili kullanıcılar erişebilir
public class ShoppingCartController {

    private final ShoppingCartDao shoppingCartDao;
    private final ProductDao productDao;

    public ShoppingCartController(ShoppingCartDao shoppingCartDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.productDao = productDao;
    }

    // 1. Sepeti getir (GET /cart)
    @GetMapping("")
    public ShoppingCart getShoppingCart(Principal principal) {
        try {
            String username = principal.getName();
            return shoppingCartDao.getShoppingCartByUsername(username);
        } catch (Exception e) {
            e.printStackTrace(); // Hatanın konsola yazılmasını sağlar
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sepet alınamadı.");
        }
    }

    // 2. Sepete ürün ekle (POST /cart/products/{productId})
    @PostMapping("/products/{productId}")
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal) {
        try {
            String username = principal.getName();
            Product product = productDao.getById(productId);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ürün bulunamadı.");
            }


            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setProduct(product);
            newItem.setQuantity(1); // Varsayılan olarak 1 adet ekle

            shoppingCartDao.addToCart(username, newItem);
            return shoppingCartDao.getShoppingCartByUsername(username); // Güncellenmiş sepeti döndür
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sepete eklenemedi.");
        }
    }

    // 3. Sepetten ürün çıkar (DELETE /cart/products/{productId})
    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Başarılı olursa 204 No Content döndürür
    public void removeFromCart(@PathVariable int productId, Principal principal) {
        try {
            String username = principal.getName();
            shoppingCartDao.removeFromCart(username, productId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ürün çıkarılamadı.");
        }
    }

    // 4. Sepeti tamamen temizle (DELETE /cart)
    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Başarılı olursa 204 No Content döndürür
    public void clearCart(Principal principal) {
        try {
            String username = principal.getName();
            shoppingCartDao.clearCart(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sepet temizlenemedi.");
        }
    }
}
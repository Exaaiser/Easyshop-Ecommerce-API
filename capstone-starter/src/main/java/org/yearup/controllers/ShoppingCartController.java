package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class ShoppingCartController {

    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // 1. Sepetteki ürünleri getir
    @GetMapping("")
    public List<ShoppingCartItem> getCartItems(Principal principal) {
        try {
            String username = principal.getName();
            return shoppingCartDao.getCartItems(username);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sepet alınamadı.");
        }
    }

    // 2. Sepete ürün ekle
    @PostMapping("/products/{productId}")
    public ShoppingCartItem addToCart(@PathVariable int productId, Principal principal) {
        try {
            String username = principal.getName();
            Product product = productDao.getById(productId);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ürün bulunamadı.");
            }

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(1);

            shoppingCartDao.addToCart(username, item);
            return item;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sepete eklenemedi.");
        }
    }

    // 3. Sepetten ürün çıkar
    @DeleteMapping("/products/{productId}")
    public void removeFromCart(@PathVariable int productId, Principal principal) {
        try {
            String username = principal.getName();
            shoppingCartDao.removeFromCart(username, productId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ürün çıkarılamadı.");
        }
    }

    // 4. Sepeti tamamen temizle
    @DeleteMapping("")
    public void clearCart(Principal principal) {
        try {
            String username = principal.getName();
            shoppingCartDao.clearCart(username);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sepet temizlenemedi.");
        }
    }
}

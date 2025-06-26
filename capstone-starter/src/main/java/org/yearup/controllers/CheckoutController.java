package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.User;
import org.yearup.models.contracts.Contract;
import org.yearup.models.contracts.SalesContract;
import org.yearup.utilities.ContractFileManager;

import java.security.Principal;

@RestController
@RequestMapping("/checkout")
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class CheckoutController {

    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;

    public CheckoutController(ShoppingCartDao shoppingCartDao, UserDao userDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    @PostMapping("")
    public Contract checkout(Principal principal) {
        try {
            String username = principal.getName();
            var user = userDao.getByUserName(username);

            var items = shoppingCartDao.getCartItems(username);
            System.out.println("Items count: " + items.size());

            if (items.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sepet boş.");
            }

            Contract contract = new SalesContract(username, items);
            System.out.println("Contract created!");

            ContractFileManager.saveContract(contract);
            System.out.println("Contract saved!");

            shoppingCartDao.clearCart(username);
            System.out.println("Cart cleared!");

            return contract;

        } catch (Exception e) {
            e.printStackTrace(); // Burası kritik!
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Checkout başarısız.");
        }
    }
}

package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.handlers.HelperFunctions;
import com.lambdaschool.shoppingcart.models.Cart;
import com.lambdaschool.shoppingcart.models.Product;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.repositories.UserRepository;
import com.lambdaschool.shoppingcart.services.CartService;
import com.lambdaschool.shoppingcart.services.UserAuditing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carts")
public class CartController
{
    @Autowired
    private CartService cartService;
    @Autowired
    private HelperFunctions helperFunctions;

    @GetMapping(value = "/user", produces = {"application/json"})
    public ResponseEntity<?> listAllCarts()
    {
        String name = helperFunctions.getCurrentAuditor();
        List<Cart> myCarts = cartService.findAllByUsername(name);
        return new ResponseEntity<>(myCarts, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/cart/{cartId}",
            produces = {"application/json"})
    public ResponseEntity<?> getCartById(
            @PathVariable
                    Long cartId)
    {
        Cart p = cartService.findCartById(cartId);
        return new ResponseEntity<>(p,
                                    HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping(value = "/create/user/{userid}/product/{productid}")
    public ResponseEntity<?> addNewCart(@PathVariable long userid,
                                        @PathVariable long productid)
    {
        User dataUser = new User();
        dataUser.setUserid(userid);

        Product dataProduct = new Product();
        dataProduct.setProductid(productid);

        cartService.save(dataUser, dataProduct);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping(value = "/update/cart/{cartid}/product/{productid}")
    public ResponseEntity<?> updateCart(@PathVariable long cartid,
                                        @PathVariable long productid)
    {
        Cart dataCart = new Cart();
        dataCart.setCartid(cartid);

        Product dataProduct = new Product();
        dataProduct.setProductid(productid);

        cartService.save(dataCart, dataProduct);
        return new ResponseEntity<>(helperFunctions.getCurrentAuditor() + " was updated", HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/cart/{cartid}/product/{productid}")
    public ResponseEntity<?> deleteFromCart(@PathVariable long cartid,
                                            @PathVariable long productid)
    {
        Cart dataCart = new Cart();
        dataCart.setCartid(cartid);

        Product dataProduct = new Product();
        dataProduct.setProductid(productid);

        cartService.delete(dataCart, dataProduct);
        return new ResponseEntity<>(helperFunctions.getCurrentAuditor() + " was updated", HttpStatus.OK);
    }
}

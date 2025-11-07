package com.demo.service;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.Product.Product;
import com.demo.model.cart.Cart;
import com.demo.model.cart.CartItem;
import com.demo.model.order.OrderItemDTO;
import com.demo.model.user.User;
import com.demo.repo.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    public Cart findCartByUserId(Long userId) {
        return cartRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
    }

    public List<CartItem> findCartItemsByUserId(Long userId) {
        Cart cart = cartRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, "userId", userId));
        return cart.getCartItems();
    }

    public List<CartItem> findCartItemsByCartId(Long cartId){
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, "cartId", cartId));
        return cart.getCartItems();
    }

    private void addProductToCart(Cart cart, OrderItemDTO cartItem) {
        Long productId = cartItem.getProductId();
        Product product = productService.findProductById(productId);
        cart.addOrUpdateItem(product, cartItem.getQuantity());
    }

    public String addItemsToCartByUserId(List<OrderItemDTO> productQty, Long userId) {
        Cart cart = getOrCreateUserCart(userId);
        for (OrderItemDTO item : productQty) {
            addProductToCart(cart, item);
        }
        cartRepo.save(cart);
        return "Items Added Successfully To Cart";
    }

    public String addItemsToCart(List<OrderItemDTO> productQty, Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, "cartId", cartId));
                        //RuntimeException("Could Not Locate Resource: Cart"));

        for (OrderItemDTO item : productQty) {
            addProductToCart(cart, item);
        }
        cartRepo.save(cart);
        return "Items Added Successfully To Cart";
    }

    public String emptyCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, "cartId", cartId));
        if (cart.isEmpty()) {
            return "Cart is already empty";
        }

        cart.clearItems();
        cartRepo.save(cart);
        return "Cart Emptied Successfully";
    }

    private Cart getOrCreateUserCart(Long userId) {
        return cartRepo.findByUser_UserId(userId)
                .orElseGet(() -> createNewCartForUser(userId));
    }

    private Cart createNewCartForUser(Long userId) {
        User user = userService.findUserById(userId);
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepo.save(newCart);
    }
}

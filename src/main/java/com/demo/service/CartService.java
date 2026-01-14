package com.demo.service;

import com.demo.exception.BadRequestException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.model.address.Address;
import com.demo.model.cart.CartItemConverter;
import com.demo.model.order.Order;
import com.demo.model.order.OrderAddress;
import com.demo.model.product.Product;
import com.demo.model.cart.Cart;
import com.demo.model.cart.CartItem;
import com.demo.model.order.OrderItemDTO;
import com.demo.model.user.User;
import com.demo.model.user.UserPrincipal;
import com.demo.repo.CartRepo;
import com.demo.repo.ProductRepo;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//TODO Use AuthenticationPrincipal instead of userId in CartService
@Service
public class CartService {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    OrderService orderService;

    public Cart returnUserCart(UserPrincipal userPrincipal) {
        User user = userPrincipal.user();
        return cartRepo.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, "userId",  user.getUserId()));
    }

    public List<CartItem> findCartItemsByUserId(UserPrincipal userPrincipal) {
        User user = userPrincipal.user();
        Cart cart = cartRepo.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, "userId", user.getUserId()));
        return cart.getCartItems();
    }

    public List<CartItem> findCartItemsByCartId(Long cartId){
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, "cartId", cartId));
        return cart.getCartItems();
    }

    private void addProductToCart(Cart cart, OrderItemDTO cartItem) {
        Long productId = cartItem.getProductId();
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", productId));
        cart.addOrUpdateItem(product, cartItem.getQuantity());
    }

    public String addItemsToUserCart(UserPrincipal userPrincipal , List<OrderItemDTO> productQty) {
        Long userId = userPrincipal.user().getUserId();
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
        Cart cart = getOrCreateUserCart(user);
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

    private Cart getOrCreateUserCart(User user) {
        return cartRepo.findByUser_UserId(user.getUserId())
                .orElseGet(() -> createNewCartForUser(user));
    }

    private Cart createNewCartForUser(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepo.save(newCart);
    }

    public Order placeCartToOrder(UserPrincipal userPrincipal, Long cartid) {
        Long userId = userPrincipal.user().getUserId();
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
        List<CartItem> cartItems = findCartItemsByCartId(cartid);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cannot place order with empty cart");
        }

        if (user.getAddresses() == null || user.getAddresses().isEmpty()) {
            throw new ResourceNotFoundException(Address.class, "Address List empty", "address");
        }

        List<OrderItemDTO> orderItems = CartItemConverter.toOrderItemDTOSList(cartItems);

        Address userAddress = user.getAddresses().getFirst();
        OrderAddress shippingAddress = new OrderAddress();
        shippingAddress.setStreetAddress(userAddress.getStreetAddress());
        shippingAddress.setPincode(userAddress.getPincode());
        shippingAddress.setCity(userAddress.getCity());
        shippingAddress.setState(userAddress.getState());

        Order order = orderService.createOrder(userPrincipal, orderItems, shippingAddress);
        emptyCart(cartid);
        return order;
    }
}

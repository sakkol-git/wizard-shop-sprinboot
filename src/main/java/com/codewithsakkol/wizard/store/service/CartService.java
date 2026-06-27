package com.codewithsakkol.wizard.store.service;

import com.codewithsakkol.wizard.store.dtos.cart.AddToCardRequest;
import com.codewithsakkol.wizard.store.dtos.cart.CartDto;
import com.codewithsakkol.wizard.store.dtos.cartItem.CartItemDto;
import com.codewithsakkol.wizard.store.entities.Cart;
import com.codewithsakkol.wizard.store.entities.CartItem;
import com.codewithsakkol.wizard.store.entities.Product;
import com.codewithsakkol.wizard.store.exceptions.CartItemNotFoundException;
import com.codewithsakkol.wizard.store.exceptions.CartNotFoundException;
import com.codewithsakkol.wizard.store.exceptions.ProductNotFoundException;
import com.codewithsakkol.wizard.store.mapper.CartMapper;
import com.codewithsakkol.wizard.store.repositories.CartRepository;
import com.codewithsakkol.wizard.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDto createCart(){
        Cart cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }

    public CartItemDto addItemToCart(UUID cartId, Long productId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
           throw  new CartNotFoundException();
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        var cartItemDto = cartMapper.toCartItemDto(cartItem);

        return  cartItemDto;
    }

    public CartDto updateItem(UUID cartId, Long productId, int quantity){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
           throw new CartNotFoundException();
        }

        CartItem cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new CartItemNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return  cartMapper.toCartDto(cart);
    }

    public void  deleteItem(UUID cartId, Long productId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        CartItem cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new CartItemNotFoundException();
        }

        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
           throw  new CartNotFoundException();
        }

        cartRepository.delete(cart);
    }
}

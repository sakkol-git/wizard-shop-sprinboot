package com.codewithsakkol.wizard.store.carts;

import com.codewithsakkol.wizard.store.carts.AddToCardRequest;
import com.codewithsakkol.wizard.store.carts.CartDto;
import com.codewithsakkol.wizard.store.carts.CartItemDto;
import com.codewithsakkol.wizard.store.carts.Cart;
import com.codewithsakkol.wizard.store.carts.CartItem;
import com.codewithsakkol.wizard.store.products.Product;
import com.codewithsakkol.wizard.store.common.ResourceNotFoundException;
import com.codewithsakkol.wizard.store.carts.CartMapper;
import com.codewithsakkol.wizard.store.carts.CartRepository;
import com.codewithsakkol.wizard.store.products.ProductRepository;
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
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        var cartItemDto = cartMapper.toCartItemDto(cartItem);

        return  cartItemDto;
    }

    public CartDto updateItem(UUID cartId, Long productId, int quantity){
        Cart cart = cartRepository.getCartWithItems(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        CartItem cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return  cartMapper.toCartDto(cart);
    }

    public void deleteItem(UUID cartId, Long productId){
        Cart cart = cartRepository.getCartWithItems(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        CartItem cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        cartRepository.delete(cart);
    }
}

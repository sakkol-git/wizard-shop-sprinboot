package com.codewithsakkol.wizard.store.controller;

import com.codewithsakkol.wizard.store.dtos.cart.AddToCardRequest;
import com.codewithsakkol.wizard.store.dtos.cart.CartDto;
import com.codewithsakkol.wizard.store.dtos.cartItem.CartItemDto;
import com.codewithsakkol.wizard.store.dtos.cartItem.UpdateCardItemRequest;
import com.codewithsakkol.wizard.store.entities.Cart;
import com.codewithsakkol.wizard.store.exceptions.CartItemNotFoundException;
import com.codewithsakkol.wizard.store.exceptions.CartNotFoundException;
import com.codewithsakkol.wizard.store.exceptions.ProductNotFoundException;
import com.codewithsakkol.wizard.store.mapper.CartMapper;
import com.codewithsakkol.wizard.store.repositories.CartRepository;
import com.codewithsakkol.wizard.store.repositories.ProductRepository;
import com.codewithsakkol.wizard.store.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var CartDto = cartService.createCart();
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(CartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(CartDto);
    }


    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCard(
            @PathVariable UUID cartId,
            @RequestBody AddToCardRequest addToCardRequest)
    {
        var cartItemDto = cartService.addItemToCart(cartId, addToCardRequest.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);

    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cardId) {
        Cart cart = cartRepository.getCartWithItems(cardId).orElse(null);
        if (cart == null) {
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(cartMapper.toCartDto(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @RequestBody UpdateCardItemRequest request){
        var cartDto = cartService.updateItem(cartId, productId, request.getQuantity());

        return  ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCardItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId)
 {
        cartService.deleteItem(cartId, productId);
        return  ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(
            @PathVariable UUID cartId
    ){
        cartService.clearCart(cartId);
        return  ResponseEntity.noContent().build();

    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String , String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Cart Not Founded"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String , String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Product Not Founded in the cart"));
    }


    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Map<String , String>> handlePCartItemNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "CartItem Not Founded in the cart"));

    }

}

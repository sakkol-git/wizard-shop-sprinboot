package com.codewithsakkol.wizard.store.carts;

import com.codewithsakkol.wizard.store.carts.CartDto;
import com.codewithsakkol.wizard.store.carts.CartItemDto;
import com.codewithsakkol.wizard.store.carts.Cart;
import com.codewithsakkol.wizard.store.carts.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toCartDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toCartItemDto(CartItem cartItem);
}

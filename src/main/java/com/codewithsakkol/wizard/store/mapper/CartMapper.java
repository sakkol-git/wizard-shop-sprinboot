package com.codewithsakkol.wizard.store.mapper;

import com.codewithsakkol.wizard.store.dtos.cart.CartDto;
import com.codewithsakkol.wizard.store.dtos.cartItem.CartItemDto;
import com.codewithsakkol.wizard.store.entities.Cart;
import com.codewithsakkol.wizard.store.entities.CartItem;
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

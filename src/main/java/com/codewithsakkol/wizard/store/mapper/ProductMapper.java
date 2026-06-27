package com.codewithsakkol.wizard.store.mapper;

import com.codewithsakkol.wizard.store.dtos.product.ProductDto;
import com.codewithsakkol.wizard.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto requestProduct);
    void update(ProductDto request, @MappingTarget Product product);
}

package com.codewithsakkol.wizard.store.controller;

import com.codewithsakkol.wizard.store.dtos.product.ProductDto;
import com.codewithsakkol.wizard.store.entities.Category;
import com.codewithsakkol.wizard.store.entities.Product;
import com.codewithsakkol.wizard.store.mapper.ProductMapper;
import com.codewithsakkol.wizard.store.repositories.CategoryRepository;
import com.codewithsakkol.wizard.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import com.codewithsakkol.wizard.store.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;


    @GetMapping
    public List<ProductDto> getAllProduct(@RequestParam(required = false, defaultValue = "", name = "categoryId") Byte categoryId ){
        List<Product> products;
        if (categoryId == null) {
            products = productRepository.findAll();
        }else {
            products = productRepository.findByCategoryId(categoryId);
        }
        return products
                .stream()
                .map(productMapper::toProductDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return ResponseEntity.ok(productMapper.toProductDto(product));
    }
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto requestProduct, UriComponentsBuilder uriBuilder){
        Category category = categoryRepository.findById(requestProduct.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", requestProduct.getCategoryId()));
        Product product = productMapper.toProduct(requestProduct);
        product.setCategory(category);
        productRepository.save(product);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(uri).body(productMapper.toProductDto(product));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto requestProduct){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productMapper.update(requestProduct, product);
        productRepository.save(product);
        return  ResponseEntity.ok(productMapper.toProductDto(product));

    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteProduct(@PathVariable Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
        return  ResponseEntity.ok().build();
    }




}

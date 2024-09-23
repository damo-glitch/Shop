package org.dreamdevwork.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.dreamdevwork.shop.exceptions.ProductNotFoundException;
import org.dreamdevwork.shop.models.Category;
import org.dreamdevwork.shop.models.Product;
import org.dreamdevwork.shop.repositories.CategoryRepository;
import org.dreamdevwork.shop.repositories.ProductRepository;
import org.dreamdevwork.shop.request.AddProductRequest;
import org.dreamdevwork.shop.request.UpdateProductResquest;
import org.dreamdevwork.shop.services.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest addProductRequest) {
        //check if category is found in the DB
        //if yes, set it as the new product category
        //if no, the save it as a new category
        //the set as the new product category
        Category category = Optional.ofNullable(
                categoryRepository.findByName(addProductRequest.getCategory().getName())
        ).orElseGet(
                () -> {
                    Category newCategory = new Category(addProductRequest.getCategory().getName());
                    return categoryRepository.save(newCategory);
                }
        );
        addProductRequest.setCategory(category);
        return productRepository.save(createProduct(addProductRequest, category));
    }

    private Product createProduct(AddProductRequest addProductRequest, Category category){
        return new Product(
                addProductRequest.getName(),
                addProductRequest.getBrand(),
                addProductRequest.getPrice(),
                addProductRequest.getInventory(),
                addProductRequest.getDescription(),
                category
        );
    }

    private Product updateExistingProduct(Product existingProduit, UpdateProductResquest updateProductResuest){
        existingProduit.setName(updateProductResuest.getName());
        existingProduit.setBrand(updateProductResuest.getBrand());
        existingProduit.setPrice(updateProductResuest.getPrice());
        existingProduit.setDescription(updateProductResuest.getDescription());
        existingProduit.setInventory(updateProductResuest.getInventory());


        Category category = categoryRepository.findByName(updateProductResuest.getCategory().getName());
        existingProduit.setCategory(category);
        return existingProduit;
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public Product updateProduct(UpdateProductResquest updateProductResquest, Long idProduct) {
        return productRepository.findById(idProduct)
                .map(exitingProduct -> updateExistingProduct(exitingProduct, updateProductResquest))
                .map(productRepository :: save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }
}

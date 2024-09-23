package org.dreamdevwork.shop.services;

import org.dreamdevwork.shop.models.Product;
import org.dreamdevwork.shop.request.AddProductRequest;
import org.dreamdevwork.shop.request.UpdateProductResquest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest addProductRequest);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(UpdateProductResquest updateProductResquest, Long idProduct);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBand(String category, String brand);
    List<Product>  getProductsByName(String name);
    List<Product> getProductsByBandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

}

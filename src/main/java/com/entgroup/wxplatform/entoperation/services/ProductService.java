package com.entgroup.wxplatform.entoperation.services;


import com.entgroup.wxplatform.entoperation.domain.Product;

public interface ProductService {
    Iterable<Product> listAllProducts();

    Product getProductById(Integer id);

    Product saveProduct(Product product);
}

package com.mpsp.splitstack.estore.ProductService.query.rest;

import com.mpsp.splitstack.estore.ProductService.core.data.ProductEntity;
import com.mpsp.splitstack.estore.ProductService.core.data.ProductsRepository;
import com.mpsp.splitstack.estore.ProductService.query.FindProductsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductsQueryHandler {

    private final ProductsRepository productsRepository;

    public ProductsQueryHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery findProductsQuery) {
        List<ProductEntity> storedProducts = productsRepository.findAll();
        return storedProducts.stream()
                .map(product -> {
                    ProductRestModel restModel = new ProductRestModel();
                    BeanUtils.copyProperties(product, restModel);
                    return restModel;
                })
                .toList();
    }
}

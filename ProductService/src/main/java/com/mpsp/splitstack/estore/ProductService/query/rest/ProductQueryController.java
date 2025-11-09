package com.mpsp.splitstack.estore.ProductService.query.rest;

import com.mpsp.splitstack.estore.ProductService.core.data.ProductPriceOnDatePOJO;
import com.mpsp.splitstack.estore.ProductService.core.data.ProductsRepository;
import com.mpsp.splitstack.estore.ProductService.query.FindProductsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<ProductRestModel> getProducts(){
        return queryGateway.query(new FindProductsQuery(), ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
    }

    @GetMapping("/{productId}/price")
    public ResponseEntity<BigDecimal> getProductPriceOnDate(
            @PathVariable String productId,
            @RequestParam("onDate") String onDate) {
        try {
            // Parse the date in dd/MM/yyyy format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(onDate, formatter);
            ProductPriceOnDatePOJO query = new ProductPriceOnDatePOJO(productId, date);
            BigDecimal price = queryGateway.query(query, BigDecimal.class).join();
            if (price == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(price);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

package com.mpsp.splitstack.estore.ProductService.core.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PRODUCT_LOOKUP")
public class ProductLookUpEntity {

    private static final long serialVersionUID = 1L;
    @Id
    private String productId;
    private String title;
}

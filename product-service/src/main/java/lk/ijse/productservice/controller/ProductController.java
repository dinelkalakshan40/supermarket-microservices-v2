package lk.ijse.productservice.controller;

import lk.ijse.productservice.dto.ProductDTO;
import lk.ijse.productservice.entity.Product;
import lk.ijse.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductDTO saveProduct(@RequestBody ProductDTO productDTO) {
        log.info("Added new Product");
        log.warn("This is a warn example");
        log.error("This is error example");
        return productService.saveProduct(productDTO);
    }
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        log.info("Fetching all products");
        log.debug("Debug: getProducts called");
        return productService.getAllProducts();
    }
    @GetMapping("/{productId}")
    public ProductDTO getProduct(@PathVariable String productId) {
        return productService.getProductById(productId);
    }
    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "Product deleted";
    }

}

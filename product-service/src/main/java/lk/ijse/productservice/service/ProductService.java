package lk.ijse.productservice.service;

import jakarta.transaction.Transactional;
import lk.ijse.productservice.dto.ProductDTO;
import lk.ijse.productservice.entity.Product;
import lk.ijse.productservice.repo.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepo productRepo;

    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product saved = productRepo.save(product);
        return modelMapper.map(saved, ProductDTO.class);
    }
    public List<ProductDTO> getAllProducts() {
        return productRepo.findAll().stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
    }
    public ProductDTO getProductById(int id) {
        Product product =productRepo.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        return modelMapper.map(product, ProductDTO.class);
    }
    public ProductDTO updateProduct(int id,ProductDTO productDTO) {
        Product existingProduct =productRepo.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        existingProduct.setProductId(productDTO.getProductId());
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        Product updatedProduct = productRepo.save(existingProduct);
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }
    public void deleteProduct(int id) {
        Product product = productRepo.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        productRepo.delete(product);
    }

}

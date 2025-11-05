package com.microservice_product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice_product.dto.ProductDTO;
import com.microservice_product.model.Product;
import com.microservice_product.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public List<ProductDTO> findAll(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToDTO).toList();
    }

    //Metodo auxiliar para convertir modelo a DTO
    private ProductDTO convertToDTO(Product product){
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setQuantity(product.getQuantity());
        dto.setPrice(product.getPrice());

        return dto;
    }

    public ProductDTO findById(Long id){

        Product productSearched = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found."));

        ProductDTO productFound = new ProductDTO();

        productFound.setId(productSearched.getId());
        productFound.setName(productSearched.getName());
        productFound.setQuantity(productSearched.getQuantity());
        productFound.setPrice(productSearched.getPrice());

        return productFound;
        
    }

    public ProductDTO saveProduct(ProductDTO productDTO){
        Product newProduct = new Product();

        newProduct.setId(productDTO.getId());
        newProduct.setName(productDTO.getName());
        newProduct.setQuantity(productDTO.getQuantity());
        newProduct.setPrice(productDTO.getPrice());

        Product savedProduct = productRepository.save(newProduct);

        ProductDTO returningProduct = new ProductDTO();

        returningProduct.setId(savedProduct.getId());
        returningProduct.setName(savedProduct.getName());
        returningProduct.setQuantity(savedProduct.getQuantity());
        returningProduct.setPrice(savedProduct.getPrice());

        return returningProduct;
    }

    public ProductDTO updateProduct(Long id, ProductDTO updatedProduct) {
    
        Product productToUpdate = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found."));

        productToUpdate.setName(updatedProduct.getName());
        productToUpdate.setQuantity(updatedProduct.getQuantity());
        productToUpdate.setPrice(updatedProduct.getPrice());

        Product savedProduct = productRepository.save(productToUpdate);

        ProductDTO returningProduct = new ProductDTO();

        returningProduct.setId(savedProduct.getId());
        returningProduct.setName(savedProduct.getName());
        returningProduct.setQuantity(savedProduct.getQuantity());
        returningProduct.setPrice(savedProduct.getPrice());

        return returningProduct;

    }

    public void deleteById(Long id){

        productRepository.deleteById(id);
    }

    public int getStockById(Long id){
        Product productToConsult = productRepository.findById(id).get();

        return productToConsult.getQuantity();
    }


   public ProductDTO getProductsByIds(Long id) {
        Product productToGet = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        ProductDTO product = new ProductDTO();

        product.setId(productToGet.getId());
        product.setName(productToGet.getName());
        product.setQuantity(productToGet.getQuantity());
        product.setPrice(productToGet.getPrice());

        return product;
    }


}

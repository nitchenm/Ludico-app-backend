package com.microservice_product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.microservice_product.dto.ProductDTO;
import com.microservice_product.model.Product;
import com.microservice_product.repository.ProductRepository;
import com.microservice_product.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {


    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void testFindAll(){
        when(productRepository.findAll()).thenReturn(List.of(new Product((long)1, "Patata", 20, 2000)));

        List<ProductDTO> products = productService.findAll();
        assertNotNull(products);
        assertEquals(1, products.size());
    }

    @Test
    public void testFindById(){
        Long id = (long)1;
        Product product = new Product(id, "Patata", 20, 2000);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        ProductDTO found = productService.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }


    @Test
    public void testSave(){
        
        //Instanciamos el DTO con el que vamos a trabajar
        ProductDTO productDTO = new ProductDTO((long)1, "Patata", 20,2000);
        

        //Se crea el producto que se va aguardar
        Product productToSave = new Product();

        //Le pasamos los valores del DTO
        productToSave.setId(productDTO.getId());
        productToSave.setName(productDTO.getName());
        productToSave.setQuantity(productDTO.getQuantity());
        productToSave.setPrice(productDTO.getPrice());

        //Creamos el producto que se supone que va a devolver la base de dato
        Product savedProduct = new Product();

        //Le pasamos manualmente los valores que ya estan establecidos
        savedProduct.setId((long)1);
        savedProduct.setName("Patata");
        savedProduct.setQuantity(20);
        savedProduct.setPrice(2000);

        //DEfinimos el comportamiento del mock
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        //llamamos al propio metodo del service con nuestro DTO
        ProductDTO productResult = productService.saveProduct(productDTO);

        //Hacemos las validaciones
        assertNotNull(productResult);
        assertEquals("Patata", productResult.getName());
        assertEquals(20, productResult.getQuantity());
        assertEquals(2000.0, productResult.getPrice());
    }

    @Test
    public void testDeleteById(){
        Long id = (long)1;
        doNothing().when(productRepository).deleteById(id);

        productService.deleteById(id);
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetStockById(){
        //genero el id del producto que voy a buscar
        Long id = (long) 1;

        //Genero el producto para buscar
        Product product = new Product();

        //Le seteo el id generado
        product.setId(id);

        //Le seteo un stock para la prueba
        product.setQuantity(20);

        //Se define el comportamiento del mock usando el prducto creado anteriormente
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        //Se llama al metodo real del service y se captura el resultado
        int stock = productService.getStockById(id);

        //se verifica que no sea nulo
        assertNotNull(product);

        //Y por ultimo, se verifica que el stock asignado sea igual al stock buscado
        assertEquals(20, stock);
    }

    @Test
    public void testUpdateProduct(){
        Long id = (long) 1;

        //primero es necesario crear el producto original que va a ser updateado
        Product existingProduct = new Product();
        
        //Le seteamos el id que ya se definio arriba y los valores que ya tiene de base
        // y que posteriormente van a ser updateados
        existingProduct.setId(id);
        existingProduct.setName("Patata");
        existingProduct.setQuantity(20);
        existingProduct.setPrice(20000);

        //Luego creamos el producto con los nuevos valores para updatear a
        //los valores del producto que ya existe

        ProductDTO updatedProduct = new ProductDTO();

        //le seteamos los nuevos valores menos el id porque ese no cambia
        updatedProduct.setName("papa");
        updatedProduct.setQuantity(30);
        updatedProduct.setPrice(25000);


        //aca primero debemos llamar al findById con los datos del producto que ya existe
        //para que el metodo sepa que existe un producto con ese id que es el que 
        //posteriormente va a ser updateado

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));

        //Y aca decimos que cuando el repositorio guarde cualquier instancua de la clase producto
        //Se va a retornar el mismo objeto de producto que se le paso como argumento al metodo save
        //Que en este caso seria el producto ya updateado
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //llamamos al propio metodo del service
        ProductDTO productToUpdate = productService.updateProduct(id, updatedProduct);

        
        //Se hacen las validaciones para verificar que los datos fueron cambiados

        //Se verifica que el nuevo nombre sea igual al nombre del produto updateado
        assertEquals("papa", productToUpdate.getName());

        //Se verifica el stock
        assertEquals(30, productToUpdate.getQuantity());

        //Se verifica el precio
        assertEquals(25000, productToUpdate.getPrice());

        //Se verifica que se llamo una vez a findById
        verify(productRepository).findById(id);

        // y por ultimo se verifica que se guardaron los cambios
        verify(productRepository).save(existingProduct);

    }

    @Test
    public void testGetProductById() {
        
        Long id = (long) 1;

        Product product = new Product();
        product.setId(id);
        product.setName("Patata");
        product.setQuantity(20);
        product.setPrice(20000);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        
        ProductDTO result = productService.getProductsByIds(id);

        
        assertEquals(id, result.getId());
        assertEquals("Patata", result.getName());
        assertEquals(20, result.getQuantity());
        assertEquals(20000, result.getPrice());
    }



}

package br.com.suafeira.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suafeira.service.ProductService;
import br.com.suafeira.to.ProductTO;
import br.com.suafeira.to.form.ProductForm;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping(value = "insert")
	@CacheEvict(value = "findProducts", allEntries = true)
	public ResponseEntity<?> register(@RequestBody ProductForm productForm) {
		productService.save(productForm.convertToProduct());
		return new ResponseEntity<>(HttpStatus.CREATED); 		
	}
	
	@GetMapping
	@Cacheable(value = "findProducts")
	public ResponseEntity<?> findAll() {
		List<ProductTO> products = productService.findAll();
		products.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
}

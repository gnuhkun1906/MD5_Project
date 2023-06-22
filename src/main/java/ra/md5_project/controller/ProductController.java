package ra.md5_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import ra.md5_project.dto.response.ProductDTO;
import ra.md5_project.dto.response.ResponseMessage;
import ra.md5_project.model.Category;
import ra.md5_project.model.Product;
import ra.md5_project.model.Store;
import ra.md5_project.model.Users;
import ra.md5_project.security.userPrincipal.UserDetailService;
import ra.md5_project.service.IService.ICategoryService;
import ra.md5_project.service.IService.IProductService;
import ra.md5_project.service.IService.IStoreService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProductController {
    private final IProductService productService;
    private final ICategoryService categoryService;
    private final UserDetailService userDetailService;
    private final IStoreService storeService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(Pageable pageable,
                                     @RequestParam("sortBy") Optional<String> sort,
                                     @RequestParam("order") Optional<String> order,
                                     @RequestParam("search") Optional<String> search
    ) {
        Page<Product> listProduct=null;
        Sort sortable = null;
        if (sort.isPresent()) {
            if (sort.get().equals("productName")) {
                if (!order.isPresent()) {
                    sortable = Sort.by("productName").ascending();
                }else if (order.get().equals("DESC")){
                    sortable = Sort.by("productName").descending();
                }
            } else if (sort.get().equals("price")) {
                if (!order.isPresent()) {
                    sortable = Sort.by("price").ascending();
                }else if (order.get().equals("DESC")){
                    sortable = Sort.by("price").descending();
                }
            } else if (sort.get().equals("createdDate")) {
                if (order.isPresent()) {
                    sortable = Sort.by("createdDate").ascending();
                }else if (order.get().equals("DESC")){
                    sortable = Sort.by("createdDate").descending();
                }
            }
        } else {
            sortable = Sort.by("productName").ascending();
        }
        assert sortable != null;
        pageable = PageRequest.of(pageable.getPageNumber(), 2, sortable);
        if (search.isPresent()) {
            listProduct=productService.findByProductNameContaining(search.get(),pageable);
        } else {
            listProduct = productService.findProductByStatus(true,pageable);
            for (Product p:listProduct) {
                if (p.getQuantity() == 0) {
                    p.setStock(false);
                    productService.save(p);
                }
            }
        }
        return new ResponseEntity<>(listProduct, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        if (product.getCreatedDate() == null) {
            product.setCreatedDate(LocalDate.now().toString());
        }
        Long id= product.getCategory().getId();
        Optional<Category> category= categoryService.findById(id);
        if (!category.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Category Not Found!!",null)
            );
        }
        product.setCategory(category.get());
        Users users = userDetailService.getUserFromAuthentication();
        Store store= users.getStore();
        product.setStore(store);
        product.setStatus(false);
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    @GetMapping("/findProductByStatus")
    @PreAuthorize("hasAuthority('ROLE_SM')")
    public ResponseEntity<?> findProductByStatus(Pageable pageable) {
        Page<Product> listProduct = productService.findProductByStatus(false,pageable);
        return new  ResponseEntity<>(listProduct,HttpStatus.OK);
    }


    @PutMapping("/confirmStatus")
    @PreAuthorize("hasAuthority('ROLE_SM')")
    public ResponseEntity<?> confirmStatus(@RequestParam("storeId") Long storeId,
                                           @RequestParam("productId") Long productId) {
        Optional<Store> store  = storeService.findById(storeId);
        if (!store.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Store not found !!", null)
            );
        }
        Optional<Product> optionalProduct  = productService.findById(productId);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Product not found !!", null)
            );
        }
        List<Product> list = store.get().getListProduct();
        if (!list.contains(optionalProduct.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Product is not in the Store !!", null)
            );
        }
        optionalProduct.get().setStatus(true);
        productService.save(optionalProduct.get());
        return ResponseEntity.ok("Product is confirm successfully!!");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SELLER')")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        Users users = userDetailService.getUserFromAuthentication();
        Store store=users.getStore();
        Optional<Product> optionalProduct = productService.findById(id);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed","Product Not Found!!", null)
            );
        }
        List<Product> list = store.getListProduct();
        if (!list.contains(optionalProduct.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed","Product is not in the Store !!", null)
            );
        }
        product=Product.builder()
                .id(optionalProduct.get().getId())
                .productName(product.getProductName()==null?optionalProduct.get().getProductName(): product.getProductName())
                .tittle(product.getTittle()==null?optionalProduct.get().getTittle():product.getTittle())
                .description(product.getDescription()==null?optionalProduct.get().getDescription():product.getDescription())
                .quantity(product.getQuantity()==0?optionalProduct.get().getQuantity():product.getQuantity())
                .price(product.getPrice()==0?optionalProduct.get().getPrice():product.getPrice())
                .createdDate(product.getCreatedDate()==null?optionalProduct.get().getCreatedDate():product.getCreatedDate())
                .status(product.isStatus()==false?optionalProduct.get().isStatus():product.isStatus())
                .stock(product.isStock()==false?optionalProduct.get().isStock():product.isStock())
                .category(product.getCategory()==null?optionalProduct.get().getCategory():product.getCategory())
                .listImage(product.getListImage()==null?optionalProduct.get().getListImage():product.getListImage())
                .store(product.getStore()==null?optionalProduct.get().getStore():product.getStore())
                .build();
        return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
    }

    @PutMapping("/changeStatus/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SELLER')")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id) {
        Users users = userDetailService.getUserFromAuthentication();
        Store store=users.getStore();
        Optional<Product> optionalProduct = productService.findById(id);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed","Product Not Found", null)
            );
        }
        List<Product> list = store.getListProduct();
        if (!list.contains(optionalProduct.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed","Product is not in the Store !!", null)
            );
        }
//        product.setId(optionalProduct.get().getId());
        optionalProduct.get().setStatus(!optionalProduct.get().isStatus());
        return new ResponseEntity<>(productService.save(optionalProduct.get()), HttpStatus.OK);
    }

}

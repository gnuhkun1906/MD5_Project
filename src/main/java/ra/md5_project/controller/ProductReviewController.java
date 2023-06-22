package ra.md5_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import ra.md5_project.dto.response.ResponseMessage;
import ra.md5_project.model.Product;
import ra.md5_project.model.ProductReviews;
import ra.md5_project.model.Users;
import ra.md5_project.security.userPrincipal.UserDetailService;
import ra.md5_project.service.IService.IProductReviewService;
import ra.md5_project.service.IService.IProductService;
import ra.md5_project.service.IService.IUserService;

import javax.persistence.SequenceGenerators;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/productReview")
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProductReviewController {

    private final IProductReviewService productReviewService;
    private final IProductService productService;
    private final IUserService userService;
    private final UserDetailService userDetailService;

    @GetMapping("/findAll/{id}")
    public ResponseEntity<?> findAllReviewByProduct(@PathVariable("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Product Not Found", "")
            );
        }

        List<ProductReviews> listProductReview =
                productReviewService.findProductReviewsByProductId(product.get().getId());
        return new ResponseEntity<>(listProductReview, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> postReview(@RequestBody ProductReviews productReviews) {
        Users users= userDetailService.getUserFromAuthentication();
        productReviews.setUser(users);
        Product product = productReviews.getProduct();
        Optional<Product> productOptional = productService.findById(product.getId());
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Product not found!!", "")
            );
        }
        productReviews.setProduct(productOptional.get());
        productReviews.setCreatedDate(LocalDate.now().toString());
        return new ResponseEntity<>(productReviewService.save(productReviews), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable("id") Long id) {
        Optional<ProductReviews> pro = productReviewService.findById(id);
        if (!pro.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Review Not Found!!", "")
            );
        }
        productReviewService.delete(id);
        return ResponseEntity.ok(
                new ResponseMessage("Ok", "Delete Successfully", "")
        );
    }
}

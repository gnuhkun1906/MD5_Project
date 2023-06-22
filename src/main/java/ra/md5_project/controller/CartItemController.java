package ra.md5_project.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ra.md5_project.dto.response.ResponseMessage;
import ra.md5_project.model.CartItems;
import ra.md5_project.model.Product;
import ra.md5_project.model.Users;
import ra.md5_project.security.userPrincipal.UserDetailService;
import ra.md5_project.service.IService.ICartItemService;
import ra.md5_project.service.IService.IProductService;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("api/cart")
@RequiredArgsConstructor

public class CartItemController {

    private final ICartItemService cartItemService;
    private final IProductService productService;
    private final UserDetailService userDetailService;

    @GetMapping("/findCart")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> findAll(Pageable pageable) {
        Long userId = userDetailService.getUserFromAuthentication().getId();
        List<CartItems> listCartItem = cartItemService.findCartItemsByUserId(userId);
        List<Product> listProduct = (List<Product>) productService.findProducts();
        for (CartItems c : listCartItem) {
            for (Product p : listProduct) {
                if (c.getProduct().getId() == p.getId()) {
                    c.setPrice(p.getPrice());
                }
            }
        }
        return new ResponseEntity<>(listCartItem, HttpStatus.OK);
    }

    @PostMapping("/addToCart")
    @PreAuthorize("hasAuthority('ROLE_USER')")
//    @PreAuthorize("permitAll()")
    public ResponseEntity<?> addToCart(@RequestBody CartItems cartItems) {
//        List<CartItems> listCartItem = (List<CartItems>) cartItemService.findCartItems();
        Users users = userDetailService.getUserFromAuthentication();
        Product product = cartItems.getProduct();
        Long productId = product.getId();
        Optional<Product> products = productService.findById(productId);
        if (!products.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Product does not exist", "")
            );
        }
        if (product.isStock() == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed","The product is out of stock ",null)
            );
        }
        cartItems.setProduct(products.get());
        cartItems.setPrice(products.get().getPrice());
        cartItems.setUser(users);
        // Check quantity
        Optional<CartItems> cartItemsOptional = cartItemService.findCartItemsByProductId(productId);
        if (!cartItemsOptional.isPresent()) {
//            cartItems.setPrice(products.get().getPrice());
            cartItemService.save(cartItems);
            return ResponseEntity.ok(
                    new ResponseMessage(
                            "OK", "AddToCart Successfully", cartItems
                    )
            );
        } else {
            cartItemsOptional.get().setQuantity(cartItemsOptional.get().getQuantity() + 1);
            cartItemsOptional.get().setPrice(cartItemsOptional.get().getQuantity() * products.get().getPrice());
            cartItemService.save(cartItemsOptional.get());
            return ResponseEntity.ok(
                    new ResponseMessage(
                            "OK", "AddToCart Successfully", cartItemsOptional.get()
                    )
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteCart(@PathVariable("id") Long id) {
        Users user = userDetailService.getUserFromAuthentication();
        List<CartItems> listCartItem = cartItemService.findCartItemsByUserId(user.getId());
        Optional<CartItems> cartItemsOptional = cartItemService.findById(id);
        if (!cartItemsOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "CartItem not found", null)
            );
        }
        if (!listCartItem.contains(cartItemsOptional.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "CartItem not found", null)
            );
        }
        cartItemService.delete(id);
        return ResponseEntity.ok(
                new ResponseMessage("Ok", "Delete Successfully", "")
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> updateQuantity(@PathVariable("id") Long id, @RequestBody CartItems cartItems) {
        Users user = userDetailService.getUserFromAuthentication();
        Optional<CartItems> cartItemsOptional = cartItemService.findById(id);
        List<CartItems> listCartItem = cartItemService.findCartItemsByUserId(user.getId());
        if (!cartItemsOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "CartItem not found", null)
            );
        }
        if (!listCartItem.contains(cartItemsOptional.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "CartItem not found", null)
            );
        }

        cartItems.setId(cartItemsOptional.get().getId());
        cartItems.setUser(user);
        cartItems.setProduct(cartItemsOptional.get().getProduct());
        cartItems.setPrice(cartItems.getQuantity() * cartItemsOptional.get().getProduct().getPrice());
        cartItemService.save(cartItems);
        return ResponseEntity.ok(
                new ResponseMessage("Ok", "Update Successfully", cartItems)
        );
    }


}

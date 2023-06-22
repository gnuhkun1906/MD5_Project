package ra.md5_project.controller;

import com.sun.org.apache.regexp.internal.RE;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ra.md5_project.dto.response.ResponseMessage;

import ra.md5_project.model.*;
import ra.md5_project.security.userPrincipal.UserDetailService;
import ra.md5_project.service.IService.*;
import ra.md5_project.service.serviceIPM.OrderDetailService;


import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor

public class OrderController {
    private final IOrderService orderService;
    private final IUserService userService;
    private final ICartItemService cartItemService;
    private final IOrderDetailService orderDetailService;
    private final IProductService productService;
    private final UserDetailService userDetailService;

    @GetMapping("/findAllByStore")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<?> findOrderByStoreId() {
        Users users = userDetailService.getUserFromAuthentication();
        Store store = users.getStore();
        Long storeId = store.getId();
        List<Order> listOrder = orderService.findOrderByStoreId(storeId);
        return new ResponseEntity<>(listOrder, HttpStatus.OK);
    }

    @GetMapping("/findAllByUser")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> findOrderByUser() {
        Users users = userDetailService.getUserFromAuthentication();
//        pageable=PageRequest.of(pageable.getPageNumber(),4,Sort.by("status").ascending());
        List<Order> listOrder = users.getOrder();
        return new ResponseEntity<>(listOrder, HttpStatus.OK);
    }


    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> detailOrder(@PathVariable("id") Long id, Pageable pageable) {
        Users user = userDetailService.getUserFromAuthentication();
        List<Order> listOrderByUSer = user.getOrder();
        Optional<Order> orderOptional = orderService.findById(id);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Order not found!!", "")
            );
        }
        if (!listOrderByUSer.contains(orderOptional.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Order is not in your OrderList", null)
            );
        }
        pageable = PageRequest.of(pageable.getPageNumber(), 4, Sort.by("status").ascending());
        List<Order> listOrder = orderService.findOrderByUserId(orderOptional.get().getUser().getId(), pageable);
        return new ResponseEntity<>(listOrder, HttpStatus.OK);
    }

//    @GetMapping("/detailByStore/{id}")
//    @PreAuthorize("hasAuthority('ROLE_SELLER')")
//    public ResponseEntity<?> detailByStore(@PathVariable("id") Long id) {
//
//    }


    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> checkout(@RequestBody Order order) {
        Users users = userDetailService.getUserFromAuthentication();
        order.setUser(users);
        List<CartItems> listCartItems = cartItemService.findCartItemsByUserId(users.getId());
        if (listCartItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed", "You Cannot Order because ListCartItem Empty!!", null)
            );
        }
        float sumTotalPrice = cartItemService.sumPriceByUserId(users.getId());
        order.setTotalPrice(sumTotalPrice);
        order.setStatus(1);
        order.setCreatedDate(LocalDate.now().toString());
        // Check Time Receiver
        if (order.getTimeReceiver() == null || order.getTimeReceiver().trim().equals("")) {
            order.setTimeReceiver(LocalDate.now().plusDays(2).toString());
        } else {
            if (LocalDate.now().isBefore(LocalDate.parse(order.getTimeReceiver()))) {
                order.setTimeReceiver(order.getTimeReceiver());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseMessage("Failed", "TimeReceiver incorrect!", "")
                );
            }
        }
        List<CartItems> listCartItem = cartItemService.findCartItemsByUserId(users.getId());
       List<Product> listProduct = (List<Product>) productService.findProducts();
        for (CartItems c : listCartItem) {
            for (Product p: listProduct) {
                if (c.getProduct().getId() == p.getId()) {
                    if (p.isStock()==false) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                new ResponseMessage("Failed","Product is SoldOut!!",null)
                        );
                    }
                    if (c.getQuantity() > p.getQuantity()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                new ResponseMessage("Failed","The quantity you want to order is larger than the quantity in stock",null)
                        );
                    }else {
                        p.setQuantity(p.getQuantity() - c.getQuantity());
                    }
                }
            }
        }

        orderService.save(order);
        for (CartItems c : listCartItem) {
            OrderDetail orderDetail = new OrderDetail().builder()
                    .price(c.getPrice())
                    .product(productService.findById(c.getProduct().getId()).get())
                    .quantity(c.getQuantity())
                    .order(orderService.findById(order.getId()).get())
                    .build();
            orderDetailService.save(orderDetail);


            cartItemService.delete(c.getId());
        }
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/confirm/{id}")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<?> confirmStatus(@PathVariable("id") Long id) {
        Optional<Order> orderOptional = orderService.findById(id);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed", "Order not found!!", "")
            );
        }

        if (orderOptional.get().getStatus() == 1) {
            orderOptional.get().setStatus(2);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed", "Order Confirmed!!", "")
            );
        }
        return new ResponseEntity<>(orderService.save(orderOptional.get()), HttpStatus.OK);
    }
//    @PutMapping("/change/{id}")


    @DeleteMapping("/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Transactional
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id) {
        Optional<Order> orderOptional = orderService.findById(id);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed", "Order not found!!", "")
            );
        }
        Users users = userDetailService.getUserFromAuthentication();
        List<Order> listOrder = users.getOrder();
        if (!listOrder.contains(orderOptional.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Order is not in your ListOrder", null)
            );
        }
        if (orderOptional.get().getStatus() == 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed", "Orders are being delivered!", "")
            );
        } else if (orderOptional.get().getStatus() == 1) {
            orderDetailService.deleteOrderDetailByOrder_Id(orderOptional.get().getId());
            orderService.delete(orderOptional.get().getId());
            return ResponseEntity.ok(
                    new ResponseMessage("OK", "Orders Cancel Successfully", "")
            );
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

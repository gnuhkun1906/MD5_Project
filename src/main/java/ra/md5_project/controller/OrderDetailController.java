package ra.md5_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import ra.md5_project.dto.response.ResponseMessage;
import ra.md5_project.model.*;
import ra.md5_project.security.userPrincipal.UserDetailService;
import ra.md5_project.service.IService.ICartItemService;
import ra.md5_project.service.IService.IOrderDetailService;
import ra.md5_project.service.IService.IOrderService;
import ra.md5_project.service.serviceIPM.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/orderDetail")
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    private final IOrderService orderService;
    private final ICartItemService cartItemService;
    private final UserDetailService userDetailService;
    private final RoleService roleService;

    @GetMapping("/findByOrderId/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> findOrderDetailByOrderId(Pageable pageable, @PathVariable("id") Long id) {
        Users user = userDetailService.getUserFromAuthentication();
//        Set<Roles> setRole = user.getListRoles();
        Optional<Order> optionalOrder = orderService.findById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Order Not Found", "")
            );
        }
            List<Order> listOrder = user.getOrder();
            if (!listOrder.contains(optionalOrder.get())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseMessage("Failed", "Order is not in your OrderList", null)
                );
            }
        List<OrderDetail> listOrderDetail = orderDetailService.findOrderDetailsByOrderId(optionalOrder.get().getId());
        return new ResponseEntity<>(listOrderDetail, HttpStatus.OK);
    }


    @GetMapping("/findOrderDetailByStoreId")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<?> findByStore() {
        Users user= userDetailService.getUserFromAuthentication();
        Long storeId= user.getStore().getId();
        List<OrderDetail> listOrderDetail = orderDetailService.findOrderDetailByOrOrderIdAndStoreId(storeId);
        return new ResponseEntity<>(listOrderDetail,HttpStatus.OK);
    }



}

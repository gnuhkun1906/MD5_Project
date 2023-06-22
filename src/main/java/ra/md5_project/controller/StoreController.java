package ra.md5_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import ra.md5_project.dto.response.ResponseMessage;
import ra.md5_project.dto.response.ResponseStore;
import ra.md5_project.model.ERole;
import ra.md5_project.model.Roles;
import ra.md5_project.model.Store;
import ra.md5_project.model.Users;
import ra.md5_project.security.userPrincipal.UserDetailService;
import ra.md5_project.service.IService.IRoleService;
import ra.md5_project.service.IService.IStoreService;
import ra.md5_project.service.IService.IUserService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor

public class StoreController {
    private final IStoreService storeService;
    private final UserDetailService userDetailService;
    private final IRoleService roleService;
    private final IUserService userService;

    @GetMapping("/findAll")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> findAll(Pageable pageable) {
        Iterable<Store> listStore = storeService.findAll(pageable);
        return new ResponseEntity<>(listStore, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailStore(@PathVariable("id") Long id) {
        Optional<Store> optionalStore = storeService.findById(id);
        if (!optionalStore.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Failed", "Your account already has a store!!", ""));
        } else {
            return ResponseEntity.ok(
                    new ResponseStore().builder()
                            .id(optionalStore.get().getId())
                            .storeName(optionalStore.get().getStoreName())
                            .address(optionalStore.get().getAddress())
                            .phone(optionalStore.get().getPhone())
                            .status(optionalStore.get().isStatus())
                            .user(optionalStore.get().getUser())
                            .listProduct(optionalStore.get().getListProduct())
                            .build()
            );
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> createStore(@RequestBody Store store) {
        Users user = userDetailService.getUserFromAuthentication();
        Optional<Store> optionalStore = storeService.findStoreByUserId(user.getId());
        if (optionalStore.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Failed", "Your account already has a store!!", ""));
        } else {
            store.setStatus(true);
            store.setUser(user);
            Store newStore = storeService.save(store);
            Set<Roles> setRole = user.getListRoles();
            setRole.add(roleService.findByRoleName(ERole.valueOf("ROLE_SELLER")).get());
            user.setStore(newStore);
            userService.save(user);
            return new ResponseEntity<>(newStore, HttpStatus.CREATED);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<?> updateStore(@PathVariable("id") Long id, @RequestBody Store store) {
        Optional<Store> storeOptional = storeService.findById(id);
        if (!storeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Store with Id not found!!", "")
            );
        }
        store.setId(id);
        store.setUser(storeOptional.get().getUser());
        return ResponseEntity.ok(
                new ResponseMessage("Ok", "Update successfully", storeService.save(store))
        );
    }

    @PutMapping("/changeStatus/{id}")
    @PreAuthorize("hasAuthority('ROLE_SM')")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id) {
        Optional<Store> storeOptional = storeService.findById(id);
        if (!storeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Store with Id not found!!", "")
            );
        }
        storeOptional.get().setStatus(!storeOptional.get().isStatus());
        storeOptional.get().setUser(storeOptional.get().getUser());
        return ResponseEntity.ok(
                new ResponseMessage("Ok", "Update successfully", storeService.save(storeOptional.get()))
        );
    }


}

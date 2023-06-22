package ra.md5_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.md5_project.dto.response.ResponseMessage;
import ra.md5_project.model.Category;
import ra.md5_project.model.Users;
import ra.md5_project.security.userPrincipal.UserPrincipal;
import ra.md5_project.service.IService.ICategoryService;
import ra.md5_project.service.serviceIPM.EmailSenderService;
import ra.md5_project.service.serviceIPM.UserService;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/category")
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CategoryController {
    private final ICategoryService categoryService;


    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), 10);
        Page<Category> listCategory = categoryService.findAll(pageable);
        return new ResponseEntity<>(listCategory, HttpStatus.OK);
    }

    @PostMapping("/create")
//    @Secured({"ROLE_ADMIN", "ROLE_SM"})
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SM')")
    public ResponseEntity<?> createCategory(@RequestBody Category category)  {
        return ResponseEntity.ok(
                new ResponseMessage("OK","Create Successfully",categoryService.save(category))
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SM')")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed","Category Not Found!!",null)
            );
        }
        category.setId(categoryOptional.get().getId());
        return ResponseEntity.ok(
                new ResponseMessage("Ok","Update Successfully",categoryService.save(category))
        );
    }

    @PutMapping("/changeStatus/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SM')")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                   new ResponseMessage("Failed", "Category Not Found!!", null)
           );
        }
        categoryOptional.get().setStatus(!categoryOptional.get().isStatus());
        return ResponseEntity.ok(
                new ResponseMessage("Ok", "Change Status Successfully",categoryService.save(categoryOptional.get()))
        );
    }


}

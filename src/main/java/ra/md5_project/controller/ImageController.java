package ra.md5_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ra.md5_project.dto.response.ResponseData;
import ra.md5_project.dto.response.ResponseMessage;
import ra.md5_project.model.Image;
import ra.md5_project.model.Product;
import ra.md5_project.service.IService.IImageService;
import ra.md5_project.service.IService.IProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor

public class ImageController {

    private final IImageService imageService;
    private final IProductService productService;

    @PostMapping("/uploadFile")
// @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public List<ResponseData> uploadFile(@RequestParam("file") MultipartFile[] files) throws Exception {
        List<Image> savedImages = imageService.save(files);
        List<ResponseData> listResponse = new ArrayList<>();

        for (int i = 0; i < savedImages.size(); i++) {
            Image image = savedImages.get(i);
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(String.valueOf(image.getId()))
                    .toUriString();

            listResponse.add(new ResponseData(
                    image.getId(),
                    image.getImage(),
                    downloadUrl,
                    files[i].getContentType(),
                    files[i].getSize()
            ));
        }

        return listResponse;
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Image image= null;
        image= imageService.getImage(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "image;fileName=\""+image.getImage()+"\"")
                .body(new ByteArrayResource(image.getData()));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateImage(@PathVariable("id") Long id,@RequestBody Image image) {
        Optional<Image> imageOptional = imageService.findById(id);
        if (!imageOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Image not Found", null)
            );
        }
        Optional<Product> product = productService.findById(image.getProduct().getId());
        if (!product.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseMessage("Failed", "Product not Found", null)
            );
        }
//        image.setId(imageOptional.get().getId());
        Image image1 = Image.builder()
                .id(imageOptional.get().getId())
                .fileType(image.getFileType()==null?imageOptional.get().getFileType() :image.getFileType())
                .image(image.getImage()==null?imageOptional.get().getImage() : image.getImage())
                .data(image.getData()==null ? imageOptional.get().getData() : image.getData())
                .product(image.getProduct()== null ?imageOptional.get().getProduct() : image.getProduct())
                .productReviews(image.getProductReviews()==null?imageOptional.get().getProductReviews() : image.getProductReviews())
                .build();
        imageService.save(image1);
        return ResponseEntity.status(HttpStatus.OK).body("Update successfully!!!");
    }

}

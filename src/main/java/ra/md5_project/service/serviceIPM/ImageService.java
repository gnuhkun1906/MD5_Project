package ra.md5_project.service.serviceIPM;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ra.md5_project.model.Image;
import ra.md5_project.repository.IImageRepository;
import ra.md5_project.service.IService.IImageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final IImageRepository iImageRepository;
    @Override
    public List<Image> save(MultipartFile[] files) throws Exception {
        List<Image> listImage= new ArrayList<>();
        for (MultipartFile file: files) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if (fileName.contains("..")) {
                    throw  new Exception("FileName contains invalid path sequence "+fileName);
                }
                Image image= Image.builder()
                        .image(fileName)
                        .fileType(file.getContentType())
                        .data(file.getBytes())
                        .build();
                 listImage.add(iImageRepository.save(image));
            } catch (Exception e) {
                throw  new Exception("Could Not Save File: "+ fileName);
            }
        }
        return listImage;
    }

    @Override
    public Image getImage(String fileId) throws Exception {
        return iImageRepository.findById(Long.valueOf(fileId))
                .orElseThrow(
                        ()-> new Exception("File Not Found with Id : "+fileId));
    }


    @Override
    public Page<Image> findAll(Pageable pageable) {
        return iImageRepository.findAll(pageable);
    }

    @Override
    public Optional<Image> findById(Long id) {
        return iImageRepository.findById(id);
    }

    @Override
    public Image save(Image image) {
        return iImageRepository.save(image);
    }

    @Override
    public void delete(Long id) {
            iImageRepository.deleteById(id);
    }
}

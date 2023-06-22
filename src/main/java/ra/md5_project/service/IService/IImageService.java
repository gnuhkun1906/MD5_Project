package ra.md5_project.service.IService;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ra.md5_project.model.Image;
import ra.md5_project.service.IGenericService;

import java.util.List;

public interface IImageService extends IGenericService<Image> {

    List<Image> save(MultipartFile[] file) throws Exception;
    Image getImage(String fileId) throws Exception;

}

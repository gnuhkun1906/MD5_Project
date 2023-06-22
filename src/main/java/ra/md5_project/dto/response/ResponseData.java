package ra.md5_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.md5_project.model.Image;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseData{
    private Long id;
    private String fileName;
    private String downloadURL;
    private String fileType;
    private long fileSize;
}

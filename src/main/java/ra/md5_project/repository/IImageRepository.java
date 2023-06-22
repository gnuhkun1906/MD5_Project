package ra.md5_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.md5_project.model.Image;

@Repository
public interface IImageRepository extends JpaRepository<Image, Long> {
}

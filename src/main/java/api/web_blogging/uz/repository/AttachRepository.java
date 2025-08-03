package api.web_blogging.uz.repository;
import api.web_blogging.uz.entity.AttachEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AttachRepository extends CrudRepository<AttachEntity, String> {

    Object findAll(Pageable pageable);
}

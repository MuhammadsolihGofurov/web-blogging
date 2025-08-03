package api.web_blogging.uz.repository;

import api.web_blogging.uz.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, String>, PagingAndSortingRepository<PostEntity, String> {


    Page<PostEntity> getAllByProfileIdAndVisibleTrueOrderByCreatedAtDesc(Integer profileId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update PostEntity set visible = false where id =?1")
    void deleteByIdAndVisibleFalse(String id);
}

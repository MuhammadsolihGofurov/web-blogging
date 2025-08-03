package api.web_blogging.uz.repository;


import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);

    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer id);


    @Modifying
    @Transactional
    @Query("update ProfileEntity set status  =?2 where id = ?1")
    void changeStatus(Integer profileId, GeneralStatus status);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set password =?2 where id = ?1")
    void updatePassword(Integer id, String encode);


    @Modifying
    @Transactional
    @Query("update ProfileEntity  set name =?2 where id =?1")
    void updateProfileName(Integer id, String name);

    @Modifying
    @Transactional
    @Query("update ProfileEntity  set tempUsername =?2 where id =?1")
    void updateProfileTempUsername(Integer userId, String username);

    @Modifying
    @Transactional
    @Query("update ProfileEntity  set username =?2 where id =?1")
    void updateUsername(Integer userId, String tempUsername);


    @Modifying
    @Transactional
    @Query("update ProfileEntity  set photoId =?2 where id =?1")
    void updatePhoto(Integer userId, String photoId);

    Page<ProfileEntity> findAllByVisibleIsTrueOrderByCreatedAtDesc(PageRequest pageRequest);

    @Query("From ProfileEntity where (lower(username) like ?1 or lower(name) like ?1) and visible = true")
    Page<ProfileEntity> filterWithQuery(String query, PageRequest pageRequest);

//    @Modifying
//    @Transactional
//    @Query("update ProfileEntity  set password =?2 where id =?1")
//    void updatePassword(Integer id, String password);

}

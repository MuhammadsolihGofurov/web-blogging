package api.web_blogging.uz.repository;


import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);
    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer id);


    @Modifying
    @Transactional
    @Query("update ProfileEntity set status  =?2 where id = ?1")
    void changeStatus(Integer profileId, GeneralStatus status);

}

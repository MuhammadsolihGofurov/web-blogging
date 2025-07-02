package api.web_blogging.uz.repository;

import api.web_blogging.uz.entity.ProfileRoleEntity;
import api.web_blogging.uz.enums.ProfileRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProfileRoleRepository extends JpaRepository<ProfileRoleEntity, Integer> {

    @Transactional
    @Modifying
    void deleteByProfileId(Integer profileId);

    @Query("select p.roles From ProfileRoleEntity p where p.profileId = ?1")
    List<ProfileRole> getAllRolesListByProfileId(Integer profileId);

}

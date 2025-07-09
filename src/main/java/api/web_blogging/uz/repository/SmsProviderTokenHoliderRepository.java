package api.web_blogging.uz.repository;

import api.web_blogging.uz.entity.SmsProviderTokenHolider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsProviderTokenHoliderRepository extends CrudRepository<SmsProviderTokenHolider, Integer> {

    Optional<SmsProviderTokenHolider> findTop1();

}

package muschterm.ofx_api.repositories;

import io.micronaut.data.jpa.repository.JpaRepository;
import muschterm.ofx_api.entities.Account;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AccountRepository<A extends Account>
	extends JpaRepository<A, String>,
	        JpaSpecificationExecutor<A> {

	Optional<A> findByNumber(String number);

}

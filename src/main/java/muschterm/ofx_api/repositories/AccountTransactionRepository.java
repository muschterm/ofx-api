package muschterm.ofx_api.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import muschterm.ofx_api.entities.AccountTransaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@Repository
public interface AccountTransactionRepository
	extends JpaRepository<AccountTransaction, String>,
	        JpaSpecificationExecutor<AccountTransaction> {

	Optional<AccountTransaction> findByTransactionId(String transactionId);

}

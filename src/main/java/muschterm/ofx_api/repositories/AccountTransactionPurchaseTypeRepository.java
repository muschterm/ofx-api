package muschterm.ofx_api.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import muschterm.ofx_api.entities.AccountTransactionPurchaseType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface AccountTransactionPurchaseTypeRepository
	extends JpaRepository<AccountTransactionPurchaseType, Integer>,
	        JpaSpecificationExecutor<AccountTransactionPurchaseType> {
}

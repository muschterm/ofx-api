package muschterm.ofx_api.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import muschterm.ofx_api.entities.AccountTransactionCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface AccountTransactionCategoryRepository
	extends JpaRepository<AccountTransactionCategory, Integer>,
	        JpaSpecificationExecutor<AccountTransactionCategory> {
}

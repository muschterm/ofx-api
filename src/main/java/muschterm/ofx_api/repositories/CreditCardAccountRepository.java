package muschterm.ofx_api.repositories;

import io.micronaut.data.annotation.Repository;
import muschterm.ofx_api.entities.CreditCardAccount;

@Repository
public interface CreditCardAccountRepository extends AccountRepository<CreditCardAccount> {
}

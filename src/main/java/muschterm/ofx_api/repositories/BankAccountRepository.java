package muschterm.ofx_api.repositories;

import io.micronaut.data.annotation.Repository;
import muschterm.ofx_api.entities.BankAccount;

@Repository
public interface BankAccountRepository extends AccountRepository<BankAccount> {
}

package muschterm.ofx_api.services;

import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import muschterm.ofx_api.entities.BankAccount;
import muschterm.ofx_api.entities.FinancialInstitution;
import muschterm.ofx_api.repositories.BankAccountRepository;

import javax.transaction.Transactional;

@Singleton
public class BankAccountService {

	private final BankAccountRepository bankAccountRepository;

	@Inject
	public BankAccountService(BankAccountRepository bankAccountRepository) {
		this.bankAccountRepository = bankAccountRepository;
	}

	@Transactional
	public BankAccount process(
		FinancialInstitution financialInstitution,
		BankStatementResponse ofxBankStatementResponse
	) {
		var bankAccount = bankAccountRepository
			.findByNumber(ofxBankStatementResponse.getAccount().getAccountNumber())
			.orElse(null);

		if (bankAccount != null) {
			bankAccount = bankAccountRepository.update(
				bankAccount.fromOfx(financialInstitution, ofxBankStatementResponse)
			);
		}
		else {
			bankAccount = bankAccountRepository.save(
				new BankAccount().fromOfx(financialInstitution, ofxBankStatementResponse)
			);
		}

		return bankAccount;
	}

}

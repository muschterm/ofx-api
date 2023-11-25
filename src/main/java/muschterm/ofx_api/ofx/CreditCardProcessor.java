package muschterm.ofx_api.ofx;

import com.webcohesion.ofx4j.domain.data.creditcard.CreditCardResponseMessageSet;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import muschterm.ofx_api.entities.FinancialInstitution;
import muschterm.ofx_api.services.CreditCardAccountService;

@Singleton
public class CreditCardProcessor {

	private final CreditCardAccountService creditCardAccountService;
	private final AccountStatementProcessor accountStatementProcessor;

	@Inject
	public CreditCardProcessor(
		CreditCardAccountService creditCardAccountService,
		AccountStatementProcessor accountStatementProcessor
	) {
		this.creditCardAccountService = creditCardAccountService;
		this.accountStatementProcessor = accountStatementProcessor;
	}

	public void process(
		FinancialInstitution financialInstitution,
		CreditCardResponseMessageSet creditCardResponseMessageSet
	) {
		var creditCardStatementResponseTransactions = creditCardResponseMessageSet.getStatementResponses();
		for (var creditCardStatementResponseTransaction : creditCardStatementResponseTransactions) {
			var creditCardStatementResponse = creditCardStatementResponseTransaction.getWrappedMessage();
			var creditCardAccount = creditCardAccountService.process(
				financialInstitution,
				creditCardStatementResponse
			);

			accountStatementProcessor.process(creditCardAccount, creditCardStatementResponse);
		}
	}

}

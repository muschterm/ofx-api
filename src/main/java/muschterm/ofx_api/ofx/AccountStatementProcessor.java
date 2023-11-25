package muschterm.ofx_api.ofx;

import com.webcohesion.ofx4j.domain.data.common.StatementResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import muschterm.ofx_api.entities.Account;
import muschterm.ofx_api.services.AccountTransactionService;

@Singleton
public class AccountStatementProcessor {

	private final AccountTransactionService accountTransactionService;

	@Inject
	public AccountStatementProcessor(AccountTransactionService accountTransactionService) {
		this.accountTransactionService = accountTransactionService;
	}

	public void process(
		Account account,
		StatementResponse ofxStatementResponse
	) {
		var ofxTransactionList = ofxStatementResponse.getTransactionList();
		if (ofxTransactionList != null) {
			var ofxTransactions = ofxTransactionList.getTransactions();
			if (ofxTransactions != null) {
				for (var ofxTransaction : ofxTransactions) {
					accountTransactionService.process(ofxTransaction, account);
				}
			}
		}
	}

}

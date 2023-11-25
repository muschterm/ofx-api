package muschterm.ofx_api.entities;

import muschterm.ofx_api.db.JPQLSpecification;

import java.time.LocalDate;

public class AccountTransactionSpecification extends JPQLSpecification<AccountTransaction, String> {


	protected AccountTransactionSpecification() {
		super(
			"""
				SELECT t from account_transaction t\
				"""
		);
	}

	public AccountTransactionSpecification hasAccountId(String accountId) {
		equals("t.account", accountId);
		return this;
	}

	public AccountTransactionSpecification betweenPostedDate(
		LocalDate start,
		LocalDate end
	) {
		between("t.postedDate", start, end);
		return this;
	}

	public AccountTransactionSpecification sincePostedDate(LocalDate since) {
		equals("t.postedDate", since);
		return this;
	}

}

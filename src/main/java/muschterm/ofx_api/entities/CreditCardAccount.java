package muschterm.ofx_api.entities;

import com.webcohesion.ofx4j.domain.data.creditcard.CreditCardStatementResponse;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name = CreditCardAccount.TABLE_NAME)
@Getter
@Setter
public class CreditCardAccount extends Account {

	static final String TABLE_NAME = "credit_card_account";

	@Override
	protected String shortName() {
		return "Credit Card";
	}

	public CreditCardAccount fromOfx(
		FinancialInstitution financialInstitution,
		CreditCardStatementResponse ofxCreditCardStatementResponse
	) {
		super.fromOfx(
			financialInstitution,
			ofxCreditCardStatementResponse.getAccount(),
			ofxCreditCardStatementResponse
		);

		return this;
	}

}

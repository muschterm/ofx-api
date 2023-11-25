package muschterm.ofx_api.entities;

import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import muschterm.ofx_api.enums.BankAccountType;

@Entity(name = BankAccount.TABLE_NAME)
@Table(
	indexes = {
		@Index(name = "IDXBankAccount_type", columnList = "type")
	}
)
@Getter
@Setter
public class BankAccount extends Account {

	static final String TABLE_NAME = "bank_account";

	static final String COLUMN_BANK_ID = "bank_id";
	static final String COLUMN_TYPE = "type";

	/**
	 * USA: Routing and transit number.
	 */
	@Column(name = COLUMN_BANK_ID, length = 9, nullable = false)
	@NotNull
	@Size(max = 9)
	private String bankId;

	@Column(name = COLUMN_TYPE, length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	@Size(max = 20)
	private BankAccountType type;

	@Override
	protected String shortName() {
		return type.getName();
	}

	public BankAccount fromOfx(
		FinancialInstitution financialInstitution,
		BankStatementResponse ofxBankStatementResponse
	) {
		var ofxBankAccountDetails = ofxBankStatementResponse.getAccount();

		super.fromOfx(
			financialInstitution,
			ofxBankAccountDetails,
			ofxBankStatementResponse
		);

		bankId = ofxBankAccountDetails.getBankId();
		type = BankAccountType.lookup(ofxBankAccountDetails.getAccountType());

		return this;
	}

}

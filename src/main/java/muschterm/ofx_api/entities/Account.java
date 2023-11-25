package muschterm.ofx_api.entities;

import com.webcohesion.ofx4j.domain.data.common.AccountDetails;
import com.webcohesion.ofx4j.domain.data.common.StatementResponse;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import muschterm.ofx_api.utils.DateUtil;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity(name = Account.TABLE_NAME)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(
	indexes = {
		@Index(name = Account.IDX_NUMBER, columnList = Account.COLUMN_NUMBER),
		@Index(name = Account.IDX_FINANCIAL_INSTITUTION_ID, columnList = Account.COLUMN_FINANCIAL_INSTITUTION_ID)
	}
)
@Getter
@Setter
public abstract class Account {

	static final String TABLE_NAME = "account";

	static final String COLUMN_ID = "id";
	static final String COLUMN_FINANCIAL_INSTITUTION_ID = "financial_institution_id";
	static final String COLUMN_NUMBER = "number";
	private static final String LEDGER_BALANCE = "ledger_balance";
	static final String COLUMN_LEDGER_BALANCE_AMOUNT = LEDGER_BALANCE + "_" + BalanceDetail.COLUMN_AMOUNT;
	static final String COLUMN_LEDGER_BALANCE_AS_OF_DATE = LEDGER_BALANCE + "_" + BalanceDetail.COLUMN_AS_OF_DATE;
	private static final String AVAILABLE_BALANCE = "available_balance";
	static final String COLUMN_AVAILABLE_BALANCE_AMOUNT = AVAILABLE_BALANCE + "_" + BalanceDetail.COLUMN_AMOUNT;
	static final String COLUMN_AVAILABLE_BALANCE_AS_OF_DATE = AVAILABLE_BALANCE + "_" + BalanceDetail.COLUMN_AS_OF_DATE;
	static final String COLUMN_NAME = "name";

	private static final String FK = "FK";
	private static final String IDX = "IDX";
	private static final String FK_FINANCIAL_INSTITUTION_ID =
		FK +
		TABLE_NAME +
		"__" +
		COLUMN_FINANCIAL_INSTITUTION_ID;
	static final String IDX_FINANCIAL_INSTITUTION_ID =
		IDX +
		TABLE_NAME +
		"__" +
		COLUMN_FINANCIAL_INSTITUTION_ID;
	static final String IDX_NUMBER =
		IDX +
		TABLE_NAME +
		"__" +
		COLUMN_NUMBER;

	@Id
	@Column(name = COLUMN_ID, length = 40)
	@Size(max = 40)
	protected String id;

	@ManyToOne(optional = false)
	@JoinColumn(
		name = COLUMN_FINANCIAL_INSTITUTION_ID,
		foreignKey = @ForeignKey(name = FK_FINANCIAL_INSTITUTION_ID)
	)
	@NotNull
	private FinancialInstitution financialInstitution;

	@Column(name = COLUMN_NUMBER, length = 22, nullable = false)
	@Size(max = 22)
	private String number;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(
			name = "amount",
			column = @Column(name = COLUMN_AVAILABLE_BALANCE_AMOUNT)
		),
		@AttributeOverride(
			name = "asOfDate",
			column = @Column(name = COLUMN_AVAILABLE_BALANCE_AS_OF_DATE)
		)
	})
	private BalanceDetail availableBalance;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(
			name = "amount",
			column = @Column(name = COLUMN_LEDGER_BALANCE_AMOUNT)
		),
		@AttributeOverride(
			name = "asOfDate",
			column = @Column(name = COLUMN_LEDGER_BALANCE_AS_OF_DATE)
		)
	})
	private BalanceDetail ledgerBalance;

	@Column(name = COLUMN_NAME, length = 100, nullable = false)
	@NotNull
	@Size(max = 100)
	private String name;

	static final String COLUMN_CREATED_TIMESTAMP = "created_timestamp";
	static final String COLUMN_UPDATED_TIMESTAMP = "updated_timestamp";

	@Column(name = COLUMN_CREATED_TIMESTAMP)
	@CreationTimestamp
	private Instant createdTimestamp;

	@Column(name = COLUMN_UPDATED_TIMESTAMP)
	@UpdateTimestamp
	@Version
	private Instant updatedTimestamp;

	protected abstract String shortName();

	public String shortAccountNumber() {
		return number.substring(number.length() - 4);
	}

	protected void fromOfx(
		FinancialInstitution financialInstitution,
		AccountDetails ofxAccountDetails,
		StatementResponse ofxStatementResponse
	) {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}

		this.financialInstitution = financialInstitution;
		number = ofxAccountDetails.getAccountNumber();

		var ofxAvailableBalance = ofxStatementResponse.getAvailableBalance();
		if (availableBalance == null) {
			availableBalance = new BalanceDetail().fromOfx(ofxAvailableBalance);
		}
		else {
			var newAsOfDate = DateUtil.handleStaticDate(ofxAvailableBalance.getAsOfDate());
			if (newAsOfDate.isAfter(availableBalance.getAsOfDate())) {
				availableBalance.fromOfx(ofxAvailableBalance);
			}
		}

		var ofxLedgerBalance = ofxStatementResponse.getLedgerBalance();
		if (ledgerBalance == null) {
			ledgerBalance = new BalanceDetail().fromOfx(ofxLedgerBalance);
		}
		else {
			var newAsOfDate = DateUtil.handleStaticDate(ofxLedgerBalance.getAsOfDate());
			if (newAsOfDate.isAfter(ledgerBalance.getAsOfDate())) {
				ledgerBalance.fromOfx(ofxLedgerBalance);
			}
		}

		name = String.format("%s ...%s", shortName(), shortAccountNumber());
	}

}

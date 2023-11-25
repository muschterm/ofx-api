package muschterm.ofx_api.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import muschterm.ofx_api.enums.TransactionType;
import muschterm.ofx_api.utils.DateUtil;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = AccountTransaction.TABLE_NAME)
@Table(
	indexes = {
		@Index(name = "IDXAccountTransaction_account_id_transaction_id", columnList = "account_id, transaction_id DESC"),
		@Index(name = "IDXAccountTransaction_transaction_id", columnList = "transaction_id"),
		@Index(name = "IDXAccountTransaction_posted_date", columnList = "posted_date DESC"),
		@Index(name = "IDXAccountTransaction_amount", columnList = "amount DESC"),
		@Index(name = "IDXAccountTransaction_purchase_type_id", columnList = "purchase_type_id"),
		@Index(name = "IDXAccountTransaction_merchant_id", columnList = "merchant_id")
	}
)
@Getter
@Setter
public class AccountTransaction {

	static final String TABLE_NAME = "account_transaction";

	static final String COLUMN_ID = "id";
	static final String COLUMN_TRANSACTION_ID = "transaction_id";
	static final String COLUMN_ACCOUNT_ID = "account_id";
	static final String COLUMN_TYPE = "type";
	static final String COLUMN_NAME = "name";
	static final String COLUMN_MEMO = "memo";
	static final String COLUMN_AMOUNT = "amount";
	static final String COLUMN_POSTED_DATE = "posted_date";

	// non ofx specific
	static final String COLUMN_PURCHASE_TYPE_ID = "purchase_type_id";
	static final String COLUMN_MERCHANT_ID = "merchant_id";
	static final String COLUMN_DESCRIPTION = "description";

	private static final String FK = "FK";
	private static final String FK_ACCOUNT_ID =
		FK +
		TABLE_NAME +
		"__" +
		COLUMN_ACCOUNT_ID;

	private static final String FK_PURCHASE_TYPE_ID =
		FK +
		TABLE_NAME +
		"__" +
		COLUMN_PURCHASE_TYPE_ID;

	private static final String FK_MERCHANT_ID =
		FK +
		TABLE_NAME +
		"__" +
		COLUMN_MERCHANT_ID;

	@Id
	@Column(name = COLUMN_ID, length = 40)
	@Size(max = 40)
	private String id;

	@Column(name = COLUMN_TRANSACTION_ID)
	@Size(max = 255)
	private String transactionId;

	@ManyToOne(optional = false)
	@JoinColumn(
		name = COLUMN_ACCOUNT_ID,
		foreignKey = @ForeignKey(name = FK_ACCOUNT_ID)
	)
	private Account account;

	@Column(name = COLUMN_TYPE, length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	@Size(max = 20)
	private TransactionType type;

	@Column(name = COLUMN_NAME, length = 32, nullable = false)
	@NotNull
	@Size(max = 32)
	private String name;

	@Column(name = COLUMN_MEMO)
	@Size(max = 255)
	private String memo;

	@Column(name = COLUMN_AMOUNT, precision = 9, scale = 2, nullable = false)
	@NotNull
	private double amount;

	@Column(name = COLUMN_POSTED_DATE, nullable = false)
	@NotNull
	private LocalDate postedDate;

	// ****************
	// not ofx specific
	// ****************

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
		name = COLUMN_PURCHASE_TYPE_ID,
		foreignKey = @ForeignKey(name = FK_PURCHASE_TYPE_ID)
	)
	private AccountTransactionPurchaseType purchaseType;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
		name = COLUMN_MERCHANT_ID,
		foreignKey = @ForeignKey(name = FK_MERCHANT_ID)
	)
	private AccountTransactionMerchant merchant;

	@Column(name = COLUMN_DESCRIPTION)
	@Size(max = 255)
	private String description;

	static final String COLUMN_CREATED_TIMESTAMP = "created_timestamp";
	static final String COLUMN_UPDATED_TIMESTAMP = "updated_timestamp";

	@Column(name = COLUMN_CREATED_TIMESTAMP)
	@CreationTimestamp
	private Instant createdTimestamp;

	@Column(name = COLUMN_UPDATED_TIMESTAMP)
	@UpdateTimestamp
	@Version
	private Instant updatedTimestamp;

	public AccountTransaction fromOfx(
		com.webcohesion.ofx4j.domain.data.common.Transaction ofxTransaction
	) {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}

		transactionId = ofxTransaction.getId();
		type = TransactionType.lookup(ofxTransaction.getTransactionType());
		name = ofxTransaction.getName();
		memo = ofxTransaction.getMemo();
		amount = ofxTransaction.getAmount();
		postedDate = DateUtil.handleStaticDate(ofxTransaction.getDatePosted());

		return this;
	}

}

package muschterm.ofx_api.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

@Entity
@Table(
	indexes = {
		@Index(name = "IDXAccountTransactionMerchant_name", columnList = "name"),
		@Index(name = "IDXAccountTransactionMerchant_category_id", columnList = "category_id"),
	}
)
@Getter
@Setter
public class AccountTransactionMerchant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(
		name = "category_id",
		foreignKey = @ForeignKey(name = "FKAccountTransactionMerchant_category_id")
	)
	private AccountTransactionCategory category;

	@Column(length = 100)
	@Size(max = 100)
	private String name;

	@Size(max = 255)
	private String description;

	static final String COLUMN_CREATED_TIMESTAMP = "created_timestamp";
	static final String COLUMN_UPDATED_TIMESTAMP = "updated_timestamp";

	@Column(name = COLUMN_CREATED_TIMESTAMP)
	@CreationTimestamp
	private Instant createdTimestamp;

	@Column(name = COLUMN_UPDATED_TIMESTAMP)
	@Version
	private Instant updatedTimestamp;

	public static Specification<AccountTransactionMerchant> containsName(String name) {
		return (root, cq, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<AccountTransactionMerchant> startsWithName(String name) {
		return (root, cq, cb) -> cb.like(root.get("name"), name + "%");
	}

	public static Specification<AccountTransactionMerchant> endsWithName(String name) {
		return (root, cq, cb) -> cb.like(root.get("name"), "%" + name);
	}

}

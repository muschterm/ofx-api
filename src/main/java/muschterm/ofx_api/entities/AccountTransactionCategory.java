package muschterm.ofx_api.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(
	indexes = {
		@Index(name = "IDXAccountTransactionCategory_name", columnList = "name"),
	}
)
@Getter
@Setter
public class AccountTransactionCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(cascade = CascadeType.ALL)
	private AccountTransactionCategory parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<AccountTransactionCategory> children;

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

	public void setParent(AccountTransactionCategory parent) {
		if (this.parent != null) {
			// remove from children of old parent
			this.parent.children.removeIf(accountTransactionCategory ->
				accountTransactionCategory.id == this.id
			);
		}

		this.parent = parent;
		this.parent.children.add(this);
	}

	public static Specification<AccountTransactionCategory> containsName(String name) {
		return (root, cq, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<AccountTransactionCategory> startsWithName(String name) {
		return (root, cq, cb) -> cb.like(root.get("name"), name + "%");
	}

	public static Specification<AccountTransactionCategory> endsWithName(String name) {
		return (root, cq, cb) -> cb.like(root.get("name"), "%" + name);
	}

}

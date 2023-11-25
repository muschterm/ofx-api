package muschterm.ofx_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Embeddable
@Getter
@Setter
public class Shared {

	static final String COLUMN_CREATED_TIMESTAMP = "created_timestamp";
	static final String COLUMN_UPDATED_TIMESTAMP = "updated_timestamp";

	@Column(name = COLUMN_CREATED_TIMESTAMP)
	@CreationTimestamp
	private Instant createdTimestamp;

	@Column(name = COLUMN_UPDATED_TIMESTAMP)
	@Version
	private Instant updatedTimestamp;

}

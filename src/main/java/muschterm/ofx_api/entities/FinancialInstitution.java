package muschterm.ofx_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
public class FinancialInstitution {

	@Id
	@Column(length = 32)
	@Size(max = 32)
	private String id;

	@Column(length = 32)
	@Size(max = 32)
	private String organization;

	@CreationTimestamp
	private Instant createdTimestamp;

	@Version
	private Instant updatedTimestamp;

	public FinancialInstitution fromOfx(
		com.webcohesion.ofx4j.domain.data.signon.FinancialInstitution ofxFinancialInstitution
	) {
		id = ofxFinancialInstitution.getId();
		organization = ofxFinancialInstitution.getOrganization();

		return this;
	}

}

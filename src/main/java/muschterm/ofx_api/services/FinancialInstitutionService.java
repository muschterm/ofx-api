package muschterm.ofx_api.services;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import muschterm.ofx_api.entities.FinancialInstitution;
import muschterm.ofx_api.repositories.FinancialInstitutionRepository;

import javax.transaction.Transactional;

@Singleton
public class FinancialInstitutionService {

	private static final String UNKNOWN_ID = "000000";
	private static final String UNKNOWN_ORGANIZATION = "Unknown";

	private final FinancialInstitutionRepository financialInstitutionRepository;

	@Inject
	public FinancialInstitutionService(
		FinancialInstitutionRepository financialInstitutionRepository
	) {
		this.financialInstitutionRepository = financialInstitutionRepository;
	}

	@Transactional
	public FinancialInstitution handleFinancialInstitution(
		com.webcohesion.ofx4j.domain.data.signon.FinancialInstitution ofxFinancialInstitution
	) {
		var financialInstitution = financialInstitutionRepository
			.findById(ofxFinancialInstitution.getId())
			.orElse(null);
		if (financialInstitution != null) {
			financialInstitution = financialInstitutionRepository.update(
				financialInstitution.fromOfx(ofxFinancialInstitution)
			);
		}
		else {
			financialInstitution = financialInstitutionRepository.save(
				new FinancialInstitution().fromOfx(ofxFinancialInstitution)
			);
		}

		return financialInstitution;
	}

}

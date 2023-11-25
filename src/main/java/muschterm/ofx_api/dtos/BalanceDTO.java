package muschterm.ofx_api.dtos;

import muschterm.ofx_api.entities.BalanceDetail;

import java.time.LocalDate;

public record BalanceDTO(
	double amount,
	LocalDate asOfDate
) {

	public BalanceDTO(BalanceDetail balanceDetail) {
		this(
			balanceDetail.getAmount(),
			balanceDetail.getAsOfDate()
		);
	}

}

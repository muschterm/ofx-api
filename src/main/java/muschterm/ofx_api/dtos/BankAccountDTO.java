package muschterm.ofx_api.dtos;

import muschterm.ofx_api.entities.BankAccount;
import muschterm.ofx_api.enums.BankAccountType;

import java.util.UUID;

public record BankAccountDTO(
	UUID id,
	BankAccountType type,
	String name,
	BalanceDTO availableBalance,
	BalanceDTO ledgerBalance
) {

	public BankAccountDTO(BankAccount bankAccount) {
		this(
			UUID.fromString(bankAccount.getId()),
			bankAccount.getType(),
			bankAccount.getName(),
			new BalanceDTO(bankAccount.getAvailableBalance()),
			new BalanceDTO(bankAccount.getLedgerBalance())
		);
	}

}

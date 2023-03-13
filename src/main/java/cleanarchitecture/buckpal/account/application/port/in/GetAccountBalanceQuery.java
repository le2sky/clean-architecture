package cleanarchitecture.buckpal.account.application.port.in;

import cleanarchitecture.buckpal.account.domain.Account.AccountId;
import cleanarchitecture.buckpal.account.domain.Money;

public interface GetAccountBalanceQuery {

  Money getAccountBalance(AccountId accountId);
}

package cleanarchitecture.buckpal.account.application.service;

import cleanarchitecture.buckpal.account.application.port.in.GetAccountBalanceQuery;
import cleanarchitecture.buckpal.account.application.port.out.LoadAccountPort;
import cleanarchitecture.buckpal.account.domain.Account.AccountId;
import cleanarchitecture.buckpal.account.domain.Money;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAccountBalanceService implements GetAccountBalanceQuery {

  private final LoadAccountPort loadAccountPort;

  @Override
  public Money getAccountBalance(AccountId accountId) {
    return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
        .calculateBalance();
  }
}

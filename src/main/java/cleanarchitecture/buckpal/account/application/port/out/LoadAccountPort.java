package cleanarchitecture.buckpal.account.application.port.out;

import cleanarchitecture.buckpal.account.domain.Account;
import cleanarchitecture.buckpal.account.domain.Account.AccountId;
import java.time.LocalDateTime;

public interface LoadAccountPort {

  Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}

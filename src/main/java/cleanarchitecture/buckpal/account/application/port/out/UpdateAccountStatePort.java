package cleanarchitecture.buckpal.account.application.port.out;

import cleanarchitecture.buckpal.account.domain.Account;

public interface UpdateAccountStatePort {

  void updateActivities(Account account);
}

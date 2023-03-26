package cleanarchitecture.buckpal.account.application.service;

import cleanarchitecture.buckpal.account.application.port.in.SendMoneyCommand;
import cleanarchitecture.buckpal.account.application.port.in.SendMoneyUseCase;
import cleanarchitecture.buckpal.account.application.port.out.AccountLock;
import cleanarchitecture.buckpal.account.application.port.out.LoadAccountPort;
import cleanarchitecture.buckpal.account.application.port.out.UpdateAccountStatePort;
import cleanarchitecture.buckpal.account.domain.Account;
import cleanarchitecture.buckpal.account.domain.Account.AccountId;
import cleanarchitecture.buckpal.common.UseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@UseCase
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

  private final LoadAccountPort loadAccountPort;
  private final UpdateAccountStatePort updateAccountStatePort;
  private final AccountLock accountLock;
  private final MoneyTransferProperties moneyTransferProperties;


  @Override
  public boolean sendMoney(SendMoneyCommand command) {

    checkThreshold(command);

    LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

    Account sourceAccount = loadAccountPort.loadAccount(
        command.getSourceAccountId(),
        baselineDate);

    Account targetAccount = loadAccountPort.loadAccount(
        command.getTargetAccountId(),
        baselineDate);

    AccountId sourceAccountId = sourceAccount.getId()
        .orElseThrow(() -> new IllegalStateException("expected source account ID not to be empty"));
    AccountId targetAccountId = targetAccount.getId()
        .orElseThrow(() -> new IllegalStateException("expected target account ID not to be empty"));

    accountLock.lockAccount(sourceAccountId);
    if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {
      accountLock.releaseAccount(sourceAccountId);
      return false;
    }

    accountLock.lockAccount(targetAccountId);
    if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
      accountLock.releaseAccount(sourceAccountId);
      accountLock.releaseAccount(targetAccountId);
      return false;
    }

    updateAccountStatePort.updateActivities(sourceAccount);
    updateAccountStatePort.updateActivities(targetAccount);

    accountLock.releaseAccount(sourceAccountId);
    accountLock.releaseAccount(targetAccountId);
    return true;
  }

  private void checkThreshold(SendMoneyCommand command) {
    if (command.getMoney().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())) {
      throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(),
          command.getMoney());
    }
  }
}

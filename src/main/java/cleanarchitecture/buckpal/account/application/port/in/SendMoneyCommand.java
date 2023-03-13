package cleanarchitecture.buckpal.account.application.port.in;

import cleanarchitecture.buckpal.account.domain.Account.AccountId;
import cleanarchitecture.buckpal.account.domain.Money;
import cleanarchitecture.buckpal.common.SelfValidating;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

  @NotNull
  private final AccountId sourceAccountId;

  @NotNull
  private final AccountId targetAccountId;

  @NotNull
  private final Money money;

  public SendMoneyCommand(AccountId sourceAccountId, AccountId targetAccountId, Money money) {
    this.sourceAccountId = sourceAccountId;
    this.targetAccountId = targetAccountId;
    this.money = money;
    this.validateSelf();
  }
}

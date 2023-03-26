package cleanarchitecture.buckpal.account.adapter.in.web;

import cleanarchitecture.buckpal.account.application.port.in.SendMoneyCommand;
import cleanarchitecture.buckpal.account.application.port.in.SendMoneyUseCase;
import cleanarchitecture.buckpal.account.domain.Account.AccountId;
import cleanarchitecture.buckpal.account.domain.Money;
import cleanarchitecture.buckpal.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class SendMoneyController {

  private final SendMoneyUseCase sendMoneyUseCase;

  @PostMapping(path = "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
  void sendMoney(
      @PathVariable("sourceAccountId") Long sourceAccountId,
      @PathVariable("targetAccountId") Long targetAccountId,
      @PathVariable("amount") Long amount) {

    SendMoneyCommand command = new SendMoneyCommand(
        new AccountId(sourceAccountId),
        new AccountId(targetAccountId),
        Money.of(amount)
    );

    sendMoneyUseCase.sendMoney(command);
  }
}

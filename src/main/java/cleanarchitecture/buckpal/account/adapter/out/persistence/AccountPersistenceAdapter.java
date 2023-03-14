package cleanarchitecture.buckpal.account.adapter.out.persistence;

import cleanarchitecture.buckpal.account.application.port.out.LoadAccountPort;
import cleanarchitecture.buckpal.account.application.port.out.UpdateAccountStatePort;
import cleanarchitecture.buckpal.account.domain.Account;
import cleanarchitecture.buckpal.account.domain.Account.AccountId;
import cleanarchitecture.buckpal.account.domain.Activity;
import cleanarchitecture.buckpal.common.PersistenceAdapter;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
class AccountPersistenceAdapter implements
    LoadAccountPort,
    UpdateAccountStatePort {

  private final AccountRepository accountRepository;
  private final ActivityRepository activityRepository;
  private final AccountMapper accountMapper;

  @Override
  public Account loadAccount(
      AccountId accountId,
      LocalDateTime baselineDate) {

    AccountJpaEntity account =
        accountRepository.findById(accountId.getValue())
            .orElseThrow(EntityNotFoundException::new);

    List<ActivityJpaEntity> activities =
        activityRepository.findByOwnerSince(
            accountId.getValue(),
            baselineDate);

    Long withdrawalBalance = orZero(activityRepository
        .getWithdrawalBalanceUntil(
            accountId.getValue(),
            baselineDate));

    Long depositBalance = orZero(activityRepository
        .getDepositBalanceUntil(
            accountId.getValue(),
            baselineDate));

    return accountMapper.mapToDomainEntity(
        account,
        activities,
        withdrawalBalance,
        depositBalance);

  }

  private Long orZero(Long value) {
    return value == null ? 0L : value;
  }


  @Override
  public void updateActivities(Account account) {
    for (Activity activity : account.getActivityWindow().getActivities()) {
      if (activity.getId() == null) {
        activityRepository.save(accountMapper.mapToJpaEntity(activity));
      }
    }
  }

}
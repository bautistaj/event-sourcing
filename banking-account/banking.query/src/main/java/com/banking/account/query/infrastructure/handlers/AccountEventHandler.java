package com.banking.account.query.infrastructure.handlers;

import com.banking.account.query.domain.AccountRepository;
import com.banking.account.query.domain.BankAccount;
import com.banking.common.events.AccountClosedEvent;
import com.banking.common.events.AccountOpenedEvent;
import com.banking.common.events.FundsDepositedEvent;
import com.banking.common.events.FundsWithdrawEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountEventHandler implements EventHandler {
    private final AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        var backAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreateDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();

        this.accountRepository.save(backAccount);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        var bankAccount = this.accountRepository.findById(event.getId());

        if(bankAccount.isEmpty()){
            return;
        }

        var currentBalance = bankAccount.get().getBalance();
        var latestBalance = currentBalance + event.getAmount();
        bankAccount.get().setBalance(latestBalance);

        this.accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(FundsWithdrawEvent event) {
        var bankAccount = this.accountRepository.findById(event.getId());

        if (bankAccount.isEmpty()) {
            return;
        }

        var currentBalance = bankAccount.get().getBalance();
        var latestBalance = currentBalance - event.getAmount();
        bankAccount.get().setBalance(latestBalance);

        this.accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }
}

package com.banking.account.cmd.domain;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.common.events.AccountClosedEvent;
import com.banking.common.events.AccountOpenedEvent;
import com.banking.common.events.FundsDepositedEvent;
import com.banking.common.events.FundsWithdrawEvent;
import com.banking.cqrs.core.domain.AggregateRoot;

import java.util.Date;

public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private double balance;

    public double getBalance() {
        return balance;
    }

    public AccountAggregate(){

    }

    public AccountAggregate(OpenAccountCommand command){
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createDate(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event){
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount){
        if(!this.active){
            throw new IllegalStateException("The funds can't be deposit");
        }

        if(amount <= 0 ){
            throw new IllegalStateException("The funds can't be 0 or less");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsDepositedEvent event){
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFunds(double amount){
        if(!this.active){
            throw new IllegalStateException("The banking account is closed");
        }

        raiseEvent(FundsWithdrawEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsWithdrawEvent event){
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount(){
        if(!this.active){
            throw new IllegalStateException("The banking account is closed");
        }

        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountClosedEvent event){
        this.id = event.getId();
        this.active = false;
    }
}

package com.banking.account.cmd.api.command;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountCommandHandler implements CommandHandler{
    private final EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand command) {
        var aggregate = new AccountAggregate(command);
        this.eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DepositFundsCommand command) {
        var aggregate = this.eventSourcingHandler.getById(command.getId());
        aggregate.depositFunds(command.getAmount());
        this.eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        var aggregate = this.eventSourcingHandler.getById(command.getId());

        if(command.getAmount() > aggregate.getBalance()){
            throw new IllegalStateException("Cant get the money");
        }

        aggregate.withdrawFunds(command.getAmount());
        this.eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        var aggregate = this.eventSourcingHandler.getById(command.getId());
        aggregate.closeAccount();
        this.eventSourcingHandler.save(aggregate);
    }
}

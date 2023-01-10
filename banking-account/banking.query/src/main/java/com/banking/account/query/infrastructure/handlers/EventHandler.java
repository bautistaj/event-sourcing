package com.banking.account.query.infrastructure.handlers;

import com.banking.common.events.AccountClosedEvent;
import com.banking.common.events.AccountOpenedEvent;
import com.banking.common.events.FundsDepositedEvent;
import com.banking.common.events.FundsWithdrawEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);
    void on(FundsDepositedEvent event);
    void on(FundsWithdrawEvent event);
    void on(AccountClosedEvent event);
}

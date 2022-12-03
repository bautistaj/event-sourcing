package com.banking.account.cmd.api.command;

import com.banking.common.dto.AccountType;
import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}

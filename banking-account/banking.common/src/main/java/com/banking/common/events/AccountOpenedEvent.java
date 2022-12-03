package com.banking.common.events;

import com.banking.common.dto.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccountOpenedEvent {
    private String accountHolder;
    private AccountType accountType;
    private Date createDate;
    private double openingBalance;
}

package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.cmd.api.dto.OpenAccountResponse;
import com.banking.common.dto.BaseResponse;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/openBankAccount")
public class OpenAccountController {
    private final CommandDispatcher commandDispatcher;

    @PostMapping()
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand openAccountCommand) {
        var id = UUID.randomUUID().toString();
        openAccountCommand.setId(id);

        try {

            this.commandDispatcher.send(openAccountCommand);

            return new ResponseEntity<BaseResponse>(new OpenAccountResponse("The bank account was created correctly",
                    id), HttpStatus.CREATED);

        }catch (IllegalStateException illegalStateException){
            log.warn(MessageFormat.format("The account wan not create {0}",illegalStateException.getMessage()));
            return new ResponseEntity<BaseResponse>(new BaseResponse(illegalStateException.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception exception) {
            log.error(MessageFormat.format("The account wan not create {0}",exception.getMessage()));
            return new ResponseEntity<BaseResponse>(new BaseResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

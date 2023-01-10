package com.banking.account.query.infrastructure.consumers;

import com.banking.account.query.infrastructure.handlers.EventHandler;
import com.banking.common.events.AccountClosedEvent;
import com.banking.common.events.AccountOpenedEvent;
import com.banking.common.events.FundsDepositedEvent;
import com.banking.common.events.FundsWithdrawEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountEventConsumer implements EventConsumer{
    private final EventHandler eventHandler;

    @KafkaListener(topics = "AccountOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountOpenedEvent event, Acknowledgment acknowledgment) {
        this.eventHandler.on(event);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "FundsDepositedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsDepositedEvent event, Acknowledgment acknowledgment) {
        this.eventHandler.on(event);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "FundsWithdrawEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsWithdrawEvent event, Acknowledgment acknowledgment) {
        this.eventHandler.on(event);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "AccountClosedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountClosedEvent event, Acknowledgment acknowledgment) {
        this.eventHandler.on(event);
        acknowledgment.acknowledge();
    }
}

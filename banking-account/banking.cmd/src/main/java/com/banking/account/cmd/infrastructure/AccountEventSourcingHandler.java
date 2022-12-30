package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.domain.AggregateRoot;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import com.banking.cqrs.core.infrastructure.EventStore;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@AllArgsConstructor
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    private final EventStore eventStore;

    @Override
    public void save(AggregateRoot aggregateRoot) {
        this.eventStore.saveEvents(aggregateRoot.getId(), aggregateRoot.getUncommittedChanges(), aggregateRoot.getVersion());
        aggregateRoot.markChangesCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvent(id);

        if(events != null && !events.isEmpty()){
            aggregate.replayEvents(events);
            var latestVersion = events.stream().map(x -> x.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }

        return aggregate;
    }
}

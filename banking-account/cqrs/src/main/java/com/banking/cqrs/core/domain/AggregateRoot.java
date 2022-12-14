package com.banking.cqrs.core.domain;

import com.banking.cqrs.core.events.BaseEvent;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AggregateRoot {
    protected String id;
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

    public String getId(){
        return this.id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BaseEvent> getUncommittedChanges(){
        return this.changes;
    }

    public void markChangesCommitted(){
        this.changes.clear();
    }

    protected void applyChanges(BaseEvent event, Boolean isNewEvent){
        try {

            Method method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);

        }catch (NoSuchMethodException noSuchMethodException){
            logger.log(Level.WARNING, MessageFormat.format("The method wasn't fount {0} ", event.getClass()));
        }catch (Exception exception){
            logger.log(Level.SEVERE, MessageFormat.format("Errors applying events to the aggregate ",event));
        }finally {
            if(isNewEvent){
                this.changes.add(event);
            }
        }
    }

    public void raiseEvent(BaseEvent event){
        applyChanges(event, true);
    }

    public void replayEvents(Iterable<BaseEvent> events){
        events.forEach(event -> applyChanges(event, false));
    }
}

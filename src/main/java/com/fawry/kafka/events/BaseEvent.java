package com.fawry.kafka.events;

public abstract class BaseEvent {
    private final String email;

    public BaseEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
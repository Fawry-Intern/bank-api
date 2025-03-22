package com.fawry.kafka.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetPasswordEvent extends BaseEvent{

    private final String username;
    private final String linkToken;

    public ResetPasswordEvent(@JsonProperty("email") String email,
                              @JsonProperty("username") String username,
                              @JsonProperty("linkToken") String linkToken
    ) {
        super(email);
        this.username = username;
        this.linkToken = linkToken;
    }

    public String getUsername() {
        return username;
    }
    public String getLinkToken() {
        return linkToken;
    }
}

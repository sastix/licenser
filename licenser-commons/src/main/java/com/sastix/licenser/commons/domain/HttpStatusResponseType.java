package com.sastix.licenser.commons.domain;

public enum HttpStatusResponseType {

    SUCCESSFUL_OPERATION(200, "Licenser Successful operation"),
    BAD_REQUEST(400, "Licenser Bad Request"),
    NOT_FOUND(404, "Licenser responded with not found.")
    ;

    private final int value;
    private final String reasonPhrase;

    HttpStatusResponseType(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}

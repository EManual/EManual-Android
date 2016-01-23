package io.github.emanual.app.event;

/**
 * Author: jayin
 * Date: 1/22/16
 */
public class UnPackFinishEvent {
    private Exception exception;

    public UnPackFinishEvent() {
        this(null);
    }
    public UnPackFinishEvent(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}

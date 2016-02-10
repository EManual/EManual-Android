package io.github.emanual.app.ui.event;

/**
 * 教程下载后解压完成事件
 * Author: jayin
 * Date: 1/22/16
 */
public class UnPackFinishEvent extends BaseEvent {
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

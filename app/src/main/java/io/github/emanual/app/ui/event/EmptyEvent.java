package io.github.emanual.app.ui.event;

/**
 * 空事件
 * Author: jayin
 * Date: 1/22/16
 */
public class EmptyEvent extends BaseEvent {
    String message;

    public EmptyEvent(String message) {
        this.message = message;
    }
}

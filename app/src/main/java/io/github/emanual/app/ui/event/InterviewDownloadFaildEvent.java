package io.github.emanual.app.ui.event;

import cz.msebera.android.httpclient.Header;

/**
 * Author: jayin
 * Date: 2/23/16
 */
public class InterviewDownloadFaildEvent extends BaseEvent {
    private int statusCode;
    private Header[] headers;
    private Throwable throwable;

    public InterviewDownloadFaildEvent(int statusCode, Header[] headers, Throwable throwable) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.throwable = throwable;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}

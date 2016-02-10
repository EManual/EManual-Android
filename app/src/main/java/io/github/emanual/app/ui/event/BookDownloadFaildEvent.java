package io.github.emanual.app.ui.event;

import cz.msebera.android.httpclient.Header;

/**
 * Author: jayin
 * Date: 2/11/16
 */
public class BookDownloadFaildEvent extends BaseEvent {

    private int statusCode;
    private Header[] headers;
    private Throwable throwable;

    public BookDownloadFaildEvent(int statusCode, Header[] headers, Throwable throwable) {
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

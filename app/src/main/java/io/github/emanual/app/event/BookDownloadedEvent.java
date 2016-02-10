package io.github.emanual.app.event;

import java.io.File;

import io.github.emanual.app.entity.FeedsItemObject;

/**
 * 书下载完毕事件
 * Author: jayin
 * Date: 1/22/16
 */
public class BookDownloadedEvent {
    private File file;
    private  FeedsItemObject feedsItemObject;

    public BookDownloadedEvent(File file, FeedsItemObject feedsItemObject){
        this.file = file;
        this.feedsItemObject = feedsItemObject;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FeedsItemObject getFeedsItemObject() {
        return feedsItemObject;
    }

    public void setFeedsItemObject(FeedsItemObject feedsItemObject) {
        this.feedsItemObject = feedsItemObject;
    }
}

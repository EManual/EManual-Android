package io.github.emanual.app.ui.event;

import java.io.File;

import io.github.emanual.app.entity.FeedsItemObject;

/**
 * 书下载完毕事件
 * Author: jayin
 * Date: 1/22/16
 */
public class BookDownloadEndEvent extends BaseEvent{
    private File file;
    private  FeedsItemObject feedsItemObject;

    public BookDownloadEndEvent(File file, FeedsItemObject feedsItemObject){
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

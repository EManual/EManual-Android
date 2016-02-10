package io.github.emanual.app.ui.event;

import java.io.File;

import io.github.emanual.app.entity.FeedsItemEntity;

/**
 * 书下载完毕事件
 * Author: jayin
 * Date: 1/22/16
 */
public class BookDownloadEndEvent extends BaseEvent{
    private File file;
    private FeedsItemEntity feedsItemEntity;

    public BookDownloadEndEvent(File file, FeedsItemEntity feedsItemEntity){
        this.file = file;
        this.feedsItemEntity = feedsItemEntity;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FeedsItemEntity getFeedsItemEntity() {
        return feedsItemEntity;
    }

    public void setFeedsItemEntity(FeedsItemEntity feedsItemEntity) {
        this.feedsItemEntity = feedsItemEntity;
    }
}

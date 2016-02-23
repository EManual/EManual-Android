package io.github.emanual.app.ui.event;

import java.io.File;

import io.github.emanual.app.entity.FeedsItemEntity;

/**
 * Author: jayin
 * Date: 2/23/16
 */
public class InterviewDownloadEndEvent extends BaseEvent {
    private File file;
    private FeedsItemEntity feedsItemEntity;

    public InterviewDownloadEndEvent(File file, FeedsItemEntity feedsItemEntity){
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

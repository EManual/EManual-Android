package io.github.emanual.app.ui.event;

/**
 * Author: jayin
 * Date: 2/23/16
 */
public class InterviewDownloadProgressEvent extends BaseEvent {
    private long bytesWritten;
    private long totalSize;

    /**
     * @param bytesWritten 已下载
     * @param totalSize    总大小
     */
    public InterviewDownloadProgressEvent(long bytesWritten, long totalSize) {
        this.bytesWritten = bytesWritten;
        this.totalSize = totalSize;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
}

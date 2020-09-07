package com.example.videoplayinglistview;

import android.provider.MediaStore;

public interface IVideoDownloadListener {
    public void onVideoDownloaded(MediaStore.Video video);
}
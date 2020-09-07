package com.example.videoplayinglistview;

import android.provider.MediaStore;

public interface IVideoPreparedListener {

    public void onVideoPrepared(MediaStore.Video video);
}
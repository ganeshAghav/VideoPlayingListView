package com.example.videoplayinglistview;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity   implements IVideoDownloadListener{

    private static String TAG = "MainActivity";
    private Context context;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private VideosAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MediaStore.Video> urls;
    VideosDownloader videosDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        urls = new ArrayList<MediaStore.Video>();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VideosAdapter(MainActivity.this, urls);
        mRecyclerView.setAdapter(mAdapter);

        videosDownloader = new VideosDownloader(context);
        videosDownloader.setOnVideoDownloadListener(this);

        if(Utils.hasConnection(context))
        {
            getVideoUrls();

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        int findFirstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

                        Video video;
                        if (urls != null && urls.size() > 0)
                        {
                            if (findFirstCompletelyVisibleItemPosition >= 0) {
                                video = urls.get(findFirstCompletelyVisibleItemPosition);
                                mAdapter.videoPlayerController.setcurrentPositionOfItemToPlay(findFirstCompletelyVisibleItemPosition);
                                mAdapter.videoPlayerController.handlePlayBack(video);
                            }
                            else
                            {
                                video = urls.get(firstVisiblePosition);
                                mAdapter.videoPlayerController.setcurrentPositionOfItemToPlay(firstVisiblePosition);
                                mAdapter.videoPlayerController.handlePlayBack(video);
                            }
                        }
                    }
                }
            });
        }
        else
            Toast.makeText(context, "No internet available", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVideoDownloaded(MediaStore.Video video) {
        mAdapter.videoPlayerController.handlePlayBack(video);
    }

    private void getVideoUrls()
    {
        MediaStore.Video video1 = new MediaStore.Video("0", "1", "http://techslides.com/demos/sample-videos/small.mp4");
        urls.add(video1);
        MediaStore.Video video2 = new MediaStore.Video("1", "2", "http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4");
        urls.add(video2);
        MediaStore.Video video3 = new MediaStore.Video("2", "3", "http://sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4");
        urls.add(video3);
        MediaStore.Video video4 = new MediaStore.Video("3", "4", "http://dev.exiv2.org/attachments/341/video-2012-07-05-02-29-27.mp4");
        urls.add(video4);
        MediaStore.Video video5 = new MediaStore.Video("4", "5", "http://techslides.com/demos/sample-videos/small.mp4");
        urls.add(video5);
        MediaStore.Video video6 = new MediaStore.Video("5", "6", "http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4");
        urls.add(video6);
        MediaStore.Video video7 = new MediaStore.Video("6", "7", "http://sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4");
        urls.add(video7);

        mAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        videosDownloader.startVideosDownloading(urls);
    }
}
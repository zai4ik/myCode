package com.test.vkmusic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class audioService extends Service {

    int nowPos;
    String[] audioList, audioListUrl;
    String type;
    MediaPlayer mediaPlayer;
    NotificationManager mNotificationManager;

    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("App", "onCreate сервис");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("App", "onStartCommand сервис");
        type = intent.getStringExtra("type");
        Log.e("App", "type: " + type);
        if (type.equals("play")){
            nowPos = intent.getIntExtra("position", 1);
            Log.e("App", "position: " + nowPos);
            audioList = intent.getStringArrayExtra("audioList");
            Log.e("App", "audioList: " + audioList.length);
            audioListUrl = intent.getStringArrayExtra("audioListUrl");
            Log.e("App", "audioListUrl: " + audioListUrl.length);
            onPlayAudio();
            Log.e("App", "onPlayAudio()");
        }
        else if (type.equals("pre")) {onPreAudio();}
        else if (type.equals("next")) {onNextAudio();}
        else if (type.equals("pause")) {onPauseAudio();}
        else if (type.equals("stop")) {stopSelf(); mNotificationManager.cancel(1);}
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e("App", "onDestroy сервис");
    }

    public IBinder onBind(Intent intent) {
        Log.e("App", "onBind сервис");
        return null;
    }

    private void onPlayAudio(){
        if (mediaPlayer.isPlaying()) {
            Log.e("App", "Остановка проигровавшейся музыки");
            mediaPlayer.reset();
        }
        Uri myUri = Uri.parse(audioListUrl[nowPos]);
        Log.e("App", "Переменная Uri");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.e("App", "Назначение STREAM_MUSIC");
        try {
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            Log.e("App", "Назначение url исходного файла");
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("App", "Назначение загразки");
        mediaPlayer.start();
        Log.e("App", "Воспроизведение");
        onCreateNotification(nowPos);
    }

    private void onNextAudio(){
        Log.e("App", "Остановка проигровавшейся музыки");
        mediaPlayer.reset();
        nowPos++;
        Uri myUri = Uri.parse(audioListUrl[nowPos]);
        Log.e("App", "Переменная Uri");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.e("App", "Назначение STREAM_MUSIC");
        try {
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            Log.e("App", "Назначение url исходного файла");
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("App", "Назначение загразки");
        mediaPlayer.start();
        Log.e("App", "Воспроизведение");
        onCreateNotification(nowPos);
    }

    private void onPreAudio(){
        Log.e("App", "Остановка проигровавшейся музыки");
        mediaPlayer.reset();
        nowPos--;
        Uri myUri = Uri.parse(audioListUrl[nowPos]);
        Log.e("App", "Переменная Uri");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.e("App", "Назначение STREAM_MUSIC");
        try {
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            Log.e("App", "Назначение url исходного файла");
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("App", "Назначение загразки");
        mediaPlayer.start();
        Log.e("App", "Воспроизведение");
        onCreateNotification(nowPos);
    }

    private void onPauseAudio(){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mNotificationManager.cancel(1);
        }
        else {
            mediaPlayer.start();
            onCreateNotification(nowPos);
        }
    }

    public void onCreateNotification(Integer position){
        mNotificationManager.cancel(1);
        Notification notification = new Notification(android.R.drawable.ic_media_play, audioList[position], System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "ВК.Музыка", audioList[position], contentIntent);
        mNotificationManager.notify(1, notification);
    }
}

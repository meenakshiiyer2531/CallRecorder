package com.example.callrecorder;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 200;
    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private String audioFilePath = null;

    private Button btnRecord;
    private Button btnPlay;
    private MediaRecorder recorder = null;
    private static String fileName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecord = findViewById(R.id.btnRecord);
        btnPlay = findViewById(R.id.btnPlay);
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/Call recording.mp3";


    }
    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_AUDIO_PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    public void clickPlay(View view) {

        if (!isRecording) {
            btnPlay.setEnabled(true);
        }
        Toast.makeText(this, "Played Recording", Toast.LENGTH_SHORT).show();
        playRecording();
    }


    public void clickRecord(View view) throws IOException {
        if (isRecording) {
            stopRecording();
            isRecording = false;
            btnRecord.setText("Start Recording");
            btnPlay.setEnabled(true);
        } else {

            startRecording();
            btnRecord.setText("Stop Recording");
            btnPlay.setEnabled(false);

        }
    }
//    private void startRecording() throws IOException {
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setOutputFile(fileName);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//
//            recorder.prepare();
//
//
//        recorder.start();
//    }
//
//    private void stopRecording() {
//        recorder.stop();
//        recorder.release();
//        recorder = null;
//    }
    public void startRecording() {
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/Call recording.mp3";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(fileName);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecording = false;
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
    }

    public void playRecording() {
        // Implement code to play the recorded audio file using MediaPlayer or any other audio playback mechanism.
        // Here's a basic example to get you started:
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
                btnRecord.setText("Stop Recording");
                btnPlay.setEnabled(false);
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
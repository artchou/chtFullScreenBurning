package com.artchou.immersive02;

import android.content.Context;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    VideoView videoV;
    MediaController mediaC;
    int loopNum ;
    //Date fromTime = Calendar.getInstance().getTime();
    Calendar fromTime = Calendar.getInstance();

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chtLog";
    FileOutputStream outputStream;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoV = findViewById(R.id.videoView);
        mediaC = new MediaController(this);

        File dir = new File(path);
        dir.mkdirs();

        String videopath = "android.resource://com.artchou.immersive02/"+R.raw.videotest;
        Uri uri = Uri.parse(videopath);

        videoV.setVideoURI(uri);
        videoV.setMediaController(mediaC);
        videoV.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        videoV.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               // mVideoCompleted = true;
               loopNum ++;
                Date nTime = Calendar.getInstance().getTime();

                CharSequence sdf = DateFormat.format("yyyy-MM-dd kk:mm:ss", fromTime.getTime());
                Log.i("s",sdf.toString());
                String mTime = nTime.toString();
                Calendar now = Calendar.getInstance();
                String month = String.valueOf(fromTime.get(Calendar.MONTH) + 1); // Note: zero based!
                String day = String.valueOf(fromTime.get(Calendar.DAY_OF_MONTH));
                String hour = String.valueOf(fromTime.get(Calendar.HOUR_OF_DAY));
                String minute = String.valueOf(fromTime.get(Calendar.MINUTE));
                String second = String.valueOf(fromTime.get(Calendar.SECOND));
                String logFileName = month+day+hour+minute+second+".txt";
                Log.i("month",month);
                Log.i("day",day);
                Log.i("hour",hour);
                Log.i("minute",minute);
                Log.i("second",second);
                Log.i("logFileName",logFileName);


                //Log.i("MyActivity","MyClass.getView() - get item number"+position);
                File file=new File(path,logFileName);
                Log.i("PATH",path);
                Log.i("filename",logFileName);
                if(!file.exists())
                {
                    try {
                        file.createNewFile();
                        Log.i("file.exists","file.exists");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.i("not exist","file not exists, create new");
                    }
                }

                long diffT = nTime.getTime() - fromTime.getTime().getTime();
                long days = diffT / (1000 * 60 * 60 * 24);
                long hours = (diffT - days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                long minutes = (diffT - days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
                long seconds = (diffT - days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)- minutes*(1000 * 60))/(1000);

                final Toast tag = Toast.makeText(getApplicationContext(),
                        "run :" + loopNum + "\n"
                        +"start time at " + fromTime.getTime() + "\n"
                        +"runs time :" + hours+"小时"+minutes+"分" + seconds +"秒" +"\n\n",
                        Toast.LENGTH_LONG);
                tag.show();
                // add log to logfile under /sdcard/chtLog
                // by file name MDHMS, Month Date Hour Minute Second from nTime
                // save content loopNum + finish time
                //
                String logContent = String.valueOf(loopNum) +","+ hours+"小时"+minutes+"分" + seconds +"秒,\n";
                Log.i("logContent",logContent);

               // second try file writer
                try{
                    FileWriter fw = new FileWriter(path+"/"+logFileName,true);
                    BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                    bw.write(logContent);
                    //bw.newLine();
                    bw.close();
                }catch(IOException e){
                    e.printStackTrace();
                }


                /* first write to file, seems not work.
                try {
                    outputStream = openFileOutput(logFileName, Context.MODE_PRIVATE);
                    outputStream.write(logContent.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                */

                new CountDownTimer(90000, 1000)
                {

                    public void onTick(long millisUntilFinished) {tag.show();}
                    public void onFinish() {tag.show();}

                }.start();
            }
        });

        mediaC.setAnchorView(videoV);
        videoV.start();
    }


    public static void Save(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

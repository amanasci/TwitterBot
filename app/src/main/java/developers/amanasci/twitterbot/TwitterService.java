package developers.amanasci.twitterbot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();




        // do your jobs here after adding keys
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("");
        TwitterFactory tf = new TwitterFactory(cb.build());
        final Twitter twitter = tf.getInstance();


        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        /*This schedules a runnable task every second*/
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
               try {
                    //Getting Values from shared preferences
                    Integer[] Tweetno = {pref.getInt("Tweetno", 0)};
                    String name=pref.getString("Novel_Name", "Throne of World");
                    String link=pref.getString("Novel_Link","http://bit.ly/2PhCBuU");
                    String tags=pref.getString("Tags","#WritingCommunity #writers #Read #fantasy #fiction " +
                            "#reading #readingcommunity");
                    twitter.updateStatus(Tweetno[0].toString() + "\n" + name+ "\n" + link+ "\n" + tags);
                    Tweetno[0] +=1;
                    editor.putInt("Tweetno",Tweetno[0]);
                    editor.commit();

                } catch (TwitterException e) {
                    e.printStackTrace();
                }


            }
        }, 0, 6, TimeUnit.HOURS);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startmyOwnForeground();
        else
            startForeground(1, new Notification());

        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startmyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "developers.amanasci.twitterbot";
        String channelName = "TwitterBot Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_foreground))
                .setContentTitle("Bot is tweeting in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
}

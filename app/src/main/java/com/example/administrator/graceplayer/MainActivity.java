package com.example.administrator.graceplayer;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    //显示组件
    private ImageButton imgBtn_Previous;
    private ImageButton imgBtn_PlayOrPause;
    private ImageButton imgBtn_Stop;
    private ImageButton imgBtn_Next;
    private ListView list;

    //歌曲列表对象
    private ArrayList<Music> musicArrayList;

    /**Activity的任务之一的选择歌曲。在Activity中定义一个变量number，
    用于记录当前正在播放的歌曲的序号*/
    //当前歌曲的序号，下表从0开始
    private int number = 0;

    /* Called when the activity is first create */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        registerListeners();

        initMusicList();
        initListView();
        checkMusicfile();
    }

    /*获取显示组件*/
    private void findViews() {
        imgBtn_Previous = (ImageButton) findViewById(R.id.imageButton_previous);
        imgBtn_PlayOrPause = (ImageButton) findViewById(R.id.imageButton_play);
        imgBtn_Stop = (ImageButton) findViewById(R.id.imageButton_stop);
        imgBtn_Next = (ImageButton) findViewById(R.id.imageButton_next);
        list = (ListView) findViewById(R.id.listView1);

    }

    /**在Activity的显示组件的监听器中，调用写好的播放控制方法，就可以执行音乐播放了*/
    /*为显示器注册监听器*/
    private void registerListeners(){

        imgBtn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNumberToPrevious();
                play(number);
                imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_pause);
            }
        });
        imgBtn_PlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null && player.isPlaying()) {
                    pause();
                    imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_play);
                }else {
                    play(number);
                    imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_pause);
                }

            }
        });
        imgBtn_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_play);

            }
        });
        imgBtn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNumberToNext();
                play(number);
                imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_pause);

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number = position;
                play(number);
                imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_pause);
            }
        });

    }

    /**initMusicList方法中，遍历本地数据库，获取歌曲信息，使用这些信息我们可以对Music类的对象初始化，然后把这些对象添加到ArrayList中，最后我们通过操作ArrayList对象，
     * 可以获取歌曲名以及其他信息，这样做也符合面向对象编程的思想*/
    /*初始化音乐列表对象*/
    private void initMusicList() {
        musicArrayList = MusicList.getMusicList();
        //避免重复添加音乐
        if (musicArrayList.isEmpty())
        {
            //Cursor：可随机访问的结果集，用于保存数据库的查询结果
           Cursor mMusicCursor = this.getContentResolver().query(
              //MediaStore：基于SQLite的多媒体数据库，它包含了音频，视频，图片等所有多媒体文件的信息
                //MediaStore.Audio.Media.EXTERNAL_CONTENT_URI：Uri类的静态对象，存储了歌曲的路径。
                   MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                   MediaStore.Audio.AudioColumns.TITLE);
            //标题,MediaStore.Audio.AudioColumns.TITLE：歌曲标题在Cursor对象中的列名
            int indexTitle = mMusicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
            //艺术家
            int indexArtist = mMusicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
            //总时长
            int indexTotalTime = mMusicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
            //路径
            int indexPath = mMusicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);

            /**通过mMusicCursor游标遍历数据库，并将Music类对象加载到ArrayList中*/
            for(mMusicCursor.moveToFirst(); !mMusicCursor.isAfterLast();
                    mMusicCursor.moveToNext()) {
                String strTitle = mMusicCursor.getString(indexTitle);
                String strArtist = mMusicCursor.getString(indexArtist);
                String strTotoalTime = mMusicCursor.getString(indexTotalTime);
                String strPath = mMusicCursor.getString(indexPath);
                if (strArtist.equals("<unknown>"))
                    strArtist = "无艺术家";
                Music music = new Music(strTitle,strArtist,strPath,strTotoalTime);
                musicArrayList.add(music);
            }
        }
    }

    /**initListView方法中，我们定义了一个SimpleAdapter，作为listview的适配器。
     * 有了适配器，就可以设置歌曲列表中的内容了*/
    /*设置适配器并初始化listView*/
    private void initListView() {
        List<Map<String,String>> list_map = new ArrayList<Map<String,String>>();
        HashMap<String,String> map;
        SimpleAdapter simpleAdapter;
        for (Music music : musicArrayList) {
            map = new HashMap<String, String>();
            map.put("musicName",music.getMusicName());
            map.put("musicArtist",music.getMusicArtist());
            list_map.add(map);
        }
        String[] from = new String[] {"musicName","musicArtist"};
        int[] to = {R.id.listview_tv_title_item,R.id.listview_tv_artist_item};

        simpleAdapter = new SimpleAdapter(this,list_map,R.layout.listview,from,to);
        list.setAdapter(simpleAdapter);
    }


    /**如果手机中没有音乐文件的话执行播放操作会抛出异常。因此，如果手机中没有音乐文件，
     * 则将播放按钮设为不可以点击，然后通知提醒用户就可以了*/
    /*如果列表没有歌曲，则播放按钮不可用，并提醒用户*/
    private void checkMusicfile()
    {
        if(musicArrayList.isEmpty()){
            imgBtn_Next.setEnabled(false);
            imgBtn_PlayOrPause.setEnabled(false);
            imgBtn_Previous.setEnabled(false);
            imgBtn_Stop.setEnabled(false);
            Toast.makeText(getApplicationContext(),"当前没有歌曲文件",Toast.LENGTH_SHORT).show();
        }else {
            imgBtn_Next.setEnabled(true);
            imgBtn_PlayOrPause.setEnabled(true);
            imgBtn_Previous.setEnabled(true);
            imgBtn_Stop.setEnabled(true);
        }
    }

    //媒体播放类
    private MediaPlayer player = new MediaPlayer();

    /*读取音乐文件*/
    private  void load(int number) {
        try{
            player.reset();
            player.setDataSource(MusicList.getMusicList().get(number).getMusicPath());
            player.prepare();
        }catch (Exception e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*播放音乐*/
    private void play(int number) {
        //停止当前播放
        if(player != null && player.isPlaying()){
            player.stop();
        }
        load(number);
        player.start();
    }
    /*暂停音乐*/
    private void pause() {
        if (player.isPlaying()) {
            player.pause();
        }
    }
    /*停止播放*/
    private void stop() {
        player.stop();
    }
    /*恢复播放（暂停之后）*/
    private void remuse() {
        player.start();
    }
    /*重新播放(播放完成后)*/
    private void replay() {
        player.start();
    }

    /**定义两个修改游标number的方法moveNumberToNext()和moveNumberToPrevious()，
     * 分别用于选择下一曲和上一曲*/
    /*选择下一曲*/
    private void moveNumberToNext() {
        //判断是否到达了列表底端
        if ((number) == MusicList.getMusicList().size()-1){
            Toast.makeText(MainActivity.this,MainActivity.this.getString(R.string.tip_reach_bottom),Toast.LENGTH_SHORT).show();
        }else {
            ++number;
            play(number);
        }
    }

    /*选择上一曲*/
    private void moveNumberToPrevious() {
        //判断是否到达了列表顶端
        if (number == 0) {
            Toast.makeText(MainActivity.this,MainActivity.this.getString(R.string.tip_reach_top),Toast.LENGTH_SHORT).show();
            --number;
            play(number);
        }
    }

}

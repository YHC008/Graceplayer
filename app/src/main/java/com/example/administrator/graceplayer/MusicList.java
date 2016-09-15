package com.example.administrator.graceplayer;

/**
 * Created by Administrator on 2016/9/13.
 */

import java.util.ArrayList;

/**接着，我们编写MusicList.java文件的代码。
 该类包含了一个类型为Music的ArrayList对象，用于存放Music类的对象，
 起到了歌词列表的作用。
 该类采用单例模式，由于该类构造器修饰符为private，
 所以不能直接通过new方法生成该类的对象，
 该类提供了getMusicList方法，
 用户可以调用此方法获取唯一的ArrayList对象*/

/*MusicList类，采用单一实例，
* 只能通过getMusicList方法获取共享，
* 唯一的ArrayList<Music>对象*/

public class MusicList {
    private static ArrayList<Music> musicarray = new ArrayList<Music>();
    private MusicList(){}

    public static ArrayList<Music> getMusicList()
    {
        return musicarray;
    }
}

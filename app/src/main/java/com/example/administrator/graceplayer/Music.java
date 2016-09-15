package com.example.administrator.graceplayer;

/**
 * Created by Administrator on 2016/9/13.
 */
/**我们编写Music.java文件的代码。该类包含歌曲的基本属性，
 * 如歌曲名，艺术家名，歌曲路径，歌曲总时长等。
 * 该类提供一系列方法，便于用户获取歌曲名，艺术家名，
 * 歌曲路径，歌曲总时长*/
/*Music类，包括歌曲名，艺术家，路径，时长等属性，以及相关获取方法*/

public class Music {

    private String musicName;
    private String musicArtist;
    private String musicPath;
    private String musicDuration;

    public Music (String musicName,String musicArtist,String musicPath,String musicDuration)
    {
        this.musicName = musicName;
        this.musicArtist = musicArtist;
        this.musicDuration = musicDuration;
        this.musicPath = musicPath;
    }

    public String getMusicName()
    {
        return this.musicName;
    }

    public String getMusicArtist()
    {
        return this.musicArtist;
    }

    public String getMusicPath()
    {
        return this.musicPath;
    }
    public String getMusicDuration()
    {
        return this.musicDuration;
    }

}


package com.cst2335.project01;

/**
 * java DOB JOB simple class
 */
public class SongEntity {

    //Android Studio hint: to create getter and setter, put mouse on variable and
    // click "alt+insert" in Windows, "control+return" on Macintosh
    protected String  songTitle, artistName;
    protected int songId, artistId;
    protected long id;

    /**Constructor:*/
    public SongEntity(String songTitle, int songId, String artistName, int artistId, long i)
    {
        this.songTitle=songTitle;
        this.songId=songId;
        this.artistName=artistName;
        this.artistId=artistId;
        this.id= i;
    }

 //   public void update(String n, String e)
 //   {
 //       name = n;
 //       email = e;
  //  }

    /**Chaining constructor: */
 //   public SongEntity(String n, String e) {
 //       this(n, e, 0);
  //  }
    public int getArtistId() {
        return  artistId;
    }
    public String getArtistName() {
        return artistName;
    }
    public int getSongId() {
        return  songId;
    }
    public String getSongTitle() {
        return songTitle;
    }
    public long getId() {
        return id;
    }


}

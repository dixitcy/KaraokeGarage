package com.dcy.kandg;
	
public class KaraokeEvent extends Exception 
{
	private static final long serialVersionUID = 1L;
	public static final String NO_MICROPHONE = "noMicrophone";
	public static final String MICROPHONE_DENY = "denyMicrophone";
	public static final String MICROPHONE_ALLOW = "allowMicrophone";
	public static final String NO_LYRICS_FILE = "noLyricsFile";
	public static final String LYRICS_PARSE_SUCCESS = "lyricsParseSuccess";
	public static final String LYRICS_PARSE_FAIL = "lyricsParseFailure";		

	public KaraokeEvent ()
    {
    }

	public KaraokeEvent (String message)
    {
		super (message);
    }
}


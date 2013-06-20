package com.dcy.kandg;

import android.util.Log;


import java.util.HashMap;

public class Song
{
	public String fileName; 	/*name of songfile*/

	public static String LEAD_VOCAL 	= "Vocals";
	public HashMap<String, VocalTrack> vocalTracks;				 	/*Vocal tracks...Key=Value pair*/
	public VocalTrack dummyVocal;				/*notes for the sing part*/

	/*Is the song parsed from the file yet*/
	public static int LOAD_STATUS_NONE 		= 0;
	public static int LOAD_STATUS_HEADER 	= 1;
	public static int LOAD_STATUS_FULL	 	= 2;
	public int loadStatus;

	/*status of song*/
	private static int SONG_STATUS_NORMAL 				= 0;
	private static int SONG_STATUS_INSTRUMENTAL_BREAK 	= 1;
	private static int SONG_STATUS_FINISHED 			= 2;
	
    private static final String TAG = "KARAOKE_ENGINE";
    
	public Song(String songFilename)
	{ 
		dummyVocal = new VocalTrack(LEAD_VOCAL);
		fileName = songFilename;
		reload(false);
	}

	/*reload song*/
	public void reload(boolean errorIgnore)
	{
		loadStatus = LOAD_STATUS_NONE;
		vocalTracks = new HashMap<String, VocalTrack>();
		//SongParser(this); 
	}


	/*drop notes (to conserve memory), but keep info about available tracks*/
	/*void Song::dropNotes() 
	{
		// Singing
		if (!vocalTracks.empty()) 
		{
			for (VocalTracks::iterator it = vocalTracks.begin(); it != vocalTracks.end(); ++it)
				it->second.notes.clear();
		}
		b0rkedTracks = false;
		loadStatus = HEADER;
	}*/

	public void insertVocalTrack(String vocalTrack, VocalTrack track)
	{
		Log.d(TAG, "Inserting vocal track: " + vocalTrack);
		vocalTracks.remove(vocalTrack);
		vocalTracks.put(vocalTrack, track);
	}

	/*Get a selected track, or LEAD_VOCAL if not found or the first one if not found*/
	public VocalTrack getVocalTrack(String vocalTrack)
	{
		Log.d(TAG, "getVocalTrack: " + vocalTrack);		
		if (vocalTracks.containsKey(vocalTrack))
		{
			Log.d(TAG, "getVocalTrack returning vocal track:" + vocalTrack);
			return vocalTracks.get(vocalTrack);
		}
		else 
		{
			if(vocalTracks.containsKey(LEAD_VOCAL))
			{
				Log.d(TAG, "getVocalTrack returning vocal track:" + LEAD_VOCAL);
				return vocalTracks.get(LEAD_VOCAL);
			} 
			else 
			{
				for(String key: vocalTracks.keySet())
				{
					Log.d(TAG, "getVocalTrack returning vocal track:" + key);
					return vocalTracks.get(key);
				}

				Log.d(TAG, "getVocalTrack returning dummy vocal track");
				return dummyVocal;
			}
		}
	}

	/*std::vector<std::string> getVocalTrackNames() 
	{
		std::vector<std::string> result;
		BOOST_FOREACH(VocalTracks::value_type &it, vocalTracks) 
		{
			result.push_back(it.first);
		}
		return result;
	}*/

	/** Get the song status at a given timestamp **/
	/*Song::Status Song::status(Number time) 
	{
		Note target; target.end = time;
		Notes::const_iterator it = std::lower_bound(getVocalTrack().notes.begin(), getVocalTrack().notes.end(), target, noteEndLessThan);
		if (it == getVocalTrack().notes.end()) 
			return FINISHED;
		if (it->begin > time + 4.0) 
			return INSTRUMENTAL_BREAK;
		return NORMAL;
	}*/
	
	public void print()
	{
		Log.d(TAG, "Parsed file output:::::::");
		
		VocalTrack vocalTrack = getVocalTrack(LEAD_VOCAL);
		
		Log.d(TAG, "Song:::: BEGIN TIME: " + vocalTrack.beginTime + " END TIME:" + vocalTrack.endTime);
		Log.d(TAG, "Song::::MIN NOTE:" + vocalTrack.noteMin + " MAX NOTE:" + vocalTrack.noteMax);

		double len = vocalTrack.notes.size();

		Note note;
		for (int i = 0; i < len; i++)
		{
			note = vocalTrack.notes.elementAt(i);

			if(note.type == Note.NORMAL)
			{
				Log.d(TAG, "Song::::: |" + note.begin + "|" + note.end + "|" + note.note + "|" + note.syllable + "");
			}
			else if(note.type == Note.SLEEP)
			{
				Log.d(TAG, "Song:::: |" + note.begin + "|" + note.end );
			}
		}
	}
}

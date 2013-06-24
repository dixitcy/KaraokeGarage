package com.dcy.kandg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import android.content.Context;
import android.content.res.AssetManager;

import org.apache.commons.io.FileUtils;

import android.util.Log;

public class SongParser {
	public String[] m_lines;
	public Song m_song;
	public int m_linenum;
	public boolean m_relative;
	public double m_gap;
	public double m_bpm;
	public double m_prevtime;
	public int m_prevts;
	public int m_relativeShift;
	public double m_maxScore;
	public int m_tsEnd; /* The ending ts of the song */
	public String txtr;
	Context mContext;

	public Vector<BPM> m_bpms = new Vector<BPM>();
	private static final String TAG = "KARAOKE_ENGINE";

	// Convert InputStream to String. Here we get the InputStream String from
	// which we get he Lyrics and other information.
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	// ///////////////////////////////////// INPUTSTREAM STRING
	// ///////////////////////////////////////////////////

	private void lyricsFileLoaded(String txt) throws KaraokeEvent {

		Log.d(TAG, "Loading lyrics text file::::::::::::::::::::::::::::");
		// var txt:String = event.target.data;
		// trace(txt);

		if (false == txtCheck(txt)) {
			Log.d(TAG, "Invalid lyrics file::::::::::::::::::::::::::::");
			throw (new KaraokeEvent(KaraokeEvent.LYRICS_PARSE_FAIL));
		}

		Log.d(TAG, "Splitting lyrics:::::::::::::::::::::::::::::::::::");
		m_lines = txt.split("\n");
		System.out.println(m_lines);

		// txtParseHeader();
		txtParse();
		finalize(); /* Do some adjusting to the notes */

		Log.d(TAG, "Parse Complete:::::::::::::::::::::::::::::::::::");

		m_song.print();

		try {
			m_song.loadStatus = Song.LOAD_STATUS_FULL;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getline() {
		m_linenum++;
		if (m_linenum >= m_lines.length) {
			return null;
		}

		// Log.d(TAG, "Returning next lyric :::: " + m_lines[m_linenum]);
		return m_lines[m_linenum];
	}

	public SongParser(Song s) throws KaraokeEvent {
		m_linenum = -1;
		m_song = s;
		m_relative = false;
		m_gap = 0;
		m_bpm = 0.0;
		m_prevtime = 0;
		m_prevts = 0;
		m_relativeShift = 0;
		m_maxScore = 0;
		m_tsEnd = 0;

		// File file = new File(s.fileName);
		// System.out.print(s.fileName);

		// String fileContent = FileUtils.readFileToString(file, "UTF_8");
		// Log.d(TAG, fileContent);

		String fileContent = s.fileName;
		lyricsFileLoaded(fileContent);

	}

	public boolean txtCheck(String lyricsFile) {
		return (lyricsFile.charAt(0) == '#' && lyricsFile.charAt(1) >= 'A' && lyricsFile
				.charAt(1) <= 'Z');
	}

	/* Parse header data for Songs screen */
	/*
	 * public function txtParseHeader():void { Song& s = m_song; std::string
	 * line;
	 * 
	 * while (getline(line) && txtParseField(line)) {}
	 * 
	 * if (m_bpm != 0.0) addBPM(0, m_bpm);
	 * 
	 * s.insertVocalTrack(TrackName::LEAD_VOCAL,
	 * VocalTrack(TrackName::LEAD_VOCAL)); // Dummy note to indicate there is a
	 * track }
	 */

	/* Parse notes and header */
	public void txtParse() {
		String line;
		boolean result;
		VocalTrack vocal = new VocalTrack(Song.LEAD_VOCAL);

		Log.d(TAG, "Parsing headers");

		/* Parse Header */
		line = getline();
		while (null != line) {
			result = txtParseField(line);
			if (false == result) {
				break;
			}

			line = getline();
		}

		Log.d(TAG, "Parsing headers complete");

		if (m_bpm != 0.0) {
			Log.d(TAG, "BPM header found, adding header");
			addBPM(0, m_bpm);
		}

		/* Parse notes */
		while (null != line) {
			result = txtParseNote(line, vocal);
			Log.d(TAG, "WORKING");
			if (false == result) {
				Log.d(TAG, "Parse Note failed or Complete " + line);
				break;

			}

			line = getline();

		}

		Log.d(TAG, "Parsing notes complete");

		/*
		 * Workaround for the terminating : 1 0 0 line, written by some
		 * converters
		 */
		if ((vocal.notes.size() != 0)
				&& (vocal.notes.get(vocal.notes.size() - 1).type != Note.SLEEP)
				&& (vocal.notes.get(vocal.notes.size() - 1).begin == vocal.notes
						.get(vocal.notes.size() - 1).end)) {
			Log.e(TAG, "Removing last invalid note....");
			vocal.notes.remove(vocal.notes.size() - 1);
		}

		// m_song.insertVocalTrack(Song.LEAD_VOCAL, vocal);
	}

	/* header parsing */
	public boolean txtParseField(String line) {
		if (0 == line.length()) {
			Log.e(TAG, "Invalid length of line in txtParseField");
			return true;
		}

		if (line.charAt(0) != '#') {
			Log.e(TAG, "Invalid txt format, # should be first character");
			return false;
		}

		int pos = line.indexOf(':');
		if (-1 == pos) {
			Log.e(TAG, "Invalid txt format, should be #key:value, Line is :: "
					+ line);
			return false;
		}

		String key = line.substring(1, pos).trim();
		String value = line.substring(pos + 1, line.length() - 1).trim();

		Log.d(TAG, "Header Found :: Key =  " + key + " Value = " + value
				+ " Pos = " + pos);

		if (0 == value.length()) {
			return true;
		} else if (key.equals("RELATIVE")) {
			m_relative = strToBoolean(value);
			Log.d(TAG, "RELATIVE =  " + m_relative);
		} else if (key.equals("GAP")) {
			m_gap = strToNumber(value);
			m_gap *= 1e-3;
			Log.d(TAG, "GAP =  " + m_gap);
		} else if (key.equals("BPM")) {
			m_bpm = strToNumber(value);
			Log.d(TAG, "BPM =  " + m_bpm);
		}

		return true;
	}

	String myString = "#";
	String[] myStringArray = new String[300];
	int k = 0;

	/* parse body */
	public boolean txtParseNote(String line, VocalTrack vocal) {

		Log.d(TAG, "Parser: Parsing note for line -" + line);

		if ((line.length() == 0) || (line == "\r")) {
			Log.e(TAG, "Invalid length of line in txtParseNote");
			return true;
		}

		if (line.charAt(0) == '#') {
			Log.e(TAG, "Error:::Key found in the middle of notes");
			return true;
		}

		if ((line.charAt(line.length() - 1) == '\r')
				|| (line.charAt(line.length() - 1) == '\n')) {
			line = line.trim();
		}

		if (line.charAt(0) == 'E') {
			Log.e(TAG, "Error:::E found at 0th index");
			return false;
		}

		String[] values;
		int ts;
		double bpm;
		int length = 0;
		int end;

		if (line.charAt(0) == 'B') {
			values = line.split(" ");
			if (values.length < 3) {
				Log.e(TAG, "Invalid BPM line found");
				return false;
			}

			ts = strToInt(values[1]);
			bpm = strToNumber(values[2]);
			addBPM(ts, bpm);

			return true;
		}

		if (line.charAt(0) == 'P') {
			return true; /*
						 * We ignore player information for now (multiplayer
						 * hack)
						 */
		}

		Note n = new Note();
		n.type = line.charAt(0);

		ts = m_prevts;

		// trace("Note type is " + n.type);
		switch (n.type) {
		case Note.NORMAL:
		case Note.FREESTYLE:
		case Note.GOLDEN: {
			values = line.split(" ");

			// trace("LENGTH:::" + values.length);

			if (values.length < 4) {
				Log.e(TAG, "Invalid Note line found");
				return false;
			}

			ts = strToInt(values[1]);
			length = strToInt(values[2]);
			n.note = strToInt(values[3]);

			// trace("Parser: ts:len:note = " + ts + ":" + length + ":" +
			// n.note);

			n.notePrev = n.note; /* No slide notes in TXT yet. */

			if (m_relative) {
				ts += m_relativeShift;
			}

			/*
			 * Format is like : 1 6 64 Eve(Single space before syllable) or : 1
			 * 6 64 Eve(two space before syllable, reuired for spaces between
			 * words)
			 */
			if (values.length > 5) {
				n.syllable = " " + values[5];

			} else if (values.length > 4) {
				n.syllable = values[4];
				// Log.d("MY TAG", values[4]);

			}

			n.end = tsTime(ts + length);

			break;
		}

		case Note.SLEEP: {
			values = line.split(" ");
			if (values.length < 3) {
				ts = strToInt(values[1]);
				end = ts;
			} else {
				ts = strToInt(values[1]);
				end = strToInt(values[2]);
			}

			// trace("Parser: ts:end = " + ts + ":" + end);

			if (m_relative) {
				ts += m_relativeShift;
				end += m_relativeShift;
				m_relativeShift = end;
			}

			n.end = tsTime(end);
			break;
		}

		default: {
			Log.e(TAG, "Invalid note type = " + n.type);
			return false;
		}
		}

		n.begin = tsTime(ts);

		Vector<Note> notes = vocal.notes;
		if (m_relative && notes.size() == 0) {
			m_relativeShift = ts;
		}
		m_prevts = ts;

		// trace("Calculated begin and end times :: " + n.begin + " " + n.end);

		if (n.begin < m_prevtime) {
			Log.e(TAG, "ERROR!!!!!!!!!!!!BROKEN FILE::Begin" + n.begin
					+ " PrevTime" + m_prevtime);

			/*
			 * Oh no, overlapping notes (b0rked file) Can't do this because too
			 * many songs are b0rked: throw
			 * std::runtime_error("Note overlaps with previous note");
			 */
			if (notes.size() >= 1) {
				Note p = notes.get(notes.size() - 1);

				/*
				 * Workaround for songs that use semi-random timestamps for
				 * sleep
				 */
				if (p.type == Note.SLEEP) {
					p.end = p.begin;

					if (notes.size() >= 2) {
						Note p2 = notes.get(notes.size() - 2);

						if (p2.end < n.begin) {
							p.begin = p.end = n.begin;
						}
					}
				}

				/* Can we just make the previous note shorter */
				if (p.begin <= n.begin) {
					p.end = n.begin;
				} else {
					/* Nothing to do, warn and skip */
					Log.e(TAG, "ERROR:::::Skipping overlapping note in "
							+ m_song.fileName);
					return true;
				}
			} else {
				Log.e(TAG, "ERROR:::::The first note has negative timestamp");
				return false;
			}
		}

		double prevtime = m_prevtime;
		m_prevtime = n.end;

		if (n.type != Note.SLEEP && n.end > n.begin) {
			if (vocal.noteMin > n.note) {
				vocal.noteMin = n.note;
			}

			if (vocal.noteMax < n.note) {
				vocal.noteMax = n.note;
			}

			m_maxScore += n.maxScore();
		}

		if (n.type == Note.SLEEP) {
			if (notes.size() == 0) {
				return true; /* Ignore sleeps at song beginning */
			}

			n.begin = n.end = prevtime; /* Normalize sleep notes */

			Log.d(TAG, "Normailizing Notesssss " + n.end);
		}

		if (n.syllable == "") {
			myString = myString + "\n";
		} else {
			myString = myString + " " + n.syllable + " ";
		}
		myStringArray[k] = myString;
		Log.d("CHECK THIS", myString);
		Log.d("CHECK ARRAY", myStringArray[k]);

		k = k + 1;

		notes.add(n);

		return true;
	}

	public void finalize() {
		Log.d(TAG, "finalizing notes");

		for (String key : m_song.vocalTracks.keySet()) {
			/* Adjust negative notes */
			Log.d(TAG, "Adjust negative notes for vocal track " + key);
			VocalTrack vocal = m_song.vocalTracks.get(key);

			if (vocal.noteMin <= 0) {
				int shift = (1 - vocal.noteMin / 12) * 12;
				vocal.noteMin += shift;
				vocal.noteMax += shift;

				int i;
				Note element;
				for (i = 0; i < vocal.notes.size(); i++) {
					element = vocal.notes.get(i);
					element.note += shift;
					element.notePrev += shift;
				}
			}

			/* set begin/end times */
			if (vocal.notes.size() > 0) {
				vocal.beginTime = vocal.notes.get(0).begin;
				vocal.endTime = vocal.notes.get(vocal.notes.size() - 1).end;
			}

			vocal.m_scoreFactor = 1.0 / m_maxScore;
		}
	}

	public void addBPM(double ts, double bpm) {
		if (!(bpm >= 1.0 && bpm < 1e12)) {
			Log.e(TAG, "ERROR:::addBPM:::Invalid BPM value");
			return;
		}

		if ((m_bpms.size() != 0) && (m_bpms.get(m_bpms.size() - 1).ts >= ts)) {
			if (m_bpms.get(m_bpms.size() - 1).ts < ts) {
				Log.d(TAG, "ERROR:::addBPM:::Invalid BPM timestamp");
				return;
			}

			m_bpms.removeElementAt(m_bpms.size() - 1); /*
														 * Some ITG songs
														 * contain repeated BPM
														 * definitions...
														 */
		}

		m_bpms.add((new BPM(tsTime(ts), ts, bpm)));
	}

	/* Convert a timestamp (beats) into time (seconds) */
	public double tsTime(double ts) {
		if (m_bpms.size() <= 0) {
			if (ts != 0) {
				Log.d(TAG, "ERROR:::tsTime:::BPM data missing");
				return 0;
			}
			return m_gap;
		}

		int i;
		for (i = m_bpms.size() - 1; i >= 0; i--) {
			if (m_bpms.get(i).ts <= ts) {
				return m_bpms.get(i).begin + (ts - m_bpms.get(i).ts)
						* m_bpms.get(i).step;
			}
		}

		Log.e(TAG, "ERROR:::tsTime:::INTERNAL ERROR: BPM data invalid");
		return 0;
	}

	public int strToInt(String str) {
		return Integer.parseInt(str);
	}

	public double strToNumber(String str) {
		// str.replace(',', '.'); /*Fix decimal separators*/
		return Double.parseDouble(str.replace(',', '.'));
	}

	public boolean strToBoolean(String str) {
		boolean result = false;
		if (str == "YES" || str == "yes" || str == "1") {
			result = true;
		} else if (str == "NO" || str == "no" || str == "0") {
			result = false;
		}

		return result;
	}

};
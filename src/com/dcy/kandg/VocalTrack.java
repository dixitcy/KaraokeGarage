package com.dcy.kandg;

import java.util.Vector;

public class VocalTrack 
{
	public String name;
	public Vector<Note> notes;
	public int noteMin;
	public int noteMax;			/* lowest and highest note*/
	public double beginTime;
	public double endTime;		/*the period where there are notes*/
	public double m_scoreFactor; 	/*normalization factor for the scoring system*/
	public MusicalScale scale;		/*scale in which song is sung*/

	public VocalTrack(String n)
	{
		name = n;
		notes = new Vector<Note>();
		scale = new MusicalScale(440.0);
		reload();
	}

	public void reload()
	{
		notes = new Vector<Note>();
		m_scoreFactor = 0.0;
		noteMin = 2147483647;		/*Int max*/
		noteMax = -2147483648;	/*Int min*/
		beginTime = Double.NaN;
		endTime = Double.NaN;
	}
};
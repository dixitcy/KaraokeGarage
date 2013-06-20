package com.dcy.kandg;


import java.lang.String;

//typedef std::map<std::string, VocalTrack> VocalTracks;

public class Note
{
	public double begin;; 	/*begin time*/
	public double end;		/* end time */
	public double phase; 	/*Position within a measure, [0, 1*/
	public double power;	/*power of note (how well it is being hit right now)*/
	public boolean stars;	/*For performous it is list of players who sung well. For us it is only one player so boolean*/
	public int note; 			/* MIDI pitch of the note (at the end for slide notes)*/
	public int notePrev;		/*< MIDI pitch of the previous note (should be same as note for everything but SLIDE)*/
	public String syllable;	/* lyrics syllable for that note*/
	public int type;			/*Note type*/

	/*note type*/
	public static final int FREESTYLE 	= 'F';
	public static final int NORMAL 		= ':';
	public static final int GOLDEN 		= '*';
	public static final int SLIDE	 	= '+';
	public static final int SLEEP		= '-';
	public static final int TAP 		= '1';
	public static final int HOLDBEGIN 	= '2';
	public static final int HOLDEND 	= '3';
	public static final int ROLL 		= '4';
	public static final int MINE 		= 'M';
	public static final int LIFT 		= 'L';
	
	public Note()
	{
		begin 	= Double.NaN;
		end 	= Double.NaN;
		phase 	= Double.NaN;
		power	= Double.NaN;
		type 	= NORMAL;
		note 	= 0;
		notePrev = 0;
		stars 	= false;
		syllable	= "";
	}

	/*Difference of n from note*/
	public double diff(double n) 
	{ 
		return diff2(note, n); 
	}
	
	/*Difference of n from note, so that note + diff(note, n) is n (mod 12)*/
	public static double diff2(double note, double n)
	{
		return Util.remainder(n - note, 12.0);
	}
	
	/*maximum score*/
	public double maxScore()
	{
		return scoreMultiplier() * (end - begin); 
	}
	
	/*score when singing over time period (a, b), which needs not to be entirely within the note*/
	 public double score( double n, double b, double e)
	{
		 double len = Math.min(e, end) - Math.max(b, begin);
		if (len <= 0.0 || !(n > 0.0)) 
		{
			return 0.0;
		}
		
		return scoreMultiplier() * powerFactor(n) * len;
	}
	
	/* How precisely the note is hit (always 1.0 for freestyle, 0..1 for others)*/
	public double powerFactor(double note) 
	{
		if (type == FREESTYLE) 
		{
			return 1.0;
		}
		
		double error = Math.abs(diff(note));
		return (Util.clamp(1.5 - error, 0.0, 1.0));
	}

	/*compares begin of two notes*/
	/*static bool ltBegin(Note const& a, Note const& b) 
	{
		return a.begin < b.begin;
	}*/

	/* compares end of two notes*/
	/*static bool ltEnd(Note const& a, Note const& b)
	{
		return a.end < b.end; 
	}*/

	public double scoreMultiplier() 
	{
		return (type == GOLDEN ? 2.0 : 1.0); 
	}
}


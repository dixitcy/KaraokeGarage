package com.dcy.kandg;

public class BPM 
{
	public double begin;	/* Time in seconds*/
	public double step; 	/* Seconds per quarter note*/
	public double ts; 
	
	public BPM( double _begin, double _ts, double bpm)
	{
		 begin = _begin;
		 step = (0.25 * 60.0 / bpm);
		 ts = _ts; 
	}
};
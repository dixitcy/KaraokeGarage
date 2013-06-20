package com.dcy.kandg;

public class Util
{
	//private static int TEXT_SIZE = 13;
	
	public static double clamp(double val, double min, double max)
	{
		if (min > max)
		{
			return max;
		}
		
		if (val < min) 
		{
			return min;
		}
		
		if (val > max)
		{
			return max;
		}
		
		return val;
	}

	/** Implement C99 remainder function (not precisely, but almost) **/
	public static double remainder(double val, double div)
	{
		return (val - Math.round(val/div) * div);
	}
	
	/*public static function getTextFormat(color:uint, size:int = 13):TextFormat
	{
	    var textFormat:TextFormat = new TextFormat();
	    textFormat.font = "Arial";
	    textFormat.size = size;
	    textFormat.color = color;
	    textFormat.bold = true;
	    textFormat.align = TextFormatAlign.CENTER;
	    return textFormat;
	}*/
}


package com.dcy.kandg;

/*musical scale, defaults to C major*/
public class MusicalScale 
{
	public double m_baseFreq;
	public static int m_baseId = 33;
	
	public MusicalScale(double baseFreq)
	{
		m_baseFreq = baseFreq;
	}

	/*get name of note*/
	//std::string getNoteStr(Number freq) const;
	
	/*get note number for id*/
	//unsigned int getNoteNum(int id) const;
	
	/*true if sharp note*/
	//bool isSharp(int id) const;
	
	/*get frequence for note id*/
	//Number getNoteFreq(int id) const;
	
	/*get note id for frequence*/
	//int getNoteId(Number freq) const;
	
	/*get note for frequencey*/
	public double getNote(double freq) 
	{
		if (freq < 1.0) 
		{
			return Double.NaN;
		}
		
		//return (m_baseId + 12.0 * Math.log(freq / m_baseFreq) / Math.log(2.0));
		return (36 + m_baseId + 12.0 * Math.log(freq / m_baseFreq) / Math.log(2.0));			
	}		
	
	/*get note offset for frequence*/
	//Number getNoteOffset(Number freq) const;
}





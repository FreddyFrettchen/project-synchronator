package com.swe.prototype.helpers;

/**
 * @author Dany Brossel
 *
 *Diese Klasse ist ne Datenklasse weil wir dem CalendaerAdapter der MonthVIew sagen müssen, wann ein event ist und auf welchem konto das gespeichert ist, damit es in der montview angezeigt werden kann.
 */
public class DateOnSaveLocation {

	private String date;
	private boolean s;
	private boolean g;
	private boolean e;

	public DateOnSaveLocation(String date, boolean s, boolean g, boolean e) {
		this.date = date;
		this.s = s;
		this.g = g;
		this.e = e;
	}
	
	public int length(){
		return this.date.length();
	}
	

	public String getDate() {
		return this.date;
	}

	public DateOnSaveLocation setDate(String date) {
		this.date = date;
		return this;
	}

	public boolean showS() {
		
		return s;
	}

	public boolean showE() {
		return e;
	}

	public boolean showG() {
		return g;
	}
	public void setS(){
		s=true;
	}
	public void setG(){
		g=true;
	}
	public void setE(){
		e=true;
	}

}

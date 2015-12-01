/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.exchange.msProject.importer;

import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.LAG_FORMAT;
import com.aurel.track.item.budgetCost.AccountingBL;

/**
 * Helper class for linkLag and lagFormat 
 * @author Tamas
 *
 */
public class LinkLagBL {
	private static double ONE_TENTH_MINUTE = 0.1;
	private static int MINUTES_IN_HOUR = 60;
	private static int HOURS_PER_EFFECTIVE_DAY = 24;
	private static int WORKDAYS_IN_WEEK = 5;
	private static int DAYS_IN_WEEK = 7;
	private static int WORKDAYS_IN_MONAT = 20;
	private static int DAYS_IN_MONAT = 30;
	
	/**
	 * Get the days from minutes or percents
	 * @param linkLag
	 * @param lagFormat
	 * @param hoursPerWorkday
	 * @param predecessorDurationHours
	 * @return
	 */
	public static int getDaysFromLinkLag(double linkLag, int lagFormat, double hoursPerWorkday, Double predecessorDurationHours) {							
		switch (lagFormat) {
		case LAG_FORMAT.m:
		case LAG_FORMAT.em:
		case LAG_FORMAT.h:
		case LAG_FORMAT.eh:
		case LAG_FORMAT.d:
		case LAG_FORMAT.w:
		case LAG_FORMAT.mo:
		case LAG_FORMAT.mEST:
		case LAG_FORMAT.emEST:
		case LAG_FORMAT.hEST:
		case LAG_FORMAT.ehEST:
		case LAG_FORMAT.dEST:
		case LAG_FORMAT.wEST:
		case LAG_FORMAT.moEST:
			return new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*hoursPerWorkday)).intValue();				
		case LAG_FORMAT.PERCENT:
		case LAG_FORMAT.PERCENTEST:
			return new Double(linkLag*predecessorDurationHours/(hoursPerWorkday*100)).intValue();		
		case LAG_FORMAT.ePERCENT:
		case LAG_FORMAT.ePERCENTEST:
			return new Double(linkLag*predecessorDurationHours/(HOURS_PER_EFFECTIVE_DAY*100)).intValue();
		case LAG_FORMAT.ed:
		case LAG_FORMAT.edEST:
			//1 effective day
			return new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*HOURS_PER_EFFECTIVE_DAY)).intValue();			
		case LAG_FORMAT.ew:
		case LAG_FORMAT.ewEST:
			//7 effective day
			return new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*HOURS_PER_EFFECTIVE_DAY*DAYS_IN_WEEK)).intValue();			
		case LAG_FORMAT.emo:
		case LAG_FORMAT.emoEST:
			//30 effective day
			return new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*HOURS_PER_EFFECTIVE_DAY*DAYS_IN_MONAT)).intValue();								
		}
		return 0;
	}
	/**
	 * Prepares the user interface link lag value from minutes or percent
	 * @param linkLag
	 * @param lagFormat
	 * @param hoursPerWorkday
	 * @return
	 */
	public static double getUILinkLagFromMinutes(Double linkLag, Integer lagFormat, double hoursPerWorkday) {
		if (linkLag==null) {
			return 0;
		}
		switch (lagFormat.intValue()) {
		case LAG_FORMAT.m:
		case LAG_FORMAT.em:
		case LAG_FORMAT.mEST:
		case LAG_FORMAT.emEST:	
			return AccountingBL.roundToDecimalDigits(new Double(linkLag*ONE_TENTH_MINUTE), true);
		case LAG_FORMAT.h:
		case LAG_FORMAT.eh:
		case LAG_FORMAT.hEST:
		case LAG_FORMAT.ehEST:
			return AccountingBL.roundToDecimalDigits(new Double(linkLag*ONE_TENTH_MINUTE/MINUTES_IN_HOUR), true);
		case LAG_FORMAT.d:
		case LAG_FORMAT.dEST:
			return AccountingBL.roundToDecimalDigits(new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*hoursPerWorkday)), true);
		case LAG_FORMAT.ed:
		case LAG_FORMAT.edEST:
			//1 effective day
			return AccountingBL.roundToDecimalDigits(new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*HOURS_PER_EFFECTIVE_DAY)), true);
		case LAG_FORMAT.w:
		case LAG_FORMAT.wEST:
			return AccountingBL.roundToDecimalDigits(new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*hoursPerWorkday*WORKDAYS_IN_WEEK)), true);
		case LAG_FORMAT.ew:
		case LAG_FORMAT.ewEST:
			//7 effective day
			return AccountingBL.roundToDecimalDigits(new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*HOURS_PER_EFFECTIVE_DAY*DAYS_IN_WEEK)), true);
		case LAG_FORMAT.mo:		
		case LAG_FORMAT.moEST:
			return AccountingBL.roundToDecimalDigits(new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*hoursPerWorkday*WORKDAYS_IN_MONAT)), true);
		case LAG_FORMAT.emo:
		case LAG_FORMAT.emoEST:
			//30 effective day
			return AccountingBL.roundToDecimalDigits(new Double(linkLag*ONE_TENTH_MINUTE/(MINUTES_IN_HOUR*HOURS_PER_EFFECTIVE_DAY*DAYS_IN_MONAT)), true);
		case LAG_FORMAT.PERCENT:
		case LAG_FORMAT.PERCENTEST:
		case LAG_FORMAT.ePERCENT:
		case LAG_FORMAT.ePERCENTEST:
			return linkLag;
		}
		return 0;
	}
	/**
	 * Prepares the user friendly link lag value from minutes or percent
	 * @param linkLag
	 * @param lagFormat
	 * @param hoursPerWorkday
	 * @return
	 */
	public static int getMinutesFromUILinkLag(Double linkLag, int lagFormat, double hoursPerWorkday) {
		switch (lagFormat) {
		case LAG_FORMAT.m:
		case LAG_FORMAT.em:
		case LAG_FORMAT.mEST:
		case LAG_FORMAT.emEST:
			return new Double(linkLag/ONE_TENTH_MINUTE).intValue();
		case LAG_FORMAT.h:
		case LAG_FORMAT.eh:
		case LAG_FORMAT.hEST:
		case LAG_FORMAT.ehEST:
			return new Double(linkLag*MINUTES_IN_HOUR/ONE_TENTH_MINUTE).intValue();
		case LAG_FORMAT.d:
		case LAG_FORMAT.dEST:
			return new Double(linkLag*MINUTES_IN_HOUR*hoursPerWorkday/ONE_TENTH_MINUTE).intValue();
		case LAG_FORMAT.ed:
		case LAG_FORMAT.edEST:
			//1 effective day
			return new Double(linkLag*MINUTES_IN_HOUR*HOURS_PER_EFFECTIVE_DAY/ONE_TENTH_MINUTE).intValue();
		case LAG_FORMAT.w:
		case LAG_FORMAT.wEST:
			return new Double(linkLag*MINUTES_IN_HOUR*hoursPerWorkday*WORKDAYS_IN_WEEK/ONE_TENTH_MINUTE).intValue();
		case LAG_FORMAT.ew:
		case LAG_FORMAT.ewEST:
			//7 effective day
			return new Double(linkLag*MINUTES_IN_HOUR*HOURS_PER_EFFECTIVE_DAY*DAYS_IN_WEEK/ONE_TENTH_MINUTE).intValue();
		case LAG_FORMAT.mo:		
		case LAG_FORMAT.moEST:
			return new Double(linkLag*MINUTES_IN_HOUR*hoursPerWorkday*WORKDAYS_IN_MONAT/ONE_TENTH_MINUTE).intValue();
		case LAG_FORMAT.emo:
		case LAG_FORMAT.emoEST:
			//30 effective day
			return new Double(linkLag*MINUTES_IN_HOUR*HOURS_PER_EFFECTIVE_DAY*DAYS_IN_MONAT/ONE_TENTH_MINUTE).intValue();
		case LAG_FORMAT.PERCENT:
		case LAG_FORMAT.PERCENTEST:
		case LAG_FORMAT.ePERCENT:
		case LAG_FORMAT.ePERCENTEST:
			return new Double(linkLag).intValue();
		}
		return 0;
	}
	
	/**
	 * Whether the hoursPerWorkday value is needed for future calculation 
	 * @param lagFormat
	 * @return
	 */
	public static boolean isHoursPerWorkdayNeeded(Integer lagFormat) {
		if (lagFormat!=null) {
			switch (lagFormat.intValue()) {
			case LAG_FORMAT.d:
			case LAG_FORMAT.w:
			case LAG_FORMAT.mo:
			case LAG_FORMAT.dEST:
			case LAG_FORMAT.wEST:
			case LAG_FORMAT.ewEST:
			case LAG_FORMAT.moEST:
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Whether the lag format is percent
	 * @param lagFormat
	 * @return
	 */
	public static boolean lagFormatIsPercent(int lagFormat) {
		switch (lagFormat) {
		case LAG_FORMAT.PERCENT:
		case LAG_FORMAT.ePERCENT:
		case LAG_FORMAT.PERCENTEST:
		case LAG_FORMAT.ePERCENTEST:
			return true;
		default:
			return false;
		}
	}
	
	
	/**
	 * Get the user interface lagFormat
	 * @param lagFormatInt
	 * @return
	 */
	public static String getUILagFormat(Integer lagFormatInt) {
		String lagFormat = "";
		if (lagFormatInt!=null) {
			switch (lagFormatInt.intValue()) {
			case LAG_FORMAT.m:
				lagFormat = "m";
				break;
			case LAG_FORMAT.em:
				lagFormat = "em";
				break;
			case LAG_FORMAT.h:
				lagFormat = "h";
				break;
			case LAG_FORMAT.eh:
				lagFormat = "eh";
				break;			
			case LAG_FORMAT.d:
				lagFormat = "d";
				break;
			case LAG_FORMAT.ed:
				lagFormat = "ed";
				break;	
			case LAG_FORMAT.w:
				lagFormat = "w";
				break;
			case LAG_FORMAT.ew:
				lagFormat = "ew";
				break;	
			case LAG_FORMAT.mo:
				lagFormat = "mo";
				break;
			case LAG_FORMAT.emo:
				lagFormat = "emo";
				break;
			case LAG_FORMAT.PERCENT:
				lagFormat = "%";
				break;
			case LAG_FORMAT.ePERCENT:
				lagFormat = "e%";
				break;		
			case LAG_FORMAT.mEST:
				lagFormat = "m?";
				break;			
			case LAG_FORMAT.emEST:
				lagFormat = "em?";
				break;				
			case LAG_FORMAT.hEST:
				lagFormat = "h?";
				break;				
			case LAG_FORMAT.ehEST:
				lagFormat = "eh?";
				break;
			case LAG_FORMAT.dEST:
				lagFormat = "d?";
				break;
			case LAG_FORMAT.edEST:
				lagFormat = "ed?";
				break;			
			case LAG_FORMAT.wEST:
				lagFormat = "w?";
				break;
			case LAG_FORMAT.ewEST:
				lagFormat = "ew?";
				break;		
			case LAG_FORMAT.moEST:
				lagFormat = "mo?";
				break;
			case LAG_FORMAT.emoEST:
				lagFormat = "emo?";
				break;
			case LAG_FORMAT.PERCENTEST:
				lagFormat = "%?";
				break;
			case LAG_FORMAT.ePERCENTEST:
				lagFormat = "e%?";
				break;
			}
		}
		return lagFormat;
	}
}

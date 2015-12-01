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

package com.aurel.track.tql.parser;

public interface TqlParserConstants {

  int EOF = 0;
  int SIGN = 5;
  int AND = 6;
  int OR = 7;
  int EQUAL = 8;
  int NOT_EQUAL = 9;
  int GREATER_EQUAL = 10;
  int GREATER_THAN = 11;
  int LESS_EQUAL = 12;
  int LESS_THAN = 13;
  int LIKE = 14;
  int NOT_LIKE = 15;
  int OLDER_THAN = 16;
  int NEWER_THAN = 17;
  int HOURS = 18;
  int DAYS = 19;
  int WEEKS = 20;
  int MONTHS = 21;
  int YEARS = 22;
  int START_DATE_LITERAL = 23;
  int BEGINNING_OF_THIS_YEAR = 24;
  int BEGINNING_OF_THIS_QUARTER = 25;
  int BEGINNING_OF_THIS_MONTH = 26;
  int BEGINNING_OF_THIS_WEEK = 27;
  int BEGINNING_OF_THIS_DAY = 28;
  int LAST_LOGIN = 29;
  int DATE_LITERAL = 30;
  int END_DATE_LITERAL = 31;
  int ORDER_BY = 32;
  int DESC = 33;
  int ASC = 34;
  int SEMICOLON = 35;
  int COMMA = 36;
  int START_QUOTED_STRING_LITERAL = 37;
  int LITERAL = 38;
  int LETTER = 39;
  int DIGITS = 40;
  int MISC = 41;
  int CHARS = 42;
  int QUOTED_STRING_LITERAL = 43;
  int END_QUOTED_STRING_LITERAL = 44;

  int DEFAULT = 0;
  int IN_DATE_LITERAL = 1;
  int IN_QUOTED_STRING_LITERAL = 2;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "<SIGN>",
    "\"AND\"",
    "\"OR\"",
    "\"==\"",
    "\"!=\"",
    "\">=\"",
    "\">\"",
    "\"<=\"",
    "\"<\"",
    "\"LIKE\"",
    "\"NOT_LIKE\"",
    "\"OLDER_THAN\"",
    "\"NEWER_THAN\"",
    "<HOURS>",
    "<DAYS>",
    "<WEEKS>",
    "<MONTHS>",
    "<YEARS>",
    "<START_DATE_LITERAL>",
    "\"BEGINNING_OF_THIS_YEAR\"",
    "\"BEGINNING_OF_THIS_QUARTER\"",
    "\"BEGINNING_OF_THIS_MONTH\"",
    "\"BEGINNING_OF_THIS_WEEK\"",
    "\"BEGINNING_OF_THIS_DAY\"",
    "\"LAST_LOGIN\"",
    "<DATE_LITERAL>",
    "\")\"",
    "\"ORDER_BY\"",
    "\"DESC\"",
    "\"ASC\"",
    "\";\"",
    "\",\"",
    "\"\\\"\"",
    "<LITERAL>",
    "<LETTER>",
    "<DIGITS>",
    "<MISC>",
    "<CHARS>",
    "<QUOTED_STRING_LITERAL>",
    "\"\\\"\"",
    "\"(\"",
    "\")\"",
  };

}

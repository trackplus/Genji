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

public class TqlParserTokenManager implements TqlParserConstants
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x53f03c0c0L) != 0L)
         {
            jjmatchedKind = 38;
            return 23;
         }
         if ((active0 & 0x200000000L) != 0L)
         {
            jjmatchedKind = 38;
            return 27;
         }
         return -1;
      case 1:
         if ((active0 & 0x100000080L) != 0L)
            return 23;
         if ((active0 & 0x63f03c040L) != 0L)
         {
            if (jjmatchedPos != 1)
            {
               jjmatchedKind = 38;
               jjmatchedPos = 1;
            }
            return 23;
         }
         return -1;
      case 2:
         if ((active0 & 0x33f03c000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 2;
            return 23;
         }
         if ((active0 & 0x400000040L) != 0L)
            return 23;
         return -1;
      case 3:
         if ((active0 & 0x13f038000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 3;
            return 23;
         }
         if ((active0 & 0x200004000L) != 0L)
            return 23;
         return -1;
      case 4:
         if ((active0 & 0x13f038000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 4;
            return 23;
         }
         return -1;
      case 5:
         if ((active0 & 0x13f038000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 5;
            return 23;
         }
         return -1;
      case 6:
         if ((active0 & 0x13f038000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 6;
            return 23;
         }
         return -1;
      case 7:
         if ((active0 & 0x100008000L) != 0L)
            return 23;
         if ((active0 & 0x3f030000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 7;
            return 23;
         }
         return -1;
      case 8:
         if ((active0 & 0x3f030000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 8;
            return 23;
         }
         return -1;
      case 9:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 9;
            return 23;
         }
         if ((active0 & 0x20030000L) != 0L)
            return 23;
         return -1;
      case 10:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 10;
            return 23;
         }
         return -1;
      case 11:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 11;
            return 23;
         }
         return -1;
      case 12:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 12;
            return 23;
         }
         return -1;
      case 13:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 13;
            return 23;
         }
         return -1;
      case 14:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 14;
            return 23;
         }
         return -1;
      case 15:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 15;
            return 23;
         }
         return -1;
      case 16:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 16;
            return 23;
         }
         return -1;
      case 17:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 17;
            return 23;
         }
         return -1;
      case 18:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 18;
            return 23;
         }
         return -1;
      case 19:
         if ((active0 & 0x1f000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 19;
            return 23;
         }
         return -1;
      case 20:
         if ((active0 & 0xf000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 20;
            return 23;
         }
         if ((active0 & 0x10000000L) != 0L)
            return 23;
         return -1;
      case 21:
         if ((active0 & 0x6000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 21;
            return 23;
         }
         if ((active0 & 0x9000000L) != 0L)
            return 23;
         return -1;
      case 22:
         if ((active0 & 0x4000000L) != 0L)
            return 23;
         if ((active0 & 0x2000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 22;
            return 23;
         }
         return -1;
      case 23:
         if ((active0 & 0x2000000L) != 0L)
         {
            jjmatchedKind = 38;
            jjmatchedPos = 23;
            return 23;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 33:
         return jjMoveStringLiteralDfa1_0(0x200L);
      case 34:
         return jjStopAtPos(0, 37);
      case 40:
         return jjStopAtPos(0, 45);
      case 41:
         return jjStopAtPos(0, 46);
      case 44:
         return jjStopAtPos(0, 36);
      case 59:
         return jjStopAtPos(0, 35);
      case 60:
         jjmatchedKind = 13;
         return jjMoveStringLiteralDfa1_0(0x1000L);
      case 61:
         return jjMoveStringLiteralDfa1_0(0x100L);
      case 62:
         jjmatchedKind = 11;
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 65:
      case 97:
         return jjMoveStringLiteralDfa1_0(0x400000040L);
      case 66:
      case 98:
         return jjMoveStringLiteralDfa1_0(0x1f000000L);
      case 68:
      case 100:
         return jjMoveStringLiteralDfa1_0(0x200000000L);
      case 76:
      case 108:
         return jjMoveStringLiteralDfa1_0(0x20004000L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa1_0(0x28000L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa1_0(0x100010080L);
      default :
         return jjMoveNfa_0(5, 0);
   }
}
private final int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 61:
         if ((active0 & 0x100L) != 0L)
            return jjStopAtPos(1, 8);
         else if ((active0 & 0x200L) != 0L)
            return jjStopAtPos(1, 9);
         else if ((active0 & 0x400L) != 0L)
            return jjStopAtPos(1, 10);
         else if ((active0 & 0x1000L) != 0L)
            return jjStopAtPos(1, 12);
         break;
      case 65:
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x20000000L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x21f020000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000L);
      case 76:
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x10000L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x40L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x8000L);
      case 82:
      case 114:
         if ((active0 & 0x80L) != 0L)
         {
            jjmatchedKind = 7;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x100000000L);
      case 83:
      case 115:
         return jjMoveStringLiteralDfa2_0(active0, 0x400000000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 67:
      case 99:
         if ((active0 & 0x400000000L) != 0L)
            return jjStartNfaWithStates_0(2, 34, 23);
         break;
      case 68:
      case 100:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(2, 6, 23);
         return jjMoveStringLiteralDfa3_0(active0, 0x100010000L);
      case 71:
      case 103:
         return jjMoveStringLiteralDfa3_0(active0, 0x1f000000L);
      case 75:
      case 107:
         return jjMoveStringLiteralDfa3_0(active0, 0x4000L);
      case 83:
      case 115:
         return jjMoveStringLiteralDfa3_0(active0, 0x220000000L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa3_0(active0, 0x8000L);
      case 87:
      case 119:
         return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa4_0(active0, 0x8000L);
      case 67:
      case 99:
         if ((active0 & 0x200000000L) != 0L)
            return jjStartNfaWithStates_0(3, 33, 23);
         break;
      case 69:
      case 101:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(3, 14, 23);
         return jjMoveStringLiteralDfa4_0(active0, 0x100030000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x1f000000L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa4_0(active0, 0x20000000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa5_0(active0, 0x20000000L);
      case 76:
      case 108:
         return jjMoveStringLiteralDfa5_0(active0, 0x8000L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa5_0(active0, 0x1f000000L);
      case 82:
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x100030000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa6_0(active0, 0x100030000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa6_0(active0, 0x8000L);
      case 76:
      case 108:
         return jjMoveStringLiteralDfa6_0(active0, 0x20000000L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa6_0(active0, 0x1f000000L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private final int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 66:
      case 98:
         return jjMoveStringLiteralDfa7_0(active0, 0x100000000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa7_0(active0, 0x1f000000L);
      case 75:
      case 107:
         return jjMoveStringLiteralDfa7_0(active0, 0x8000L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa7_0(active0, 0x20000000L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa7_0(active0, 0x30000L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private final int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 69:
      case 101:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(7, 15, 23);
         break;
      case 71:
      case 103:
         return jjMoveStringLiteralDfa8_0(active0, 0x20000000L);
      case 72:
      case 104:
         return jjMoveStringLiteralDfa8_0(active0, 0x30000L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa8_0(active0, 0x1f000000L);
      case 89:
      case 121:
         if ((active0 & 0x100000000L) != 0L)
            return jjStartNfaWithStates_0(7, 32, 23);
         break;
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
private final int jjMoveStringLiteralDfa8_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(6, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(7, active0);
      return 8;
   }
   switch(curChar)
   {
      case 65:
      case 97:
         return jjMoveStringLiteralDfa9_0(active0, 0x30000L);
      case 71:
      case 103:
         return jjMoveStringLiteralDfa9_0(active0, 0x1f000000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa9_0(active0, 0x20000000L);
      default :
         break;
   }
   return jjStartNfa_0(7, active0);
}
private final int jjMoveStringLiteralDfa9_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(7, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(8, active0);
      return 9;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa10_0(active0, 0x1f000000L);
      case 78:
      case 110:
         if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(9, 16, 23);
         else if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(9, 17, 23);
         else if ((active0 & 0x20000000L) != 0L)
            return jjStartNfaWithStates_0(9, 29, 23);
         break;
      default :
         break;
   }
   return jjStartNfa_0(8, active0);
}
private final int jjMoveStringLiteralDfa10_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(8, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(9, active0);
      return 10;
   }
   switch(curChar)
   {
      case 79:
      case 111:
         return jjMoveStringLiteralDfa11_0(active0, 0x1f000000L);
      default :
         break;
   }
   return jjStartNfa_0(9, active0);
}
private final int jjMoveStringLiteralDfa11_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(9, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(10, active0);
      return 11;
   }
   switch(curChar)
   {
      case 70:
      case 102:
         return jjMoveStringLiteralDfa12_0(active0, 0x1f000000L);
      default :
         break;
   }
   return jjStartNfa_0(10, active0);
}
private final int jjMoveStringLiteralDfa12_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(10, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(11, active0);
      return 12;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa13_0(active0, 0x1f000000L);
      default :
         break;
   }
   return jjStartNfa_0(11, active0);
}
private final int jjMoveStringLiteralDfa13_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(11, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(12, active0);
      return 13;
   }
   switch(curChar)
   {
      case 84:
      case 116:
         return jjMoveStringLiteralDfa14_0(active0, 0x1f000000L);
      default :
         break;
   }
   return jjStartNfa_0(12, active0);
}
private final int jjMoveStringLiteralDfa14_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(12, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(13, active0);
      return 14;
   }
   switch(curChar)
   {
      case 72:
      case 104:
         return jjMoveStringLiteralDfa15_0(active0, 0x1f000000L);
      default :
         break;
   }
   return jjStartNfa_0(13, active0);
}
private final int jjMoveStringLiteralDfa15_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(13, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(14, active0);
      return 15;
   }
   switch(curChar)
   {
      case 73:
      case 105:
         return jjMoveStringLiteralDfa16_0(active0, 0x1f000000L);
      default :
         break;
   }
   return jjStartNfa_0(14, active0);
}
private final int jjMoveStringLiteralDfa16_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(14, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(15, active0);
      return 16;
   }
   switch(curChar)
   {
      case 83:
      case 115:
         return jjMoveStringLiteralDfa17_0(active0, 0x1f000000L);
      default :
         break;
   }
   return jjStartNfa_0(15, active0);
}
private final int jjMoveStringLiteralDfa17_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(15, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(16, active0);
      return 17;
   }
   switch(curChar)
   {
      case 95:
         return jjMoveStringLiteralDfa18_0(active0, 0x1f000000L);
      default :
         break;
   }
   return jjStartNfa_0(16, active0);
}
private final int jjMoveStringLiteralDfa18_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(16, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(17, active0);
      return 18;
   }
   switch(curChar)
   {
      case 68:
      case 100:
         return jjMoveStringLiteralDfa19_0(active0, 0x10000000L);
      case 77:
      case 109:
         return jjMoveStringLiteralDfa19_0(active0, 0x4000000L);
      case 81:
      case 113:
         return jjMoveStringLiteralDfa19_0(active0, 0x2000000L);
      case 87:
      case 119:
         return jjMoveStringLiteralDfa19_0(active0, 0x8000000L);
      case 89:
      case 121:
         return jjMoveStringLiteralDfa19_0(active0, 0x1000000L);
      default :
         break;
   }
   return jjStartNfa_0(17, active0);
}
private final int jjMoveStringLiteralDfa19_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(17, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(18, active0);
      return 19;
   }
   switch(curChar)
   {
      case 65:
      case 97:
         return jjMoveStringLiteralDfa20_0(active0, 0x10000000L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa20_0(active0, 0x9000000L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa20_0(active0, 0x4000000L);
      case 85:
      case 117:
         return jjMoveStringLiteralDfa20_0(active0, 0x2000000L);
      default :
         break;
   }
   return jjStartNfa_0(18, active0);
}
private final int jjMoveStringLiteralDfa20_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(18, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(19, active0);
      return 20;
   }
   switch(curChar)
   {
      case 65:
      case 97:
         return jjMoveStringLiteralDfa21_0(active0, 0x3000000L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa21_0(active0, 0x8000000L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa21_0(active0, 0x4000000L);
      case 89:
      case 121:
         if ((active0 & 0x10000000L) != 0L)
            return jjStartNfaWithStates_0(20, 28, 23);
         break;
      default :
         break;
   }
   return jjStartNfa_0(19, active0);
}
private final int jjMoveStringLiteralDfa21_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(19, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(20, active0);
      return 21;
   }
   switch(curChar)
   {
      case 75:
      case 107:
         if ((active0 & 0x8000000L) != 0L)
            return jjStartNfaWithStates_0(21, 27, 23);
         break;
      case 82:
      case 114:
         if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(21, 24, 23);
         return jjMoveStringLiteralDfa22_0(active0, 0x2000000L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa22_0(active0, 0x4000000L);
      default :
         break;
   }
   return jjStartNfa_0(20, active0);
}
private final int jjMoveStringLiteralDfa22_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(20, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(21, active0);
      return 22;
   }
   switch(curChar)
   {
      case 72:
      case 104:
         if ((active0 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_0(22, 26, 23);
         break;
      case 84:
      case 116:
         return jjMoveStringLiteralDfa23_0(active0, 0x2000000L);
      default :
         break;
   }
   return jjStartNfa_0(21, active0);
}
private final int jjMoveStringLiteralDfa23_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(21, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(22, active0);
      return 23;
   }
   switch(curChar)
   {
      case 69:
      case 101:
         return jjMoveStringLiteralDfa24_0(active0, 0x2000000L);
      default :
         break;
   }
   return jjStartNfa_0(22, active0);
}
private final int jjMoveStringLiteralDfa24_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(22, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(23, active0);
      return 24;
   }
   switch(curChar)
   {
      case 82:
      case 114:
         if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(24, 25, 23);
         break;
      default :
         break;
   }
   return jjStartNfa_0(23, active0);
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0x0L, 0x2000000000000000L
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 33;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 27:
               case 23:
                  if ((0x3ff200000000000L & l) == 0L)
                     break;
                  if (kind > 38)
                     kind = 38;
                  jjCheckNAdd(23);
                  break;
               case 5:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 38)
                        kind = 38;
                     jjCheckNAdd(23);
                  }
                  else if ((0x280000000000L & l) != 0L)
                  {
                     if (kind > 5)
                        kind = 5;
                     jjCheckNAdd(0);
                  }
                  break;
               case 0:
                  if ((0x280000000000L & l) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjCheckNAdd(0);
                  break;
               case 22:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 38)
                     kind = 38;
                  jjCheckNAdd(23);
                  break;
               case 29:
                  if (curChar == 32)
                     jjAddStates(0, 1);
                  break;
               case 30:
                  if (curChar == 40)
                     kind = 23;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 27:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 38)
                        kind = 38;
                     jjCheckNAdd(23);
                  }
                  if ((0x200000002L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 31;
                  if ((0x200000002L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 25;
                  break;
               case 5:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 38)
                        kind = 38;
                     jjCheckNAdd(23);
                  }
                  if ((0x1000000010L & l) != 0L)
                     jjAddStates(2, 3);
                  else if ((0x200000002000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 20;
                  else if ((0x200000002000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 15;
                  else if ((0x80000000800000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 9;
                  else if ((0x10000000100L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 1:
                  if ((0x4000000040000L & l) == 0L)
                     break;
                  if (kind > 18)
                     kind = 18;
                  jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 2:
                  if ((0x8000000080000L & l) != 0L && kind > 18)
                     kind = 18;
                  break;
               case 3:
                  if ((0x20000000200000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 4:
                  if ((0x800000008000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 6:
                  if ((0x80000000800L & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 7:
                  if ((0x8000000080000L & l) != 0L && kind > 20)
                     kind = 20;
                  break;
               case 8:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 9:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 10:
                  if ((0x80000000800000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 9;
                  break;
               case 11:
                  if ((0x10000000100L & l) == 0L)
                     break;
                  if (kind > 21)
                     kind = 21;
                  jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 12:
                  if ((0x8000000080000L & l) != 0L && kind > 21)
                     kind = 21;
                  break;
               case 13:
                  if ((0x10000000100000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 14:
                  if ((0x400000004000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  break;
               case 15:
                  if ((0x800000008000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 16:
                  if ((0x200000002000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 17:
                  if ((0x4000000040000L & l) == 0L)
                     break;
                  if (kind > 22)
                     kind = 22;
                  jjstateSet[jjnewStateCnt++] = 18;
                  break;
               case 18:
                  if ((0x8000000080000L & l) != 0L && kind > 22)
                     kind = 22;
                  break;
               case 19:
                  if ((0x200000002L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 17;
                  break;
               case 20:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 19;
                  break;
               case 21:
                  if ((0x200000002000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 20;
                  break;
               case 22:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 38)
                     kind = 38;
                  jjCheckNAdd(23);
                  break;
               case 23:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 38)
                     kind = 38;
                  jjCheckNAdd(23);
                  break;
               case 24:
                  if ((0x1000000010L & l) != 0L)
                     jjAddStates(2, 3);
                  break;
               case 25:
                  if ((0x200000002000000L & l) == 0L)
                     break;
                  if (kind > 19)
                     kind = 19;
                  jjstateSet[jjnewStateCnt++] = 26;
                  break;
               case 26:
                  if ((0x8000000080000L & l) != 0L && kind > 19)
                     kind = 19;
                  break;
               case 28:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(0, 1);
                  break;
               case 31:
                  if ((0x10000000100000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 28;
                  break;
               case 32:
                  if ((0x200000002L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 31;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 27:
               case 23:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 38)
                     kind = 38;
                  jjCheckNAdd(23);
                  break;
               case 5:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 38)
                     kind = 38;
                  jjCheckNAdd(23);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 33 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private final int jjStopStringLiteralDfa_2(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_2(int pos, long active0)
{
   return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
}
private final int jjStartNfaWithStates_2(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_2(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_2()
{
   switch(curChar)
   {
      case 34:
         return jjStopAtPos(0, 44);
      default :
         return jjMoveNfa_2(0, 0);
   }
}
static final long[] jjbitVec1 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec3 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private final int jjMoveNfa_2(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 1;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0xfffffffbffffffffL & l) == 0L)
                     break;
                  kind = 43;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  kind = 43;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 43)
                     kind = 43;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private final int jjStopStringLiteralDfa_1(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_1(int pos, long active0)
{
   return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
}
private final int jjStartNfaWithStates_1(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_1(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_1()
{
   switch(curChar)
   {
      case 41:
         return jjStopAtPos(0, 31);
      default :
         return jjMoveNfa_1(0, 0);
   }
}
private final int jjMoveNfa_1(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 1;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0xfffffdffffffffffL & l) == 0L)
                     break;
                  kind = 30;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  kind = 30;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 30)
                     kind = 30;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   29, 30, 27, 32, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 255:
         return ((jjbitVec0[i2] & l2) != 0L);
      default : 
         return false;
   }
}
private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec3[i2] & l2) != 0L);
      default : 
         if ((jjbitVec1[i1] & l1) != 0L)
            return true;
         return false;
   }
}
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, "\75\75", "\41\75", "\76\75", 
"\76", "\74\75", "\74", null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, "\51", null, null, null, "\73", "\54", 
"\42", null, null, null, null, null, null, "\42", "\50", "\51", };
public static final String[] lexStateNames = {
   "DEFAULT", 
   "IN_DATE_LITERAL", 
   "IN_QUOTED_STRING_LITERAL", 
};
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 
   -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, 0, -1, -1, 
};
static final long[] jjtoToken = {
   0x787fffffffe1L, 
};
static final long[] jjtoSkip = {
   0x1eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[33];
private final int[] jjstateSet = new int[66];
protected char curChar;
public TqlParserTokenManager(SimpleCharStream stream)
{
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public TqlParserTokenManager(SimpleCharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 33; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 3 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   switch(curLexState)
   {
     case 0:
       try { input_stream.backup(0);
          while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
             curChar = input_stream.BeginToken();
       }
       catch (java.io.IOException e1) { continue EOFLoop; }
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_0();
       break;
     case 1:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_1();
       break;
     case 2:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_2();
       break;
   }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else
        {
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
           continue EOFLoop;
        }
     }
     int error_line = input_stream.getEndLine();
     int error_column = input_stream.getEndColumn();
     String error_after = null;
     boolean EOFSeen = false;
     try { input_stream.readChar(); input_stream.backup(1); }
     catch (java.io.IOException e1) {
        EOFSeen = true;
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
        if (curChar == '\n' || curChar == '\r') {
           error_line++;
           error_column = 0;
        }
        else
           error_column++;
     }
     if (!EOFSeen) {
        input_stream.backup(1);
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
     }
     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

}

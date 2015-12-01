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

/**
 * This class implements the TqlParser. It is used for transforming
 * a TQL statement into an equivalent abstract syntax tree.
 *
 * @author Clemens Fuchslocher &lt;clfuit00@fht-esslingen.de&gt;
 */
public class TqlParser/*@bgen(jjtree)*/implements TqlParserTreeConstants, TqlParserConstants {/*@bgen(jjtree)*/
  protected JJTTqlParserState jjtree = new JJTTqlParserState();public final static int ROOT = JJTROOT;
        public final static int OR_EXPRESSION = JJTOREXPRESSION;
        public final static int AND_EXPRESSION = JJTANDEXPRESSION;
        public final static int RELATIONAL_EXPRESSION = JJTRELATIONALEXPRESSION;
        public final static int LITERAL_EXPRESSION = JJTLITERALEXPRESSION;
        public final static int DATE_OPERATOR = JJTDATEOPERATOR;

        public final static int ORDER_BY_EXPRESSION = JJTORDERBY;
        public final static int ORDER_BY_DESCENDING = 1000;
        public final static int ORDER_BY_ASCENDING = 2000;

// Non terminals
  final public SimpleNode start() throws ParseException {
 /*@bgen(jjtree) Root */
  ASTRoot jjtn000 = new ASTRoot(JJTROOT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ORDER_BY:
        OrderBy();
        break;
      default:
        jj_la1[0] = jj_gen;
        ;
      }
      OrExpression();
                                        jjtree.closeNodeScope(jjtn000, true);
                                        jjtc000 = false;
                jjtn000.setType (ROOT);
                jjtn000.setValue ("ROOT");
                {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public void OrExpression() throws ParseException {
 /*@bgen(jjtree) #OrExpression(> 1) */
  ASTOrExpression jjtn000 = new ASTOrExpression(JJTOREXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      AndExpression();
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case OR:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_1;
        }
        jj_consume_token(OR);
                jjtn000.setType (OR_EXPRESSION);
                jjtn000.setOperator (OR_EXPRESSION);
                jjtn000.setValue ("OR");
        AndExpression();
      }
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, jjtree.nodeArity() > 1);
          }
    }
  }

  final public void AndExpression() throws ParseException {
 /*@bgen(jjtree) #AndExpression(> 1) */
  ASTAndExpression jjtn000 = new ASTAndExpression(JJTANDEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      RelationalExpression();
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case AND:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_2;
        }
        jj_consume_token(AND);
                jjtn000.setType (AND_EXPRESSION);
                jjtn000.setOperator (AND_EXPRESSION);
                jjtn000.setValue ("AND");
        RelationalExpression();
      }
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, jjtree.nodeArity() > 1);
          }
    }
  }

  final public void RelationalExpression() throws ParseException {
 /*@bgen(jjtree) #RelationalExpression(> 1) */
        ASTRelationalExpression jjtn000 = new ASTRelationalExpression(JJTRELATIONALEXPRESSION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);int timeUnit = -1;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case START_DATE_LITERAL:
      case START_QUOTED_STRING_LITERAL:
      case LITERAL:
        LiteralExpression();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case EQUAL:
        case NOT_EQUAL:
        case GREATER_EQUAL:
        case GREATER_THAN:
        case LESS_EQUAL:
        case LESS_THAN:
        case LIKE:
        case NOT_LIKE:
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case EQUAL:
            jj_consume_token(EQUAL);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (EQUAL);
                                jjtn000.setValue ("==");
            break;
          case NOT_EQUAL:
            jj_consume_token(NOT_EQUAL);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (NOT_EQUAL);
                                jjtn000.setValue ("!=");
            break;
          case GREATER_EQUAL:
            jj_consume_token(GREATER_EQUAL);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (GREATER_EQUAL);
                                jjtn000.setValue (">=");
            break;
          case GREATER_THAN:
            jj_consume_token(GREATER_THAN);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (GREATER_THAN);
                                jjtn000.setValue (">");
            break;
          case LESS_EQUAL:
            jj_consume_token(LESS_EQUAL);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (LESS_EQUAL);
                                jjtn000.setValue ("<=");
            break;
          case LESS_THAN:
            jj_consume_token(LESS_THAN);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (LESS_THAN);
                                jjtn000.setValue ("<");
            break;
          case LIKE:
            jj_consume_token(LIKE);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (LIKE);
                                jjtn000.setValue ("LIKE");
            break;
          case NOT_LIKE:
            jj_consume_token(NOT_LIKE);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (NOT_LIKE);
                                jjtn000.setValue ("NOT_LIKE");
            break;
          default:
            jj_la1[3] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          LiteralExpression();
          break;
        case OLDER_THAN:
        case NEWER_THAN:
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case OLDER_THAN:
            jj_consume_token(OLDER_THAN);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (OLDER_THAN);
                                jjtn000.setValue ("OLDER_THAN");
            break;
          case NEWER_THAN:
            jj_consume_token(NEWER_THAN);
                                jjtn000.setType (RELATIONAL_EXPRESSION);
                                jjtn000.setOperator (NEWER_THAN);
                                jjtn000.setValue ("NEWER_THAN");
            break;
          default:
            jj_la1[4] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case BEGINNING_OF_THIS_YEAR:
          case BEGINNING_OF_THIS_QUARTER:
          case BEGINNING_OF_THIS_MONTH:
          case BEGINNING_OF_THIS_WEEK:
          case BEGINNING_OF_THIS_DAY:
          case LAST_LOGIN:
            DateOperator();
            break;
          case START_DATE_LITERAL:
          case START_QUOTED_STRING_LITERAL:
          case LITERAL:
            LiteralExpression();
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case HOURS:
            case DAYS:
            case WEEKS:
            case MONTHS:
            case YEARS:
              timeUnit = TimeUnits();
                                        jjtn000.setTimeUnit (timeUnit);
              break;
            default:
              jj_la1[5] = jj_gen;
              ;
            }
            break;
          default:
            jj_la1[6] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        default:
          jj_la1[7] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      case 45:
        jj_consume_token(45);
        OrExpression();
        jj_consume_token(46);
        break;
      default:
        jj_la1[8] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, jjtree.nodeArity() > 1);
          }
    }
  }

  final public void DateOperator() throws ParseException {
 /*@bgen(jjtree) DateOperator */
        ASTDateOperator jjtn000 = new ASTDateOperator(JJTDATEOPERATOR);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token sign = null;
        Token value = null;
        int timeUnit = -1;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BEGINNING_OF_THIS_YEAR:
        jj_consume_token(BEGINNING_OF_THIS_YEAR);
                        jjtn000.setType (DATE_OPERATOR);
                        jjtn000.setDateOperator(BEGINNING_OF_THIS_YEAR);
                        jjtn000.setValue ("BEGINNING_OF_THIS_YEAR");
        break;
      case BEGINNING_OF_THIS_QUARTER:
        jj_consume_token(BEGINNING_OF_THIS_QUARTER);
                        jjtn000.setType (DATE_OPERATOR);
                        jjtn000.setDateOperator(BEGINNING_OF_THIS_QUARTER);
                        jjtn000.setValue ("BEGINNING_OF_THIS_QUARTER");
        break;
      case BEGINNING_OF_THIS_MONTH:
        jj_consume_token(BEGINNING_OF_THIS_MONTH);
                        jjtn000.setType (DATE_OPERATOR);
                        jjtn000.setDateOperator(BEGINNING_OF_THIS_MONTH);
                        jjtn000.setValue ("BEGINNING_OF_THIS_MONTH");
        break;
      case BEGINNING_OF_THIS_WEEK:
        jj_consume_token(BEGINNING_OF_THIS_WEEK);
                        jjtn000.setType (DATE_OPERATOR);
                        jjtn000.setDateOperator(BEGINNING_OF_THIS_WEEK);
                        jjtn000.setValue ("BEGINNING_OF_THIS_WEEK");
        break;
      case BEGINNING_OF_THIS_DAY:
        jj_consume_token(BEGINNING_OF_THIS_DAY);
                        jjtn000.setType (DATE_OPERATOR);
                        jjtn000.setDateOperator(BEGINNING_OF_THIS_DAY);
                        jjtn000.setValue ("BEGINNING_OF_THIS_DAY");
        break;
      case LAST_LOGIN:
        jj_consume_token(LAST_LOGIN);
                        jjtn000.setType (DATE_OPERATOR);
                        jjtn000.setDateOperator(LAST_LOGIN);
                        jjtn000.setValue ("LAST_LOGIN");
        break;
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SIGN:
        sign = jj_consume_token(SIGN);
                        jjtn000.setDateOperatorSign(sign == null ? "" : sign.image);
        LiteralExpression();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case HOURS:
        case DAYS:
        case WEEKS:
        case MONTHS:
        case YEARS:
          timeUnit = TimeUnits();
                                jjtn000.setDateOperatorTimeUnit (timeUnit);
          break;
        default:
          jj_la1[10] = jj_gen;
          ;
        }
        break;
      default:
        jj_la1[11] = jj_gen;
        ;
      }
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public int TimeUnits() throws ParseException {
 /*@bgen(jjtree) #TimeUnits(> 1) */
  ASTTimeUnits jjtn000 = new ASTTimeUnits(JJTTIMEUNITS);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case HOURS:
        jj_consume_token(HOURS);
                  jjtree.closeNodeScope(jjtn000, jjtree.nodeArity() > 1);
                  jjtc000 = false;
                {if (true) return HOURS;}
        break;
      case DAYS:
        jj_consume_token(DAYS);
                   jjtree.closeNodeScope(jjtn000, jjtree.nodeArity() > 1);
                   jjtc000 = false;
                {if (true) return DAYS;}
        break;
      case WEEKS:
        jj_consume_token(WEEKS);
                    jjtree.closeNodeScope(jjtn000, jjtree.nodeArity() > 1);
                    jjtc000 = false;
                {if (true) return WEEKS;}
        break;
      case MONTHS:
        jj_consume_token(MONTHS);
                     jjtree.closeNodeScope(jjtn000, jjtree.nodeArity() > 1);
                     jjtc000 = false;
                {if (true) return MONTHS;}
        break;
      case YEARS:
        jj_consume_token(YEARS);
                    jjtree.closeNodeScope(jjtn000, jjtree.nodeArity() > 1);
                    jjtc000 = false;
                {if (true) return YEARS;}
        break;
      default:
        jj_la1[12] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, jjtree.nodeArity() > 1);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public Token LiteralExpression() throws ParseException {
 /*@bgen(jjtree) LiteralExpression */
        ASTLiteralExpression jjtn000 = new ASTLiteralExpression(JJTLITERALEXPRESSION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token token = null;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LITERAL:
        token = jj_consume_token(LITERAL);
                            jjtree.closeNodeScope(jjtn000, true);
                            jjtc000 = false;
                jjtn000.setType (LITERAL_EXPRESSION);
                jjtn000.setValue (token.image);
                {if (true) return token;}
        break;
      case START_QUOTED_STRING_LITERAL:
        jj_consume_token(START_QUOTED_STRING_LITERAL);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case QUOTED_STRING_LITERAL:
          token = jj_consume_token(QUOTED_STRING_LITERAL);
          break;
        default:
          jj_la1[13] = jj_gen;
          ;
        }
        jj_consume_token(END_QUOTED_STRING_LITERAL);
                                                                                                         jjtree.closeNodeScope(jjtn000, true);
                                                                                                         jjtc000 = false;
                jjtn000.setType (LITERAL_EXPRESSION);
                jjtn000.setValue (token == null ? "" : token.image);
                {if (true) return token;}
        break;
      case START_DATE_LITERAL:
        jj_consume_token(START_DATE_LITERAL);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DATE_LITERAL:
          token = jj_consume_token(DATE_LITERAL);
          break;
        default:
          jj_la1[14] = jj_gen;
          ;
        }
        jj_consume_token(END_DATE_LITERAL);
                                                                              jjtree.closeNodeScope(jjtn000, true);
                                                                              jjtc000 = false;
                jjtn000.setType (DATE_LITERAL);
                jjtn000.setValue (token == null ? "" : token.image);
                {if (true) return token;}
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public void SortExpression() throws ParseException {
 /*@bgen(jjtree) SortExpression */
        ASTSortExpression jjtn000 = new ASTSortExpression(JJTSORTEXPRESSION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token token = null;
    try {
      token = jj_consume_token(LITERAL);
                jjtn000.setType (ORDER_BY_EXPRESSION);
                jjtn000.setValue (token.image);
                jjtn000.setOperator (ORDER_BY_ASCENDING);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DESC:
      case ASC:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DESC:
          jj_consume_token(DESC);
                        jjtn000.setOperator (ORDER_BY_DESCENDING);
          break;
        case ASC:
          jj_consume_token(ASC);
                        jjtn000.setOperator (ORDER_BY_ASCENDING);
          break;
        default:
          jj_la1[16] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_la1[17] = jj_gen;
        ;
      }
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public void OrderBy() throws ParseException {
 /*@bgen(jjtree) OrderBy */
        ASTOrderBy jjtn000 = new ASTOrderBy(JJTORDERBY);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token token = null;
    try {
      jj_consume_token(ORDER_BY);
      SortExpression();

      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        jj_consume_token(COMMA);
        SortExpression();
        break;
      default:
        jj_la1[18] = jj_gen;
        ;
      }
      jj_consume_token(SEMICOLON);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  public TqlParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[19];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_0();
      jj_la1_1();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0x0,0x80,0x40,0xff00,0x30000,0x7c0000,0x3f800000,0x3ff00,0x800000,0x3f000000,0x7c0000,0x20,0x7c0000,0x0,0x40000000,0x800000,0x0,0x0,0x0,};
   }
   private static void jj_la1_1() {
      jj_la1_1 = new int[] {0x1,0x0,0x0,0x0,0x0,0x0,0x60,0x0,0x2060,0x0,0x0,0x0,0x0,0x800,0x0,0x60,0x6,0x6,0x10,};
   }

  public TqlParser(java.io.InputStream stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new TqlParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  public TqlParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new TqlParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  public TqlParser(TqlParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  public void ReInit(TqlParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[47];
    for (int i = 0; i < 47; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 19; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 47; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}

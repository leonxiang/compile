//### This file created by BYACC 1.8(/Java extension  1.13)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//###           14 Sep 06  -- Keltin Leung-- ReduceListener support, eliminate underflow report in error recovery
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 11 "Parser.y"
package decaf.frontend;

import decaf.tree.Tree;
import decaf.tree.Tree.*;
import decaf.error.*;
import java.util.*;
//#line 25 "Parser.java"
interface ReduceListener {
  public boolean onReduce(String rule);
}




public class Parser
             extends BaseParser
             implements ReduceListener
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

ReduceListener reduceListener = null;
void yyclearin ()       {yychar = (-1);}
void yyerrok ()         {yyerrflag=0;}
void addReduceListener(ReduceListener l) {
  reduceListener = l;}


//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:SemValue
String   yytext;//user variable to return contextual strings
SemValue yyval; //used to return semantic vals from action routines
SemValue yylval;//the 'lval' (result) I got from yylex()
SemValue valstk[] = new SemValue[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new SemValue();
  yylval=new SemValue();
  valptr=-1;
}
final void val_push(SemValue val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    SemValue[] newstack = new SemValue[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final SemValue val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final SemValue val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short VOID=257;
public final static short BOOL=258;
public final static short INT=259;
public final static short STRING=260;
public final static short CLASS=261;
public final static short DOUBLE=262;
public final static short NULL=263;
public final static short EXTENDS=264;
public final static short THIS=265;
public final static short WHILE=266;
public final static short FOR=267;
public final static short IF=268;
public final static short ELSE=269;
public final static short RETURN=270;
public final static short BREAK=271;
public final static short NEW=272;
public final static short PRINT=273;
public final static short READ_INTEGER=274;
public final static short READ_LINE=275;
public final static short LITERAL=276;
public final static short IDENTIFIER=277;
public final static short AND=278;
public final static short OR=279;
public final static short STATIC=280;
public final static short REPEAT=281;
public final static short UNTIL=282;
public final static short LESS_EQUAL=283;
public final static short GREATER_EQUAL=284;
public final static short EQUAL=285;
public final static short NOT_EQUAL=286;
public final static short UMINUS=287;
public final static short EMPTY=288;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    5,    5,
    5,    5,    2,    6,    6,    7,    7,    7,    9,    9,
   10,   10,    8,    8,   11,   12,   12,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   14,   14,   14,
   25,   25,   22,   22,   24,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   27,   27,   26,   26,   28,   28,   16,   17,   18,   21,
   15,   29,   29,   19,   19,   20,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    1,
    2,    3,    6,    2,    0,    2,    2,    0,    1,    0,
    3,    1,    7,    6,    3,    2,    0,    1,    2,    1,
    1,    2,    1,    2,    2,    2,    1,    3,    1,    0,
    2,    0,    2,    4,    5,    1,    1,    1,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    2,    2,    3,    3,    1,    4,    5,    5,
    1,    1,    1,    0,    3,    1,    5,    6,    9,    1,
    6,    2,    0,    2,    1,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,   14,   18,
    0,    7,    8,    6,   10,    0,    9,    0,   13,   16,
    0,    0,   17,   11,    0,    4,    0,    0,    0,    0,
   12,    0,   22,    0,    0,    0,    0,    5,    0,    0,
    0,   27,   24,   21,   23,    0,   72,   67,    0,    0,
    0,    0,   80,    0,    0,    0,    0,   71,    0,    0,
    0,    0,   25,   28,   37,   26,    0,   30,   31,    0,
   33,    0,    0,    0,    0,    0,    0,    0,   48,    0,
    0,    0,   46,    0,   47,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   29,   32,   34,   35,   36,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   41,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   65,   66,    0,    0,   62,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   68,    0,
    0,   86,    0,    0,   44,    0,    0,   77,    0,    0,
   69,    0,    0,   70,   45,    0,    0,   81,   78,    0,
   82,    0,   79,
};
final static short yydgoto[] = {                          2,
    3,    4,   64,   21,   34,    8,   11,   23,   35,   36,
   65,   46,   66,   67,   68,   69,   70,   71,   72,   73,
   74,   83,   76,   85,   78,  156,   79,  124,  168,
};
final static short yysindex[] = {                      -253,
 -241,    0, -253,    0, -226,    0, -230,  -68,    0,    0,
  523,    0,    0,    0,    0, -229,    0, -174,    0,    0,
    2,  -88,    0,    0,  -86,    0,   26,  -26,   30, -174,
    0, -174,    0,  -71,   33,   31,   40,    0,  -22, -174,
  -22,    0,    0,    0,    0,  326,    0,    0,   70,   80,
   83,   -3,    0,  431,   85,   86,   89,    0,  355,   -3,
   -3,  601,    0,    0,    0,    0,   77,    0,    0,   78,
    0,   95,   99,  102,  104,  438,    0, -131,    0,   -3,
   -3,   -3,    0,  438,    0,  123,   76,   -3,  129,  130,
 -110,  -42,  -42, -101,  106,    0,    0,    0,    0,    0,
   -3,   -3,   -3,   -3,   -3,   -3,   -3,   -3,   -3,   -3,
   -3,   -3,   -3,   -3,    0,   -3,  140,  132,  124,  262,
  141,  639,  438,  -19,    0,    0,  145,  146,    0,  438,
  491,  470,  640,  640,  512,  512,  163,  163,  -42,  -42,
  -42,  640,  640,  359,   -3,  355,   -3,  355,    0,  383,
   -3,    0,   -3,   -3,    0,  147,  151,    0,  156,  -73,
    0,  438,  417,    0,    0,   -3,  355,    0,    0,  166,
    0,  355,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  204,    0,   88,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  149,    0,    0,  171,
    0,  171,    0,    0,    0,  172,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -58,    0,    0,    0,    0,
    0,  -57,    0,    0,    0,    0,    0,    0,  -58,  -63,
  -63,  -63,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  459,    0,   -2,    0,    0,  -63,
  -58,  -63,    0,  158,    0,    0,    0,  -63,    0,    0,
    0,    9,   35,    0,    0,    0,    0,    0,    0,    0,
  -63,  -63,  -63,  -63,  -63,  -63,  -63,  -63,  -63,  -63,
  -63,  -63,  -63,  -63,    0,  -63,  -28,    0,    0,    0,
    0,  -63,  -17,    0,    0,    0,    0,    0,    0,  -30,
   91,  285,   52,  284,  565,  606,  427,  611,   62,   71,
   97,  450,  483,    0,  -31,  -58,  -63,  -58,    0,    0,
  -63,    0,  -63,  -63,    0,    0,  180,    0,    0,  -33,
    0,   18,    0,    0,    0,  -18,  -58,    0,    0,    0,
    0,  -58,    0,
};
final static short yygindex[] = {                         0,
    0,  219,  220,   87,   10,    0,    0,    0,  213,    0,
   59,    0,   14,  -75,    0,    0,    0,    0,    0,    0,
    0,  668,  839,  740,    0,    0,    0,  107,    0,
};
final static int YYTABLESIZE=993;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         83,
   40,   85,   28,  115,   28,  119,   83,    1,   43,   74,
   38,   83,   43,   43,   43,   43,   43,   43,   43,   28,
   22,  152,   40,   76,  151,   83,   76,   25,   38,   61,
   43,   43,   43,   43,   47,    5,   62,    7,   39,   47,
   47,   60,   47,   47,   47,   63,    9,   24,  116,   63,
   63,   63,   63,   63,   10,   63,   39,   47,   75,   47,
   26,   75,   43,   87,   43,   30,   31,   63,   63,   32,
   63,   64,   91,   39,   40,   64,   64,   64,   64,   64,
   41,   64,   12,   13,   14,   15,   16,   17,   47,   83,
  170,   83,   58,   64,   64,   58,   64,   43,   51,   45,
   42,   63,   51,   51,   51,   51,   51,   52,   51,   80,
   58,   52,   52,   52,   52,   52,   33,   52,   33,   81,
   51,   51,   82,   51,   88,   89,   44,   64,   90,   52,
   52,   60,   52,   53,   60,   96,   97,   53,   53,   53,
   53,   53,  112,   53,   58,  117,  129,  110,  108,   60,
  109,  115,  111,   98,   51,   53,   53,   99,   53,  158,
  100,  160,  121,   52,  101,  114,  122,  113,  112,  125,
  126,  127,  146,  110,  108,  128,  109,  115,  111,  145,
  171,  149,  147,   60,  153,  173,  154,  165,   27,   53,
   29,  114,  112,  113,  151,  167,  116,  110,  108,  112,
  109,  115,  111,    1,  110,   38,  172,    5,  115,  111,
   15,   20,   19,   42,  166,  114,   84,  113,   42,   42,
   73,    6,  116,   83,   83,   83,   83,   83,   83,   83,
   20,   83,   83,   83,   83,    0,   83,   83,   83,   83,
   83,   83,   83,   83,   37,   42,  116,   83,   83,   43,
   43,  157,    0,  116,   43,   43,   43,   43,   42,   47,
    0,   48,    0,    0,    0,    0,    0,    0,   54,    0,
   56,   57,   58,    0,    0,   47,   47,    0,    0,    0,
   47,   47,   47,   47,    0,    0,   63,   63,    0,    0,
    0,   63,   63,   63,   63,    0,    0,    0,  112,    0,
    0,    0,  148,  110,  108,    0,  109,  115,  111,    0,
    0,    0,   64,   64,    0,    0,    0,   64,   64,   64,
   64,  114,    0,  113,   59,   61,    0,   59,   61,   58,
   58,    0,    0,    0,    0,    0,   58,   58,    0,   51,
   51,    0,   59,   61,   51,   51,   51,   51,   52,   52,
    0,    0,  116,   52,   52,   52,   52,    0,   61,    0,
    0,    0,    0,    0,    0,   62,    0,    0,   60,   60,
   60,    0,    0,    0,   53,   53,   59,   61,    0,   53,
   53,   53,   53,  102,  103,    0,    0,   61,  104,  105,
  106,  107,    0,    0,   62,  112,    0,    0,    0,   60,
  110,  108,    0,  109,  115,  111,    0,    0,    0,  102,
  103,    0,    0,    0,  104,  105,  106,  107,  114,  112,
  113,    0,    0,    0,  110,  108,    0,  109,  115,  111,
    0,    0,    0,  102,  103,    0,    0,    0,  104,  105,
  106,  107,  114,    0,  113,    0,    0,    0,   42,  116,
   63,  155,    0,  112,    0,    0,    0,  169,  110,  108,
    0,  109,  115,  111,    0,    0,    0,   49,    0,   49,
   49,   49,    0,  116,  112,  161,  114,   42,  113,  110,
  108,    0,  109,  115,  111,   49,   49,    0,   49,    0,
   57,    0,    0,   57,    0,   46,    0,  114,    0,  113,
   46,   46,    0,   46,   46,   46,  112,  116,   57,    0,
    0,  110,  108,    0,  109,  115,  111,    0,   46,   49,
   46,    0,    0,   56,    0,    0,   56,  112,  116,  114,
    0,  113,  110,  108,    0,  109,  115,  111,    0,  102,
  103,   56,   57,    0,  104,  105,  106,  107,  112,   46,
  114,    0,  113,  110,  108,    0,  109,  115,  111,    0,
  116,   59,   59,   61,    0,    0,    0,    0,   59,   59,
    0,  114,    0,  113,    0,   56,    0,    0,    0,    0,
    0,  116,   12,   13,   14,   15,   16,   17,   47,    0,
   48,   49,   50,   51,    0,   52,   53,   54,   55,   56,
   57,   58,  116,    0,    0,   54,   59,    0,   54,    0,
    0,   12,   13,   14,   15,   16,   17,   47,    0,   48,
   49,   50,   51,   54,   52,   53,   54,   55,   56,   57,
   58,    0,    0,   61,    0,   59,  102,  103,    0,    0,
   62,  104,  105,  106,  107,   60,   55,   19,    0,   55,
    0,   50,    0,   50,   50,   50,    0,   54,    0,    0,
  102,  103,    0,    0,   55,  104,  105,  106,  107,   50,
   50,   61,   50,    0,    0,    0,  112,    0,   62,    0,
    0,  110,  108,   60,  109,  115,  111,   12,   13,   14,
   15,   16,   17,    0,  102,  103,    0,    0,   55,  104,
  105,  106,  107,   50,   49,   49,    0,   86,    0,   49,
   49,   49,   49,   75,    0,  102,  103,    0,    0,    0,
  104,  105,  106,  107,    0,    0,   75,   57,   57,    0,
  116,   31,    0,    0,   57,   57,   46,   46,    0,    0,
    0,   46,   46,   46,   46,    0,    0,  102,   75,    0,
    0,    0,  104,  105,  106,  107,    0,    0,    0,    0,
   56,   56,    0,    0,    0,    0,    0,   56,   56,    0,
    0,    0,    0,  104,  105,  106,  107,    0,    0,   12,
   13,   14,   15,   16,   17,   77,    0,    0,    0,    0,
    0,    0,    0,    0,  104,  105,    0,    0,   77,    0,
    0,    0,   18,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   75,    0,   75,    0,    0,    0,    0,
   77,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   75,   75,    0,    0,    0,    0,   75,
    0,    0,   54,   54,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   94,    0,   47,    0,   48,    0,    0,    0,    0,
    0,    0,   54,    0,   56,   57,   58,    0,    0,    0,
    0,    0,    0,   55,   55,   77,    0,   77,   50,   50,
   84,    0,    0,   50,   50,   50,   50,    0,   92,   93,
   95,   47,    0,   48,    0,   77,   77,    0,    0,    0,
   54,   77,   56,   57,   58,    0,    0,    0,  118,    0,
  120,    0,    0,    0,    0,    0,  123,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  130,
  131,  132,  133,  134,  135,  136,  137,  138,  139,  140,
  141,  142,  143,    0,  144,    0,    0,    0,    0,    0,
  150,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  123,    0,  159,    0,    0,    0,  162,
    0,  163,  164,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   59,   59,   91,   46,   91,   81,   40,  261,   37,   41,
   41,   45,   41,   42,   43,   44,   45,   46,   47,   91,
   11,   41,   41,   41,   44,   59,   44,   18,   59,   33,
   59,   60,   61,   62,   37,  277,   40,  264,   41,   42,
   43,   45,   45,   46,   47,   37,  277,  277,   91,   41,
   42,   43,   44,   45,  123,   47,   59,   60,   41,   62,
   59,   44,   91,   54,   93,   40,   93,   59,   60,   40,
   62,   37,   59,   41,   44,   41,   42,   43,   44,   45,
   41,   47,  257,  258,  259,  260,  261,  262,   91,  123,
  166,  125,   41,   59,   60,   44,   62,   39,   37,   41,
  123,   93,   41,   42,   43,   44,   45,   37,   47,   40,
   59,   41,   42,   43,   44,   45,   30,   47,   32,   40,
   59,   60,   40,   62,   40,   40,   40,   93,   40,   59,
   60,   41,   62,   37,   44,   59,   59,   41,   42,   43,
   44,   45,   37,   47,   93,  277,   41,   42,   43,   59,
   45,   46,   47,   59,   93,   59,   60,   59,   62,  146,
   59,  148,   40,   93,   61,   60,   91,   62,   37,   41,
   41,  282,   41,   42,   43,  277,   45,   46,   47,   40,
  167,   41,   59,   93,   40,  172,   41,   41,  277,   93,
  277,   60,   37,   62,   44,  269,   91,   42,   43,   37,
   45,   46,   47,    0,   42,  277,   41,   59,   46,   47,
  123,   41,   41,  277,   59,   60,   59,   62,  277,  277,
   41,    3,   91,  257,  258,  259,  260,  261,  262,  263,
   11,  265,  266,  267,  268,   -1,  270,  271,  272,  273,
  274,  275,  276,  277,   32,  277,   91,  281,  282,  278,
  279,  145,   -1,   91,  283,  284,  285,  286,  277,  263,
   -1,  265,   -1,   -1,   -1,   -1,   -1,   -1,  272,   -1,
  274,  275,  276,   -1,   -1,  278,  279,   -1,   -1,   -1,
  283,  284,  285,  286,   -1,   -1,  278,  279,   -1,   -1,
   -1,  283,  284,  285,  286,   -1,   -1,   -1,   37,   -1,
   -1,   -1,   41,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,  278,  279,   -1,   -1,   -1,  283,  284,  285,
  286,   60,   -1,   62,   41,   41,   -1,   44,   44,  278,
  279,   -1,   -1,   -1,   -1,   -1,  285,  286,   -1,  278,
  279,   -1,   59,   59,  283,  284,  285,  286,  278,  279,
   -1,   -1,   91,  283,  284,  285,  286,   -1,   33,   -1,
   -1,   -1,   -1,   -1,   -1,   40,   -1,   -1,  278,  279,
   45,   -1,   -1,   -1,  278,  279,   93,   93,   -1,  283,
  284,  285,  286,  278,  279,   -1,   -1,   33,  283,  284,
  285,  286,   -1,   -1,   40,   37,   -1,   -1,   -1,   45,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,  278,
  279,   -1,   -1,   -1,  283,  284,  285,  286,   60,   37,
   62,   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,
   -1,   -1,   -1,  278,  279,   -1,   -1,   -1,  283,  284,
  285,  286,   60,   -1,   62,   -1,   -1,   -1,  123,   91,
  125,   93,   -1,   37,   -1,   -1,   -1,   41,   42,   43,
   -1,   45,   46,   47,   -1,   -1,   -1,   41,   -1,   43,
   44,   45,   -1,   91,   37,   93,   60,  123,   62,   42,
   43,   -1,   45,   46,   47,   59,   60,   -1,   62,   -1,
   41,   -1,   -1,   44,   -1,   37,   -1,   60,   -1,   62,
   42,   43,   -1,   45,   46,   47,   37,   91,   59,   -1,
   -1,   42,   43,   -1,   45,   46,   47,   -1,   60,   93,
   62,   -1,   -1,   41,   -1,   -1,   44,   37,   91,   60,
   -1,   62,   42,   43,   -1,   45,   46,   47,   -1,  278,
  279,   59,   93,   -1,  283,  284,  285,  286,   37,   91,
   60,   -1,   62,   42,   43,   -1,   45,   46,   47,   -1,
   91,  278,  279,  279,   -1,   -1,   -1,   -1,  285,  286,
   -1,   60,   -1,   62,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   91,  257,  258,  259,  260,  261,  262,  263,   -1,
  265,  266,  267,  268,   -1,  270,  271,  272,  273,  274,
  275,  276,   91,   -1,   -1,   41,  281,   -1,   44,   -1,
   -1,  257,  258,  259,  260,  261,  262,  263,   -1,  265,
  266,  267,  268,   59,  270,  271,  272,  273,  274,  275,
  276,   -1,   -1,   33,   -1,  281,  278,  279,   -1,   -1,
   40,  283,  284,  285,  286,   45,   41,  125,   -1,   44,
   -1,   41,   -1,   43,   44,   45,   -1,   93,   -1,   -1,
  278,  279,   -1,   -1,   59,  283,  284,  285,  286,   59,
   60,   33,   62,   -1,   -1,   -1,   37,   -1,   40,   -1,
   -1,   42,   43,   45,   45,   46,   47,  257,  258,  259,
  260,  261,  262,   -1,  278,  279,   -1,   -1,   93,  283,
  284,  285,  286,   93,  278,  279,   -1,  277,   -1,  283,
  284,  285,  286,   46,   -1,  278,  279,   -1,   -1,   -1,
  283,  284,  285,  286,   -1,   -1,   59,  278,  279,   -1,
   91,   93,   -1,   -1,  285,  286,  278,  279,   -1,   -1,
   -1,  283,  284,  285,  286,   -1,   -1,  278,   81,   -1,
   -1,   -1,  283,  284,  285,  286,   -1,   -1,   -1,   -1,
  278,  279,   -1,   -1,   -1,   -1,   -1,  285,  286,   -1,
   -1,   -1,   -1,  283,  284,  285,  286,   -1,   -1,  257,
  258,  259,  260,  261,  262,   46,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  283,  284,   -1,   -1,   59,   -1,
   -1,   -1,  280,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  146,   -1,  148,   -1,   -1,   -1,   -1,
   81,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  166,  167,   -1,   -1,   -1,   -1,  172,
   -1,   -1,  278,  279,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  261,   -1,  263,   -1,  265,   -1,   -1,   -1,   -1,
   -1,   -1,  272,   -1,  274,  275,  276,   -1,   -1,   -1,
   -1,   -1,   -1,  278,  279,  146,   -1,  148,  278,  279,
   52,   -1,   -1,  283,  284,  285,  286,   -1,   60,   61,
   62,  263,   -1,  265,   -1,  166,  167,   -1,   -1,   -1,
  272,  172,  274,  275,  276,   -1,   -1,   -1,   80,   -1,
   82,   -1,   -1,   -1,   -1,   -1,   88,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  101,
  102,  103,  104,  105,  106,  107,  108,  109,  110,  111,
  112,  113,  114,   -1,  116,   -1,   -1,   -1,   -1,   -1,
  122,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  145,   -1,  147,   -1,   -1,   -1,  151,
   -1,  153,  154,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=288;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"VOID","BOOL","INT","STRING",
"CLASS","DOUBLE","NULL","EXTENDS","THIS","WHILE","FOR","IF","ELSE","RETURN",
"BREAK","NEW","PRINT","READ_INTEGER","READ_LINE","LITERAL","IDENTIFIER","AND",
"OR","STATIC","REPEAT","UNTIL","LESS_EQUAL","GREATER_EQUAL","EQUAL","NOT_EQUAL",
"UMINUS","EMPTY",
};
final static String yyrule[] = {
"$accept : Program",
"Program : ClassList",
"ClassList : ClassList ClassDef",
"ClassList : ClassDef",
"VariableDef : Variable ';'",
"Variable : Type IDENTIFIER",
"Type : INT",
"Type : VOID",
"Type : BOOL",
"Type : DOUBLE",
"Type : STRING",
"Type : CLASS IDENTIFIER",
"Type : Type '[' ']'",
"ClassDef : CLASS IDENTIFIER ExtendsClause '{' FieldList '}'",
"ExtendsClause : EXTENDS IDENTIFIER",
"ExtendsClause :",
"FieldList : FieldList VariableDef",
"FieldList : FieldList FunctionDef",
"FieldList :",
"Formals : VariableList",
"Formals :",
"VariableList : VariableList ',' Variable",
"VariableList : Variable",
"FunctionDef : STATIC Type IDENTIFIER '(' Formals ')' StmtBlock",
"FunctionDef : Type IDENTIFIER '(' Formals ')' StmtBlock",
"StmtBlock : '{' StmtList '}'",
"StmtList : StmtList Stmt",
"StmtList :",
"Stmt : VariableDef",
"Stmt : SimpleStmt ';'",
"Stmt : IfStmt",
"Stmt : WhileStmt",
"Stmt : RepeatStmt ';'",
"Stmt : ForStmt",
"Stmt : ReturnStmt ';'",
"Stmt : PrintStmt ';'",
"Stmt : BreakStmt ';'",
"Stmt : StmtBlock",
"SimpleStmt : LValue '=' Expr",
"SimpleStmt : Call",
"SimpleStmt :",
"Receiver : Expr '.'",
"Receiver :",
"LValue : Receiver IDENTIFIER",
"LValue : Expr '[' Expr ']'",
"Call : Receiver IDENTIFIER '(' Actuals ')'",
"Expr : LValue",
"Expr : Call",
"Expr : Constant",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr '<' Expr",
"Expr : Expr '>' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : '(' Expr ')'",
"Expr : '-' Expr",
"Expr : '!' Expr",
"Expr : READ_INTEGER '(' ')'",
"Expr : READ_LINE '(' ')'",
"Expr : THIS",
"Expr : NEW IDENTIFIER '(' ')'",
"Expr : NEW Type '[' Expr ']'",
"Expr : '(' CLASS IDENTIFIER ')' Expr",
"Constant : LITERAL",
"Constant : NULL",
"Actuals : ExprList",
"Actuals :",
"ExprList : ExprList ',' Expr",
"ExprList : Expr",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"RepeatStmt : REPEAT Stmt UNTIL '(' Expr ')'",
"ForStmt : FOR '(' SimpleStmt ';' Expr ';' SimpleStmt ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"PrintStmt : PRINT '(' ExprList ')'",
};

//#line 431 "Parser.y"
    
	/**
	 * 打印当前归约所用的语法规则<br>
	 * 请勿修改。
	 */
    public boolean onReduce(String rule) {
		if (rule.startsWith("$$"))
			return false;
		else
			rule = rule.replaceAll(" \\$\\$\\d+", "");

   	    if (rule.endsWith(":"))
    	    System.out.println(rule + " <empty>");
   	    else
			System.out.println(rule);
		return false;
    }
    
    public void diagnose() {
		addReduceListener(this);
		yyparse();
	}
//#line 580 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        //if (yychar < 0)    //it it didn't work/error
        //  {
        //  yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
        //  }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0 || valptr<0)   //check for under & overflow here
            {
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0 || valptr<0)   //check for under & overflow here
              {
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    if (reduceListener == null || reduceListener.onReduce(yyrule[yyn])) // if intercepted!
      switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 53 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).clist, val_peek(0).loc);
					}
break;
case 2:
//#line 59 "Parser.y"
{
						yyval.clist.add(val_peek(0).cdef);
					}
break;
case 3:
//#line 63 "Parser.y"
{
                		yyval.clist = new ArrayList<Tree.ClassDef>();
                		yyval.clist.add(val_peek(0).cdef);
                	}
break;
case 5:
//#line 73 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 79 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 83 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 8:
//#line 87 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 9:
//#line 91 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.DOUBLE ,val_peek(0). loc);
					}
break;
case 10:
//#line 95 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 11:
//#line 99 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 12:
//#line 103 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 13:
//#line 109 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 14:
//#line 115 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 15:
//#line 119 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 16:
//#line 125 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 17:
//#line 129 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 18:
//#line 133 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 20:
//#line 141 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 21:
//#line 148 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 22:
//#line 152 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 23:
//#line 159 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 163 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 25:
//#line 169 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 26:
//#line 175 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 27:
//#line 179 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 28:
//#line 186 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 29:
//#line 191 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 38:
//#line 207 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 39:
//#line 211 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 40:
//#line 215 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 42:
//#line 222 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 43:
//#line 228 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 44:
//#line 235 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 45:
//#line 241 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 46:
//#line 250 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 49:
//#line 256 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 50:
//#line 260 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 51:
//#line 264 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 52:
//#line 268 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 53:
//#line 272 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 54:
//#line 276 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 280 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 284 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 57:
//#line 288 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 58:
//#line 292 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 59:
//#line 296 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 60:
//#line 300 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 61:
//#line 304 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 308 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 63:
//#line 312 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 64:
//#line 316 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 65:
//#line 320 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 66:
//#line 324 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 67:
//#line 328 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 68:
//#line 332 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 69:
//#line 336 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 70:
//#line 340 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 71:
//#line 346 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 72:
//#line 350 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 74:
//#line 357 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 75:
//#line 364 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 76:
//#line 368 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 77:
//#line 375 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 78:
//#line 381 "Parser.y"
{
						yyval.stmt = new Tree.RepeatLoop(val_peek(1).expr, val_peek(4).stmt, val_peek(5).loc);
					}
break;
case 79:
//#line 387 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 80:
//#line 393 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 81:
//#line 399 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 82:
//#line 405 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 83:
//#line 409 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 84:
//#line 415 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 85:
//#line 419 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 86:
//#line 425 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
//#line 1173 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        //if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################

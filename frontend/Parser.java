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
public final static short NULL=262;
public final static short EXTENDS=263;
public final static short THIS=264;
public final static short WHILE=265;
public final static short FOR=266;
public final static short IF=267;
public final static short ELSE=268;
public final static short RETURN=269;
public final static short BREAK=270;
public final static short NEW=271;
public final static short PRINT=272;
public final static short READ_INTEGER=273;
public final static short READ_LINE=274;
public final static short LITERAL=275;
public final static short IDENTIFIER=276;
public final static short AND=277;
public final static short OR=278;
public final static short STATIC=279;
public final static short REPEAT=280;
public final static short UNTIL=281;
public final static short LESS_EQUAL=282;
public final static short GREATER_EQUAL=283;
public final static short EQUAL=284;
public final static short NOT_EQUAL=285;
public final static short UMINUS=286;
public final static short EMPTY=287;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    5,    5,
    5,    2,    6,    6,    7,    7,    7,    9,    9,   10,
   10,    8,    8,   11,   12,   12,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   14,   14,   14,   25,
   25,   22,   22,   24,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   27,
   27,   26,   26,   28,   28,   16,   17,   18,   21,   15,
   29,   29,   19,   19,   20,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    2,
    3,    6,    2,    0,    2,    2,    0,    1,    0,    3,
    1,    7,    6,    3,    2,    0,    1,    2,    1,    1,
    2,    1,    2,    2,    2,    1,    3,    1,    0,    2,
    0,    2,    4,    5,    1,    1,    1,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    2,    2,    3,    3,    1,    4,    5,    5,    1,
    1,    1,    0,    3,    1,    5,    6,    9,    1,    6,
    2,    0,    2,    1,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,   13,   17,
    0,    7,    8,    6,    9,    0,    0,   12,   15,    0,
    0,   16,   10,    0,    4,    0,    0,    0,    0,   11,
    0,   21,    0,    0,    0,    0,    5,    0,    0,    0,
   26,   23,   20,   22,    0,   71,   66,    0,    0,    0,
    0,   79,    0,    0,    0,    0,   70,    0,    0,    0,
    0,   24,   27,   36,   25,    0,   29,   30,    0,   32,
    0,    0,    0,    0,    0,    0,    0,   47,    0,    0,
    0,   45,    0,   46,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   28,   31,   33,   34,   35,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   40,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,   65,    0,    0,   61,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   67,    0,    0,
   85,    0,    0,   43,    0,    0,   76,    0,    0,   68,
    0,    0,   69,   44,    0,    0,   80,   77,    0,   81,
    0,   78,
};
final static short yydgoto[] = {                          2,
    3,    4,   63,   20,   33,    8,   11,   22,   34,   35,
   64,   45,   65,   66,   67,   68,   69,   70,   71,   72,
   73,   82,   75,   84,   77,  155,   78,  123,  167,
};
final static short yysindex[] = {                      -253,
 -256,    0, -253,    0, -234,    0, -240,  -76,    0,    0,
  234,    0,    0,    0,    0, -238, -173,    0,    0,  -11,
  -88,    0,    0,  -87,    0,   15,  -44,   21, -173,    0,
 -173,    0,  -85,   33,   37,   50,    0,  -22, -173,  -22,
    0,    0,    0,    0,  318,    0,    0,   77,   79,   80,
  576,    0,   74,   83,   85,   86,    0,  353,  576,  576,
   -3,    0,    0,    0,    0,   68,    0,    0,   70,    0,
   73,   76,   78,   75,  443,    0, -130,    0,  576,  576,
  576,    0,  443,    0,  110,   63,  576,  117,  119, -120,
  -25,  -25, -113,  106,    0,    0,    0,    0,    0,  576,
  576,  576,  576,  576,  576,  576,  576,  576,  576,  576,
  576,  576,  576,    0,  576,  127,  132,  111,  261,  130,
  561,  443,  -17,    0,    0,  143,  131,    0,  443,  505,
  475,  161,  161,  139,  139,  174,  174,  -25,  -25,  -25,
  161,  161,  358,  576,  353,  576,  353,    0,  382,  576,
    0,  576,  576,    0,  146,  149,    0,  393,  -73,    0,
  443,  417,    0,    0,  576,  353,    0,    0,  155,    0,
  353,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  200,    0,   82,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  150,    0,    0,  169,    0,
  169,    0,    0,    0,  171,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -58,    0,    0,    0,    0,    0,
  -57,    0,    0,    0,    0,    0,    0,  -58,  -74,  -74,
  -74,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  464,    0,   -2,    0,    0,  -74,  -58,
  -74,    0,  154,    0,    0,    0,  -74,    0,    0,    0,
    9,   35,    0,    0,    0,    0,    0,    0,    0,  -74,
  -74,  -74,  -74,  -74,  -74,  -74,  -74,  -74,  -74,  -74,
  -74,  -74,  -74,    0,  -74,  -28,    0,    0,    0,    0,
  -74,   18,    0,    0,    0,    0,    0,    0,  -36,  456,
  121,   52,  284,  588,  593,  644,  670,   62,   71,   97,
  406,  515,    0,  -31,  -58,  -74,  -58,    0,    0,  -74,
    0,  -74,  -74,    0,    0,  176,    0,    0,  -33,    0,
   26,    0,    0,    0,  -30,  -58,    0,    0,    0,    0,
  -58,    0,
};
final static short yygindex[] = {                         0,
    0,  212,  211,   44,   11,    0,    0,    0,  204,    0,
   60,    0,  221,  -55,    0,    0,    0,    0,    0,    0,
    0,  651,  793,  698,    0,    0,    0,  100,    0,
};
final static int YYTABLESIZE=955;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         82,
   39,   84,   27,   27,   37,   27,   82,    1,   42,   73,
   39,   82,   42,   42,   42,   42,   42,   42,   42,    5,
  114,   21,   37,  151,  118,   82,  150,   24,    7,   60,
   42,   42,   42,   42,   46,    9,   61,   23,   38,   46,
   46,   59,   46,   46,   46,   62,   10,   25,   30,   62,
   62,   62,   62,   62,   29,   62,   38,   46,   75,   46,
   31,   75,   42,   86,   42,  115,   74,   62,   62,   74,
   62,   63,   32,   38,   32,   63,   63,   63,   63,   63,
   39,   63,   43,   12,   13,   14,   15,   16,   46,   82,
   40,   82,   57,   63,   63,   57,   63,   42,   50,   44,
   41,   62,   50,   50,   50,   50,   50,   51,   50,  169,
   57,   51,   51,   51,   51,   51,   79,   51,   80,   81,
   50,   50,   87,   50,   88,   89,   95,   63,   96,   51,
   51,   97,   51,   52,   98,  100,   99,   52,   52,   52,
   52,   52,  111,   52,   57,  116,  128,  109,  107,  120,
  108,  114,  110,  121,   50,   52,   52,  124,   52,  125,
  126,   60,  127,   51,   60,  113,  144,  112,  111,  146,
  148,  153,  145,  109,  107,  111,  108,  114,  110,   60,
  109,  107,  152,  108,  114,  110,  164,   26,   28,   52,
   37,  113,  150,  112,  166,  171,  115,  111,  113,    1,
  112,   41,  109,  107,   14,  108,  114,  110,    5,   19,
  111,   18,   83,   60,    6,  109,   72,   41,   41,  114,
  110,   19,  115,   82,   82,   82,   82,   82,   82,  115,
   82,   82,   82,   82,   36,   82,   82,   82,   82,   82,
   82,   82,   82,  156,   41,   41,   82,   82,   42,   42,
    0,  115,    0,   42,   42,   42,   42,   93,   46,    0,
   47,    0,    0,    0,  115,    0,    0,   53,    0,   55,
   56,   57,    0,    0,   46,   46,    0,    0,   90,   46,
   46,   46,   46,    0,    0,   62,   62,    0,    0,    0,
   62,   62,   62,   62,    0,    0,    0,  111,    0,    0,
    0,  147,  109,  107,    0,  108,  114,  110,    0,    0,
    0,   63,   63,    0,    0,    0,   63,   63,   63,   63,
  113,    0,  112,    0,   58,    0,    0,   58,   57,   57,
   12,   13,   14,   15,   16,   57,   57,    0,   50,   50,
    0,    0,   58,   50,   50,   50,   50,   51,   51,   85,
   60,  115,   51,   51,   51,   51,    0,   61,   18,    0,
    0,    0,   59,    0,    0,  157,    0,  159,    0,    0,
    0,    0,    0,   52,   52,    0,   58,    0,   52,   52,
   52,   52,  101,  102,    0,   60,  170,  103,  104,  105,
  106,  172,   61,    0,  111,    0,    0,   59,   60,  109,
  107,    0,  108,  114,  110,    0,    0,    0,  101,  102,
    0,    0,    0,  103,  104,  105,  106,  113,  111,  112,
  103,  104,    0,  109,  107,    0,  108,  114,  110,  111,
    0,    0,    0,    0,  109,  107,    0,  108,  114,  110,
   41,  113,   62,  112,    0,    0,   56,    0,  115,   56,
  154,  165,  113,  111,  112,    0,    0,  168,  109,  107,
    0,  108,  114,  110,   56,    0,    0,    0,    0,    0,
    0,    0,  115,    0,  160,   41,  113,    0,  112,  111,
    0,    0,    0,  115,  109,  107,    0,  108,  114,  110,
   12,   13,   14,   15,   16,    0,   59,    0,   56,   59,
   45,    0,  113,    0,  112,   45,   45,  115,   45,   45,
   45,  111,   17,    0,   59,    0,  109,  107,    0,  108,
  114,  110,    0,   45,    0,   45,    0,    0,    0,    0,
    0,    0,    0,  115,  113,    0,  112,  101,  102,    0,
    0,  111,  103,  104,  105,  106,  109,  107,   59,  108,
  114,  110,    0,    0,   45,   55,    0,    0,   55,    0,
   58,   58,    0,    0,  113,  115,  112,   58,   58,    0,
    0,    0,    0,   55,   12,   13,   14,   15,   16,   46,
    0,   47,   48,   49,   50,    0,   51,   52,   53,   54,
   55,   56,   57,   60,    0,  115,    0,   58,    0,    0,
   61,    0,    0,    0,    0,   59,    0,   55,   60,   12,
   13,   14,   15,   16,   46,   61,   47,   48,   49,   50,
   59,   51,   52,   53,   54,   55,   56,   57,   53,    0,
    0,   53,   58,   54,  101,  102,   54,    0,    0,  103,
  104,  105,  106,    0,    0,    0,   53,    0,    0,    0,
    0,   54,    0,   30,    0,    0,    0,    0,  101,  102,
    0,    0,    0,  103,  104,  105,  106,    0,    0,  101,
  102,    0,    0,    0,  103,  104,  105,  106,    0,    0,
   53,    0,   56,   56,   48,   54,   48,   48,   48,   56,
   56,    0,    0,  101,  102,   74,    0,    0,  103,  104,
  105,  106,   48,   48,    0,   48,    0,    0,   74,    0,
   49,    0,   49,   49,   49,    0,    0,    0,    0,  101,
  102,    0,    0,    0,  103,  104,  105,  106,   49,   49,
   74,   49,   59,   59,    0,    0,   48,    0,    0,    0,
   45,   45,   76,    0,    0,   45,   45,   45,   45,    0,
    0,  101,    0,    0,    0,   76,  103,  104,  105,  106,
    0,    0,   49,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   76,    0,    0,
    0,    0,    0,    0,    0,    0,  103,  104,  105,  106,
    0,   55,   55,    0,    0,   74,    0,   74,   55,   55,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   74,   74,    0,    0,    0,
    0,   74,   46,    0,   47,    0,    0,    0,    0,    0,
    0,   53,    0,   55,   56,   57,    0,   46,    0,   47,
    0,    0,   76,   83,   76,    0,   53,    0,   55,   56,
   57,   91,   92,   94,    0,    0,    0,    0,    0,    0,
    0,    0,   76,   76,   53,   53,    0,    0,   76,   54,
   54,  117,    0,  119,    0,    0,    0,    0,    0,  122,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  129,  130,  131,  132,  133,  134,  135,  136,
  137,  138,  139,  140,  141,  142,    0,  143,    0,    0,
    0,    0,    0,  149,    0,    0,    0,    0,    0,    0,
   48,   48,    0,    0,    0,   48,   48,   48,   48,    0,
    0,    0,    0,    0,    0,    0,  122,    0,  158,    0,
    0,    0,  161,    0,  162,  163,   49,   49,    0,    0,
    0,   49,   49,   49,   49,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   59,   59,   91,   91,   41,   91,   40,  261,   37,   41,
   41,   45,   41,   42,   43,   44,   45,   46,   47,  276,
   46,   11,   59,   41,   80,   59,   44,   17,  263,   33,
   59,   60,   61,   62,   37,  276,   40,  276,   41,   42,
   43,   45,   45,   46,   47,   37,  123,   59,   93,   41,
   42,   43,   44,   45,   40,   47,   59,   60,   41,   62,
   40,   44,   91,   53,   93,   91,   41,   59,   60,   44,
   62,   37,   29,   41,   31,   41,   42,   43,   44,   45,
   44,   47,   39,  257,  258,  259,  260,  261,   91,  123,
   41,  125,   41,   59,   60,   44,   62,   38,   37,   40,
  123,   93,   41,   42,   43,   44,   45,   37,   47,  165,
   59,   41,   42,   43,   44,   45,   40,   47,   40,   40,
   59,   60,   40,   62,   40,   40,   59,   93,   59,   59,
   60,   59,   62,   37,   59,   61,   59,   41,   42,   43,
   44,   45,   37,   47,   93,  276,   41,   42,   43,   40,
   45,   46,   47,   91,   93,   59,   60,   41,   62,   41,
  281,   41,  276,   93,   44,   60,   40,   62,   37,   59,
   41,   41,   41,   42,   43,   37,   45,   46,   47,   59,
   42,   43,   40,   45,   46,   47,   41,  276,  276,   93,
  276,   60,   44,   62,  268,   41,   91,   37,   60,    0,
   62,  276,   42,   43,  123,   45,   46,   47,   59,   41,
   37,   41,   59,   93,    3,   42,   41,  276,  276,   46,
   47,   11,   91,  257,  258,  259,  260,  261,  262,   91,
  264,  265,  266,  267,   31,  269,  270,  271,  272,  273,
  274,  275,  276,  144,  276,  276,  280,  281,  277,  278,
   -1,   91,   -1,  282,  283,  284,  285,  261,  262,   -1,
  264,   -1,   -1,   -1,   91,   -1,   -1,  271,   -1,  273,
  274,  275,   -1,   -1,  277,  278,   -1,   -1,   58,  282,
  283,  284,  285,   -1,   -1,  277,  278,   -1,   -1,   -1,
  282,  283,  284,  285,   -1,   -1,   -1,   37,   -1,   -1,
   -1,   41,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   -1,  277,  278,   -1,   -1,   -1,  282,  283,  284,  285,
   60,   -1,   62,   -1,   41,   -1,   -1,   44,  277,  278,
  257,  258,  259,  260,  261,  284,  285,   -1,  277,  278,
   -1,   -1,   59,  282,  283,  284,  285,  277,  278,  276,
   33,   91,  282,  283,  284,  285,   -1,   40,  125,   -1,
   -1,   -1,   45,   -1,   -1,  145,   -1,  147,   -1,   -1,
   -1,   -1,   -1,  277,  278,   -1,   93,   -1,  282,  283,
  284,  285,  277,  278,   -1,   33,  166,  282,  283,  284,
  285,  171,   40,   -1,   37,   -1,   -1,   45,  278,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,  277,  278,
   -1,   -1,   -1,  282,  283,  284,  285,   60,   37,   62,
  282,  283,   -1,   42,   43,   -1,   45,   46,   47,   37,
   -1,   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,
  123,   60,  125,   62,   -1,   -1,   41,   -1,   91,   44,
   93,   59,   60,   37,   62,   -1,   -1,   41,   42,   43,
   -1,   45,   46,   47,   59,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   91,   -1,   93,  123,   60,   -1,   62,   37,
   -1,   -1,   -1,   91,   42,   43,   -1,   45,   46,   47,
  257,  258,  259,  260,  261,   -1,   41,   -1,   93,   44,
   37,   -1,   60,   -1,   62,   42,   43,   91,   45,   46,
   47,   37,  279,   -1,   59,   -1,   42,   43,   -1,   45,
   46,   47,   -1,   60,   -1,   62,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   60,   -1,   62,  277,  278,   -1,
   -1,   37,  282,  283,  284,  285,   42,   43,   93,   45,
   46,   47,   -1,   -1,   91,   41,   -1,   -1,   44,   -1,
  277,  278,   -1,   -1,   60,   91,   62,  284,  285,   -1,
   -1,   -1,   -1,   59,  257,  258,  259,  260,  261,  262,
   -1,  264,  265,  266,  267,   -1,  269,  270,  271,  272,
  273,  274,  275,   33,   -1,   91,   -1,  280,   -1,   -1,
   40,   -1,   -1,   -1,   -1,   45,   -1,   93,   33,  257,
  258,  259,  260,  261,  262,   40,  264,  265,  266,  267,
   45,  269,  270,  271,  272,  273,  274,  275,   41,   -1,
   -1,   44,  280,   41,  277,  278,   44,   -1,   -1,  282,
  283,  284,  285,   -1,   -1,   -1,   59,   -1,   -1,   -1,
   -1,   59,   -1,   93,   -1,   -1,   -1,   -1,  277,  278,
   -1,   -1,   -1,  282,  283,  284,  285,   -1,   -1,  277,
  278,   -1,   -1,   -1,  282,  283,  284,  285,   -1,   -1,
   93,   -1,  277,  278,   41,   93,   43,   44,   45,  284,
  285,   -1,   -1,  277,  278,   45,   -1,   -1,  282,  283,
  284,  285,   59,   60,   -1,   62,   -1,   -1,   58,   -1,
   41,   -1,   43,   44,   45,   -1,   -1,   -1,   -1,  277,
  278,   -1,   -1,   -1,  282,  283,  284,  285,   59,   60,
   80,   62,  277,  278,   -1,   -1,   93,   -1,   -1,   -1,
  277,  278,   45,   -1,   -1,  282,  283,  284,  285,   -1,
   -1,  277,   -1,   -1,   -1,   58,  282,  283,  284,  285,
   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   80,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  282,  283,  284,  285,
   -1,  277,  278,   -1,   -1,  145,   -1,  147,  284,  285,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  165,  166,   -1,   -1,   -1,
   -1,  171,  262,   -1,  264,   -1,   -1,   -1,   -1,   -1,
   -1,  271,   -1,  273,  274,  275,   -1,  262,   -1,  264,
   -1,   -1,  145,   51,  147,   -1,  271,   -1,  273,  274,
  275,   59,   60,   61,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  165,  166,  277,  278,   -1,   -1,  171,  277,
  278,   79,   -1,   81,   -1,   -1,   -1,   -1,   -1,   87,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  100,  101,  102,  103,  104,  105,  106,  107,
  108,  109,  110,  111,  112,  113,   -1,  115,   -1,   -1,
   -1,   -1,   -1,  121,   -1,   -1,   -1,   -1,   -1,   -1,
  277,  278,   -1,   -1,   -1,  282,  283,  284,  285,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  144,   -1,  146,   -1,
   -1,   -1,  150,   -1,  152,  153,  277,  278,   -1,   -1,
   -1,  282,  283,  284,  285,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=287;
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
"CLASS","NULL","EXTENDS","THIS","WHILE","FOR","IF","ELSE","RETURN","BREAK",
"NEW","PRINT","READ_INTEGER","READ_LINE","LITERAL","IDENTIFIER","AND","OR",
"STATIC","REPEAT","UNTIL","LESS_EQUAL","GREATER_EQUAL","EQUAL","NOT_EQUAL",
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

//#line 427 "Parser.y"
    
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
//#line 570 "Parser.java"
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
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 10:
//#line 95 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 11:
//#line 99 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 12:
//#line 105 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 13:
//#line 111 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 14:
//#line 115 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 15:
//#line 121 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 16:
//#line 125 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 17:
//#line 129 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 19:
//#line 137 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 20:
//#line 144 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 21:
//#line 148 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 22:
//#line 155 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 23:
//#line 159 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 165 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 25:
//#line 171 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 26:
//#line 175 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 27:
//#line 182 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 28:
//#line 187 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 37:
//#line 203 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 38:
//#line 207 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 39:
//#line 211 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 41:
//#line 218 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 42:
//#line 224 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 43:
//#line 231 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 44:
//#line 237 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 45:
//#line 246 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 48:
//#line 252 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 49:
//#line 256 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 50:
//#line 260 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 51:
//#line 264 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 52:
//#line 268 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 53:
//#line 272 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 54:
//#line 276 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 280 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 284 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 57:
//#line 288 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 58:
//#line 292 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 59:
//#line 296 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 60:
//#line 300 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 61:
//#line 304 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 62:
//#line 308 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 63:
//#line 312 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 64:
//#line 316 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 65:
//#line 320 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 66:
//#line 324 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 67:
//#line 328 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 68:
//#line 332 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 69:
//#line 336 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 70:
//#line 342 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 71:
//#line 346 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 73:
//#line 353 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 74:
//#line 360 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 75:
//#line 364 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 76:
//#line 371 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 77:
//#line 377 "Parser.y"
{
						yyval.stmt = new Tree.RepeatLoop(val_peek(1).expr, val_peek(4).stmt, val_peek(5).loc);
					}
break;
case 78:
//#line 383 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 79:
//#line 389 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 80:
//#line 395 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 81:
//#line 401 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 82:
//#line 405 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 83:
//#line 411 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 84:
//#line 415 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 85:
//#line 421 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
//#line 1157 "Parser.java"
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

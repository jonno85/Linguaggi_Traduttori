package it.polito.lt.skype.generated.parser;

import java_cup.runtime.*;
import it.polito.lt.skype.generated.parser.sym;
import java.util.*;
import java.text.*;
import it.polito.lt.skype.manager.*;
import it.polito.lt.skype.command.*;
import it.polito.lt.skype.bot.*;


 	/* NB: dato che JFlex genera la classe e i costruttori omettendo "public" 
	* e dato che l'ant-clean deve pulire anche i generati, 
	* ricordarsi ad ogni generazione dello scanner di aggiungere public 
	* al nome della classe Lexer ai costruttori!
	* NB2: risolto con ant task da jonni
	* */




%%
%class Lexer
%cup
%line
%column
%caseless
%unicode
%xstate comment
%state script

%{
	/* Per disattivare il debugging, quindi la stampa dei simboli riconosciuti
	da parte dello scanner impostare la costante _DEBUG a false */
	private static final boolean _DEBUG = false;
	private Symbol symbol(int type) {
		if (_DEBUG) System.out.print("# "+type+" "+yytext()+"\n");	
		return new Symbol(type, yyline, yycolumn);
  	}
	private Symbol symbol(int type, Object value) {
	    	if (_DEBUG) System.out.print("# "+type+" > " +value+"\n");
		return new Symbol(type, yyline, yycolumn, value);
	}
	private int lines=0;	
%}



sp = " "

nl = \n|\r|\r\n
tab = [\t]
key = (home|etc|usr|var|tmp|root|boot|opt|dev|lib|bin|sbin|var|sys|mnt|media|logs|eclipse|user)
ac = "/*"
cc = "*/"
RO = "("
RC = ")"
SO = "["
SC = "]"
plus = "+"
minus = "-"
times = "*"
div = "/"
pv = ";"

sep_dir = "/"
//sep = {sep_dir}|{minus}|{sp}

//des_mont = (naio|braio|zo|ile|gio|gno|lio|sto|tembre|obre|embre|uary|ruary|ch|il|e|y|tember|ober|ember)
//des_day = ((t|col|v)?edi|erdi|ato|enica|(s|nes|rs|ur)?day)
Month = jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec
//Day = (lun|mar|mer|gio|ven|sab|dom|sun|mon|tue|wed|thu|fri|sat){des_day}?
//g = (01|02|03|04|05|06|07|08|09)
//data = ({g}|[10-31]){minus}({g}|[10-12]){minus}((199{digit})|(20{digit}{digit}))
sep_data= {sep_dir}|{minus}|{sp}
data	= {day}{sep_data}{month}{sep_data}{year}
day	= 0[1-9]|[1-2][0-9]|3[0-1]//|{Day}
month	= 0[1-9]|1[0-2]|{Month}
year	= [0-9]{4}

conf = (i|a|e)

com_ex = (esegui|exec)
com_script_start = (start|inizio)
com_script_end = (end|fine)
com_script_throw = (launch|lancia)
com_mkdir = (mkdir)
com_cp = (copia|cp|copy)
com_mv = (muovi|mv|move)
com_cd = (vai|go|change|cd)
com_rm = (elimina|cancella|delete|cancel|rm)
com_find = (cerca|trova|seek|find)
com_ls = (mostra|show|list|ls)
com_str = (crea|create)
com_if = (se|if)
com_if_2 = (allora|then)
com_if_m = (altrimenti|else)
com_if_e = (fineif|endif)
com_for = (foreach)
com_for_m = (do)
com_for_e = (prossimo|next)
com_p = (print|stampa)

where = (dove|where) 

prep_supporto = ("più"|piu|meno|to|for) //da rivedere

c_ug = (uguale|equal|like|=)
c_ugg = (==)

c_dis = (min|max|"?"|diverso|include)

min = ("<")
magg = (">")
minug = ("<=")
maggug = (">=")
diver = ("!="|"<>")


c_quan = (all|only|just)



c_or = (or|"|")
c_and = (and|"&")
c_not = ("!")
c_andd = (andand |"&&")
c_orr = (oror|"||")

bool = (true|false)


obj = (file|cartella|cartelle|file|directory|directories|dir)

date_criteria = (((dat|or)(a|e))|((giorn|tip|modificat)(o|i))|modify|((date|hour|day)s?))
dimension_criteria = (dimension(e|i|s)?)
permission_criteria = (((permess)(o|i))|(permission)s?)


order = (asc|desc|cres|decr)
unit = (byte|kb|kbyte|kilobyte|mb|mbyte|megabyte|gb|gbyte|gigabyte)

ext = (htm|html|php|zip|rar|tar|gzip|bz2|list|conf|sh|py|pdf|doc|c|h|txt|cpp|ss)

script_ext = ".ss"

digit=[0-9]
float = {digit}+("."{digit}+)
int = {digit}+ 
name = NAME
result = RESULT

id=[^0-9+.,;:"-"" "'|\\\"%?&/()#\[\]\{\}"\t""\n""\r""\r\n"=\<\>\*][^+.,;:" "'|\\\"%?&/()"\n""\r""\r\n"#\[\]\{\}"\t"=\<\>?\*]*
str= '([^\n\r']+|\\)*'
//[0-9a-zA-Z+.,;:" "?|\\\"%&/()"\n""\r""\r\n"#\[\]\{\}"\t"=\<\>*]+

sp_char = {times}|"?"

//{id}"."{ext}|{times}"."{times}|{id}"."{times}

file = ({sp_char}*|{id}*)+"."({sp_char}*|{id}*)+

%%
/*
<script> {
	{com_script_end}	{yybegin(YYINITIAL); return symbol(sym.End_S); }
}
*/

<comment>{
	{cc}				{yybegin(YYINITIAL);} 
	.				{;}
}

<YYINITIAL>{
{ac}					{yybegin(comment);} 
{com_script_start}			{/*yybegin(script);*/ return symbol(sym.Start_S);} 
{com_script_throw}			{return symbol(sym.Throw_S);}
{com_script_end}			{/*yybegin(YYINITIAL);*/ return symbol(sym.End_S); }

{data}					{
						Utility.mf("Data raccolta: " +yytext());
						/*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Date date = sdf.parse(yytext());
						GregorianCalendar calendar = new GregorianCalendar();
						calendar.setTime(date);
						Utility.mf("Date: "+calendar.toString());
						return symbol(sym.GMA,calendar);*/
						String tdata = yytext().substring(0,2)+yytext().substring(3,5)+yytext().substring(6,10);
						Utility.mf("Data trimmed: " +tdata);
						return symbol(sym.Data, new String(tdata));
					}
//{month}					{return symbol(sym.Month);}
//{giorn}					{return symbol(sym.Day);}

{where}					{return symbol(sym.Where);}
({times}?".")?{ext}			{return symbol(sym.Ext,new String(yytext()));}

{date_criteria}				{return symbol(sym.Date_Criteria);}
{dimension_criteria}			{return symbol(sym.Dimension_Criteria);}
{permission_criteria}			{return symbol(sym.Permission_Criteria);}

{obj}					{return symbol(sym.Obj,new String(yytext()));}

{prep_supporto}				{return symbol(sym.Prep_supp);}

{order}					{return symbol(sym.Order,new String(yytext()));}

{min}					{return symbol(sym.Min);}
{magg}					{return symbol(sym.Magg);}
{minug}					{return symbol(sym.Minug);}
{maggug} 				{return symbol(sym.Maggug);}
{diver} 				{return symbol(sym.Diver);}

{c_ug}					{return symbol(sym.C_Ug);}
{c_ugg}					{return symbol(sym.C_Ugg);}
{c_or}					{return symbol(sym.C_Or,new String("|"));}
{c_and}					{return symbol(sym.C_And,new String("&"));}
{c_not}					{return symbol(sym.C_Not,new String("!"));}
{c_andd}				{return symbol(sym.C_Andd,new String("&&"));}
{c_orr}					{return symbol(sym.C_Orr,new String("||"));}
{com_ex}				{return symbol(sym.Com_Ex);}
//{com_script}				{return symbol(sym.Com_Scr);}
{com_rm}				{return symbol(sym.Com_Rm);}
{com_find}{prep_supporto}?		{return symbol(sym.Com_Find);}
{com_cp}				{return symbol(sym.Com_Cp);}
{com_cd}				{return symbol(sym.Com_Cd);}
{com_ls}				{return symbol(sym.Com_Ls);}
{com_str}				{return symbol(sym.Com_Str);}
{com_mv}				{return symbol(sym.Com_Mov);}
{com_mkdir}				{return symbol(sym.Com_MKDir);}
{com_p}					{return symbol(sym.Com_P);}
{com_if}				{return symbol(sym.Com_If);}
{com_if_2}				{return symbol(sym.Com_If_2);}
{com_if_m}				{return symbol(sym.Com_If_m);}
{com_if_e}				{return symbol(sym.Com_If_e);}
{com_for}				{return symbol(sym.Com_For);}
{com_for_e}				{return symbol(sym.Com_For_e);}
{com_for_m}				{return symbol(sym.Com_For_m);}
{min}{min}				{return symbol(sym.Minor);}

"$"{name}				{return symbol(sym.Name);}
"$"{result}				{return symbol(sym.Result);}
"$"{id}					{return symbol(sym.Var,new String(yytext()));}
{bool}					{return symbol(sym.Bool,new Boolean(yytext()));}
({sep_dir}?{id})+{sep_dir}?		{return symbol(sym.Path,new String(yytext()));}
{nl}+					{lines++;System.out.println("\t\tlinea:"+lines);return symbol(sym.EL);}

{int}" "?{unit}				{return symbol(sym.IUnit);}
{float}" "?{unit}			{return symbol(sym.FUnit);}
{int}					{return symbol(sym.Int, new Integer(yytext()));}
{float}					{return symbol(sym.Vint, new Float(yytext()));}
{RO}					{return symbol(sym.RO);}
{RC}					{return symbol(sym.RC);}
{SO}					{return symbol(sym.SO);}
{SC}					{return symbol(sym.SC);}
{minus}					{return symbol(sym.Minus,new String(yytext()));}
{times}					{return symbol(sym.Times,new String(yytext()));}
{plus}					{return symbol(sym.Plus,new String(yytext()));}
{div}					{return symbol(sym.Div,new String(yytext()));}
{pv}					{return symbol(sym.Pv);}


{id}{script_ext}			{return symbol(sym.FileScript,new String(yytext()));}
{tab}					{;}
{sp}					{;}
","					{;}
{str}					{String s = new String(yytext());
					return symbol(sym.Str,s.substring(1, s.length()-1));}
//{id}					{return symbol(sym.ID,new String(yytext()));}
.					{System.out.println("errore: "+yytext());}
{file}					{return symbol(sym.File,new String(yytext()));}


}



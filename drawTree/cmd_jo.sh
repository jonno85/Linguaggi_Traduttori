#! /bin/tcsh -f
#source ~/POLITO/Linguaggi_Traduttori/drawTree/ambiente_traduttori
rm *.class;
rm Lexer.java parser.java sym.java Lexer.java~
jflex scanner_clean.jflex;
java java_cup/MainDrawTree parser_clean.cup;

javac *.java;
java Main ../input/in.txt;

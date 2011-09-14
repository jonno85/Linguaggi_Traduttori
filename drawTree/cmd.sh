#! /bin/tcsh -f
#source ~/POLITO/Linguaggi_Traduttori/drawTree/ambiente_traduttori
jflex scanner_clean.jflex;
java java_cup/MainDrawTree parser_clean.cup;
javac *.java;
java Main prova.txt;

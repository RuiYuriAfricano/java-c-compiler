package entidades;

import java.util.ArrayList;

/**
 *
 * @author Rui Malemba
 */
public class PalavrasReservadas {
    public static ArrayList<String> reservedWords;
    public PalavrasReservadas(){
    reservedWords = new ArrayList<String>();
    reservedWords.add("auto");
    reservedWords.add("break");
    reservedWords.add("case");
    //reservedWords.add("char");
    reservedWords.add("const");
    reservedWords.add("continue");
    reservedWords.add("default");
    reservedWords.add("do");
    //reservedWords.add("double");
    reservedWords.add("else");
    reservedWords.add("enum");
    reservedWords.add("extern");
    //reservedWords.add("float");
    reservedWords.add("for");
    reservedWords.add("goto");
    reservedWords.add("if");
    //reservedWords.add("int");
    reservedWords.add("long");
    reservedWords.add("register");
    reservedWords.add("return");
    reservedWords.add("short");
    reservedWords.add("signed");
    reservedWords.add("sizeof");
    reservedWords.add("static");
    reservedWords.add("struct");
    reservedWords.add("switch");
    reservedWords.add("typedef");
    reservedWords.add("union");
    reservedWords.add("unsigned");
    //reservedWords.add("void");
    reservedWords.add("volatile");
    reservedWords.add("while");
    // Palavras reservadas adicionais em C99
    reservedWords.add("_Bool");
    reservedWords.add("_Complex");
    reservedWords.add("_Imaginary");
    reservedWords.add("inline");
    reservedWords.add("restrict");
    reservedWords.add("_Alignas");
    reservedWords.add("_Alignof");
    reservedWords.add("_Atomic");
    reservedWords.add("_Generic");
    reservedWords.add("_Noreturn");
    reservedWords.add("_Static_assert");
    reservedWords.add("_Thread_local");
    // Palavras reservadas adicionais em C padrão
    //reservedWords.add("main");
    reservedWords.add("printf");
    reservedWords.add("scanf");
    reservedWords.add("gets");
    reservedWords.add("puts");

  
    }
}

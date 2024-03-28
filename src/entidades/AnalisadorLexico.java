package entidades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Rui Malemba
 */
public class AnalisadorLexico {

    private ArrayList<String> ficheiroLido;
    private String path = "programaC.txt";
    private String textoDaLinha = "";
    private int contarLinhasLidas = 0, indice = 0;
    private String textoLexema = "";
    private String lerChar = "";
    private int estado = 0;
    private int guardarLinha;
    private ArrayList<String> listaDeErros = new ArrayList<>();
    public int qtdErros = 0;

    public AnalisadorLexico(String path) {
        this.path = path;
        lerFicheiro();
    }

    // Método para ler do ficheiro de texto
    public void lerFicheiro() {
        BufferedReader reader;
        String text = "";
        ficheiroLido = new ArrayList<>();
        try {

            reader = new BufferedReader(new FileReader(path));
            text = reader.readLine();
            while (text != null) {
                ficheiroLido.add(text);
                text = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

    }

    // Método analex
    public ArrayList<TipoLex> analex() {
        ArrayList<TipoLex> result = new ArrayList<>();
        TipoLex lex = new TipoLex();
        boolean end = false;
        boolean bb = false;
        boolean simboloConhecido = true;

        boolean outro = false;
        do {
            boolean test = estado == 2 || estado == 200 || estado == 201 || estado == 202 || estado == 203
                    || estado == 204 || estado == 205 || estado == 6 || estado == 7 || estado == 12
                    || estado == 15 || estado == 19 || estado == 22 || estado == 23 || estado == 30
                    || estado == 31 || estado == 33 || estado == 36 || estado == 38 || estado == 47
                    || estado == 48 || estado == 49 || estado == 50 || estado == 51 || estado == 52
                    || estado == 53 || estado == 54 || estado == 55 || estado == 56 || estado == 57
                    || estado == 70 || estado == 64 || estado == 66;

            if (contarLinhasLidas < ficheiroLido.size()) {
                textoDaLinha = ficheiroLido.get(contarLinhasLidas);
            } else if (contarLinhasLidas == ficheiroLido.size() && !textoLexema.equals("") && simboloConhecido && test) {
                contarLinhasLidas--;
            }

            switch (estado) {
                // Estado inicial
                case 0: {
                    simboloConhecido = true;
                    // Só vai ler um simbolo, como se fosse "char lerChar";

                    if (contarLinhasLidas != ficheiroLido.size()) {
                        lerChar = Character.toString(textoDaLinha.charAt(indice));
                    }

                    //Avança para o próximo símbolo
                    if (Character.isWhitespace(lerChar.charAt(0))) {
                        indice++;
                        break;
                    } // Variavel
                    else if (Character.isLetter(lerChar.charAt(0)) || lerChar.charAt(0) == '_') {
                        estado = 1;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } // valor númerico
                    else if (Character.isDigit(lerChar.charAt(0))) {
                        estado = 3;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } // valor textual(string)
                    else if (lerChar.charAt(0) == '"') {
                        estado = 18;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } // valor carater(char)
                    else if (lerChar.charAt(0) == '\'') {
                        estado = 20;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } /*
                     * Paranteses, paranteses reto, chavetas, ponto e virgula
                     * virgula, ponto, interrogação, dois pontos, asterisco
                     */ else if (lerChar.charAt(0) == '(') {
                        estado = 47;

                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == ')') {
                        estado = 48;

                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == '[') {
                        estado = 49;

                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == ']') {
                        estado = 50;

                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == '{') {
                        estado = 51;
                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == '}') {
                        estado = 52;
                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == ';') {
                        estado = 53;

                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == ',') {
                        estado = 54;
                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == '?') {
                        estado = 55;
                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == ':') {
                        estado = 56;
                        textoLexema += lerChar;
                        indice++;

                    } else if (lerChar.charAt(0) == '.') {
                        estado = 57;
                        textoLexema += lerChar;
                        indice++;

                    } //Operador *
                    else if (lerChar.charAt(0) == '*') {
                        estado = 58;
                        textoLexema += lerChar;
                        indice++;

                    } // comentário e operador /
                    else if (lerChar.charAt(0) == '/') {
                        estado = 8;

                        if (textoDaLinha.substring(indice, textoDaLinha.length()).length() > 1) {

                            textoLexema += lerChar;
                            indice++;
                        }

                    } // operador aritmético
                    else if (lerChar.charAt(0) == '+') {
                        estado = 27;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } else if (lerChar.charAt(0) == '-') {
                        estado = 28;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } else if (lerChar.charAt(0) == '%') {
                        estado = 29;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } // operador relacional e um de atribuição
                    else if (lerChar.charAt(0) == '=' || lerChar.charAt(0) == '>' || lerChar.charAt(0) == '<') {
                        estado = 32;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } // operador lógico
                    else if (lerChar.charAt(0) == '&') {
                        estado = 34;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } else if (lerChar.charAt(0) == '|') {

                        estado = 35;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } else if (lerChar.charAt(0) == '!') {
                        if (textoDaLinha.substring(indice, textoDaLinha.length()).length() > 0 && Character.toString(textoDaLinha.charAt(indice + 1)).charAt(0) == '=') {
                            textoLexema += lerChar;
                            indice++;
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                            textoLexema += lerChar;
                            if (lerChar.charAt(0) == '=') {
                                estado = 33;
                            }
                        } else {
                            estado = 36;

                            if (textoDaLinha.length() > 1) {
                                textoLexema += lerChar;
                                indice++;
                            }
                        }

                    } // diretivas
                    else if (lerChar.charAt(0) == '#') {
                        estado = 37;

                        if (textoDaLinha.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }

                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " Símbolo desconhecido.");

                        if (indice != textoDaLinha.length()) {
                            indice++;
                        }

                        estado = 0;
                        simboloConhecido = false;
                    }
                    break;
                }
                // Estado 1
                case 1: {
                    if (indice < textoDaLinha.length()) {
                        lerChar = Character.toString(textoDaLinha.charAt(indice));
                    }
                    for (int i = indice; i < textoDaLinha.length(); i++) {
                        if (Character.isLetter(lerChar.charAt(0)) || lerChar.charAt(0) == '_'
                                || Character.isDigit(lerChar.charAt(0))) {
                            textoLexema += lerChar;
                            indice++;
                        } else {
                            i = textoDaLinha.length();
                            outro = true;
                        }

                        if (i + 1 < textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                        }
                    }
                    guardarLinha = contarLinhasLidas;
                    PalavrasReservadas kw = new PalavrasReservadas();
                    if (kw.reservedWords.indexOf(textoLexema) > -1) {
                        estado = 200;
                    } else if (textoLexema.equals("int")) {
                        estado = 201;
                    } else if (textoLexema.equals("float")) {
                        estado = 202;
                    } else if (textoLexema.equals("double")) {
                        estado = 203;
                    } else if (textoLexema.equals("char")) {
                        estado = 204;
                    } else if (textoLexema.equals("void")) {
                        estado = 205;
                    } else {
                        estado = 2;
                    }

                    break;
                }
                // Estado 2
                case 2: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_ID);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);

                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 200 palavra reservada
                case 200: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_KW);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);

                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 201 int
                case 201: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_INT);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);

                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 202 float
                case 202: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_FLOAT);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);

                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 203 double
                case 203: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_DOUBLE);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);

                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 204 char
                case 204: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_CHAR);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);

                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 205 void
                case 205: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_VOID);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);

                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 3
                case 3: {
                    boolean aux = false, erro = false;
                    if (indice == textoDaLinha.length()) {
                        break;
                    }
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    for (int i = indice; i < textoDaLinha.length(); i++) {
                        if (Character.isDigit(lerChar.charAt(0))) {
                            textoLexema += lerChar;
                            indice++;
                        } else if (lerChar.charAt(0) == '.') {
                            textoLexema += lerChar;
                            indice++;
                            i = textoDaLinha.length();
                            aux = true;// vai para o estado 4
                        } else {
                            i = textoDaLinha.length(); //para evitar loop infinito
                        }

                        if (i + 1 < textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                        }
                    }

                    guardarLinha = contarLinhasLidas;
                    if (aux) {
                        estado = 4;

                    } else {
                        estado = 7;
                    }
                    break;
                }

                // Estado 4
                case 4: {
                    if (indice == textoDaLinha.length()) {
                        break;
                    }
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    String sbstr = textoDaLinha.substring(indice, textoDaLinha.length());
                    if (Character.isDigit(lerChar.charAt(0))) {

                        if (sbstr.length() > 1) {
                            textoLexema += lerChar;
                            indice++;
                        }
                        estado = 5;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " Esperava-se um número após o ponto.");
                        estado = 0;
                    }

                    break;
                }
                // Estado 5
                case 5: {
                    if (indice == textoDaLinha.length()) {
                        break;
                    }
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    for (int i = indice; i < textoDaLinha.length(); i++) {
                        if (Character.isDigit(lerChar.charAt(0))) {
                            textoLexema += lerChar;
                            indice++;
                        } else {
                            i = textoDaLinha.length();
                        }

                        if (i + 1 < textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                        }
                    }
                    guardarLinha = contarLinhasLidas;
                    estado = 6;

                    break;
                }

                // Estado 6
                case 6: {

                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_NUMREAL);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 7
                case 7: {

                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_NUMINT);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;
                    }

                    break;
                }
                // Estado 8
                case 8: {
                    if (indice == textoDaLinha.length()) {
                        break;
                    }
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    int primeiroBarra = textoDaLinha.indexOf("/");
                    if (lerChar.charAt(0) == '/' && !textoLexema.isEmpty() && textoDaLinha.substring(primeiroBarra, textoDaLinha.length()).length() > 1) {//Comentario inline

                        estado = 9;
                    } else if (lerChar.charAt(0) == '*') {//Comentario multi line
                        textoLexema += lerChar;
                        indice++;
                        estado = 13;
                    } else if (lerChar.charAt(0) == '=') {//Operador /=
                        textoLexema += lerChar;
                        indice++;
                        estado = 64;
                    } else {//Operador /
                        textoLexema += lerChar;
                        //indice++;
                        estado = 66;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 9
                case 9: {
                    if (indice == textoDaLinha.length()) {
                        break;
                    }
                    lerChar = Character.toString(textoDaLinha.charAt(indice));

                    if (textoDaLinha.length() == 2 && lerChar.charAt(0) == '/') {//final do comentario

                        textoLexema += lerChar;
                        indice++;
                        estado = 12;
                    } else {//texto do comentario
                        textoLexema += lerChar;
                        indice++;
                        estado = 10;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 10
                case 10: {
                    if (indice == textoDaLinha.length()) {
                        break;
                    }
                    lerChar = Character.toString(textoDaLinha.charAt(indice));

                    for (int i = indice; i < textoDaLinha.length(); i++) {

                        textoLexema += lerChar;
                        indice++;

                        if (i + 1 < textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                        }
                    }
                    guardarLinha = contarLinhasLidas;
                    estado = 12;

                    break;
                }

                // Estado 12
                case 12: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_CM);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 13
                case 13: {

                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));

                    if (lerChar.charAt(0) == '*') {//final do comentario

                        textoLexema += lerChar;
                        indice++;
                        estado = 14;
                    } else {//texto do comentario
                        textoLexema += lerChar;
                        indice++;
                        estado = 16;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 14
                case 14: {

                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    int state = 14;
                    for (int i = indice; i < textoDaLinha.length(); i++) {
                        if (lerChar.charAt(0) == '*') {
                            textoLexema += lerChar;
                            indice++;

                        } else if (lerChar.charAt(0) == '/') {
                            textoLexema += lerChar;
                            indice++;
                            state = 15;
                            i = textoDaLinha.length();
                        } else {
                            textoLexema += lerChar;
                            indice++;
                            state = 16;
                            i = textoDaLinha.length();
                        }

                        if (i + 1 < textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                        }
                    }

                    estado = state;
                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 15
                case 15: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_CM);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 16
                case 16: {
                    boolean sair = true;
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    int i = indice;
                    char lerCharAnterior = 0;
                    int count = textoDaLinha.length();
                    while (i < count) {
                        textoLexema += lerChar;
                        indice++;
                        if (lerCharAnterior == '*' && lerChar.charAt(0) == '/') {
                            i = count;
                            sair = false;
                        }

                        if (i + 1 == count && contarLinhasLidas + 1 < ficheiroLido.size() && !(lerCharAnterior == '*' && lerChar.charAt(0) == '/')) {
                            indice = 0;
                            contarLinhasLidas++;
                            textoDaLinha = ficheiroLido.get(contarLinhasLidas);
                            count = textoDaLinha.length() + 1;
                            i = indice;
                            textoLexema += "\n\t\t\t\t\t\t\t";
                        }

                        if (i + 1 <= textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                            if (i == 0) {
                                lerCharAnterior = lerChar.charAt(0);
                            } else {
                                lerCharAnterior = Character.toString(textoDaLinha.charAt(indice - 1)).charAt(0);
                            }
                        }

                        i++;
                    }
                    if (!sair) {
                        guardarLinha = contarLinhasLidas;
                        estado = 15;
                    } else {
                        contarLinhasLidas++;
                        listaDeErros.add("Erro na linha: " + contarLinhasLidas + " faltou fechar comentário: " + textoLexema);
                        estado = 0;
                    }
                    break;

                }

                // Estado 17
                /*case 17: {

                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    int state = 17;
                    for (int i = indice; i < textoDaLinha.length(); i++) {
                        if (lerChar.charAt(0) == '*') {
                            textoLexema += lerChar;
                            indice++;

                            if (i + 1 < textoDaLinha.length()) {
                                lerChar = Character.toString(textoDaLinha.charAt(indice));
                            }

                        } else if (lerChar.charAt(0) == '/') {
                            textoLexema += lerChar;
                            indice++;
                            state = 15;
                            i = textoDaLinha.length();
                        }
                    }

                    estado = state;
                    guardarLinha = contarLinhasLidas;

                    break;
                }*/
                // Estado 18
                case 18: {
                    boolean erro = true;
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    for (int i = indice; i < textoDaLinha.length(); i++) {

                        textoLexema += lerChar;
                        indice++;
                        if (lerChar.charAt(0) == '"') {
                            i = textoDaLinha.length();
                            erro = false;
                        }
                        if (i + 1 < textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                        }
                    }
                    if (erro) {
                        contarLinhasLidas++;
                        listaDeErros.add("Erro na linha: " + contarLinhasLidas + " string inválida: " + textoLexema);
                        estado = 0;
                    } else {
                        guardarLinha = contarLinhasLidas;
                        estado = 19;
                    }
                    break;
                }

                // Estado 19
                case 19: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_S);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 20
                case 20: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '\'' && textoDaLinha.length() == 2) {
                        textoLexema += lerChar;
                        indice++;
                        estado = 22;
                    } else if (textoDaLinha.length() > indice && (textoDaLinha.charAt(indice) == '\\'
                            || textoDaLinha.substring(indice).startsWith("\\\"")
                            || textoDaLinha.substring(indice).startsWith("\\'")
                            || textoDaLinha.substring(indice).startsWith("\\n")
                            || textoDaLinha.substring(indice).startsWith("\\r")
                            || textoDaLinha.substring(indice).startsWith("\\t")
                            || textoDaLinha.substring(indice).startsWith("\\b")
                            || textoDaLinha.substring(indice).startsWith("\\f"))) {

                        textoLexema = textoDaLinha.substring(indice + 1, indice + 3);
                        indice += textoLexema.length();
                        estado = 24;

                    } else if (textoDaLinha.length() > 1) {
                        textoLexema += lerChar;
                        indice++;
                        estado = 21;
                    } else if (textoDaLinha.trim().equals("'")) {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " char inválido " + textoLexema);
                        estado = 0;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }
                // Estado 22
                case 22: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_C);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 21
                case 21: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));

                    if (lerChar.charAt(0) == '\'') {
                        textoLexema += lerChar;
                        indice++;
                        estado = 23;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " char inválido " + textoLexema);
                        estado = 0;
                    }

                    guardarLinha = contarLinhasLidas;
                    break;
                }
                // Estado 22
                case 23: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_C);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 24
                case 24: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));

                    if (lerChar.charAt(0) == '\'') {
                        textoLexema += lerChar;
                        indice++;
                        estado = 23;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " char inválido " + textoLexema);
                        estado = 0;
                    }

                    guardarLinha = contarLinhasLidas;
                    break;
                }

                // Estado 27
                case 27: {
                    if (indice >= textoDaLinha.length()) {
                        break;
                    }

                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '+') {//Operador /=
                        textoLexema += lerChar;
                        indice++;
                        estado = 30;
                    } else if (lerChar.charAt(0) == '=') {//Operador /
                        textoLexema += lerChar;
                        indice++;
                        estado = 30;
                    } else {
                        estado = 30;
                    }
                    guardarLinha = contarLinhasLidas;

                    break;
                }
                // Estado 28
                case 28: {
                    if (indice >= textoDaLinha.length()) {
                        break;
                    }

                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '-') {//Operador /=
                        textoLexema += lerChar;
                        indice++;
                        estado = 30;
                    } else if (lerChar.charAt(0) == '=') {//Operador /
                        textoLexema += lerChar;
                        indice++;
                        estado = 30;
                    } else {
                        estado = 30;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }
                // Estado 29
                case 29: {
                    if (indice >= textoDaLinha.length()) {
                        break;
                    }

                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '=') {//Operador /=
                        textoLexema += lerChar;
                        indice++;
                        estado = 30;
                    } else {
                        indice++;
                        estado = 30;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 30
                case 30: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_OPA);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 31
                case 31: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_OPATB);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 32
                case 32: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '=') {//Operador ==
                        textoLexema += lerChar;
                        indice++;
                        estado = 33;
                    } else if (textoLexema.equals("=") && lerChar.charAt(0) != '=') {
                        //indice++;
                        estado = 31;//Atribuição
                    } else {
                        estado = 33;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 33
                case 33: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_OPR);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 34
                case 34: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '&') {//Operador /=
                        textoLexema += lerChar;
                        indice++;
                        estado = 36;
                    } else {
                        estado = 36;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 35
                case 35: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '|') {//Operador /=
                        textoLexema += lerChar;
                        indice++;
                        estado = 36;
                    } else {
                        estado = 36;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 36
                case 36: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_OPL);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 37
                case 37: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (textoDaLinha.substring(indice, textoDaLinha.length()).startsWith("include")) {
                        textoLexema += "include";
                        indice += 7;
                        lerChar = Character.toString(textoDaLinha.charAt(indice));
                        if (Character.isWhitespace(lerChar.charAt(0))) {
                            textoLexema += lerChar;
                            indice++;
                        }

                        estado = 41;
                    } else if (textoDaLinha.substring(indice, textoDaLinha.length()).startsWith("endif")) {
                        textoLexema += "endif";
                        indice += 5;
                        estado = 38;
                    } else if (textoDaLinha.substring(indice, textoDaLinha.length()).startsWith("undef")) {
                        textoLexema += "undef";
                        indice += 5;
                        estado = 39;
                    } else if (textoDaLinha.substring(indice, textoDaLinha.length()).startsWith("define")) {
                        textoLexema += "define";
                        indice += 6;
                        estado = 39;
                    } else if (textoDaLinha.substring(indice, textoDaLinha.length()).startsWith("ifdef")) {
                        textoLexema += "ifdef";
                        indice += 5;
                        estado = 39;
                    } else if (textoDaLinha.substring(indice, textoDaLinha.length()).startsWith("ifndef")) {
                        textoLexema += "ifndef";
                        indice += 6;
                        estado = 39;
                    } else {
                        indice++;
                        estado = 36;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 38
                case 38: {

                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_DIR);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 39
                case 39: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (Character.isLetter(lerChar.charAt(0)) || Character.isWhitespace(lerChar.charAt(0))) {
                        textoLexema += lerChar;
                        indice++;
                        estado = 40;
                        guardarLinha = contarLinhasLidas;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " diretiva está incompleta " + textoLexema);
                        estado = 0;
                    }
                    break;
                }

                // Estado 40
                case 40: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    for (int i = indice; i < textoDaLinha.length(); i++) {

                        textoLexema += lerChar;
                        indice++;

                        if (i + 1 < textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                        }
                    }

                    guardarLinha = contarLinhasLidas;
                    estado = 38;

                    break;
                }

                // Estado 41
                case 41: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '<') {//Operador /=
                        textoLexema += lerChar;
                        indice++;
                        estado = 42;
                    } else if (lerChar.charAt(0) == '"') {
                        indice++;
                        estado = 44;
                    } else {
                        listaDeErros.add("Erro na linha: " + contarLinhasLidas + " diretiva include está incompleta " + textoLexema);
                        estado = 0;
                    }
                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 42
                case 42: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (Character.isLetter(lerChar.charAt(0))) {
                        textoLexema += lerChar;
                        indice++;
                        estado = 43;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " diretiva include está incompleta " + textoLexema);
                        estado = 0;
                    }
                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 43
                case 43: {
                    boolean erro = true;
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    for (int i = indice; i < textoDaLinha.length() && erro; i++) {
                        if (Character.isLetter(lerChar.charAt(0)) || lerChar.charAt(0) == '_'
                                || Character.isDigit(lerChar.charAt(0))) {
                            textoLexema += lerChar;
                            indice++;
                        } else if (lerChar.charAt(0) == '.') {
                            erro = false;
                            textoLexema += lerChar;
                            indice++;

                        }
                        if (i + 1 < textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                        }
                    }

                    if (!erro) {
                        guardarLinha = contarLinhasLidas;
                        estado = 83;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " diretiva include está incompleta falta o simbolo ." + textoLexema);
                        estado = 0;
                    }

                    break;
                }

                // Estado 44
                case 44: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (Character.isLetter(lerChar.charAt(0))) {
                        textoLexema += lerChar;
                        indice++;
                        estado = 45;
                    } else {
                        listaDeErros.add("Erro na linha: " + contarLinhasLidas + " diretiva include está incompleta " + textoLexema);
                        estado = 0;
                    }
                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 45
                case 45: {
                    boolean erro = true;
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    for (int i = indice; i < textoDaLinha.length() && erro; i++) {
                        if (Character.isLetter(lerChar.charAt(0)) || lerChar.charAt(0) == '_'
                                || Character.isDigit(lerChar.charAt(0))) {
                            textoLexema += lerChar;
                            indice++;
                        } else if (lerChar.charAt(0) == '.') {
                            erro = false;
                            textoLexema += lerChar;
                            indice++;

                        }
                        if (i + 1 < textoDaLinha.length()) {
                            lerChar = Character.toString(textoDaLinha.charAt(indice));
                        }
                    }

                    if (!erro) {
                        guardarLinha = contarLinhasLidas;
                        estado = 81;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " diretiva include está incompleta falta o simbolo ." + textoLexema);
                        estado = 0;
                    }

                    break;
                }

                // Estado 47
                case 47: {

                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_AP);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 48
                case 48: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_FP);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 49
                case 49: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_APR);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 83
                case 83: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == 'c' || lerChar.charAt(0) == 'h') {
                        textoLexema += lerChar;
                        indice++;
                        estado = 80;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " diretiva include está incompleta " + textoLexema);
                        estado = 0;
                    }
                    guardarLinha = contarLinhasLidas;

                    break;
                }
                // Estado 83
                case 80: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '>') {
                        textoLexema += lerChar;
                        indice++;
                        estado = 38;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " diretiva include está incompleta " + textoLexema);
                        estado = 0;
                    }
                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 50
                case 50: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_FPR);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 81
                case 81: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == 'c' || lerChar.charAt(0) == 'h') {
                        textoLexema += lerChar;
                        indice++;
                        estado = 82;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " diretiva include está incompleta " + textoLexema);
                        estado = 0;
                    }
                    guardarLinha = contarLinhasLidas;

                    break;
                }
                // Estado 82
                case 82: {
                    if(indice == textoDaLinha.length())
                        break;
                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '"') {
                        textoLexema += lerChar;
                        indice++;
                        estado = 38;
                    } else {
                        listaDeErros.add("Erro na linha: " + (contarLinhasLidas + 1) + " diretiva include está incompleta " + textoLexema);
                        estado = 0;
                    }
                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 51
                case 51: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_AC);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 52
                case 52: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_FC);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 53
                case 53: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_PVIRG);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 54
                case 54: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_VIRG);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 55
                case 55: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_PINTER);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }
                // Estado 56
                case 56: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_DPTO);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 57
                case 57: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_PTO);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 58
                case 58: {
                    if (indice >= textoDaLinha.length()) {
                        break;
                    }

                    lerChar = Character.toString(textoDaLinha.charAt(indice));
                    if (lerChar.charAt(0) == '=') {//Operador /=
                        textoLexema += lerChar;
                        indice++;
                        estado = 70;
                    } else if (lerChar.charAt(0) != '*') {//Operador /

                        indice++;
                        estado = 70;
                    } else {
                        estado = 0;
                    }

                    guardarLinha = contarLinhasLidas;

                    break;
                }

                // Estado 70
                case 70: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema);
                        lex.setToken(Token.TK_OPA);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 64
                case 64: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(textoLexema.substring(0, 2));
                        lex.setToken(Token.TK_OPA);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

                // Estado 66
                case 66: {
                    if (end == true) {
                        estado = 0;
                        end = false;
                    } else {
                        lex = new TipoLex();
                        lex.setLexema(Character.toString(textoLexema.charAt(0)));
                        lex.setToken(Token.TK_OPA);
                        lex.setLinha(guardarLinha + 1);
                        result.add(lex);
                        textoLexema = "";
                        end = true;

                    }

                    break;
                }

            }
            guardarLinha = contarLinhasLidas;
            if (indice == textoDaLinha.length()) {
                //só zera se a próxima linha existir
                if (contarLinhasLidas + 1 < ficheiroLido.size()) {
                    indice = 0;
                }
                contarLinhasLidas++;

            }

        } while (contarLinhasLidas <= ficheiroLido.size());

        lex = new TipoLex();
        lex.setToken(Token.TK_FIM);
        lex.setLexema("");
        lex.setLinha(++guardarLinha);
        result.add(lex);
        if (ficheiroLido.size() != 0) {
            result.get(0).setListaDeErros(listaDeErros);
        }

        return result;
    }

}

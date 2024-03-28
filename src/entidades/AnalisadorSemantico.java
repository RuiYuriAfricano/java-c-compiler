package entidades;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author Rui Malemba
 */
public class AnalisadorSemantico {

    //Atributos
    private AnalisadorSintatico parser;
    private ArrayList<TipoLex> listaTokens, listaTokensCopia;
    private TipoLex t, tokenAnterior, tokenDoubleAnterior, tokenSeguinte, tokenDoubleSeguinte;
    private int qtdErros = 0;
    private ArrayList<String> listaDeErrosSemantico = new ArrayList<>();
    private ArrayList<String> listaDeVariaveis = new ArrayList<>();
    private int qtdTokens = 0;

    //Construtor
    public AnalisadorSemantico(String path) {
        parser = new AnalisadorSintatico(path);

        listaTokens = parser.program();
        listaTokensCopia = new ArrayList<>(listaTokens);
    }

    //Getters e setters
    public int getQtdTokens() {
        return qtdTokens;
    }

    public void setQtdTokens(int qtdTokens) {
        if (qtdTokens < listaTokens.size()) {
            this.qtdTokens = qtdTokens;
        }
    }

    public ArrayList<TipoLex> program() {

        //Se não tiver tokens nesta tabela então retorna a tabela com o token default apenas, token do índice 0, que guarda a lista de erros
        if (listaTokens.size() <= 1) {
            return listaTokens;
        }
        
        //remove os comentários e directivas da lista de tokens
        removeComentsDiret();
        //*****************************************************************************************************************************
        //Chamar todos métodos do semanricos de verificação aqui
        //*****************************************************************************************************************************
        //Método que verifica se uma variável usada foi declarada
        verificaSeUmaVariavelFoiDeclarada();

        //Método que verifica a compatibilidade de tipos
        verificaCompatibilidadeDeTipos();

        //Método que se uma variável foi declarada duas vezes ou mais no mesmo escopo
        verificaSeUmaVariavelFoiDeclaradaMaisDeDuasVezes();

        //Método que verifica expressões booleanas
        verificaExpressoesBooleanas();

        //Método que verifica a compatibilidade de tipos nos parametros das funções em relação aos argumentos
        verificaCompatibilidadeDeTiposNosParametros();

        //Método que valida scanf %d e %f
        verificaExpressoesDeEntradaScanf();

        //Método que valida printf %d e %f
        verificaExpressoesDeSaidaPrintf();

        //Verifica se o identificador main foi bem declarado
        verificaOIdentificadorMain();

        //juntar os comentários e directivas a lista de tokens original
        mergeComentsDiret();
        
        ArrayList<String> totalErros = juntarArrayLis(listaDeErrosSemantico);
        listaTokens.get(0).setListaDeErros(totalErros);//colocar sempre na posição 0, minha lógica
        return listaTokens;
    }

    //Método para juntar erros léxicos, sintáticos e semânticos
    private ArrayList<String> juntarArrayLis(ArrayList<String> errosSemantico) {
        ArrayList<String> resultado = new ArrayList<>();
        for (int i = 0; i < listaTokens.get(0).getListaDeErros().size(); i++) {
            resultado.add(listaTokens.get(0).getListaDeErros().get(i));
        }
        for (int i = 0; i < errosSemantico.size(); i++) {
            resultado.add(errosSemantico.get(i));
        }

        return resultado;
    }

    private boolean pesquisar(ArrayList<TipoLex> arr, TipoLex t) {
        for (TipoLex obj : arr) {
            if ((obj.getLexema().equals(t.getLexema())
                    || (obj.getLexema().equals(t.getVarAtrib()))) && (obj.getEscopo() <= t.getEscopo()) && (obj.getFuncao().equals(t.getFuncao()))) {
                return true; // Encontrou um objeto que atende aos critérios de igualdade
            }
        }
        return false; // Não encontrou nenhum objeto que atenda aos critérios de igualdade
    }

    //Método que verifica se uma variável usada foi declarada
    private void verificaSeUmaVariavelFoiDeclarada() {
        String tipo = "", funcName = "";
        int contFunc = 0, guardaContFunc = 0;
        ArrayList<TipoLex> declarados = new ArrayList<>();
        ArrayList<TipoLex> funcoesDeclaradas = new ArrayList<>();
        for (int j = 0; j < listaTokens.size(); j++) {
            t = analex();
            guardaContFunc = contFunc;
            if ((t.getToken() == Token.TK_ID) && (tokenAnterior.getToken() == Token.TK_INT || tokenAnterior.getToken() == Token.TK_FLOAT || tokenAnterior.getToken() == Token.TK_DOUBLE
                    || tokenAnterior.getToken() == Token.TK_VOID || tokenAnterior.getToken() == Token.TK_CHAR) && (tokenSeguinte.getToken() == Token.TK_AP)) {
                contFunc++;
                funcName = t.getLexema() + "()";
            }

            if (tokenSeguinte.getToken() != Token.TK_AP && t.getToken() != Token.TK_FIM) {
                t.setFuncao(funcName);
            }

            if (t.getToken() == Token.TK_ID && tokenSeguinte.getToken() != Token.TK_AP && !pesquisar(declarados, t)) {

                if (t.getTipo().equals("")) {
                    listaDeErrosSemantico.add("Erro: variável " + t.getLexema() + " na linha " + t.getLinha() + ", não foi declarada." + "\n");
                } else {
                    declarados.add(t);
                }
            } else if (t.getToken() == Token.TK_ID && tokenSeguinte.getToken() == Token.TK_AP && !pesquisar(declarados, t)) {
                funcoesDeclaradas.add(t);
            }
        }

        setQtdTokens(0);
        //Atribuir tipos a variaveis usadas
        for (int i = 0; i < listaTokens.size(); i++) {
            t = analex();

            //procura ele na lista dos declarados
            if (pesquisar(declarados, t) || pesquisar(funcoesDeclaradas, t)) {
                //se existe, agora procura todas cópias dela utilizada no código, não na declaração

                TipoLex tt;
                for (int j = 0; j < listaTokens.size(); j++) {
                    tt = listaTokens.get(j);
                    if (t.getLexema().equals(tt.getLexema()) && t.getEscopo() <= tt.getEscopo()) {
                        if (j > 0) {
                            if (!((listaTokens.get(j - 1).getToken() == Token.TK_INT
                                    || listaTokens.get(j - 1).getToken() == Token.TK_CHAR || listaTokens.get(j - 1).getToken() == Token.TK_DOUBLE
                                    || listaTokens.get(j - 1).getToken() == Token.TK_FLOAT) && listaTokens.get(j).getTipo().isEmpty())) {
                                tt.setTipo(t.getTipo());
                            }
                            {
                                tt.setTipo(t.getTipo());
                            }
                        } else {
                            tt.setTipo(t.getTipo());
                        }

                    }
                }

            }

        }

        setQtdTokens(0);
        //atribuir tipos aos valores de atribuição que são identificadores e não são literal
        for (int i = 0; i < listaTokens.size(); i++) {
            t = analex();

            //procura o valor de atribuição na lista dos declarados
            if (declarados.contains(t)) {

                TipoLex tt;
                for (int j = 0; j < listaTokens.size(); j++) {
                    tt = listaTokens.get(j);

                    if (t.getVarAtrib().equals(tt.getLexema()) && t.getEscopo() <= tt.getEscopo() && tt.getToken() == Token.TK_ID) {
                        t.setTipoVarAtrib(tt.getTipo());
                    }
                }

            }
        }
        setQtdTokens(0);

    }

    //Método que verifica a compatibilidade de tipos
    private void verificaCompatibilidadeDeTipos() {

        for (int j = 0; j < listaTokens.size(); j++) {
            t = analex();
            if (t.getToken() == Token.TK_ID && tokenSeguinte.getToken() != Token.TK_AP) {

                if (!t.getTipo().isEmpty() && !t.getTipoVarAtrib().isEmpty()) {
                    if (!t.getTipo().equals(t.getTipoVarAtrib()) && !(t.getTipo().equals("float") || t.getTipo().equals("double") && t.getTipoVarAtrib().equals("real") || t.getTipoVarAtrib().equals("int"))) {
                        listaDeErrosSemantico.add("Erro: variável " + t.getLexema() + " na linha " + t.getLinha() + ", tem incopatibilidade na atribuição." + "\n");
                    }
                }
            }

        }
        setQtdTokens(0);
    }

    //Método que se uma variável foi declarada duas vezes ou mais no mesmo escopo
    private void verificaSeUmaVariavelFoiDeclaradaMaisDeDuasVezes() {
        ArrayList<TipoLex> copiaTokens = new ArrayList<>();

        for (int i = 0; i < listaTokens.size(); i++) {
            boolean aux = false;
            for (TipoLex obj : copiaTokens) {
                if ((obj.getLexema().equals(listaTokens.get(i)))) {
                    aux = true;
                }
            }
            if (listaTokens.get(i).getToken() == Token.TK_ID && !aux) {
                copiaTokens.add(listaTokens.get(i));
            }
        }
        for (int i = 0; i < copiaTokens.size(); i++) {
            int countVariaveis = 0;
            int countFuncoes = 0;
            boolean temTipo = false;
            for (int j = 0; j < listaTokens.size(); j++) {
                t = analex();

                if ((t.getToken() == Token.TK_INT
                        || t.getToken() == Token.TK_CHAR || t.getToken() == Token.TK_DOUBLE || t.getToken() == Token.TK_FLOAT) && tokenDoubleSeguinte.getToken() == Token.TK_VIRG) {
                    temTipo = true;
                } else if (t.getToken() == Token.TK_PVIRG) {
                    temTipo = false;
                }

                if (copiaTokens.get(i).getToken() == Token.TK_ID && t.getToken() == Token.TK_ID) {
                    if (copiaTokens.get(i).getLexema().equals(t.getLexema()) && (tokenAnterior.getToken() == Token.TK_INT
                            || tokenAnterior.getToken() == Token.TK_CHAR || tokenAnterior.getToken() == Token.TK_DOUBLE || tokenAnterior.getToken() == Token.TK_FLOAT
                            || (temTipo))
                            && (tokenSeguinte.getToken() == Token.TK_VIRG || tokenSeguinte.getToken() == Token.TK_PVIRG || tokenSeguinte.getLexema().equals("="))) {
                        if (copiaTokens.get(i).getEscopo() == t.getEscopo()) {
                            countVariaveis++;
                        }
                    } else if (copiaTokens.get(i).getLexema().equals(t.getLexema()) && (tokenAnterior.getToken() == Token.TK_INT
                            || tokenAnterior.getToken() == Token.TK_CHAR || tokenAnterior.getToken() == Token.TK_VOID || tokenAnterior.getToken() == Token.TK_DOUBLE || tokenAnterior.getToken() == Token.TK_FLOAT)
                            && (tokenSeguinte.getToken() == Token.TK_AP)) {
                        countFuncoes++;

                    }
                }

            }
            if (countVariaveis > 1) {
                listaDeErrosSemantico.add("Erro: variável " + copiaTokens.get(i).getLexema() + " na linha " + copiaTokens.get(i).getLinha() + ", foi declarada mais de uma vez no escopo." + "\n");
            }

            if (countFuncoes > 1) {
                listaDeErrosSemantico.add("Erro: função " + copiaTokens.get(i).getLexema() + " na linha " + copiaTokens.get(i).getLinha() + ", foi declarada mais de uma vez no escopo." + "\n");
            }
            setQtdTokens(0);
        }
    }

    //Método que verifica expressões booleanas
    private void verificaExpressoesBooleanas() {

        //Validar expressões booleanas
        for (int i = 0; i < listaTokens.size(); i++) {
            t = analex();
            if (t.getLexema().equals("if") || t.getLexema().equals("for") || t.getLexema().equals("while")) {
                if (tokenDoubleSeguinte.getToken() == Token.TK_FP) {
                    listaDeErrosSemantico.add("Erro: Expressão booleana inválida "
                            + " na linha " + t.getLinha() + ". É importante colocar alguma expressão dentro de parentes");
                }
            }
            if (t.getLexema().equals("do") && (tokenSeguinte.getLexema().equals("while") || (tokenSeguinte.getLexema().equals("{") && tokenDoubleSeguinte.getLexema().equals("}")))) {
                listaDeErrosSemantico.add("Erro: Expressão booleana inválida "
                        + " na linha " + t.getLinha() + ". É importante colocar alguma expressão que sirva de condição de paragem.");
            }
        }
        setQtdTokens(0);
        //Validar operadores aritmeticos e Validar operadores relacionais
        for (int i = 0; i < listaTokens.size(); i++) {
            t = analex();
            boolean res = false;
            if (t.getLexema().equals("++") || t.getLexema().equals("--")) {
                if (tokenAnterior.getToken() != Token.TK_NUMINT && tokenAnterior.getToken() != Token.TK_C && !tokenAnterior.getTipo().equals("int") && !tokenAnterior.getTipo().equals("char")
                        && tokenAnterior.getToken() != Token.TK_NUMREAL && !tokenAnterior.getTipo().equals("int") && !tokenAnterior.getTipo().equals("float") && !tokenAnterior.getTipo().equals("double")) {
                    listaDeErrosSemantico.add("Erro: Operação com o valor " + tokenAnterior.getLexema() + " na linha " + t.getLinha() + ", é inválida, por ter um tipo incompatível." + "\n");

                }
            } else if (t.getToken() == Token.TK_OPA || t.getToken() == Token.TK_OPR || (t.getToken() == Token.TK_OPATB && !t.getLexema().equals("="))) {
                if ((tokenAnterior.getToken() == Token.TK_NUMINT || tokenAnterior.getToken() == Token.TK_C || tokenAnterior.getTipo().equals("int") || tokenAnterior.getTipo().equals("char"))
                        && (tokenSeguinte.getToken() == Token.TK_NUMINT || tokenSeguinte.getToken() == Token.TK_C || tokenSeguinte.getTipo().equals("int") || tokenSeguinte.getTipo().equals("char"))) {
                    res = true;
                } else if ((tokenAnterior.getToken() == Token.TK_NUMINT || tokenAnterior.getToken() == Token.TK_NUMREAL || tokenAnterior.getTipo().equals("int") || tokenAnterior.getTipo().equals("float") || tokenAnterior.getTipo().equals("double"))
                        && (tokenSeguinte.getToken() == Token.TK_NUMINT || tokenSeguinte.getToken() == Token.TK_NUMREAL || tokenSeguinte.getTipo().equals("int") || tokenSeguinte.getTipo().equals("float") || tokenSeguinte.getTipo().equals("double"))) {
                    res = true;
                } else if (!res) {
                    listaDeErrosSemantico.add("Erro: Operação entre " + tokenAnterior.getLexema() + " e " + tokenSeguinte.getLexema() + " na linha " + t.getLinha() + ", é inválida, são de tipos incompatíveis." + "\n");
                }
            }
        }
        setQtdTokens(0);
    }

    //Método que verifica a compatibilidade de tipos nos parametros das funções em relação aos argumentos
    private void verificaCompatibilidadeDeTiposNosParametros() {
        for (int i = 0; i < listaTokens.size(); i++) {
            t = analex();
            int contarParametros = 0, qtdParametros = 0, qtdArgs = 0;
            if (t.getToken() == Token.TK_ID && (tokenAnterior.getToken() == Token.TK_INT
                    || tokenAnterior.getToken() == Token.TK_CHAR || tokenAnterior.getToken() == Token.TK_DOUBLE || tokenAnterior.getToken() == Token.TK_FLOAT
                    || tokenAnterior.getToken() == Token.TK_VOID) && tokenSeguinte.getToken() == Token.TK_AP) {

                //Contar os parametros
                TipoLex r = analex();
                for (int j = i; r.getToken() != Token.TK_FP; j++) {
                    if ((r.getToken() == Token.TK_INT
                            || r.getToken() == Token.TK_CHAR || r.getToken() == Token.TK_DOUBLE || r.getToken() == Token.TK_FLOAT) && (tokenSeguinte.getToken() == Token.TK_ID)) {
                        qtdParametros++;
                    }
                    r = analex();
                }
                setQtdTokens(0);
                boolean res = false;
                int guardai = i;
                for (int j = 0; j < listaTokens.size(); j++) {
                    TipoLex tt = analex();

                    if (tt.getToken() == Token.TK_ID && tt.getLexema().equals(t.getLexema()) && !(tokenAnterior.getToken() == Token.TK_INT
                            || tokenAnterior.getToken() == Token.TK_CHAR || tokenAnterior.getToken() == Token.TK_DOUBLE || tokenAnterior.getToken() == Token.TK_FLOAT
                            || tokenAnterior.getToken() == Token.TK_VOID) && tokenSeguinte.getToken() == Token.TK_AP) {
                        res = true;
                        setQtdTokens(j);

                        //Contar os argumentos
                        r = analex();
                        for (int k = j; r.getToken() != Token.TK_FP; k++) {
                            if ((r.getToken() == Token.TK_ID || r.getToken() == Token.TK_NUMINT || r.getToken() == Token.TK_NUMREAL || r.getToken() == Token.TK_C)
                                    && (tokenSeguinte.getToken() == Token.TK_VIRG || tokenSeguinte.getToken() == Token.TK_FP)) {
                                qtdArgs++;

                            }
                            r = analex();
                        }
                    }
                    if (res) {

                        int incrementoJ = j;
                        if (i == guardai) {
                            i += 2;
                        } else {
                            i += 3;
                        }

                        setQtdTokens(i);

                        if (this.qtdTokens < listaTokens.size()) {
                            t = analex();
                            if(tokenAnterior.getToken() == Token.TK_CM || tokenAnterior.getToken() == Token.TK_DIR)
                                i++;
                        }

                        j += 2;
                        setQtdTokens(j);

                        if (this.qtdTokens < listaTokens.size()) {
                            tt = analex();
                            if(tokenAnterior.getToken() == Token.TK_CM || tokenAnterior.getToken() == Token.TK_DIR)
                                j++;
                            incrementoJ = j-incrementoJ;
                        }
                        
                        if (t.getLexema().equals(tt.getTipo()) || (t.getLexema().equals("int") && tt.getTipo().equals("char"))
                                || (t.getLexema().equals("int") && tt.getToken() == Token.TK_C)
                                || (t.getLexema().equals("float") && tt.getToken() == Token.TK_NUMINT)
                                || (t.getLexema().equals("double") && tt.getToken() == Token.TK_NUMINT)
                                || (t.getLexema().equals("float") && tt.getTipo().equals("int"))
                                || (t.getLexema().equals("double") && tt.getTipo().equals("int"))
                                || (t.getLexema().equals("int") && tt.getToken() == Token.TK_NUMINT)
                                || (t.getLexema().equals("double") && tt.getToken() == Token.TK_NUMREAL)
                                || (t.getLexema().equals("float") && tt.getToken() == Token.TK_NUMREAL)
                                || (t.getLexema().equals("char") && tt.getToken() == Token.TK_C)
                                || (t.getLexema().equals("char") && tt.getToken() == Token.TK_NUMINT)
                                || (t.getLexema().equals("char") && tt.getTipo().equals("int"))
                                && (t.getToken() == Token.TK_INT
                                || t.getToken() == Token.TK_CHAR || t.getToken() == Token.TK_DOUBLE || t.getToken() == Token.TK_FLOAT)) {

                            contarParametros++;
                        }
                        if (tokenAnterior.getToken() == Token.TK_FP) {
                            res = false;
                            i = guardai;
                            setQtdTokens(j);

                            if (qtdParametros > 0) {
                                if (contarParametros != qtdParametros) {
                                    listaDeErrosSemantico.add("Erro: Há incompatibilidade nos paramêtros que a função esperava receber na linha " + tt.getLinha() + ", "
                                            + "verifique a quantidade de argumentos que você passou na função e a assinatura da função em si." + "\n");
                                } else if (qtdArgs != qtdParametros) {
                                    listaDeErrosSemantico.add("Erro: Há incompatibilidade nos paramêtros que a função esperava receber na linha " + tt.getLinha() + ", "
                                            + "verifique os argumentos que você passou na função e a assinatura da função em si." + "\n");
                                }
                            }
                            contarParametros = 0;
                            qtdArgs = 0;
                        }

                        j-= incrementoJ-1;

                    }

                }

                i = guardai;
                setQtdTokens(i + 1);
            }

        }
        setQtdTokens(0);
    }

    //Método que valida scanf %d e %f
    private void verificaExpressoesDeEntradaScanf() {
        //Tratar o scanf
        for (int i = 0; i < listaTokens.size(); i++) {
            t = analex();
            if (t.getLexema().equals("scanf")) {

                //Pega os formatos
                String str = tokenDoubleSeguinte.getLexema().trim();
                for (int j = 0; j < str.length() - 1; j++) {
                    if (str.charAt(j) == '%') {
                        if (Character.isLetter(str.charAt(j + 1))) {
                            int s = 1;
                            int k = j + 2;
                            while (k < str.length() && Character.isLetter(str.charAt(k))) {
                                s++;
                                k++;
                            }
                            if (s > 1) {
                                listaDeErrosSemantico.add("Erro: Na linha " + t.getLinha() + ", "
                                        + "há erro de expressão era esperado apenas uma letra após '%'." + "\n");
                            }
                        }
                    }
                }

                String[] arr = str.split("[^a-zA-Z]+");
                ArrayList<String> tiposDeFormatos = new ArrayList<>();
                for (String letra : arr) {
                    if (!letra.isEmpty()) {
                        tiposDeFormatos.add(letra);
                    }
                }

                ArrayList<String> identificadores = new ArrayList<>();

                //Pega os identificadores
                TipoLex r = analex();
                for (int j = i; r.getToken() != Token.TK_FP; j++) {
                    if (r.getToken() == Token.TK_ID && (tokenSeguinte.getToken() == Token.TK_VIRG || tokenSeguinte.getToken() == Token.TK_FP)) {
                        identificadores.add(r.getTipo());
                    }
                    r = analex();
                }
                if (tiposDeFormatos.size() > 0 && identificadores.size() > 0 && tiposDeFormatos.size() == identificadores.size()) {
                    for (int j = 0; j < identificadores.size(); j++) {
                        if ((identificadores.get(j).equals("int") && !tiposDeFormatos.get(j).equals("d"))
                                || ((identificadores.get(j).equals("float") || identificadores.get(j).equals("double")) && !tiposDeFormatos.get(j).equals("f"))) {
                            listaDeErrosSemantico.add("Erro: Na linha " + t.getLinha() + ", "
                                    + "o tipo de dados e o especificador de formato não são incompatíveis." + "\n");
                        }
                    }
                } else {
                    listaDeErrosSemantico.add("Erro: Na linha " + t.getLinha() + ", "
                            + "o tipo de dados do(s) identificador(es) e o especificador de formato devem existir, na mesma quantidade." + "\n");
                }
                setQtdTokens(i + 1);
            }
        }

        setQtdTokens(0);
    }

    //Método que valida printf e scanf %d e %f
    private void verificaExpressoesDeSaidaPrintf() {
        //Tratar o printf
        for (int i = 0; i < listaTokens.size(); i++) {
            t = analex();
            if (t.getLexema().equals("printf")) {

                //Pega os formatos
                String str = tokenDoubleSeguinte.getLexema();

                ArrayList<String> tiposDeFormatos = new ArrayList<>();

                for (int j = 0; j < str.length(); j++) {
                    if (str.charAt(j) == '%') {

                        if ((j + 1) < str.length()) {

                            if (Character.isLetter(str.charAt(j + 1))) {
                                tiposDeFormatos.add(Character.toString(str.charAt(j + 1)));
                            } else {
                                listaDeErrosSemantico.add("Erro: Na linha " + t.getLinha() + ", "
                                        + "há erro de expressão era esperado apenas uma letra após '%'." + "\n");
                            }
                        } else {
                            listaDeErrosSemantico.add("Erro: Na linha " + t.getLinha() + ", "
                                    + "há erro de expressão era esperado apenas uma letra após '%'." + "\n");
                        }
                    }
                }

                ArrayList<String> identificadores = new ArrayList<>();

                //Pega os identificadores
                TipoLex r = analex();
                for (int j = i; r.getToken() != Token.TK_FP; j++) {
                    if (r.getToken() == Token.TK_ID && (tokenSeguinte.getToken() == Token.TK_VIRG || tokenSeguinte.getToken() == Token.TK_FP)) {
                        identificadores.add(r.getTipo());
                    }
                    r = analex();
                }

                if (tiposDeFormatos.size() > 0 && identificadores.size() > 0 && tiposDeFormatos.size() == identificadores.size()) {
                    for (int j = 0; j < identificadores.size(); j++) {
                        if ((identificadores.get(j).equals("int") && !tiposDeFormatos.get(j).equals("d"))
                                || ((identificadores.get(j).equals("float") || identificadores.get(j).equals("double")) && !tiposDeFormatos.get(j).equals("f"))) {
                            listaDeErrosSemantico.add("Erro: Na linha " + t.getLinha() + ", "
                                    + "o tipo de dados e o especificador de formato não são incompatíveis." + "\n");
                        }
                    }
                } else if(tiposDeFormatos.size() > 0 || identificadores.size() > 0) {
                    listaDeErrosSemantico.add("Erro: Na linha " + t.getLinha() + ", "
                            + "o tipo de dados do(s) identificador(es) e o especificador de formato devem existir, na mesma quantidade." + "\n");
                }

                setQtdTokens(i + 1);
            }
        }
        setQtdTokens(0);
    }

    //Verifica se o identificador main foi bem declarado
    private void verificaOIdentificadorMain() {
        //Tratar o main
        boolean aux = false;
        int a = 0;
        for (int i = 0; i < listaTokens.size(); i++) {
            t = analex();

            if (t.getLexema().equals("main")) {
                if (tokenAnterior.getToken() != Token.TK_INT) {
                    listaDeErrosSemantico.add("Erro: Na linha " + t.getLinha() + ", "
                            + "o tipo de dados da função main deve ser int" + "\n");
                }

                if (tokenDoubleSeguinte.getToken() != Token.TK_VOID && tokenDoubleSeguinte.getToken() != Token.TK_FP) {
                    listaDeErrosSemantico.add("Erro: Na linha " + t.getLinha() + ", "
                            + "a função main não deve receber paramêtros, declare como void ou vazio dentro dos parentêses" + "\n");
                }

            }
        }

    }

    //Método iterador dos tokens
    public TipoLex analex() {

        TipoLex r = listaTokens.get(getQtdTokens());

        if (getQtdTokens() == 0) {
            tokenAnterior = r;
        } else {
            tokenAnterior = listaTokens.get(getQtdTokens() - 1);
            if (getQtdTokens() - 2 >= 0) {
                tokenDoubleAnterior = listaTokens.get(getQtdTokens() - 2);
            }
        }
        if (getQtdTokens() + 1 < listaTokens.size()) {
            tokenSeguinte = listaTokens.get(getQtdTokens() + 1);
            if (getQtdTokens() + 2 < listaTokens.size()) {
                tokenDoubleSeguinte = listaTokens.get(getQtdTokens() + 2);
            }

        }

        setQtdTokens(getQtdTokens() + 1);
        //Ignora o comentário e as directivas
        if (r.getToken() == Token.TK_DIR || r.getToken() == Token.TK_CM) {
            r = analex();
        }

        
        return r;
    }

    private void carregarListaDeVariaveis() {
        for (int i = 0; i < listaTokens.size(); i++) {
            t = analex();
            if (t.getToken() == Token.TK_ID && tokenSeguinte.getToken() != Token.TK_AP) {
                if (!listaDeVariaveis.contains(t.getLexema())) {
                    listaDeVariaveis.add(t.getLexema());
                }
            }
        }
        setQtdTokens(0);

    }

    private void mergeComentsDiret() {
        for(int i = 0; i < listaTokensCopia.size(); i++){
            if(listaTokensCopia.get(i).getToken() == Token.TK_CM || listaTokensCopia.get(i).getToken() == Token.TK_DIR)
                listaTokens.add(i, listaTokensCopia.get(i));
        }
    }
    
    private void removeComentsDiret() {
    for (int i = listaTokens.size() - 1; i >= 0; i--) {
        if (listaTokens.get(i).getToken() == Token.TK_CM || listaTokens.get(i).getToken() == Token.TK_DIR) {
            TipoLex r = listaTokens.remove(i);
        }
    }
}

}

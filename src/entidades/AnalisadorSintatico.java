package entidades;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Rui Malemba
 */
public class AnalisadorSintatico {

    AnalisadorLexico analiserLexico;
    ArrayList<TipoLex> listaTokens;
    TipoLex t, tokenAnterior, tokenDoubleAnterior, tokenSeguinte, tokenDoubleSeguinte;
    public int qtdErros = 0;
    private int qtdTokens = 0;
    private ArrayList<String> listaDeErros = new ArrayList<>();
    private String tipo = "";
    private String varAtrib = "";
    private String tipoVarAtrib = "";
    private int escopo = 0;
    private boolean aux;

    public AnalisadorSintatico(String path) {
        analiserLexico = new AnalisadorLexico(path);

        listaTokens = analiserLexico.analex();
    }

    public int getQtdTokens() {
        return qtdTokens;
    }

    public void setQtdTokens(int qtdTokens) {
        if (qtdTokens < listaTokens.size()) {
            this.qtdTokens = qtdTokens;
        }
    }

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

        //Antes de andar com o analex
        if ((r.getToken() == Token.TK_INT || r.getToken() == Token.TK_CHAR || r.getToken() == Token.TK_DOUBLE || r.getToken() == Token.TK_FLOAT || r.getToken() == Token.TK_VOID)
                && tokenSeguinte.getToken() == Token.TK_ID) {
            this.tipo = r.getLexema();//atualiza o tipo
        }
        if (r.getLexema().equals("=") && tokenAnterior.getToken() == Token.TK_ID) {
            this.varAtrib = tokenSeguinte.getLexema();//atualiza o valor de atribuição
        }        // atualiza o tipo valor de atribuição
        if (r.getToken() == Token.TK_NUMINT && tokenAnterior.getLexema().equals("=")) {
            this.tipoVarAtrib = "int";
        } else if (r.getToken() == Token.TK_NUMREAL && tokenAnterior.getLexema().equals("=")) {
            this.tipoVarAtrib = "real";
        } else if ((r.getToken() == Token.TK_C || r.getToken() == Token.TK_S) && tokenAnterior.getLexema().equals("=")) {
            this.tipoVarAtrib = "char";
        }
        

        if (r.getToken() == Token.TK_AC) {
            this.escopo++;
        }
        
        if (tokenSeguinte != null) {
            if (tokenSeguinte.getToken() == Token.TK_VIRG || tokenSeguinte.getToken() == Token.TK_PVIRG || tokenSeguinte.getToken() == Token.TK_INT
                    || tokenSeguinte.getToken() == Token.TK_DOUBLE || tokenSeguinte.getToken() == Token.TK_FLOAT || tokenSeguinte.getToken() == Token.TK_CHAR) {
                tokenSeguinte.setTipo("");
            } else {
                tokenSeguinte.setTipo(this.tipo);
            }
        }

        if (tokenDoubleSeguinte != null) {
            if (((r.getToken() == Token.TK_INT || r.getToken() == Token.TK_CHAR || r.getToken() == Token.TK_DOUBLE || r.getToken() == Token.TK_FLOAT || r.getToken() == Token.TK_VOID)
                    && tokenSeguinte.getToken() == Token.TK_ID && tokenDoubleSeguinte.getToken() == Token.TK_VIRG)) {
                aux = false; //pode limpar a string tipo
            } else if ((r.getToken() == Token.TK_VIRG && tokenSeguinte.getToken() == Token.TK_ID) || (r.getToken() == Token.TK_ID && tokenSeguinte.getToken() == Token.TK_VIRG)) {
                aux = false; //Não pode limpar a string tipo
            }
            else{
                aux = true;
            }
        } else {
            aux = true;
        }

        if (aux) {
            this.tipo = "";
        }

        if (getQtdTokens() - 2 >= 0) {
            tokenDoubleAnterior.setTipoVarAtrib(this.tipoVarAtrib);
            this.tipoVarAtrib = "";
        }
        if (getQtdTokens() - 1 >= 0) {
            tokenAnterior.setVarAtrib(this.varAtrib);
            this.varAtrib = "";
        }
        r.setEscopo(this.escopo);

        setQtdTokens(getQtdTokens() + 1);
        if (r.getToken() == Token.TK_DIR || r.getToken() == Token.TK_CM) {
            r = analex();
        }
        return r;
    }

    private void modoPane() {

        boolean controller = true;

        do {
            t = analex();

            controller = t.getToken() != Token.TK_PVIRG && t.getToken() != Token.TK_FIM;

        } while (controller);
    }

    private ArrayList<String> juntarArrayLis(ArrayList<String> errosSintatico) {
        ArrayList<String> resultado = new ArrayList<>();
        for (int i = 0; i < listaTokens.get(0).getListaDeErros().size(); i++) {
            resultado.add(listaTokens.get(0).getListaDeErros().get(i));
        }
        for (int i = 0; i < errosSintatico.size(); i++) {
            resultado.add(errosSintatico.get(i));
        }

        return resultado;
    }

    public ArrayList<TipoLex> program() {
        decl_list1();
        ArrayList<String> totalErros = juntarArrayLis(listaDeErros);
        listaTokens.get(0).setListaDeErros(totalErros);//colocar sempre na posição 0, minha lógica
        return listaTokens;
    }

    public void decl_list1() {

        if (getQtdTokens() > 0) {
            if (!(t.getToken() == Token.TK_INT || t.getToken() == Token.TK_FLOAT || t.getToken() == Token.TK_DOUBLE || t.getToken() == Token.TK_CHAR || t.getToken() == Token.TK_VOID)) {
                t = analex();
            }
        } else {
            t = analex();
        }

        if (t.getToken() == Token.TK_INT || t.getToken() == Token.TK_FLOAT || t.getToken() == Token.TK_DOUBLE || t.getToken() == Token.TK_CHAR || t.getToken() == Token.TK_VOID) {
            decl();
            decl_list1();
        } else if (tokenAnterior.getToken() == Token.TK_FC && t.getToken() == Token.TK_FIM) {
            vazio();
        } else {

            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um tipo de dados na linha " + tokenAnterior.getLinha() + "!\n ");
            } else {
                listaDeErros.add("Erro: Esperava-se um tipo de dados na linha " + t.getLinha() + "!\n ");
            }
            qtdErros++;
            modoPane();
        }

    }

    void decl() {
        if (t.getToken() == Token.TK_INT || t.getToken() == Token.TK_FLOAT || t.getToken() == Token.TK_DOUBLE || t.getToken() == Token.TK_CHAR || t.getToken() == Token.TK_VOID) {
            t = analex();
            if (t.getToken() == Token.TK_ID) {
                t = analex();
                decl1();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um identificador na linha " + tokenAnterior.getLinha() + "!\n");
                } else {
                    listaDeErros.add("Erro: Esperava-se um identificador na linha " + t.getLinha() + "!\n");
                }
                qtdErros++;
                modoPane();
            }
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um tipo de dados na linha " + tokenAnterior.getLinha() + "!\n ");
            } else {
                listaDeErros.add("Erro: Esperava-se um tipo de dados na linha " + t.getLinha() + "!\n ");
            }
            qtdErros++;
            modoPane();
        }
    }

    void decl1() {
        if (t.getToken() == Token.TK_APR) {
            exp();
            if (t.getToken() == Token.TK_FPR) {
                t = analex();
                if (t.getToken() == Token.TK_PVIRG) {
                    t = analex();
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se ponto e vírgula na linha " + tokenAnterior.getLinha() + "!");
                        modoPane();
                    } else {
                        listaDeErros.add("Erro: Esperava-se ponto e vírgula na linha " + t.getLinha() + "!");
                        modoPane();
                    }
                    qtdErros++;
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um fecha pararentes reto na linha " + tokenAnterior.getLinha() + ".");
                    modoPane();
                } else {
                    listaDeErros.add("Erro: Esperava-se um fecha pararentes reto na linha " + t.getLinha() + ".");
                    modoPane();
                }
                qtdErros++;
            }
        } else if (t.getToken() == Token.TK_AP) {
            paramas();
            //t = analex();//provisório
            if (t.getToken() == Token.TK_FP) {
                t = analex();
                if (t.getToken() == Token.TK_PVIRG) {
                    t = analex();
                } else if (t.getToken() == Token.TK_AC) {
                    comando();
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se ponto e vírgula ou { na linha " + tokenAnterior.getLinha() + "!");

                    } else {
                        listaDeErros.add("Erro: Esperava-se ponto e vírgula ou { na linha " + t.getLinha() + "!");

                    }
                    qtdErros++;
                    modoPane();
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + tokenAnterior.getLinha() + ".");

                } else {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + t.getLinha() + ".");

                }

                qtdErros++;
                modoPane();
            }
        } else if (t.getToken() == Token.TK_PVIRG) {
            t = analex();
        } else if (t.getToken() == Token.TK_VIRG) {
            t = analex();
            if (t.getToken() == Token.TK_ID) {
                t = analex();
                decl1();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um identificador na linha " + tokenAnterior.getLinha() + "!\n");
                } else {
                    listaDeErros.add("Erro: Esperava-se um identificador na linha " + t.getLinha() + "!\n");
                }
                qtdErros++;
                modoPane();
            }
        } else if (t.getLexema().equals("=")) {
            t = analex();
            stmt_list();
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se [ ou ( ou ; ou = na linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: Esperava-se [ ou ( ou ; ou = na linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
        }
    }

    public void comando() {
        if (t.getToken() == Token.TK_AC) {
            local_decls1();
            stmt_list();
            if (t.getToken() == Token.TK_FC) {
                t = analex();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um fecha chavetas na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um fecha chavetas na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
            }
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um abre chavetas. na linha " + tokenAnterior.getLinha() + "");
            } else {
                listaDeErros.add("Erro: Esperava-se um abre chavetas. na linha " + t.getLinha() + "");
            }
            qtdErros++;
            modoPane();
        }
    }

    public void local_decls1() {
        if (t.getToken() == Token.TK_AC) {
            t = analex();
        }

        if (t.getToken() == Token.TK_INT || t.getToken() == Token.TK_FLOAT || t.getToken() == Token.TK_DOUBLE || t.getToken() == Token.TK_CHAR) {
            local_decl();
            local_decls1();
        } /*else if (t.getToken() == Token.TK_ID) {
            listaDeErros.add("Erro: Esperava-se um tipo de dados. na linha " + t.getLinha() + "\n");
            qtdErros++;
        }*/ else {
            if (t.getToken() == Token.TK_AC) {
                t = analex();
            }
        }
    }

    public void local_decl() {

        t = analex();
        if (t.getToken() == Token.TK_ID) {
            local_decl1();
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um identificador na linha " + tokenAnterior.getLinha() + "!\n");
            } else {
                listaDeErros.add("Erro: Esperava-se um identificador na linha " + t.getLinha() + "!\n");
            }
            qtdErros++;
            modoPane();
        }

    }

    public void local_decl1() {
        t = analex();
        if (t.getToken() == Token.TK_PVIRG) {
            t = analex();
            return;
        } else if (t.getToken() == Token.TK_VIRG) {
            t = analex();
            if (t.getToken() == Token.TK_ID) {
                local_decl1();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um identificador na linha " + tokenAnterior.getLinha() + "!\n");
                } else {
                    listaDeErros.add("Erro: Esperava-se um identificador na linha " + t.getLinha() + "!\n");
                }
                qtdErros++;
                modoPane();
            }
        } else if (t.getToken() == Token.TK_APR) {
            exp();
            if (!(tokenAnterior.getToken() == Token.TK_NUMINT || tokenAnterior.getToken() == Token.TK_ID)) {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se que fosse definido o tamanho do array! Na linha" + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se que fosse definido o tamanho do array! Na linha" + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
            }
            if (t.getToken() == Token.TK_FPR) {
                t = analex();
                if (t.getToken() == Token.TK_PVIRG) {
                    t = analex();
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + tokenAnterior.getLinha() + ".");
                    } else {
                        listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + t.getLinha() + ".");
                    }
                    qtdErros++;
                    modoPane();
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes reto na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes reto na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
            }

        } else if (t.getLexema().equals("=")) {
            t = analex();
            stmt_list();
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um \"; ou [\"; ou =\" na linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: Esperava-se um \"; ou [\"; ou =\" na linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
        }
    }

    public void paramas() {
        t = analex();
        if (t.getToken() == Token.TK_INT || t.getToken() == Token.TK_FLOAT || t.getToken() == Token.TK_DOUBLE || t.getToken() == Token.TK_CHAR) {
            param_list();
        } else if (t.getToken() == Token.TK_VOID) {
            t = analex();
        }
        return;
    }

    public void param_list() {
        if (t.getToken() == Token.TK_INT || t.getToken() == Token.TK_FLOAT || t.getToken() == Token.TK_DOUBLE || t.getToken() == Token.TK_CHAR) {
            param();
            param_list1();
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um tipo de dados na linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: Esperava-se um tipo de dados na linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
            return;
        }
    }

    public void param_list1() {
        if (t.getToken() == Token.TK_VIRG) {
            t = analex();
            param_list();
            param_list1();
        }
    }

    public void param() {
        t = analex();
        if (t.getToken() == Token.TK_ID) {
            param1();
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um identificador na linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: Esperava-se um identificador na linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
            return;
        }
    }

    public void param1() {

        t = analex();

        if (t.getToken() == Token.TK_APR) {
            t = analex();
            if (t.getToken() == Token.TK_FPR) {
                t = analex();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes reto na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes reto na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }

        }
    }

    public void exp() {
        t = analex();
        if (t.getToken() == Token.TK_ID) {
            t = analex();
            exp1();
            exp3();
        } else if (t.getLexema().equals("+")) {
            t = analex();
            exp();
            exp3();
        } else if (t.getLexema().equals("-")) {
            t = analex();
            exp();
            exp3();
        } else if (t.getLexema().equals("!")) {
            t = analex();
            exp();
            exp3();
        } else if (t.getLexema().equals("--")) {
            t = analex();
            exp();
            exp3();
        } else if (t.getLexema().equals("++")) {
            t = analex();
            exp();
            exp3();
        } else if (t.getToken() == Token.TK_NUMINT || t.getToken() == Token.TK_NUMREAL || t.getToken() == Token.TK_C || t.getToken() == Token.TK_S) {
            t = analex();
            exp3();
        } else if (tokenAnterior.getToken() == Token.TK_ID) {//minha ideia
            exp1();
            exp3();
        }
    }

    public void exp1() {

        if (t.getToken() == Token.TK_APR) {
            exp();
            if (t.getToken() == Token.TK_FPR) {
                t = analex();
                exp2();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes reto na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes reto na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        } else if (t.getToken() == Token.TK_PTO) {
            t = analex();
            if (t.getLexema().equals("sizeof")) {
                t = analex();
                if (t.getToken() == Token.TK_AP) {
                    exp();
                    if (t.getToken() == Token.TK_FP) {
                        t = analex();
                    } else {
                        if (tokenAnterior.getLinha() != t.getLinha()) {
                            listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + tokenAnterior.getLinha() + ".");
                        } else {
                            listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + t.getLinha() + ".");
                        }
                        qtdErros++;
                        modoPane();
                        return;
                    }
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + tokenAnterior.getLinha() + ".");
                    } else {
                        listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + t.getLinha() + ".");
                    }
                    qtdErros++;
                    modoPane();
                    return;
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se sizeof na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se sizeof na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        } else if (t.getToken() == Token.TK_AP) {
            arg();
            if (t.getToken() == Token.TK_FP) {
                t = analex();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        }
    }

    public void exp2() {
        if (t.getLexema().equals("=")) {
            exp();
        }
    }

    public void exp3() {
        if (t.getLexema().equals("||") || t.getLexema().equals("=") || t.getLexema().equals("==") || t.getLexema().equals("<=") || t.getLexema().equals("<")
                || t.getLexema().equals(">") || t.getLexema().equals(">=") || t.getLexema().equals("&&") || t.getLexema().equals("+") || t.getLexema().equals("%")
                || t.getLexema().equals("-") || t.getLexema().equals("*") || t.getLexema().equals("/") || t.getLexema().equals("++") || t.getLexema().equals("--")
                || t.getLexema().equals("+=") || t.getLexema().equals("!=") || t.getLexema().equals("-=") || t.getLexema().equals("*=") || t.getLexema().equals("/=")
                || t.getLexema().equals("|") || t.getLexema().equals("&") || t.getLexema().equals("?") || t.getLexema().equals(":")) {
            exp();
            exp3();
        }
    }

    public void arg() {
        arg_list();
    }

    public void arg_list() {
        exp();
        arg_list1();
    }

    public void arg_list1() {
        if (t.getToken() == Token.TK_VIRG && tokenSeguinte.getToken() != Token.TK_FP) {
            exp(); //consome no exp
            arg_list1();
        } else if (tokenSeguinte.getToken() == Token.TK_FP) {
            t = analex();
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um identificador ou um literal linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: Esperava-se um identificador ou um literal linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
        }
    }

    public void stmt_list() {
        stmt_list1();
    }

    public void stmt_list1() {
        /*if(t.getToken() != Token.TK_FC)
            t = analex();*/

        if (t.getToken() == Token.TK_ID || t.getLexema().equals("+") || t.getLexema().equals("-") || t.getLexema().equals("!")
                || t.getToken() == Token.TK_NUMINT || t.getToken() == Token.TK_NUMREAL || t.getToken() == Token.TK_C || t.getToken() == Token.TK_S || t.getToken() == Token.TK_PVIRG
                || t.getToken() == Token.TK_AC || t.getLexema().equals("if") || t.getLexema().equals("while") || t.getLexema().equals("return") || t.getLexema().equals("break")
                || t.getLexema().equals("printf") || t.getLexema().equals("scanf") || t.getLexema().equals("for") || t.getLexema().equals("do")) {
            stmt();
            stmt_list1();
        }
    }

    public void stmt() {
        if (t.getToken() == Token.TK_ID || t.getLexema().equals("+") || t.getLexema().equals("-") || t.getLexema().equals("!")
                || t.getToken() == Token.TK_NUMINT || t.getToken() == Token.TK_NUMREAL || t.getToken() == Token.TK_C || t.getToken() == Token.TK_S) {
            exp();
            if (t.getToken() == Token.TK_PVIRG) {
                t = analex();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        } else if (t.getToken() == Token.TK_PVIRG) {
            t = analex();
        } else if (t.getToken() == Token.TK_AC) {
            comando();
        } else if (t.getLexema().equals("if")) {
            if_stmt();
        } else if (t.getLexema().equals("while")) {
            while_stmt();
        } else if (t.getLexema().equals("return")) {
            return_stmt();
        } else if (t.getLexema().equals("break")) {
            break_stmt();
        } else if (t.getLexema().equals("printf")) {
            printf_stmt();
        } else if (t.getLexema().equals("scanf")) {
            scanf_stmt();
        } else if (t.getLexema().equals("for")) {
            for_stmt();
        }
        else if (t.getLexema().equals("do")) {
            do_stmt();
        }else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: sentença inválida na linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: sentença inválida na linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
            return;
        }
    }

    public void if_stmt() {
        if (t.getLexema().equals("if")) {
            t = analex();
            if (t.getToken() == Token.TK_AP) {
                exp();
                if (t.getToken() == Token.TK_FP) {
                    t = analex();
                    stmt();
                    if_stmt1();
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + tokenAnterior.getLinha() + ".");
                    } else {
                        listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + t.getLinha() + ".");
                    }
                    qtdErros++;
                    modoPane();
                    return;
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        }
    }

    public void if_stmt1() {
        if (t.getLexema().equals("else")) {
            t = analex();
            stmt();
        }
    }

    public void while_stmt() {
        //Na verdade sempre que essa função ser chamada o if abaixo vai sempre verdadeiro, é por uma questão de ler facilmente o código
        if (t.getLexema().equals("while")) {
            t = analex();
            if (t.getToken() == Token.TK_AP) {
                exp();
                if (t.getToken() == Token.TK_FP) {
                    t = analex();
                    stmt();
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + tokenAnterior.getLinha() + ".");
                    } else {
                        listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + t.getLinha() + ".");
                    }
                    qtdErros++;
                    modoPane();
                    return;
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        }
    }
    
    public void do_stmt() {
        //Na verdade sempre que essa função ser chamada o if abaixo vai sempre verdadeiro, é por uma questão de ler facilmente o código
        if (t.getLexema().equals("do")) {
            t = analex();
            stmt();
            if (t.getLexema().equals("while")) {
                t = analex();
                if (t.getToken() == Token.TK_AP) {
                    exp();
                    if (t.getToken() == Token.TK_FP) {
                        t = analex();
                        if (t.getToken() == Token.TK_PVIRG) {
                            t = analex();
                        } else {
                            listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + t.getLinha() + ".");
                            qtdErros++;
                        }

                    } else {
                        listaDeErros.add("Erro: Esperava-se um Fecha parentes na linha " + t.getLinha() + ".");
                        qtdErros++;

                    }

                } else {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + t.getLinha() + ".");
                    qtdErros++;

                }
            } else {
                listaDeErros.add("Erro: Esperava-se a palavra reservada while " + t.getLinha() + ".");
                qtdErros++;

            }
        }
    }

    public void return_stmt() {
        //Na verdade sempre que essa função ser chamada o if abaixo vai sempre verdadeiro, é por uma questão de ler facilmente o código
        if (t.getLexema().equals("return")) {
            t = analex();
            return_stmt1();
        }
    }

    public void return_stmt1() {
        if (t.getToken() == Token.TK_PVIRG) {
            t = analex();
        } else if (t.getToken() == Token.TK_ID || t.getLexema().equals("+") || t.getLexema().equals("-") || t.getLexema().equals("!")
                || t.getToken() == Token.TK_NUMINT || t.getToken() == Token.TK_NUMREAL || t.getToken() == Token.TK_C || t.getToken() == Token.TK_S) {
            exp();
            if (t.getToken() == Token.TK_PVIRG) {
                t = analex();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um ponto e vírgula ou uma expressão linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: Esperava-se um ponto e vírgula ou uma expressão linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
            return;
        }
    }

    public void break_stmt() {
        //Na verdade sempre que essa função ser chamada o if abaixo vai sempre verdadeiro, é por uma questão de ler facilmente o código
        if (t.getLexema().equals("break")) {
            t = analex();
            if (t.getToken() == Token.TK_PVIRG) {
                t = analex();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        }
    }

    public void printf_stmt() {
        //Na verdade sempre que essa função ser chamada o if abaixo vai sempre verdadeiro, é por uma questão de ler facilmente o código
        if (t.getLexema().equals("printf")) {
            t = analex();
            if (t.getToken() == Token.TK_AP) {
                t = analex();
                if (t.getToken() == Token.TK_S) {
                    t = analex();
                    ids();
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se uma string na linha " + tokenAnterior.getLinha() + ".");
                    } else {
                        listaDeErros.add("Erro: Esperava-se uma string na linha " + t.getLinha() + ".");
                    }
                    qtdErros++;
                    return;
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        }
    }

    public void ids() {
        if (t.getToken() == Token.TK_VIRG) {
            t = analex();
            if (t.getToken() == Token.TK_ID) {
                t = analex();
                ids();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um identificador na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um identificador na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        } else if (t.getToken() == Token.TK_FP) {
            t = analex();
            if (t.getToken() == Token.TK_PVIRG) {
                t = analex();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se uma vírgula ou um fecha parentes na linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: Esperava-se uma vírgula ou um fecha parentes na linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
            return;
        }
    }

    public void scanf_stmt() {
        if (t.getLexema().equals("scanf")) {
            t = analex();
            if (t.getToken() == Token.TK_AP) {
                t = analex();
                if (t.getToken() == Token.TK_S) {
                    if (t.getLexema().contains("%d") || t.getLexema().contains("%i") || t.getLexema().contains("%f") || t.getLexema().contains("%lf") || t.getLexema().contains("%c")
                            || t.getLexema().contains("%s")) {
                        t = analex();
                        scanf_stmt1();
                    } else {
                        if (tokenAnterior.getLinha() != t.getLinha()) {
                            listaDeErros.add("Erro: Esperava-se um especificador de formato na linha " + tokenAnterior.getLinha() + ".");
                        } else {
                            listaDeErros.add("Erro: Esperava-se um especificador de formato na linha " + t.getLinha() + ".");
                        }
                        qtdErros++;
                        modoPane();
                        return;
                    }
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se uma string na linha " + tokenAnterior.getLinha() + ".");
                    } else {
                        listaDeErros.add("Erro: Esperava-se uma string na linha " + t.getLinha() + ".");
                    }
                    qtdErros++;
                    modoPane();
                    return;
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        }
    }

    public void scanf_stmt1() {
        if (t.getToken() == Token.TK_VIRG) {
            t = analex();
            if (t.getLexema().equals("&")) {
                t = analex();
                if (t.getToken() == Token.TK_ID) {
                    t = analex();
                    scanf_stmt2();
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se um identificador na linha " + tokenAnterior.getLinha() + ".");
                    } else {
                        listaDeErros.add("Erro: Esperava-se um identificador na linha " + t.getLinha() + ".");
                    }
                    qtdErros++;
                    modoPane();
                    return;
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um & na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um & na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se uma vírgula na linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: Esperava-se uma vírgula na linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
            return;
        }

    }

    public void scanf_stmt2() {
        if (t.getToken() == Token.TK_FP) {
            t = analex();
            if (t.getToken() == Token.TK_PVIRG) {
                t = analex();
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        } else if (t.getToken() == Token.TK_VIRG) {
            scanf_stmt1();
        } else {
            if (tokenAnterior.getLinha() != t.getLinha()) {
                listaDeErros.add("Erro: Esperava-se um fecha parentes ou vírgula na linha " + tokenAnterior.getLinha() + ".");
            } else {
                listaDeErros.add("Erro: Esperava-se um fecha parentes ou vírgula na linha " + t.getLinha() + ".");
            }
            qtdErros++;
            modoPane();
            return;
        }

    }

    public void for_stmt() {
        //Na verdade sempre que essa função ser chamada o if abaixo vai sempre verdadeiro, é por uma questão de ler facilmente o código
        if (t.getLexema().equals("for")) {
            t = analex();
            if (t.getToken() == Token.TK_AP) {
                exp();
                if (t.getToken() == Token.TK_PVIRG) {
                    exp();
                    if (t.getToken() == Token.TK_PVIRG) {
                        exp();
                        if (t.getToken() == Token.TK_FP) {
                            t = analex();
                            stmt();
                        } else {
                            if (tokenAnterior.getLinha() != t.getLinha()) {
                                listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + tokenAnterior.getLinha() + ".");
                            } else {
                                listaDeErros.add("Erro: Esperava-se um fecha parentes na linha " + t.getLinha() + ".");
                            }
                            qtdErros++;
                            modoPane();
                            return;
                        }
                    } else {
                        if (tokenAnterior.getLinha() != t.getLinha()) {
                            listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + tokenAnterior.getLinha() + ".");
                        } else {
                            listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + t.getLinha() + ".");
                        }
                        qtdErros++;
                        modoPane();
                        return;
                    }
                } else {
                    if (tokenAnterior.getLinha() != t.getLinha()) {
                        listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + tokenAnterior.getLinha() + ".");
                    } else {
                        listaDeErros.add("Erro: Esperava-se um ponto e vírgula na linha " + t.getLinha() + ".");
                    }
                    qtdErros++;
                    modoPane();
                    return;
                }
            } else {
                if (tokenAnterior.getLinha() != t.getLinha()) {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + tokenAnterior.getLinha() + ".");
                } else {
                    listaDeErros.add("Erro: Esperava-se um abre parentes na linha " + t.getLinha() + ".");
                }
                qtdErros++;
                modoPane();
                return;
            }
        }
    }

    private void vazio() {
        return;
    }

}

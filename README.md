# java-c-compiler
Compilador de linguagem C feito com a linguagem java

Olá, Eu criei um compilador!!!

Compilador feito em linguagem Java para Compilar linguagem C.

Durante o 3º Ano da minha licenciatura tive uma disciplina denominada Compilador onde fomos orientado pelo Dr. André Filemon como se deve criar um compilador!

Para a criação deste compilador utilizamos seguintes fundamentos:

 1. Expressão Regular
 2. Autómato (Finito Determinístico)
 3. <b>Análise Léxica ( 1º fase de um compilador )</b>
 4. <b>Analise Sintática ( 2º fase de um compilador )</b><br/>
      i. Gramática Livre de Contexto ( Top-Down )<br/>
     ii. Técnica de Detenção de Erro ( Modo Pânico )<br/>
    iii. Tokens de Sincronização<br/>
     iv. Técnica de derivação ( First - Flow )
 5. <b>Analise Semântica ( 3º fase de um compilador )</b>

A linguagem de estudo do mesmo compilador foi a Linguagem C e a linguagem de implementação do compilador foi a Linguagem Java.

<h3>1.1 Porque usar C e Java para cria o compilador ?</h3>

Para o meu trabalho eu escolhi utilizar a Linguagem C porque é uma linguagem muito utilizada para fins didático e ela possui um numero de estados reduzido e um número de palavras reservadas reduzidos assim é simples aplicar uma ideia inicial e depois escalar para quando se for desenvolver compiladores para outras linguagens orientadas a objecto e de tipagem dinâmica.

O Java é uma linguagem muito utilizada também para fins didático, e durante o 2 e 3 ano da faculdade estudamos a linguagem java para resolução de algoritmos e a sua vertente no paradigma orientado a objecto, como tinha uma forte base de java então decidi utilizar ela como a linguagem de implementação que iria receber o código escrito em C e compilar.

<h3>1.2 Uso de Expressão Regular & Autómatos</h3>

É sabido por todos nós que uma variável obedece um conjunto de regras para ser declarada de acordo a linguagem de programação em uso, por exemplo no c não podemos ter o nome de variável iniciando com um numero int 3; Então para que as regras de declaração de variável seja cumprida utilizamos um expressão regular contendo as regras para posterior ser validada com este exemplo tem mais exemplos onde fizemos o uso da expressão regular!

 1. Váriavel: ID-> <b>[A-Za-z]|{[A-Za-z]||[0-9]}^*</b></b>
 2. Números Inteiros: <b>INT-> [0-9]^+</b></b>
 3. Números Reais: REAL-> <b>[0-9]^+.[0-9]^+</b></b>

Ai tem alguns exemplos de expressões regulares que usamos, para validar algumas regras mais simples, tem regras complexas que não é possível fazer a representação só com expressão regular então ai onde entrou a Utilização de autómatos, bom existe 3 tipos <b>#Autómato Finito Determinístico , #Autómato Finito Não Determinístico , #Autómato Com Movimentos Vazios</b>.

Para a criação do compilador o autómato a ser utlizado é o <b>#Autómato Finito Determinístico</b> Porque ele tem apenas um caminho para cada símbolo lido e ele não possui movimentos vazios! <a href="https://pt.wikipedia.org/wiki/M%C3%A1quina_de_estados_finitos_n%C3%A3o_determin%C3%ADstica">Ler mais sobre autómatos</a>.

Então esse foi o autómato que eu desenvolvi para o meu caso de estudo na ferramenta <a href="https://www.jflap.org/">JFLAP</a>:

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

![AFANALEX](https://github.com/RuiYuriAfricano/java-c-compiler/assets/95936638/0fab781e-d0aa-40ff-a561-2a2e5322bdcb)

para uma melhor visibilidade, disponibilizp o ficheiro source do jflap que desenhei o automáto na pasta docs.

<h3>1.3 Análise Léxica ( 1º fase de um compilador )</h3>

A análise léxica também conhecida como <b>Scanner</b> é quando o compilador pega o código escrito em um ficheiro de texto faz a leitura, parte cada símbolo e armazena em uma estrutura de dados que pode ser array, list, pilha, fila... e classifica esse símbolo lido ou conjunto de símbolos lidos caso ele reconheça este símbolo. O reconhecimento dos símbolos do compilador é feito com base uma lista de <b>Tokens</b> que é previamente criado para classificar cada símbolo lido. <a href="https://github.com/RuiYuriAfricano/java-c-compiler/blob/main/src/entidades/Token.java"> Ver lista de token </a>

Aqui tem a saída do meu compilador para um analise léxica do código:

#include <stdio.h>
#include <stdlib.h>
#define A 5
/*
Autor: Rui Malemba
Data: 10/05/2023 
*/
int main(){
	int a = 5;
	int w[3]; //Declaração do vetor W
	char letra = 'm';
	scanf("%d", &c);
	if(a == A)
		printf("Olá");
	media(11, 23.3, 'a', c);
	gets(letra);
	system("pause");
	return 0;
}
![Captura de Ecrã (576)](https://github.com/RuiYuriAfricano/java-c-compiler/assets/95936638/18c2b764-42f0-4d8f-8213-802c75121052)
![Captura de Ecrã (577)](https://github.com/RuiYuriAfricano/java-c-compiler/assets/95936638/5e81c84b-9b98-4061-a661-10187306f640)
![Captura de Ecrã (578)](https://github.com/RuiYuriAfricano/java-c-compiler/assets/95936638/8c7cf201-beb0-4254-9134-3dfd1d1290d5)
![Captura de Ecrã (579)](https://github.com/RuiYuriAfricano/java-c-compiler/assets/95936638/741038d9-b502-4cb6-854b-33a5edef8c19)
![Captura de Ecrã (580)](https://github.com/RuiYuriAfricano/java-c-compiler/assets/95936638/38a7094b-4ab4-40e9-89de-c03a066a960d)






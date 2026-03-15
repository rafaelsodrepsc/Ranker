# 🏆 Ranker — Sistema de Campeonatos

> Trabalho Final — Linguagem e Laboratório de Programação II
> Universidade Estadual da Paraíba (UEPB)
> Professor: Thiago Soares Marques

---

## Sobre o Projeto

O **Ranker** é um sistema de gerenciamento de campeonatos de futebol desenvolvido em Java, com execução via terminal. O sistema suporta dois formatos de competição — **Pontos Corridos** e **Mata-Mata** — com geração automática de confrontos, agendamento inteligente de partidas por local e simulação de resultados.

---

## Funcionalidades

### Campeonato Pontos Corridos
- Geração de confrontos com algoritmo **Round Robin** (ida e volta)
- Suporte a número par e ímpar de times (time fantasma)
- Simulação de rodadas com resultados aleatórios
- Tabela de classificação com critérios de desempate (pontos → vitórias → saldo de gols)
- Exibição colorida com últimas 5 partidas por time `(V) (E) (D) (.) ((x))`

### Campeonato Mata-Mata
- Geração de chaveamento aleatório com `Collections.shuffle()`
- Validação de potência de 2 via operação bitwise `(n & (n-1)) == 0`
- Algoritmo de **Scheduling** para agendamento automático de partidas por local e data
- Suporte a múltiplos jogos por local por dia (baseado na janela de horário)
- Simulação de fases com pênaltis em caso de empate
- Avanço automático de fase com reagendamento dos próximos confrontos

### Validações de Input
- Datas no passado bloqueadas campo a campo
- Data de fim não pode ser anterior ou igual à data de início
- Campeonato limitado a no máximo 1 ano de duração
- Dias de descanso entre 1 e 7
- Período validado contra o número de times informado antes de criar o campeonato
- Mata-Mata exige potência de 2 validada inline
- Horário de fechamento exige mínimo de 2 horas após abertura
- Inputs numéricos e de data com revalidação automática sem perder campos anteriores

### Geral
- Hierarquia de exceções customizadas (Checked e Unchecked)
- Uso de **Streams**, **Lambdas** e **Comparator** encadeado
- Interface colorida via códigos ANSI com cabeçalhos, separadores e mensagens padronizadas
- Geração de dados de teste integrada ao menu

---

## Estrutura de Pacotes

```
src/
├── exception/
│   ├── CampeonatoException.java           # Base Checked
│   ├── CampeonatoRuntimeException.java    # Base Unchecked
│   ├── PartidaJaEncerradaException.java
│   ├── PenaltisEmpatadadosException.java
│   ├── TimeJaCadastradoException.java
│   ├── TimeNaoEncontradoException.java
│   └── TimesInsuficientesException.java
├── model/
│   ├── Campeonato.java                    # Classe abstrata base
│   ├── CampeonatoMataMata.java            # Implementação Mata-Mata
│   ├── CampeonatoPontosCorridos.java      # Implementação Round Robin
│   ├── Cores.java                         # Constantes ANSI para terminal
│   ├── Local.java
│   ├── Partida.java
│   ├── Resultado.java                     # Enum de resultados
│   ├── StatusPartida.java                 # Enum de status
│   └── Time.java
├── service/
│   ├── RelatorioService.java              # Tabela, últimas partidas e estatísticas
│   └── SchedulingService.java             # Algoritmo de agendamento por data e local
├── ui/
│   └── Menu.java                          # Interface de linha de comando
└── Main.java
```

---

## Decisões de Projeto

| Decisão | Justificativa |
|---|---|
| `Round Robin` com rotação e espelhos | Algoritmo clássico documentado — garante que todos os pares se enfrentam exatamente uma vez |
| Time fantasma para ímpar | Mantém o algoritmo Round Robin sem casos especiais — folga rotaciona naturalmente |
| Potência de 2 via bitwise `(n & (n-1)) == 0` | O(1) e elegante para validação de mata-mata |
| `SchedulingService` independente | Separação entre lógica de negócio e algoritmo de agendamento |
| Locais independentes no Mata-Mata | Times não têm estádio fixo — locais cadastrados separadamente |
| Capacidade calculada dinamicamente | `(fechamento - abertura) / 2h` — sem necessidade de atributo fixo |
| Validação campo a campo | Usuário corrige só o campo errado sem perder os anteriores |
| Bases de exceção Checked e Unchecked | Permite `catch` por família e modificação centralizada |
| `Comparator` encadeado com `thenComparing` | Critérios de desempate modulares e legíveis |
| `AtomicInteger` no Stream | Contagem mutável dentro de lambda sem violar effectively final |
| `Collections.reverse()` no `ultimasPartidas` | Exibe o evento mais recente por último — leitura da esquerda para direita |
| Métodos `static` no `RelatorioService` | Sem estado na classe — serviço puro de apresentação |

---
## Algoritmo Round Robin

O `CampeonatoPontosCorridos` gera confrontos com o algoritmo Round Robin clássico, respeitando as seguintes regras:

- Cada time enfrenta todos os outros exatamente **uma vez na ida** e **uma vez na volta**
- Um time é fixado na posição 0 e os demais **rotacionam** a cada rodada
- Para número ímpar de times, adiciona um **time fantasma** (`null`) — o time que o enfrenta recebe folga
- Os pares são formados por **espelhos** — posição `j` enfrenta posição `n-1-j`
- A data de cada rodada é calculada como `dataInicio + (rodada × diasDescanso)`
```
Para cada rodada de 1 até numRodadas:
  para j de 0 até metade:
    timeA = lista[j]
    timeB = lista[tamanho - 1 - j]
    se ambos não forem fantasma → cria partida

  rotaciona: remove o último → insere na posição 1

Repete com mandantes invertidos para os jogos de volta
```

## Algoritmo de Scheduling

O `SchedulingService` agenda partidas do Mata-Mata respeitando as seguintes regras:

- Cada jogo tem duração de **2 horas**
- A capacidade de um local é calculada como `(fechamento - abertura) / 2`
- Os locais são ordenados por **número de jogos já agendados** no dia, priorizando os menos ocupados
- Se todos os locais estiverem cheios em um dia, avança para o próximo
- Se a data ultrapassar o `dataLimite`, lança `CampeonatoRuntimeException`

```
Para cada partida sem local:
  dataAtual = dataDeInicio

  enquanto não agendada:
    passou do prazo? → lança exceção

    ordena locais por jogos agendados (menos cheio primeiro)

    para cada local:
      calcula capacidade = (fechamento - abertura) / 2h
      conta jogos já agendados nesse local nesse dia
      se livre → atribui local e horário → registra ocupação

    se não agendou → avança um dia
```

---

## Tabela de Classificação

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                     BRASILEIRÃO                                         │
├──────┬───────────────┬──────┬─────┬─────┬─────┬─────┬─────┬─────┬───────────────────────┤
│ Pos  │ Time          │ Pts  │ VIT │ EMP │ DER │ GP  │ GC  │ SG  │    Últimas Partidas   │
├──────┼───────────────┼──────┼─────┼─────┼─────┼─────┼─────┼─────┼───────────────────────┤
│ 1º   │ Flamengo      │  9   │  3  │  0  │  0  │ 8   │ 2   │ +6  │ (V) (V) (E) (V) ((V)) │
│ 2º   │ Palmeiras     │  6   │  2  │  0  │  1  │ 5   │ 3   │ +2  │ (D) (V) (V) (.) ((V)) │
│ 3º   │ Vasco         │  3   │  1  │  0  │  2  │ 3   │ 5   │ -2  │ (.) (.) (D) (V) ((D)) │
│ 4º   │ Corinthians   │  0   │  0  │  0  │  3  │ 1   │ 7   │ -6  │ (.) (.) (D) (D) ((D)) │
└──────┴───────────────┴──────┴─────┴─────┴─────┴─────┴─────┴─────┴───────────────────────┘

(V) → Vitória   (D) → Derrota   (E) → Empate   (.) → Pendente   ((x)) → Último evento
```

---

## Hierarquia de Exceções

```
Exception
└── CampeonatoException (Checked)
    ├── TimesInsuficientesException
    └── PenaltisEmpatadadosException

RuntimeException
└── CampeonatoRuntimeException (Unchecked)
    ├── TimeJaCadastradoException
    ├── TimeNaoEncontradoException
    └── PartidaJaEncerradaException
```

---

## Como Executar

**Pré-requisitos:** Java 21 ou superior

```bash
# Compilar
javac -d out src/**/*.java

# Executar
java -cp out Main
```

Ou diretamente pela IDE (IntelliJ IDEA recomendado) executando a classe `Main`.

---

## Referências

- KNUST, S. *Sports scheduling: Problems and applications*. 2010. Disponível em: https://scholar.google.com
- Wikipedia. *Round-robin tournament*. Disponível em: https://en.wikipedia.org/wiki/Round-robin_tournament

---

## Autores
- Cassiel Pereira Ribeiro
- Rafael Sodré Paschoal
- Ulysses Camilo Viana
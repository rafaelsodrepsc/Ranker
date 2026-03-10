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
- Exibição colorida com últimas 5 partidas por time

### Campeonato Mata-Mata
- Geração de chaveamento aleatório com `Collections.shuffle()`
- Validação de potência de 2 via operação bitwise `(n & (n-1)) == 0`
- Algoritmo de **Scheduling** para agendamento automático de partidas por local e data
- Suporte a múltiplos jogos por local por dia (baseado na janela de horário)
- Simulação de fases com pênaltis em caso de empate
- Avanço automático de fase com geração dos próximos confrontos

### Geral
- Hierarquia de exceções customizadas (Checked e Unchecked)
- Uso de **Streams** e **Lambdas** para ordenação e filtragem
- Validação de inputs do usuário (formato de data, horário e números)
- Geração de dados de teste integrada ao menu
- Interface colorida via códigos ANSI

---

## Estrutura de Pacotes

```
src/
├── model/
│   ├── Campeonato.java               # Classe abstrata base
│   ├── CampeonatoPontosCorridos.java # Implementação Round Robin
│   ├── CampeonatoMataMata.java       # Implementação Mata-Mata
│   ├── Time.java
│   ├── Partida.java
│   ├── Local.java
│   ├── Cores.java                    # Constantes ANSI para terminal
│   ├── StatusPartida.java            # Enum  
│   └── Resultado.java                # Enum  
│       
├── exception/
│   ├── CampeonatoException.java           # Base Checked
│   ├── CampeonatoRuntimeException.java    # Base Unchecked
│   ├── TimesInsuficientesException.java
│   ├── TimeJaCadastradoException.java
│   ├── TimeNaoEncontradoException.java
│   ├── PartidaJaEncerradaException.java
│   └── PenaltisEmpatadosException.java
├── service/
│   ├── SchedulingService.java   # Algoritmo de agendamento por data e local
│   └── RelatorioService.java    # Streams para relatórios e estatísticas
├── ui/
│   └── Menu.java                # Interface de linha de comando
└── Main.java
```

---

## Decisões de Projeto

| Decisão | Justificativa |
|---|---|
| `Round Robin` com rotação | Algoritmo clássico que garante equilíbrio de confrontos |
| Time fantasma para ímpar | Mantém o algoritmo Round Robin sem casos especiais |
| Potência de 2 via bitwise | `(n & (n-1)) == 0` é O(1) e elegante para validação de mata-mata |
| `protected` em `Campeonato` | Subclasses precisam manipular as listas diretamente |
| `Local` sem capacidade fixa | Capacidade calculada dinamicamente via janela de horário ÷ 2h |
| `SchedulingService` independente | Separação entre lógica de negócio e algoritmo de agendamento |
| Bases de exceção | Permite `catch` por família e modificação centralizada |
| Locais independentes no Mata-Mata | Requisito do professor — times não têm estádio fixo |
| `Comparator` encadeado com `thenComparing` | Polimorfismo de interface para critérios de desempate modulares |
| `AtomicInteger` no Stream | Contagem mutável dentro de lambda sem violar effectively final |

---

## Algoritmo de Scheduling

O `SchedulingService` agenda partidas do Mata-Mata respeitando as seguintes regras:

- Cada jogo tem duração de **2 horas**
- A capacidade de um local é calculada como `(fechamento - abertura) / 2`
- Os locais são ordenados por **número de jogos já agendados** no dia, priorizando os menos ocupados
- Se todos os locais estiverem cheios em um dia, avança para o próximo dia
- Se a data ultrapassar o `dataLimite`, lança `CampeonatoRuntimeException`

```
Para cada partida sem local:
  dataAtual = dataDeInicio
  enquanto não agendada:
    ordena locais por jogos agendados (menos cheio primeiro)
    para cada local:
      calcula capacidade e jogos já agendados
      se livre → atribui local e horário → marca como ocupado
    se não agendou → avança um dia
```

---

##  Como Executar

**Pré-requisitos:** Java 17 ou superior

```bash
# Compilar
javac -d out src/**/*.java

# Executar
java -cp out Main
```

Ou diretamente pela IDE (IntelliJ IDEA recomendado) executando a classe `Main`.

---

##  Hierarquia de Exceções

```
Exception
└── CampeonatoException (Checked)
    └── TimesInsuficientesException
    └── PenaltisEmpatadosException

RuntimeException
└── CampeonatoRuntimeException (Unchecked)
    └── TimeJaCadastradoException
    └── TimeNaoEncontradoException
    └── PartidaJaEncerradaException
```

---

## 👥 Autores
- Cassiel Pereira Ribeiro  
- Rafael Sodré Paschoal    
- Ulysses Camilo Viana


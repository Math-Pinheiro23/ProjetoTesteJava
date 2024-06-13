import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Pessoa {
    private String nome;
    private LocalDate dataNascimento;

    public Pessoa(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public String getDataNascimentoFormatted() {
        return dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}

class Funcionario extends Pessoa {
    private BigDecimal salario;
    private String funcao;

    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = salario;
        this.funcao = funcao;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getFuncao() {
        return funcao;
    }
}

public class Principal {

    public static void main(String[] args) {
        List<Funcionario> funcionarios = new ArrayList<>();

        funcionarios.add(new Funcionario("João", LocalDate.of(1980, 5, 15), new BigDecimal("3500.00"), "Gerente"));
        funcionarios.add(new Funcionario("Maria", LocalDate.of(1990, 8, 27), new BigDecimal("2800.00"), "Analista"));
        funcionarios.add(new Funcionario("Pedro", LocalDate.of(1985, 10, 10), new BigDecimal("3200.00"), "Desenvolvedor"));
        funcionarios.add(new Funcionario("Ana", LocalDate.of(1982, 12, 5), new BigDecimal("4000.00"), "Coordenador"));

        funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));

        System.out.println("Funcionários:");
        funcionarios.forEach(funcionario -> {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("Data de Nascimento: " + funcionario.getDataNascimentoFormatted());
            System.out.println("Salário: " + funcionario.getSalario().toString());
            System.out.println("Função: " + funcionario.getFuncao());
            System.out.println();
        });

        funcionarios.forEach(funcionario -> {
            BigDecimal aumento = funcionario.getSalario().multiply(new BigDecimal("0.10"));
            BigDecimal novoSalario = funcionario.getSalario().add(aumento);
            funcionario.setSalario(novoSalario);
        });

        Map<String, List<Funcionario>> funcionariosPorFuncao = new HashMap<>();
        for (Funcionario funcionario : funcionarios) {
            String funcao = funcionario.getFuncao();
            if (!funcionariosPorFuncao.containsKey(funcao)) {
                funcionariosPorFuncao.put(funcao, new ArrayList<>());
            }
            funcionariosPorFuncao.get(funcao).add(funcionario);
        }

        System.out.println("\nFuncionários agrupados por função:");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("Função: " + funcao);
            lista.forEach(func -> {
                System.out.println("Nome: " + func.getNome());
                System.out.println("Salário: " + func.getSalario().toString());
            });
            System.out.println();
        });

        System.out.println("\nFuncionários que fazem aniversário nos meses 10 e 12:");
        funcionarios.stream()
                .filter(funcionario -> funcionario.getDataNascimento().getMonthValue() == 10 ||
                        funcionario.getDataNascimento().getMonthValue() == 12)
                .forEach(funcionario -> System.out.println(funcionario.getNome()));

        Funcionario maisVelho = funcionarios.stream()
                .max((f1, f2) -> f1.getDataNascimento().compareTo(f2.getDataNascimento()))
                .orElse(null);
        if (maisVelho != null) {
            System.out.println("\nFuncionário mais velho:");
            System.out.println("Nome: " + maisVelho.getNome());
            System.out.println("Idade: " + (LocalDate.now().getYear() - maisVelho.getDataNascimento().getYear()));
        }

        System.out.println("\nFuncionários por ordem alfabética:");
        funcionarios.stream()
                .sorted((f1, f2) -> f1.getNome().compareTo(f2.getNome()))
                .forEach(funcionario -> System.out.println(funcionario.getNome()));

        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\nTotal dos salários dos funcionários: " + totalSalarios.toString());

        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        System.out.println("\nSalários mínimos ganhos por cada funcionário:");
        funcionarios.forEach(funcionario -> {
            BigDecimal salariosMinimos = funcionario.getSalario().divide(salarioMinimo, RoundingMode.DOWN);
            System.out.println(funcionario.getNome() + ": " + salariosMinimos + " salários mínimos");
        });
    }
}

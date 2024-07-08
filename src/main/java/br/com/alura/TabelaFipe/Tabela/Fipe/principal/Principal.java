    package br.com.alura.TabelaFipe.Tabela.Fipe.principal;

    import br.com.alura.TabelaFipe.Tabela.Fipe.model.Dados;
    import br.com.alura.TabelaFipe.Tabela.Fipe.model.Modelos;
    import br.com.alura.TabelaFipe.Tabela.Fipe.model.Veiculo;
    import br.com.alura.TabelaFipe.Tabela.Fipe.service.ConsumoApi;
    import br.com.alura.TabelaFipe.Tabela.Fipe.service.ConverteDados;

    import java.util.ArrayList;
    import java.util.Comparator;
    import java.util.List;
    import java.util.Scanner;
    import java.util.stream.Collectors;

    public class Principal {
       private Scanner leitura = new Scanner(System.in);
       private ConsumoApi consumo = new ConsumoApi();
       private ConverteDados conversor = new ConverteDados();
       
   private final String URL_BASE = "https://fipe.parallelum.com.br/api/v2/";

        public void exibirMenu() {
            var menu = """
                    ***   OPÇÃO  ***
                    Carro
                    Moto
                    Caminhão
                    
                    Digite umas das opções para consultar:
                    """;    

            System.out.println(menu);
            var opcao = leitura.nextLine();
            String endereco;

            if (opcao.toLowerCase().contains("carr")){
                endereco = URL_BASE + "carros/marcas";
            }else if (opcao.toLowerCase().contains("mot")){
                endereco = URL_BASE + "motos/marcas";
            } else {
                endereco = URL_BASE + "caminhoes/marcas";
            }

            var json = consumo.obterDados(endereco);
            System.out.println(json);
            var marcas = conversor. obterLista(json, Dados.class);
            marcas.stream()
                    .sorted(Comparator.comparing(Dados::codigo))
                    .forEach(System.out::println);
            System.out.println("Informe o código da marca para consulta:");
            var codigoMarca = leitura.nextLine();

            endereco = endereco + "/" + codigoMarca + "/modelos";
            json = consumo.obterDados(endereco);
            var modeloLista = conversor.obterDados(json, Modelos.class);

            System.out.println("\nModelos dessa marca: ");
            modeloLista.modelos().stream()
                    .sorted(Comparator.comparing(Dados::codigo))
                    .forEach(System.out::println);

            System.out.println("\nDigite um trecho do nome do carro a ser buscado:");
            var nomeVeiculo = leitura.nextLine();

            List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                    .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                    .collect(Collectors.toList());
            System.out.println("\nModelos Filtrados");
            modelosFiltrados.forEach(System.out::println);

            System.out.println("Digite por favor o código do modelo para buscar os valores de avaliação:");
            var codigoModelos = leitura.nextLine();

            endereco = endereco + "/" + codigoModelos + "/anos";
            json = consumo.obterDados(endereco);
            List<Dados> anos = conversor.obterLista(json,Dados.class);
            List<Veiculo> veiculos = new ArrayList<>();

            for( int i = 0; i < anos.size(); i++){
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }
        System.out.println("\nTodos os veiculos filtrados com avaliações por ano:  ");
        veiculos.forEach(System.out::println);
    }
}
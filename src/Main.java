import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static final String NAO_VISITADO = "Branco";
    private static final String VISITADO = "Cinza";

    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        configuracoesIniciais(grafo);

        int opt = -1;

        while (opt != 0) {
            opt = Integer.parseInt(input(getMenu()));

            switch (opt) {
                case 1:
                    definirVertices(grafo);
                    break;

                case 2:
                    adicionarAresta(grafo);
                    break;

                case 3:
                    adicionarArestaDireto(grafo);
                    break;

                case 4:
                    definirVerticeInicial(grafo);
                    break;

                case 5:
                    listaArestas(grafo);
                    break;

                case 6:
                    calcularDistanciaVertices(grafo);
                    break;

                default:
                    break;
            }
        }
    }

    private static String getConfiguracoes(Grafo grafo){
        String retorno = "Grafo: ";
        retorno += grafo.isOrientado() ? "Orientado" : "Não-Orientado";
        retorno += ", ";
        retorno += grafo.isValorado() ? "Valorado" : "Não-Valorado";
        return retorno + "\t";
    }

    private static void configuracoesIniciais(Grafo grafo) {
        String valorado = "";
        String orientado = "";

        while (valorado.isEmpty() && orientado.isEmpty()){
            valorado = "s";
            orientado = input("Grafo é orientado? (S/N)").toLowerCase().replace(" ", "");
        }

        grafo.setValorado(valorado.equals("s"));
        grafo.setOrientado(orientado.equals("s"));
    }

    public static String input(String mensagem){
        return JOptionPane.showInputDialog(mensagem);
    }

    public static void output(String mensagem, String titulo) {
        JTextArea textArea = new JTextArea(mensagem);
        JOptionPane.showMessageDialog(null, textArea, titulo, 1);
    }

    public static String getMenu(){
        return "Selecione uma opção" +
                "\n1 - Definir os Vértices" +
                "\n2 - Adicionar Uma aresta" +
                "\n3 - Adicionar várias Arestas" +
                "\n4 - Definir vértice inicial" +
                "\n---------------------------------" +
                "\n5 - Lista de Arestas" +
                "\n6 - Calcular distância vértices" +
                "\n---------------------------------" +
                "\n0 - Sair";
    }

    public static void definirVertices(Grafo grafo) {
        String[] nomesVertices = input("Insira os vértices do grafo separados por vírgula. \nExemplo: A, B, C, D")
                .toUpperCase()
                .replace(" ", "")
                .split(",");

        grafo.getVertices().clear();
        grafo.getArestas().clear();

        for(int i = 0; i < nomesVertices.length; i++) {
            Integer idVertice = i+1;
            grafo.getVertices().add(new Vertice(idVertice, nomesVertices[i], NAO_VISITADO));
        }
    }

    public static void definirVerticeInicial(Grafo grafo){
        String resultado = "\n";
        for (Vertice vertice : grafo.getVertices()) {
            resultado += "\n" + vertice.getIdVertice() + " - " + vertice.getNome();
        }
        Vertice vertice = obterVertice(grafo, Integer.parseInt(input("Selecione o código do vértice inicial: \n" + resultado)));
        vertice.setInicial(true);
    }

    public static void adicionarArestaDireto(Grafo grafo) {
        String mensagem = "Insira as arestas pelo código ao lado esquerdo do vértice, utilizando vírgula entre origem e destino, \nalém de ponto e vírgula para separar os vértices, Conforme exemplo:\n";
        String exemplo = "(ORIGEM,DESTINO;ORIGEM,DESTINO) \n\n1,2;1,3;1,4\n\n";
        String verticesCadastrados = "";

        for (Vertice vertice : grafo.getVertices()) {
            verticesCadastrados += vertice.getIdVertice() + " - " + vertice.getNome() + "\n";
        }
        String input = input(mensagem + exemplo + verticesCadastrados);
        String[] arrayVertices = input.replace(" ", "")
                .replace("(", "")
                .replace(")", "")
                .split(";");

        for (String vertice : arrayVertices) {
            String[] verticesOrigemDestino = vertice.split(",");
            Vertice origem = obterVertice(grafo, Integer.parseInt(verticesOrigemDestino[0]));
            Vertice destino = obterVertice(grafo, Integer.parseInt(verticesOrigemDestino[1]));

            int valorAresta = 0;

            if(grafo.isValorado())
                valorAresta = Integer.parseInt(input("Insira valor para a Aresta: " + origem.getNome() + " - " + destino.getNome())
                        .toUpperCase()
                        .replace(" ", ""));

            Aresta aresta = new Aresta(origem, destino, valorAresta, "e" + (grafo.getArestas().size()+1));
            grafo.getArestas().add(aresta);

            if(!grafo.isOrientado()) {
                Aresta arestaNaoOrientada = new Aresta(destino, origem, valorAresta, "e" + (grafo.getArestas().size()+1));
                grafo.getArestas().add(arestaNaoOrientada);
            }
        }
    }

    public static void adicionarAresta(Grafo grafo) {
        String verticeOrigem = "Escolha pelo código, o vértice de ORIGEM!\n";
        String verticeDestino = "Agora escolha pelo código, o vértice de DESTINO!\n";
        String verticesCadastrados = "";

        for (Vertice vertice : grafo.getVertices()) {
            verticesCadastrados += vertice.getIdVertice() + " - " + vertice.getNome() + "\n";
        }
        Vertice origem = null;
        Vertice destino = null;

        while (origem == null && destino == null) {
            origem = obterVertice(grafo, Integer.parseInt(input(verticeOrigem + verticesCadastrados)));
            destino = obterVertice(grafo, Integer.parseInt(input(verticeDestino + verticesCadastrados)));
        }

        int valorAresta = 0;

        if(grafo.isValorado())
            valorAresta = Integer.parseInt(input("Insira valor para a Aresta.").toUpperCase().replace(" ", ""));

        Aresta aresta = new Aresta(origem, destino, valorAresta, "e" + (grafo.getArestas().size()+1));
        grafo.getArestas().add(aresta);

        if(!grafo.isOrientado()) {
            Aresta arestaNaoOrientada = new Aresta(destino, origem, valorAresta, "e" + (grafo.getArestas().size()+1));
            grafo.getArestas().add(arestaNaoOrientada);
        }
    }

    public static Vertice obterVertice(Grafo grafo, Integer idVertice){
        for (Vertice vertice : grafo.getVertices()) {
            if (vertice.getIdVertice().equals(idVertice))
                return vertice;
        }
        output("Vértice não encontrado.", "Erro");
        return null;
    }

    public static Set<Aresta> getArestasSemVerticeOrigemDestinoAoContrario(Grafo grafo) {
        Set<Aresta> arestas = new HashSet<>();

        for (Aresta aresta : grafo.getArestas()) {
            Aresta arestaContraria = new Aresta(aresta.getDestino(), aresta.getOrigem());
            if(grafo.getArestas().contains(aresta) && grafo.getArestas().contains(arestaContraria))
                arestas.add(aresta);
        }

        return arestas;
    }

    public static Set<Aresta> getArestasSemVerticeDuplicados(Grafo grafo) {
        Set<Aresta> arestas = new HashSet<>();

        for (Aresta aresta : grafo.getArestas()) {
            Aresta arestaContraria = new Aresta(aresta.getDestino(), aresta.getOrigem());
            if(!(arestas.contains(aresta) || arestas.contains(arestaContraria))) {
                aresta.setNome("e"+(arestas.size()+1));
                arestas.add(aresta);
            }
        }

        return arestas;
    }

    private static void listarValoresArestas(Grafo grafo) {
        String listaArestas = getConfiguracoes(grafo) + " \n\n";

        for (Aresta aresta : grafo.getArestas()) {
            listaArestas += "[" + aresta.getOrigem() + ", " + aresta.getDestino();
            listaArestas += grafo.isValorado() ?  ", " + aresta.getValor() + "]\n" : "]\n";
        }

        output(listaArestas, "Lista de Arestas");
    }

    private static void listaArestas(Grafo grafo) {
        String listaArestas = getConfiguracoes(grafo) + " \n\n";

        for (Aresta aresta : grafo.getArestas()) {
            listaArestas += "[" + aresta.getOrigem() + ", " + aresta.getDestino();
            listaArestas += grafo.isValorado() ?  ", " + aresta.getValor() + "]\n" : "]\n";
        }

        output(listaArestas, "Lista de Arestas");
    }

    private static void calcularDistanciaVertices(Grafo grafo) {
        List<Vertice> vertices = grafo.getVertices();
        List<Aresta> arestas = grafo.getArestas();
        String resultado = getConfiguracoes(grafo) + "\n\n\t";
        Vertice verticeInicial = grafo.getVerticeInicial();
        List<Vertice> PERM = new ArrayList<>();
        PERM.add(verticeInicial);

        while (!PERM.isEmpty()) {
            Vertice verticePERM = PERM.get(0);
            Vertice verticeMenorCaminho = null;
            for (Vertice adjacente : obterAdjacentes(grafo.isOrientado(), arestas, verticePERM)) {
                if(adjacente.getCor().equals(VISITADO))
                    continue;

                adjacente.setCaminho(verticePERM);
                adjacente.setValor(valorArestaEntreVertices(verticePERM, adjacente, arestas) + verticePERM.getValor());

                if(verticeMenorCaminho == null)
                    verticeMenorCaminho = adjacente;
                else if(verticeMenorCaminho.getValor() > adjacente.getValor())
                    verticeMenorCaminho = adjacente;

                if(verticeMenorCaminho != null)
                    PERM.add(verticeMenorCaminho);

            }

            verticePERM.setCor(VISITADO);
            PERM.remove(verticePERM);
        }

        for (Vertice vertice : vertices) {
            resultado += "\nVértice: " + vertice.getNome();
            resultado += " - distância: " + vertice.getValor();
            resultado += " - caminho: " + vertice.getCaminho();
        }

        output(resultado, "Distância e caminho entre vértices.");
    }

    private static List<Vertice> obterAdjacentes(boolean orientado, List<Aresta> arestas, Vertice vertice){
        List<Vertice> ajdacentes = new ArrayList<>();

        for (Aresta aresta : arestas) {
            if(aresta.getOrigem().equals(vertice)) {
                ajdacentes.add(aresta.getDestino());

            } else if (!orientado && aresta.getDestino().equals(vertice)) {
                ajdacentes.add(aresta.getOrigem());
            }
        }

        return ajdacentes;
    }

    private static int valorArestaEntreVertices(Vertice origem, Vertice destino, List<Aresta> arestas){

        for (Aresta aresta: arestas) {
            if(aresta.getOrigem().equals(origem) && aresta.getDestino().equals(destino)){
                return aresta.getValor();
            }
        }

        return 0;
    }

}

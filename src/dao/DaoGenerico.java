package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoGenerico <T>{
    //<T> diz que vai saber lidar com qualquer arquivo

    //O caminho encaminhado será pego nesse "valor"
    private String caminhoArquivo;

    //construtor
    public DaoGenerico(String nomeArquivo) {
        // Define o caminho data
        this.caminhoArquivo = "data/" + nomeArquivo;

        // --- ADICIONE ESTA LINHA PARA TESTE ---
        System.out.println("O arquivo está sendo salvo em: " + new File(this.caminhoArquivo).getAbsolutePath());

        // Verifica se a pasta existe antes de tentar salvar
        File arquivo = new File(this.caminhoArquivo);
        File pasta = arquivo.getParentFile(); // Pega a pasta "data"

        if (!pasta.exists()) {
            // Se a pasta não existir, ele vai criar de novo só
            pasta.mkdirs();
        }
    }
    //classe filha deve "ensinar" como transformar objeto em texto
    public abstract  String toCSV(T objeto);

    //classe filha deve "ensinar" como transformar texto em objeto
    public abstract T fromCSV(String linha);

    //classe filha deve "ensinar" qual é a chave única (ID/CPF/Código) do objeto
    public abstract String getId(T objeto);

    public boolean salvar (T objeto){
        // quando der false no file writer vai significar que vai adicionar algo no arquivo
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))){

            String linha = toCSV(objeto);
            bw.write(linha);
            bw.newLine();

            return true;
        }
        catch (IOException e){
            System.out.println("Erro ao salvar: " + e.getMessage());
            return false;
        }
    }

    public List<T> listar(){
        List<T> lista = new ArrayList<>();
        File file = new File(caminhoArquivo);

        //se estiver vazio, irá retornar uma lista vazia
        if(!file.exists()){
            return lista;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String linha;
            while ((linha = br.readLine()) != null){ //para quando não tiver mais linha
                if(!linha.trim().isEmpty()){//ignora linhas vazias
                    T objeto = fromCSV(linha); // regra para converter
                    lista.add(objeto);
                }
            }
        }
        catch (IOException e){
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return lista;
    }

    // essa funcao serve para listar apenas para buscar um especifico
    public T buscarPorId(String id) {
        List<T> lista = listar();
        for (T obj : lista) {
            if (getId(obj).equals(id)) {
                return obj; // Retorna o objeto se achar
            }
        }
        return null; // Retorna null se não achar
    }

    public boolean excluir(String id) {
        List<T> lista = listar();
        List<T> novaLista = new ArrayList<>();
        boolean encontrou = false;

        for (T obj : lista) {
            // Se o ID for igual, a gente NÃO adiciona na nova lista (ou seja, remove)
            if (getId(obj).equals(id)) {
                encontrou = true;
            } else {
                novaLista.add(obj);
            }
        }

        if (encontrou) {
            reescreverArquivo(novaLista);
            return true;
        }
        return false;
    }

    public boolean atualizar(T novoObjeto) {
        List<T> lista = listar();
        boolean encontrou = false;

        for (int i = 0; i < lista.size(); i++) {
            T objAtual = lista.get(i);

            // Verifica se é o mesmo ID para substituir
            if (getId(objAtual).equals(getId(novoObjeto))) {
                lista.set(i, novoObjeto); // Troca o antigo pelo novo
                encontrou = true;
                break;
            }
        }

        if (encontrou) {
            reescreverArquivo(lista);
            return true;
        }
        return false;
    }

    private void reescreverArquivo(List<T> lista) {
        // O false no FileWriter significa que NÃO é append, ele sobrescreve tudo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (T obj : lista) {
                bw.write(toCSV(obj));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao reescrever arquivo: " + e.getMessage());
        }
    }

}

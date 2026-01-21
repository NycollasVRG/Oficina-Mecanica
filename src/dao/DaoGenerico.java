package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoGenerico <T>{
    //<T> diz que vai saber lidar com qualquer arquivo

    //O caminho encaminhado será pego nesse "valor"
    private String caminhoArquivo;

    //construtor

    public DaoGenerico(String caminhoArquivo) {
        this.caminhoArquivo = "data/" + caminhoArquivo; // o data/ vai garantir que o arquivo fique salvo na pasta correta
    }

    //classe filha deve "ensinar" como transformar objeto em texto
    public abstract  String toCSV(T objeto);

    //classe filha deve "ensinar" como transformar texto em objeto
    public abstract T fromCSV(String linha);

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
}

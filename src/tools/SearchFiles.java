/*
 * UFC - Universidade Federal do Ceará
 * FDB - Fundamentos de Bancos de Dados
 * Professor: ANGELO RONCALLI ALENCAR BRAYNER
 * Equipe:
 *  Everson Magalhaes Cavalcante
 *  Belchior Dameao de Araújo Neto
 *  Este script faz parte do projeto BDSpotPer
 *  trabalho prático necessário como parte da nota 
 *  para a cadeira de Fundamentos de Bancos de Dados 2018.2

 */
package tools;

import components.Faixa;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 *
 * @author Belchior
 * 
 */
public class SearchFiles {
    private String conteudoExtraido;
    
    public void setConteudoExtraido(String Url){
        conteudoExtraido = getUrlContents(Url); //"https://www.mfiles.co.uk/classical-mp3.htm"
    }
    public String getConteudoExtraido(){
        return conteudoExtraido;
    }
    public String listaMusicas(){
        String lista = "";
        
        return lista;
    }
    public HashMap<String, String> listaCompositores(String separador){
        HashMap<String, String> retorno = new HashMap<String, String>();
        String[] lista = conteudoExtraido.split("<"+separador+">");
        for(int i = 1; i < lista.length; i++){
            String nome[] = lista[i].split("</"+separador+">");
            retorno.put(String.valueOf(i), nome[0]);
        }
        return retorno;
    }
    public HashMap<String, Faixa> listaComposicoes(String splt1, String splt2, String compositor){
        HashMap<String, Faixa> retorno = new HashMap<String, Faixa>();
        String[] lista = conteudoExtraido.split("<"+splt1+">");
        for(int i = 1; i < lista.length; i++){
            String nome[] = lista[i].split("</"+splt1+">");
            if(nome[0].equals(compositor)){
                String listaMusicas[] = lista[i].split("<"+splt2+">");
                for(int j = 1; j < listaMusicas.length; j++){
                    Faixa faixa = new Faixa();
                    String[] musica = listaMusicas[j].split("</"+splt2+">");
                    String link = musica[0].split("<a href=\"")[1].split("\"")[0];
                    faixa.setUrlDownload(link);
                    String descr = musica[0].split(">")[1].split("<")[0];
                    faixa.setDescr(descr);
                    faixa.setNewFaixaId();
                    faixa.setFaixaId(faixa.getFaixaId() + j);
                    retorno.put(String.valueOf(j), faixa);
                }
            }
        }
        return retorno;
    }
    // fonte: https://alvinalexander.com/blog/post/java/java-how-read-from-url-string-text
    // retorna uma string com o conteúdo de uma página web
    private static String getUrlContents(String theUrl)
    {
      StringBuilder content = new StringBuilder();

      // many of these calls can throw exceptions, so i've just
      // wrapped them all in one try/catch statement.
      try
      {
        // create a url object
        URL url = new URL(theUrl);

        // create a urlconnection object
        URLConnection urlConnection = url.openConnection();

        // wrap the urlconnection in a bufferedreader
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        String line;

        // read from the urlconnection via the bufferedreader
        while ((line = bufferedReader.readLine()) != null)
        {
          content.append(line + "\n");
        }
        bufferedReader.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      return content.toString();
    }
}

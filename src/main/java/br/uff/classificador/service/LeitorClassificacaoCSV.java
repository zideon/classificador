/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.classificador.service;

import au.com.bytecode.opencsv.CSVReader;
import br.uff.classificador.modelo.Classificacao;
import br.uff.classificador.modelo.Noticia;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author zideon
 */
@Service
public class LeitorClassificacaoCSV {

    public List<Classificacao> getClassificacoesPorNoticia(String path,String idNoticia) {
        List<Classificacao> saida = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(path +"/"+idNoticia + ".csv"), ',', '"', 0)) {
            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine != null) {
                    Classificacao c = new Classificacao(nextLine);
                    saida.add(c);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LeitorClassificacaoCSV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LeitorClassificacaoCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
        return saida;
    }
}

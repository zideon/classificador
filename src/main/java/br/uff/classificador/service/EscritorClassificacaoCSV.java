/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.classificador.service;

import au.com.bytecode.opencsv.CSVWriter;
import br.uff.classificador.modelo.Classificacao;
import br.uff.classificador.modelo.Noticia;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author zideon
 */
@Service
public class EscritorClassificacaoCSV {

    public void setClassificacao(String folder,Classificacao classificacao) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(folder+"/"+classificacao.getIdNoticia() + ".csv", true))) {
            //Create record
            String[] record = classificacao.getRecord();
            //Write the record to file
            writer.writeNext(record);
            //close the writer
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(EscritorClassificacaoCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

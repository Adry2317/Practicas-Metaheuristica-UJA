/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uja.meta.practica1_metaheuristica;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 *
 * @author joseantonio
 */
public class Practica1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Configurador config = new Configurador(args[0]);
        //System.out.println(config.getArchivos());
        //ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(1));
        //System.out.println(archivo.getNombreArchivo());
        int[] vectorPermutaciones = null;
        for (int i = 0; i < config.getAlgoritmos().size(); i++) {
            for (int j = 0; j < config.getArchivos().size(); j++) {
                ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(j));
                switch (config.getAlgoritmos().get(i)) {
                    case "Greedy":

                        Greedy greedy = new Greedy(archivo.getMatrizFlujo(), archivo.getMatrizDistancias(), archivo.getNombreArchivo());


                        vectorPermutaciones = greedy.calculoGreedy();

                        long coste = greedy.calculaCoste(vectorPermutaciones);

                        System.out.println("");
                        for (int k : vectorPermutaciones) {
                            System.out.print(k + " ");
                        }

                        System.out.println("Coste: " + coste);

                        guardarArchivo("log/" + config.getAlgoritmos().get(i) + "_" + archivo.getNombreArchivo() + ".txt", greedy.getLog());

                        break;
                    case "PrimeroMejor":
                        break;

                }
            }
        }
        ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(3));
        PMDLBit prueba = new PMDLBit(archivo.getMatrizFlujo(),archivo.getMatrizDistancias());
        int p[] = prueba.dlbIterativa(vectorPermutaciones);

        for (int i = 0; i<p.length; i++){
            System.out.println(p[i]+" ");
        }


    }

    public static void guardarArchivo(String ruta, String texto) {
        FileWriter fichero = null;
        PrintWriter pw = null;

        try {
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);

            pw.print(texto);
        } catch (IOException ex) {
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
            }
        }

    }

}

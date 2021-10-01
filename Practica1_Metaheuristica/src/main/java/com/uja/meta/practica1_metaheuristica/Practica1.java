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
        for (int i = 0; i < config.getArchivos().size(); i++) {
            for (int j = 0; j < config.getAlgoritmos().size(); j++) {
                ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(i));
                switch (config.getAlgoritmos().get(j)) {
                    case "Greedy":
                        System.out.println("");
                        System.out.println("Greedy");
                        Greedy greedy = new Greedy(archivo.getMatrizFlujo(), archivo.getMatrizDistancias(), archivo.getNombreArchivo());


                        vectorPermutaciones = greedy.calculoGreedy();

                        long coste = greedy.calculaCoste(vectorPermutaciones);


                        for (int k : vectorPermutaciones) {
                            System.out.print(k + " ");
                        }

                        System.out.println("Coste: " + coste);

                        guardarArchivo("log/" + config.getAlgoritmos().get(j) + "_" + archivo.getNombreArchivo() + ".txt", greedy.getLog());

                        break;
                    case "PrimeroMejor":
                        System.out.println("");
                        System.out.println("Algortimos primero el mejor.");

                        PMDLBit iterativo = new PMDLBit(archivo.getMatrizFlujo(), archivo.getMatrizDistancias());

                        int nuevaSol[] = iterativo.dlbIterativa(vectorPermutaciones);

                        long costeNuevaSol = calculaCostePrueba(vectorPermutaciones, archivo.getMatrizFlujo(), archivo.getMatrizDistancias());

                        for (int k : nuevaSol) {
                            System.out.print(k + " ");
                        }

                        System.out.println("Coste: " + costeNuevaSol);
                        break;

                }
            }
        }




    }



    public static long calculaCostePrueba(int[] vectorSolucion, int [][] matrizFlujo, int [][] matrizDistancia) {

        long coste = 0;


        for (int i = 0; i < vectorSolucion.length; i++) {
            for (int j = 0; j < vectorSolucion.length; j++) {
                if (i != j) {
                    coste = coste + (matrizFlujo[i][j] * matrizDistancia[vectorSolucion[i]][vectorSolucion[j]]);
                }
            }
        }


        return coste;
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

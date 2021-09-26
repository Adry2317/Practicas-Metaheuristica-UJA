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
        System.out.println(config.getArchivos());
        ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(1));
        System.out.println(archivo.getNombreArchivo());


        Greedy greedy  = new Greedy(archivo.getMatrizFlujo(), archivo.getMatrizDistancias(), archivo.getNombreArchivo());
        
        int[] vectorPermutaciones;
        vectorPermutaciones = greedy.calculoGreedy();

        
        long coste = greedy.calculaCoste(vectorPermutaciones);

        System.out.println("");
        for (int i : vectorPermutaciones) {
            System.out.print(i + " ");
        }

        System.out.println("Coste: " + coste);
        
        guardarArchivo("log/"+config.getAlgoritmos().get(0)+"_"+archivo.getNombreArchivo()+".txt", greedy.getLog());
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
                if(null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
            }
        }
        
    }

}

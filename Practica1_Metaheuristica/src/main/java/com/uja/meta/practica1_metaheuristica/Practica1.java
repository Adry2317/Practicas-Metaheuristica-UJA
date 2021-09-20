/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uja.meta.practica1_metaheuristica;

import com.sun.org.apache.xerces.internal.xs.XSConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javafx.util.Pair;

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
        ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(0));
        System.out.println(archivo.getNombreArchivo());

        //Matriz de flujo
        for (int i = 0; i < archivo.getMatrizFlujo().length; i++) {
            System.out.println(Arrays.toString(archivo.getMatrizFlujo()[i]));
        }

        System.out.println("");

        //Matriz de distancia.
        for (int i = 0; i < archivo.getMatrizDistancias().length; i++) {
            System.out.println(Arrays.toString(archivo.getMatrizDistancias()[i]));
        }

        int[] prueba;
        prueba = Greedy.calculoGreedy(archivo.getMatrizFlujo(), archivo.getMatrizDistancias());
        
        
        System.out.println("");
        for (int i : prueba) {
            System.out.print(i+" ");
        }

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uja.meta.practica1_metaheuristica;

import java.util.ArrayList;
import java.util.Comparator;
import javafx.util.Pair;

/**
 *
 * @author Adrian Arboledas
 */
public class Greedy {

    private StringBuilder log;
    private final ArrayList<Pair<Integer, Integer>> sumFlujo;
    private final ArrayList<Pair<Integer, Integer>> sumDistancia;
    private final int [][] matrizFlujo;
    private final int [][] matrizDistancia;
    private final String nombreArchivo;

    public Greedy(int [][] _matrizFlujo, int [][] _matrizDistancia, String _nombreArchivo) {
        this.log = new StringBuilder();
        this.sumFlujo = new ArrayList<>();
        this.sumDistancia = new ArrayList<>();
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.nombreArchivo = _nombreArchivo;
    }

    
    
    public int[] calculoGreedy() {

        int[] vectSolucion = new int[matrizFlujo.length];
        
        log.append("Ejecuión algoritmo Greedy, para el fichero de datos "+nombreArchivo+".\n");
        long tiempoInicial = System.currentTimeMillis();
        
        for (int i = 0; i < matrizFlujo.length; i++) {
            int sumFilaFlujo = 0;
            int sumFilaDist = 0;

            for (int j = 0; j < matrizFlujo.length; j++) {
                sumFilaFlujo += matrizFlujo[i][j];
                sumFilaDist += matrizDistancia[i][j];

            }
            Pair<Integer, Integer> parFlujo = new Pair<>(i, sumFilaFlujo);
            Pair<Integer, Integer> parDistancia = new Pair<>(i, sumFilaDist);
            sumFlujo.add(parFlujo);
            sumDistancia.add(parDistancia);

        }
        sumFlujo.sort((Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) -> o1.getValue().compareTo(o2.getValue()));
        sumDistancia.sort((Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) -> o1.getValue().compareTo(o2.getValue()));


        for (int i = 0; i < sumDistancia.size(); i++) {
            vectSolucion[sumFlujo.get(sumFlujo.size() - 1 - i).getKey()] = sumDistancia.get(i).getKey();
        }
        
        log.append("El tiempo total de ejecución algoritmo greedy:  ").append(System.currentTimeMillis() - tiempoInicial).append(" milisegundos.\n");
        
        return vectSolucion;
    }

    
    public long calculaCoste(int[] vectorSolucion) {
        log.append("Calculo del coste de la solución para el archivo de datos "+nombreArchivo+".\n");
        long coste = 0;
        long timeIni = System.currentTimeMillis();
        
        for (int i = 0; i < vectorSolucion.length; i++) {
            for (int j = 0; j < vectorSolucion.length; j++) {
                if (i != j) {
                    coste = coste + (matrizFlujo[i][j] * matrizDistancia[vectorSolucion[i]][vectorSolucion[j]]);
                }
            }
        }
        
        log.append("El coste de la solución: "+coste+"\n");
        
        
        log.append("El tiempo necesario para calcular el coste ha sidos: "+(System.currentTimeMillis()-timeIni)+" milisegundos.\n");
        return coste;
    }

    public String getLog() {
        return log.toString();
    }
    
    
    
}

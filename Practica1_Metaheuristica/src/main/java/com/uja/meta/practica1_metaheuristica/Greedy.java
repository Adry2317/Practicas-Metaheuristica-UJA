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

    public static int[] calculoGreedy(int[][] matrizFlujo, int[][] matrizDistancia) {

        int[] vectSolucion = new int[matrizFlujo.length];
        ArrayList<Pair<Integer, Integer>> sumFlujo = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> sumDistancia = new ArrayList<>();

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
        

        
        System.out.println("Vista de vectores prueba");
        for (Pair<Integer, Integer> pair : sumFlujo) {
            System.out.print(" indice: "+pair.getKey()+" valor: "+pair.getValue());
        }
        System.out.println("");
        
        System.out.println("Distancia");

        for (Pair<Integer, Integer> pair : sumDistancia) {
            System.out.print(" indice: "+pair.getKey()+" valor: "+pair.getValue());
        }
        
        
        System.out.println("************************************************");
        
        for (int i = 0; i < sumDistancia.size(); i++) {
            vectSolucion[sumFlujo.get(sumFlujo.size()-1-i).getKey()] = sumDistancia.get(i).getKey();
        }
        
        
        return vectSolucion;
    }
    
    public static long calculaCoste(int[] vectorSolucion, int[][] matrizFlujo, int[][] matrizDistancia){
        long coste = 0;
        
        for(int i = 0; i < vectorSolucion.length; i++){
            for(int j = 0; j < vectorSolucion.length; j++){
                if(i != j){
                    coste = coste + (matrizFlujo[i][j] * matrizDistancia[vectorSolucion[i]][vectorSolucion[j]]);
                }
            }
        }
        
        return coste;
    }
}

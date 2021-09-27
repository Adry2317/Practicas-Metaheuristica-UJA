package com.uja.meta.practica1_metaheuristica;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class PMDLBit {
    private StringBuilder log;
    private final int [][] matrizFlujo;
    private final int [][] matrizDistancia;
    private static int limIteraciones;
    private int[] dlb;

    public PMDLBit(int [][] _matrizFlujo, int [][] _matrizDistancia){
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.log = new StringBuilder();
        this.limIteraciones = 1000;
        this.dlb = new int[matrizFlujo.length];
        for(int i = 0; i < matrizFlujo.length; i++){
            dlb[i] = 0;
        }
    }

    public int[] dlbIterativa( int[] solucionActual){
        int iteraciones = 0;
        int[] resultado = solucionActual;

        while(iteraciones < limIteraciones && !compruebaDLB()){
           // System.out.println(iteraciones);
            boolean mejora_solucion = false;
            for(int i = 0; i < matrizFlujo.length || mejora_solucion; i++){
                if(dlb[i] == 0){
                    for(int j = 0; j < matrizFlujo.length || mejora_solucion; j++){
                        if(checkMove(i,j)){
                            resultado = aplicarMovimiento(i,j,resultado);
                            dlb[i] = dlb[j] = 0;
                            mejora_solucion = true;
                            iteraciones++;
                        }
                    }
                    if(!mejora_solucion){

                        dlb[i] = 1;
                    }
                }
            }

        }

        if(resultado == null)
            return resultado;

        return solucionActual;
    }





    public int[] aplicarMovimiento(int i, int j, int[] vectorPermutacion){
        int aux = vectorPermutacion[i];
        vectorPermutacion[i] = vectorPermutacion[j];
        vectorPermutacion[j] = aux;

        return vectorPermutacion;
    }

    /**
     * Función que comprueba si una solución es mejor que la actual
     * @param pos1: posición 1 a intercambiar con la 2
     * @param pos2: posición 2 a intercambiar con la 1
     * @return: true si la solución es mejor, false en caso contrario.
     */
    public boolean checkMove(int pos1, int pos2){

        int sumatorio = 0;
        for(int i = 0; i < matrizFlujo.length; i++){
           int parteA = (matrizFlujo[pos1][i] * (matrizDistancia[pos2][i] - matrizDistancia[pos1][i])) +
                    (matrizFlujo[pos2][i] * (matrizDistancia[pos1][i] - matrizDistancia[pos2][i]));

            int parteB = (matrizFlujo[i][pos1] * (matrizDistancia[i][pos2] - matrizDistancia[i][pos1])) +
                    (matrizFlujo[i][pos2] * (matrizDistancia[i][pos1] - matrizDistancia[i][pos2]));


            sumatorio +=(parteA + parteB);
        }

        if(sumatorio < 0){
            return true;
        }

        return false;

    }


    /**
     * Función que comprueba si el dlb esta completo a 1.
     * @return true si el dlb esta completo a 1, false si no lo esta
     */
    public boolean compruebaDLB(){
        int cont = 0;

        for (int i = 0; i<dlb.length; i++){
            cont += dlb[i];
        }

        if(cont == dlb.length)
            return true;

        return false;

    }


}

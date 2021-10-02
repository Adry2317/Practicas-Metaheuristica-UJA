package com.uja.meta.practica1_metaheuristica;

import javafx.util.Pair;
import sun.rmi.runtime.Log;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Random;

public class AlgPMDLBit_Clase2_Grupo1 {
    private StringBuilder log;
    private final int [][] matrizFlujo;
    private final int [][] matrizDistancia;
    private static int limIteraciones;
    private int[] dlb;
    private final String nombreArchivo;

    public AlgPMDLBit_Clase2_Grupo1(int [][] _matrizFlujo, int [][] _matrizDistancia,String _nombreArchivo){
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.log = new StringBuilder();
        this.limIteraciones = 1000;
        this.nombreArchivo = _nombreArchivo;
        this.dlb = new int[matrizFlujo.length];
        for(int i = 0; i < matrizFlujo.length; i++){
            dlb[i] = 0;
        }
    }

    public int[] dlbIterativa( int[] solucionActual){

        log.append("Ejecución Algortimo primero el mejor iterativo, para el fichero de datos "+nombreArchivo+".\n");
        long timeIni = System.currentTimeMillis();
        int iteraciones = 0;
        int[] resultado = new int[solucionActual.length];

        for (int i = 0; i < solucionActual.length; i++) {
            resultado[i] = solucionActual[i];
        }

        boolean mejora_solucion = false;

        while(iteraciones < limIteraciones && !compruebaDLB()){

             mejora_solucion = false;
            for (int i = 0; i < matrizFlujo.length && !mejora_solucion; i++){
                if (dlb[i] == 0){ //movimientos considerados para que puedan ser considerados para explorar entornos nuevos.
                    mejora_solucion = false;

                    for (int j = 0; j < matrizFlujo.length&& !mejora_solucion; j++) {
                        //comprobamos el moviemiento y si es mejor nos lo quedamos
                        if (checkMove(i,j,resultado)){
                             aplicarMovimiento(i,j,resultado); //aplicamos el movimiento
                            dlb[i] = dlb[j] = 0; //se pone a 0 porque está implicado en un movimiento de mejora.
                            mejora_solucion = true;
                            iteraciones++;
                        }
                    }

                    //si no se ha encontrado solucion mejor partiendo de esa posición actualizamos dlb
                    if(!mejora_solucion){
                        dlb[i] = 1;
                    }
                }
            }
            timeIni = System.currentTimeMillis() - timeIni;
            log.append("\nIteración: "+iteraciones+"\n");

            log.append("Vector permutaciones: ");
            for(int i = 0; i < matrizFlujo.length; i++){
                log.append(resultado[i]+" ");
            }
            log.append("\n");

            log.append("DLBIterativo: ");
            for (int i = 0; i < matrizFlujo.length; i++) {
                log.append(dlb[i]+" ");
            }
            log.append("\n");



        }
        log.append("\nTiempo total de ejecucion algoritmo Busqueda Local iterativa: " + (timeIni) + " milisegundos");
        return resultado;
    }





    public void aplicarMovimiento(int i, int j, int[] vectorPermutacion){
        int aux = vectorPermutacion[i];
        vectorPermutacion[i] = vectorPermutacion[j];
        vectorPermutacion[j] = aux;


    }

    /**
     * Función que comprueba si una solución es mejor que la actual
     * @param r: posición 1 a intercambiar con la 2
     * @param s: posición 2 a intercambiar con la 1
     * @return: true si la solución es mejor, false en caso contrario.
     */
    public boolean checkMove(int r, int s,int[] vectorPerm){

        int sumatorio = 0;


        for(int k = 0; k < matrizFlujo.length; k++){
            if(k != r && k != s) {
                sumatorio += ((matrizFlujo[r][k]*(matrizDistancia[vectorPerm[s]][vectorPerm[k]] - matrizDistancia[vectorPerm[r]][vectorPerm[k]])) +
                        (matrizFlujo[s][k]* (matrizDistancia[vectorPerm[r]][vectorPerm[k]] - matrizDistancia[vectorPerm[s]][vectorPerm[k]]))) +

                        ((matrizFlujo[k][r]*(matrizDistancia[vectorPerm[k]][vectorPerm[s]] - matrizDistancia[vectorPerm[k]][vectorPerm[r]])) +
                                (matrizFlujo[k][s]* (matrizDistancia[vectorPerm[k]][vectorPerm[r]] - matrizDistancia[vectorPerm[k]][vectorPerm[s]])));




            }
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

    public String getLog() {
        return log.toString();
    }
}

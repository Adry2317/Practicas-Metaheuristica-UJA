/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uja.meta.practica1_metaheuristica;

import java.util.ArrayList;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author joseantonio
 */
public class PrimeroMejor {
    
    private StringBuilder log;
    private final ArrayList<Pair<Integer, Integer>> sumFlujo;
    private final ArrayList<Pair<Integer, Integer>> sumDistancia;
    private final int [][] matrizFlujo;
    private final int [][] matrizDistancia;
    private final String nombreArchivo;
    private final int[] vectorPermutaciones;
    private Random aleatorio;
    
    public PrimeroMejor(int [][] _matrizFlujo, int [][] _matrizDistancia, String _nombreArchivo, int [] _vectorPermutaciones,Random _aleatorio){
        this.sumFlujo = new ArrayList<>();
        this.sumDistancia = new ArrayList<>();
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.aleatorio = _aleatorio;
        this.nombreArchivo = _nombreArchivo;
        this.vectorPermutaciones = _vectorPermutaciones;
        this.log = new StringBuilder();
    }




//    public int[] calculaBusquedaLocal(){
//        int[] copiaPermutaciones = vectorPermutaciones;
//
//        int posVector1;
//        int posVector2;
//
//        do{
//            posVector1 = aleatorio.nextInt(vectorPermutaciones.length);
//            posVector2 = aleatorio.nextInt(vectorPermutaciones.length);
//
//        }while (posVector1 == posVector2);
//
//
//        int[] dlb = new int[vectorPermutaciones.length];
//
//        for(int i = 0; i < vectorPermutaciones.length; i++){
//            dlb[i] = 0;
//        }
//
//        boolean mejoraSolucion = false;
//        for(int i = 0; i < vectorPermutaciones.length; i++){
//            if(dlb[i] == 0){
//                mejoraSolucion = false;
//
//                for(int j= 0; j < vectorPermutaciones.length; j++){
//                    if(checkMove(copiaPermutaciones, posVector1, posVector2)){
//
//                    }
//                }
//            }
//        }
//
//
//
//
//
//    }
    
    
    public boolean checkMove(int[] copiaPermutacion, int pos1, int pos2){
        
        int aux = copiaPermutacion[pos1];
        copiaPermutacion[pos1] = copiaPermutacion[pos2];
        copiaPermutacion[pos2] = aux;
        
        long costeCopia = calculaCoste(copiaPermutacion);
        long costeOriginal = calculaCoste(vectorPermutaciones);
        
        if((costeCopia - costeOriginal) < 0){
            return true;
        }
        
        return false;
        
    }
    
    public long calculaCoste(int[] vector){
        long coste = 0;
        
        
        for (int i = 0; i < vector.length; i++) {
            for (int j = 0; j < vector.length; j++) {
                if (i != j) {
                    coste = coste + (matrizFlujo[i][j] * matrizDistancia[vector[i]][vector[j]]);
                }
            }
        }
        
        return coste;
    }
    
}

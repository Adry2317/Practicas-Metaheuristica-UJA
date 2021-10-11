package com.uja.meta.practica1_metaheuristica;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class AlgGRE_Clase2_Grupo1 {

    private StringBuilder log;
    private final ArrayList<Pair<Integer, Integer>> sumFlujo;
    private final ArrayList<Pair<Integer, Integer>> sumDistancia;
    ArrayList<Pair<Integer,Integer>>sumaFlujoAleatorio;
    ArrayList<Pair<Integer,Integer>> sumaDistanciaAleatorio;
    private final int [][] matrizFlujo;
    private final int [][] matrizDistancia;
    private final String nombreArchivo;

    public AlgGRE_Clase2_Grupo1(int [][] _matrizFlujo, int [][] _matrizDistancia, String _nombreArchivo) {
        this.log = new StringBuilder();
        this.sumFlujo = new ArrayList<>();
        this.sumDistancia = new ArrayList<>();
        this.sumaDistanciaAleatorio = new ArrayList<>();
        this.sumaFlujoAleatorio = new ArrayList<>();
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



    public ArrayList<Pair<Integer,Integer>> GreedyAleatorio(){
        ArrayList<Pair<Integer,Integer>>listaCandidatos = new ArrayList<>(); //PAsar a parametros una vez temrinado


        for (int i = 0; i < matrizFlujo.length; i++) {
            int sumFilaFlujo = 0;
            int sumFilaDist = 0;

            for (int j = 0; j < matrizFlujo.length; j++) {
                sumFilaFlujo += matrizFlujo[i][j];
                sumFilaDist += matrizDistancia[i][j];

            }
            Pair<Integer, Integer> parFlujo = new Pair<>(i, sumFilaFlujo);
            Pair<Integer, Integer> parDistancia = new Pair<>(i, sumFilaDist);
            sumaFlujoAleatorio.add(parFlujo);
            sumaDistanciaAleatorio.add(parDistancia);

        }
        sumaFlujoAleatorio.sort((Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) -> o1.getValue().compareTo(o2.getValue()));
        sumaDistanciaAleatorio.sort((Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) -> o1.getValue().compareTo(o2.getValue()));

        ArrayList<Integer> cincoMejoresFlujos = new ArrayList<>();
        ArrayList<Integer> cincoMEjoresDist = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            cincoMEjoresDist.add(sumaDistanciaAleatorio.get(i).getKey());
            cincoMejoresFlujos.add(sumaFlujoAleatorio.get(sumaFlujoAleatorio.size() - 1 - i).getKey());
        }
        Random aleatorio = new Random(System.currentTimeMillis()); //semilla aleatoria.

        while(listaCandidatos.size() < 10){

            int n1 = aleatorio.nextInt(5);
            int n2 = aleatorio.nextInt(5);
            Pair<Integer,Integer> nuevo = new Pair<>(cincoMejoresFlujos.get(n1),cincoMEjoresDist.get(n2));
            boolean repetido = false;

            //comprobacion de que no estan en la lista
            for (int i = 0; i < listaCandidatos.size() && !repetido; i++){
                if(listaCandidatos.get(i).getKey() == nuevo.getKey() && listaCandidatos.get(i).getValue() == nuevo.getValue()){
                    repetido = true;
                }
            }

            if(!repetido){
                listaCandidatos.add(nuevo);
            }
        }




        return listaCandidatos;
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

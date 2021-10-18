package com.uja.meta.practica1_metaheuristica;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class AlgGRE_Clase2_Grupo1 {

    private StringBuilder log;//Log
    private final ArrayList<Pair<Integer, Integer>> sumFlujo;
    private final ArrayList<Pair<Integer, Integer>> sumDistancia;
    private final int tamListaRestringidaCandidatos;//Tamaño de la lista restringida de candidatos
    private final int mejoresLRC;//Numero de elememntos mejores de flujo y distancias a coger en greedy aleatorio
    ArrayList<Pair<Integer,Integer>>sumaFlujoAleatorio;//Lista de pares con posicion y valor de la sumatoria
    ArrayList<Pair<Integer,Integer>> sumaDistanciaAleatorio;
    private final int [][] matrizFlujo;
    private final int [][] matrizDistancia;
    private final String nombreArchivo;//Nombre de archivos

    public AlgGRE_Clase2_Grupo1(int [][] _matrizFlujo, int [][] _matrizDistancia, String _nombreArchivo, int tamLRC, int _MejoresLRC) {
        this.log = new StringBuilder();
        this.sumFlujo = new ArrayList<>();
        this.sumDistancia = new ArrayList<>();
        this.sumaDistanciaAleatorio = new ArrayList<>();
        this.sumaFlujoAleatorio = new ArrayList<>();
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.nombreArchivo = _nombreArchivo;
        this.tamListaRestringidaCandidatos = tamLRC;
        this.mejoresLRC = _MejoresLRC;
    }


    /**
     * Funcion calculo del greedy
     * @return Vector solucion con indices los flujos y valor las distancias
     * */
    public int[] calculoGreedy() {

        int[] vectSolucion = new int[matrizFlujo.length];

        log.append("Ejecuión algoritmo Greedy, para el fichero de datos "+nombreArchivo+".\n");
        long tiempoInicial = System.currentTimeMillis();

        //Sumatorio de las filas
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

        //ordenacion de menor a mayor de las filas
        sumFlujo.sort((Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) -> o1.getValue().compareTo(o2.getValue()));
        sumDistancia.sort((Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) -> o1.getValue().compareTo(o2.getValue()));

        //Creacion de la solucion con la menor distancia a mayor flujo
        for (int i = 0; i < sumDistancia.size(); i++) {
            vectSolucion[sumFlujo.get(sumFlujo.size() - 1 - i).getKey()] = sumDistancia.get(i).getKey();
        }

        log.append("El tiempo total de ejecución algoritmo greedy:  ").append(System.currentTimeMillis() - tiempoInicial).append(" milisegundos.\n");

        return vectSolucion;
    }


    /**
     * Funcion de Greedy aleatorio, devuelve una lista de candidatos a permutar
     * @return Lista de pares del tamaño de la lista de candidatos con los elementos a intercambiar
     * */
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

        //Genera una solucion
        for (int i = 0; i < 5; i++){
            cincoMEjoresDist.add(sumaDistanciaAleatorio.get(i).getKey());
            cincoMejoresFlujos.add(sumaFlujoAleatorio.get(sumaFlujoAleatorio.size() - 1 - i).getKey());
        }
        Random aleatorio = new Random(System.currentTimeMillis()); //semilla aleatoria.

        //Mientras no se generen todos los candidatos se repite en bucle escogiendo dos pares aleatorios
        while(listaCandidatos.size() < tamListaRestringidaCandidatos){

            int n1 = aleatorio.nextInt(mejoresLRC);
            int n2 = aleatorio.nextInt(mejoresLRC);
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


    /**
     * Funcion que calcula el coste de la solucion pasada
     * @param vectorSolucion: Vector con solucion para calculo del coste
     * @return coste calculado
     * */
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

    /**
     * Getter del Log
     * */
    public String getLog() {
        return log.toString();
    }


}

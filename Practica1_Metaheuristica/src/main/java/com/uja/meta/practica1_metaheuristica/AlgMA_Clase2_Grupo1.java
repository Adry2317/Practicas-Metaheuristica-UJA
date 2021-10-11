package com.uja.meta.practica1_metaheuristica;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AlgMA_Clase2_Grupo1 {

    private List<Pair<Integer,Integer>> listaCircularIntercambios;
    private ArrayList<Pair<Integer,Integer>> listaRestringidaCandidatos;

    private int[][] memoriaLargoPlazo;
    private int[] mejorSolActual;
    private int[] mejorSolGlobal;
    private int [] solucionAnterior;
    private int[] mejorPeores;
    private int [] solucionActual;
    private static double porcentajeIniOscilacion = 0.05;
    private int tamVectorSoluciones;
    private static int limIteraciones = 1000;
    private static int candidatosLRC = 10;
    private Random aleatorio;
    private int[] dlb;
    private int [][] matrizFlujo;
    private int[][] matrizDistancia;
    private final String nombreArchivo;
    private StringBuilder log;

    public AlgMA_Clase2_Grupo1(int tamMemoriaLArgoPlazo, int _tamVectorSoluciones, Random _aleatorio,int[][] _matrizDistancia, int [][] _matrizFlujo, ArrayList<Pair<Integer,Integer>> _LRC, String _nombreArchivo){
        this.listaCircularIntercambios = new LinkedList<>();
        this.listaRestringidaCandidatos = _LRC;
        this.log = new StringBuilder();
        this.memoriaLargoPlazo = new int[tamMemoriaLArgoPlazo][tamMemoriaLArgoPlazo];
        this.mejorSolGlobal = new int[tamVectorSoluciones];
        this.solucionAnterior = new int[tamVectorSoluciones];
        this.mejorSolActual = new int[tamVectorSoluciones];
        this.solucionActual = new int[tamVectorSoluciones];
        this.tamVectorSoluciones = _tamVectorSoluciones;
        this.nombreArchivo = _nombreArchivo;
        this.aleatorio = _aleatorio;
        this.dlb = new int[_tamVectorSoluciones];
        for (int i = 0; i < _tamVectorSoluciones; i++){
            dlb[i] = 0;
            for (int j = 0; j<_tamVectorSoluciones; j++){
                memoriaLargoPlazo[i][j] = 0;
            }
        }


        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
    }


    public int [] algoritmoMultiArranque(int[] solucionGreedy){
        int iteraciones = 0;
        boolean mejora_solucion = false;
        int posI = aleatorio.nextInt(tamVectorSoluciones);
        int contLRC = 0;

        log.append("Ejecución Algortimo multiarranque, para el fichero de datos "+nombreArchivo+".\n");
        long tiempoIni = System.currentTimeMillis();
        log.append("Mejores soluciones");
        log.append("\n");
        do {
            solucionActual = solucionGreedy.clone();
            aplicarMovimiento(listaRestringidaCandidatos.get(contLRC).getKey(),listaRestringidaCandidatos.get(contLRC).getValue(), solucionActual);

            solucionAnterior = solucionActual.clone();
            mejorSolGlobal = solucionActual.clone();
            mejorSolActual = solucionActual.clone();
            mejorPeores = solucionActual.clone();

            for(int i = 0; i < dlb.length; i++){
                dlb[i] = 0;
            }
            long costeMejorSolGlobal = 0;
            long costeMEjorSol = 0;
            long costeSOlActual = 0;

            while(iteraciones < limIteraciones && !compruebaDLB() ) {

                mejora_solucion = false;
                boolean EstalistaTabu;

                int conti = 0;
                int contj = 0;
                if(iteraciones == limIteraciones*porcentajeIniOscilacion ) { //oscilación estratégica
                    solucionActual = mejorPeores.clone();
                    for(int i = 0; i < dlb.length; i++){
                        dlb[i] = 0;
                    }
                }
                for (int i = posI; (conti != tamVectorSoluciones) && !mejora_solucion; i++) {
                    mejora_solucion = false;
                    if (i == tamVectorSoluciones) {
                        i = i % tamVectorSoluciones;
                    }
                    if (dlb[i] == 0) { //movimientos considerados para que puedan ser considerados para explorar entornos nuevos.
                        mejora_solucion = false;

                        for (int j = i + 1; (contj != tamVectorSoluciones) && !mejora_solucion; j++) {
                            EstalistaTabu = false;
                            if (j == tamVectorSoluciones) {
                                j = j % tamVectorSoluciones;
                            }
                            for (int k = 0; k < listaCircularIntercambios.size() && !EstalistaTabu; k++){
                                if(listaCircularIntercambios.get(k).getKey() == i && listaCircularIntercambios.get(k).getValue() == j){
                                    EstalistaTabu = true;
                                }
                            }
                            //comprobamos el moviemiento y si es mejor nos lo quedamos
                            if (checkMove(i, j, solucionActual) && !EstalistaTabu) {
                                solucionAnterior = solucionActual.clone();
                                aplicarMovimiento(i, j, solucionActual); //aplicamos el movimiento
                                dlb[i] = dlb[j] = 0; //se pone a 0 porque está implicado en un movimiento de mejora.
                                posI = i;
                                mejora_solucion = true;
                                costeMEjorSol = calculaCoste(mejorSolActual);
                                costeSOlActual = calculaCoste(solucionActual);

                                if(costeMEjorSol > costeSOlActual){
                                    mejorSolActual = solucionActual.clone();
                                }else if(costeSOlActual < calculaCoste(mejorPeores)){
                                    mejorPeores = solucionActual.clone();
                                }


                                iteraciones++;
                            }


                            contj++;
                        }
                        contj = 0;

                        //si no se ha encontrado solucion mejor partiendo de esa posición actualizamos dlb
                        if (!mejora_solucion) {
                            dlb[i] = 1;
                        }
                    }
                    conti++;
                }

            }


            costeMejorSolGlobal = calculaCoste(mejorSolGlobal);

            if(calculaCoste(solucionActual) < costeMejorSolGlobal){
                mejorSolGlobal = mejorSolActual.clone();
            }


            costeMejorSolGlobal = calculaCoste(mejorSolGlobal);
            log.append("Coste: "+costeMejorSolGlobal + " Solucion: ");
            for (int i = 0; i < mejorSolGlobal.length; i++) {
                log.append(mejorSolGlobal[i]+" ");
            }
            log.append("\n");
            contLRC++;
        }while(contLRC < candidatosLRC);

        log.append("Tiempo de ejecucion: " + (System.currentTimeMillis() - tiempoIni));

        return mejorSolGlobal;
    }

    public void aplicarMovimiento(int i, int j, int[] vectorPermutacion){
        int aux = vectorPermutacion[i];
        vectorPermutacion[i] = vectorPermutacion[j];
        vectorPermutacion[j] = aux;
        memoriaLargoPlazo[i][j]++; //aumentamos en 1 la frecuencia de aparición
        if(listaCircularIntercambios.size() == 3){

            Pair<Integer,Integer> cambio = new Pair<>(i,j);
            listaCircularIntercambios.remove(0);//borramos el primer elemento.
            listaCircularIntercambios.add(cambio); //añadimos al final de la lista
        }else{ //si no esta llena la lista.
            Pair<Integer,Integer> cambio = new Pair<>(i,j);
            listaCircularIntercambios.add(cambio);
        }


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



    public boolean compruebaDLB(){
        int cont = 0;

        for (int i = 0; i<dlb.length; i++){
            cont += dlb[i];
        }



        if(cont == dlb.length)
            return true;

        return false;

    }

    public long calculaCoste(int[] vectorSolucion) {
        //log.append("Calculo del coste de la solución para el archivo de datos "+nombreArchivo+".\n");
        long coste = 0;
        long timeIni = System.currentTimeMillis();

        for (int i = 0; i < vectorSolucion.length; i++) {
            for (int j = 0; j < vectorSolucion.length; j++) {
                if (i != j) {
                    coste = coste + (matrizFlujo[i][j] * matrizDistancia[vectorSolucion[i]][vectorSolucion[j]]);
                }
            }
        }

        //log.append("El coste de la solución: "+coste+"\n");


        //log.append("El tiempo necesario para calcular el coste ha sidos: "+(System.currentTimeMillis()-timeIni)+" milisegundos.\n");
        return coste;
    }

    public String getLog() {
        return log.toString();
    }
}

package com.uja.meta.practica1_metaheuristica;

import javafx.util.Pair;
import sun.rmi.runtime.Log;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Random;

public class AlgPMDLBit_Clase2_Grupo1 {

    private StringBuilder log; //Instancia para el log
    private final int[][] matrizFlujo;//Matriz de flujo
    private final int[][] matrizDistancia;//Matriz de distancias
    private static int limIteraciones;//Limite de iteraciones
    private int[] dlb;//Vector del dlb para intercambios
    private final String nombreArchivo;//Nombre del archivo que esta ejecutando para el log

    public AlgPMDLBit_Clase2_Grupo1(int[][] _matrizFlujo, int[][] _matrizDistancia, String _nombreArchivo, int _limIteraciones) {
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.log = new StringBuilder();
        this.limIteraciones = _limIteraciones;
        this.nombreArchivo = _nombreArchivo;

        //Se inicia el dlb a 0
        this.dlb = new int[matrizFlujo.length];
        for (int i = 0; i < matrizFlujo.length; i++) {
            dlb[i] = 0;
        }
    }

    /**
     * Funcion de DLB iterativa
     *
     * @param solucionActual: Solucion actual del Greedy
     * @return Vector con la solucion calculada
     */
    public int[] dlbIterativa(int[] solucionActual) {

        log.append("Ejecución Algortimo primero el mejor iterativo, para el fichero de datos " + nombreArchivo + ".\n");
        long timeIni = System.currentTimeMillis();

        //Se inician las iteraciones a 0
        int iteraciones = 0;
        //Se copia la solucion actual
        int[] resultado;

        resultado = solucionActual.clone();

        //Se inicializan los indices para recorrer la solucion
        boolean mejora_solucion = false;
        int iPos = 0;
        int conti = 0;
        int contj = 0;

        //Bucle del numero de iteraciones limite
        while (iteraciones < limIteraciones && !compruebaDLB()) {

            mejora_solucion = false;
            for (int i = iPos; conti != matrizFlujo.length && !mejora_solucion; i++) {
                if (i == matrizFlujo.length) {
                    i = i % matrizFlujo.length;
                }
                if (dlb[i] == 0) { //movimientos que puedan ser considerados para explorar entornos nuevos.
                    mejora_solucion = false;

                    for (int j = i + 1; contj != matrizFlujo.length && !mejora_solucion; j++) {
                        if (j == matrizFlujo.length) {
                            j = j % matrizFlujo.length;
                        }
                        //comprobamos el moviemiento y si es mejor nos lo quedamos
                        if (checkMove(i, j, resultado)) {
                            aplicarMovimiento(i, j, resultado); //aplicamos el movimiento
                            dlb[i] = dlb[j] = 0; //se pone a 0 porque está implicado en un movimiento de mejora.
                            iPos = i;
                            mejora_solucion = true;
                            log.append("\nMejora en iteracion: " + iteraciones + "\n");

                            log.append("Vector permutaciones: ");
                            for (int k = 0; k < matrizFlujo.length; k++) {
                                log.append(resultado[k] + " ");
                            }
                            log.append("Coste: " + calculaCoste(resultado));
                            log.append("\n");
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
            conti = 0;




        }
        timeIni = System.currentTimeMillis() - timeIni;
        log.append("\n");
        log.append("\nTiempo total de ejecucion algoritmo Busqueda Local iterativa: " + (timeIni) + " milisegundos");
        log.append("El coste es: ").append(calculaCoste(resultado));
        return resultado;
    }


    /**
     * Aplica un movimiento intercambiando en el vector
     *
     * @param i:                 Posicion 1 a intercambiar
     * @param j:                 Posicion 2 a intercambiar
     * @param vectorPermutacion: Vector con la solucion actual a intercambiar
     */
    public void aplicarMovimiento(int i, int j, int[] vectorPermutacion) {
        int aux = vectorPermutacion[i];
        vectorPermutacion[i] = vectorPermutacion[j];
        vectorPermutacion[j] = aux;


    }

    /**
     * Función que comprueba si una solución es mejor que la actual
     *
     * @param r: posición 1 a intercambiar con la 2
     * @param s: posición 2 a intercambiar con la 1
     * @return: true si la solución es mejor, false en caso contrario.
     */
    public boolean checkMove(int r, int s, int[] vectorPerm) {

        int sumatorio = 0;


        for (int k = 0; k < matrizFlujo.length; k++) {
            if (k != r && k != s) {
                sumatorio += ((matrizFlujo[r][k] * (matrizDistancia[vectorPerm[s]][vectorPerm[k]] - matrizDistancia[vectorPerm[r]][vectorPerm[k]])) +
                        (matrizFlujo[s][k] * (matrizDistancia[vectorPerm[r]][vectorPerm[k]] - matrizDistancia[vectorPerm[s]][vectorPerm[k]]))) +

                        ((matrizFlujo[k][r] * (matrizDistancia[vectorPerm[k]][vectorPerm[s]] - matrizDistancia[vectorPerm[k]][vectorPerm[r]])) +
                                (matrizFlujo[k][s] * (matrizDistancia[vectorPerm[k]][vectorPerm[r]] - matrizDistancia[vectorPerm[k]][vectorPerm[s]])));


            }
        }


        if (sumatorio < 0) {
            return true;
        }

        return false;

    }


    /**
     * Función que comprueba si el dlb esta completo a 1.
     *
     * @return true si el dlb esta completo a 1, false si no lo esta
     */
    public boolean compruebaDLB() {
        int cont = 0;

        for (int i = 0; i < dlb.length; i++) {
            cont += dlb[i];
        }


        if (cont == dlb.length)
            return true;

        return false;

    }

    public long calculaCoste(int[] vectorSolucion) {

        long coste = 0;
        long timeIni = System.currentTimeMillis();

        for (int i = 0; i < vectorSolucion.length; i++) {
            for (int j = 0; j < vectorSolucion.length; j++) {
                if (i != j) {
                    coste = coste + (matrizFlujo[i][j] * matrizDistancia[vectorSolucion[i]][vectorSolucion[j]]);
                }
            }
        }


        return coste;
    }

    /**
     * Getter de log
     */
    public String getLog() {
        return log.toString();
    }
}

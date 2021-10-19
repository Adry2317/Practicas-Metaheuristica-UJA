package com.uja.meta.practica1_metaheuristica;

import java.util.Random;

public class AlgPMDLBrandom_Clase2_Grupo1 {
    private StringBuilder log;
    private final int[][] matrizFlujo;
    private final int[][] matrizDistancia;
    private static int limIteraciones;
    private int[] dlb;
    private final String nombreArchivo;
    private final Random aleatorio;

    public AlgPMDLBrandom_Clase2_Grupo1(int[][] _matrizFlujo, int[][] _matrizDistancia, Random _aleatorio, String _nombreArchivo, int _limIteraciones) {
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.log = new StringBuilder();
        this.limIteraciones = _limIteraciones;
        this.aleatorio = _aleatorio;
        this.nombreArchivo = _nombreArchivo;
        this.dlb = new int[matrizFlujo.length];
        for (int i = 0; i < matrizFlujo.length; i++) {
            dlb[i] = 0;
        }
    }

    public int[] dlbRandom(int[] solucionActual) {
        int iteraciones = 0;
        int[] resultado;

        log.append("Ejecución Algortimo primero el mejor aleatorio, para el fichero de datos " + nombreArchivo + ".\n");
        long timeIni = System.currentTimeMillis();

        resultado = solucionActual.clone();

        boolean mejora_solucion = false; //boleano que controla si la solucion ha mejorado o no.
        int posI = aleatorio.nextInt(matrizFlujo.length); //inicializamos la posición del bucle "i" a una posición aleatoria del vector de permutacines.
        while (iteraciones < limIteraciones && !compruebaDLB()) {
            mejora_solucion = false;

            int conti = 0;
            int contj = 0;

            for (int i = posI; (conti != matrizFlujo.length) && !mejora_solucion; i++) {
                if (i == matrizFlujo.length) { //si hemos llegado al final realizamos el modulo, para asi recorrer  el vector de permutaciones.
                    i = i % matrizFlujo.length;
                }
                if (dlb[i] == 0) { //movimientos que puedan ser considerados para explorar entornos nuevos.
                    mejora_solucion = false;

                    for (int j = i + 1; (contj != matrizFlujo.length) && !mejora_solucion; j++) {
                        if (j == matrizFlujo.length) {
                            j = j % matrizFlujo.length;
                        }
                        //comprobamos el moviemiento y si es mejor nos lo quedamos
                        if (checkMove(i, j, resultado)) {
                            aplicarMovimiento(i, j, resultado); //aplicamos el movimiento
                            dlb[i] = dlb[j] = 0; //se pone a 0 porque está implicado en un movimiento de mejora.
                            posI = i;
                            mejora_solucion = true;
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

            timeIni = System.currentTimeMillis() - timeIni;
            log.append("\nIteración: " + iteraciones + "\n");

            log.append("Vector permutaciones: ");
            for (int i = 0; i < matrizFlujo.length; i++) {
                log.append(resultado[i] + " ");
            }
            log.append("\n");

            log.append("DLBRandom: ");
            for (int i = 0; i < matrizFlujo.length; i++) {
                log.append(dlb[i] + " ");
            }
            log.append("\n");


        }
        log.append("Tiempo total de ejecucion algoritmo Busqueda Local aleatoria: " + (timeIni) + " milisegundos");
        return resultado;
    }


    /**
     * Función encargada de aplicar un movimiento en el vector de permutaciones.
     *
     * @param i                 posicion 1
     * @param j                 posicion 2
     * @param vectorPermutacion vector de permutaciones al cual se le va a aplicar el movimiento.
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

    /**
     * Getter de log
     */
    public String getLog() {
        return log.toString();
    }
}

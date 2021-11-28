

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class AlgGRE_Clase2_Grupo1 {

    private StringBuilder log;//Log
    private final ArrayList<Pair<Integer, Integer>> sumFlujo;
    private final ArrayList<Pair<Integer, Integer>> sumDistancia;
    private final int tamListaRestringidaCandidatos;//Tamaño de la lista restringida de candidatos
    ArrayList<Pair<Integer, Integer>> sumaFlujoAleatorio;//Lista de pares con posicion y valor de la sumatoria
    ArrayList<Pair<Integer, Integer>> sumaDistanciaAleatorio;
    private final int[][] matrizFlujo;
    private final int[][] matrizDistancia;
    private final int tamPoblacion;
    private Random aleatorio;
    ArrayList<int[]> listaLRC;

    public AlgGRE_Clase2_Grupo1(int[][] _matrizFlujo, int[][] _matrizDistancia, int tamLRC,int tamPoblacion, Random aleatorio) {
        this.log = new StringBuilder();
        this.sumFlujo = new ArrayList<>();
        this.sumDistancia = new ArrayList<>();
        this.sumaDistanciaAleatorio = new ArrayList<>();
        this.sumaFlujoAleatorio = new ArrayList<>();
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.tamListaRestringidaCandidatos = tamLRC;
        this.tamPoblacion = tamPoblacion;
        this.aleatorio = aleatorio;
        this.listaLRC = new ArrayList<>();
    }


    /**
     * Funcion de Greedy aleatorio, devuelve una lista de candidatos a permutar
     *
     * @return Lista de pares del tamaño de la lista de candidatos con los elementos a intercambiar
     */
    public ArrayList<int[]> calculoGreedyAleatorio() {
        int [] vectSolucion = new int [matrizFlujo.length];
        ArrayList<Pair<Integer, Integer>> listaCandidatos = new ArrayList<>(); //PAsar a parametros una vez temrinado


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

        //Creacion de la solucion con la menor distancia a mayor flujo
        for (int i = 0; i < sumaDistanciaAleatorio.size(); i++) {
            vectSolucion[sumaFlujoAleatorio.get(sumaFlujoAleatorio.size() - 1 - i).getKey()] = sumaDistanciaAleatorio.get(i).getKey();
        }


        ArrayList<Integer> cincoMejoresFlujos = new ArrayList<>();
        ArrayList<Integer> cincoMEjoresDist = new ArrayList<>();

        //Genera una solucion
        for (int i = 0; i < 5; i++) {
            cincoMEjoresDist.add(sumaDistanciaAleatorio.get(i).getKey());
            cincoMejoresFlujos.add(sumaFlujoAleatorio.get(sumaFlujoAleatorio.size() - 1 - i).getKey());
        }


        //Mientras no se generen todos los candidatos se repite en bucle escogiendo dos pares aleatorios
        while (listaLRC.size() < tamListaRestringidaCandidatos) {

            int n1 = aleatorio.nextInt(tamListaRestringidaCandidatos);
            int n2 = aleatorio.nextInt(tamListaRestringidaCandidatos);
            Pair<Integer, Integer> nuevo = new Pair<>(cincoMejoresFlujos.get(n1), cincoMEjoresDist.get(n2));


                listaCandidatos.add(nuevo);
                aplicarMovimiento(listaCandidatos.get(listaCandidatos.size()-1).getKey(),listaCandidatos.get(listaCandidatos.size()-1).getValue(),vectSolucion);

        }



        //hacemos las 45 soluciones aleatorias.
        while(listaLRC.size() < tamPoblacion){

            int[] nuevo = new int[matrizFlujo.length];

            for (int i = 0; i < matrizFlujo.length; i++) { //lo inicializamos a -1, para poder meter el valaor 0
                nuevo[i] = -1;
            }
            int contTam = 0;

            while(contTam != nuevo.length){//Mientras que no este lleno el hijo
                int n1 = aleatorio.nextInt(matrizFlujo.length);
                boolean esta = false;

                for (int i = 0; i <matrizFlujo.length && !esta; i++) { //comprobamos que no este en el array
                    if(nuevo[i] == n1){
                        esta = true;
                    }
                }
                if(!esta){ //si no está en el array
                   nuevo[contTam] = n1;
                   contTam++;

                }
            }

            listaLRC.add(nuevo);

        }


        return listaLRC;
    }


    /**
     * Aplica un movimiento intercambiando en el vector
     *
     * @param i:                 Posicion 1 a intercambiar
     * @param j:                 Posicion 2 a intercambiar
     * @param vectorPermutacion: Vector con la solucion actual a intercambiar
     */
    public void aplicarMovimiento(int i, int j, int[] vectorPermutacion) {
        int [] vectorPermutacionCopia = vectorPermutacion.clone();
        int aux = vectorPermutacionCopia[i];
        vectorPermutacionCopia[i] = vectorPermutacionCopia[j];
        vectorPermutacionCopia[j] = aux;

        listaLRC.add(vectorPermutacionCopia);


    }




    /**
     * Funcion que calcula el coste de la solucion pasada
     *
     * @param vectorSolucion: Vector con solucion para calculo del coste
     * @return coste calculado
     */
    public long calculaCoste(int[] vectorSolucion) {

        long coste = 0;


        for (int i = 0; i < vectorSolucion.length; i++) {
            for (int j = 0; j < vectorSolucion.length; j++) {
                if (i != j) {
                    coste = coste + (matrizFlujo[i][j] * matrizDistancia[vectorSolucion[i]][vectorSolucion[j]]);
                }
            }
        }




        return coste;
    }



}

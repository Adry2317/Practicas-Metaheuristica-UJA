import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class AGE_Clase2_Grupo2 {


    private ArrayList<int[]> poblacion;
    private final int maxEvaluaciones;
    private final int probCruceEstacionario;
    private final double probMutacion;
    public final int tamPoblacion;
    public Random aleatorio;
    public final int tamTorneoSeleccion;
    public final int tamTorneoRemplazamiento;
    public final int[][] matrizFlujo;
    public final int[][] matrizDistancia;
    public final int numindividuosEstacionario;
    public final int numRondasTorneo;
    private StringBuilder log;
    private StringBuilder log2;

    public AGE_Clase2_Grupo2(ArrayList<int[]> _poblacion, int _maxEvaluaciones, int _probCruceEstacionario, double _probMutacion, Random _aleatorio, int _tamTorneoSeleccion, int _tamTorneoReemplazamiento, int[][] _matrizFlujo, int[][] _matrizDistancia, int numIndividuosEstacionario, int numRondastorneo) {

        this.poblacion = _poblacion;
        this.maxEvaluaciones = _maxEvaluaciones;
        this.probCruceEstacionario = _probCruceEstacionario;
        this.probMutacion = _probMutacion;
        this.tamPoblacion =  this.poblacion.size();
        this.aleatorio = _aleatorio;
        this.tamTorneoSeleccion = _tamTorneoSeleccion;
        this.tamTorneoRemplazamiento = _tamTorneoReemplazamiento;
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.numindividuosEstacionario = numIndividuosEstacionario;
        this.numRondasTorneo = numRondastorneo;
        this.log = new StringBuilder();
        this.log2 = new StringBuilder();

    }

    /**
     * @brief Funcion que implementa un genetico con el operador OX
     * @return Lista de individuos de la poblacion solucion
     */
    public ArrayList<int[]> estacionarioOX() {
        int contEvaluaciones = 0;

        long tInicial = System.currentTimeMillis();

        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        //Se evalua la poblacion inicial
        ArrayList<Pair<Integer,Long>> evalucionPoblacion = evalPoblacion(poblacionActual);
        log.append("Poblacion inicial: \n");
        añadirPoblacionOX(poblacionActual);

        contEvaluaciones += tamPoblacion;

        while (contEvaluaciones!=maxEvaluaciones){


            //funcion seleccion
            ArrayList<int[]> padres = seleccionTorneo(poblacionActual);



            //funcion cruce
            ArrayList<int[]> cruce = operadorCruceOX(padres);


            
            //funcion mutacion
            for (int[] hijos: cruce) {
                mutacion(hijos);
            }

            //log.append("Hijos evaluacion " + contEvaluaciones +":");
            //añadirPoblacionOX(cruce);

            //funcion reemplazamiento
            reemplazamiento(cruce, poblacionActual, evalucionPoblacion);


            contEvaluaciones+=numindividuosEstacionario;
        }


        log.append("\n + Tiempo de ejecuccion: " + (System.currentTimeMillis() - tInicial));

        return poblacionActual;
    }

    /**
     * Funcion que implementa el operador PMX
     * @return Lista con la poblacion solucion
     */
    public ArrayList<int[]> estacionarioPMX() {
        int contEvaluaciones = 0;

        long tInicial = System.currentTimeMillis();

        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        ArrayList<Pair<Integer,Long>> evalucionPoblacion = evalPoblacion(poblacionActual);

        log2.append("Poblacion inicial: \n");
        añadirPoblacionPMX(poblacionActual);

        contEvaluaciones += tamPoblacion;

        while (contEvaluaciones!=maxEvaluaciones){


            //funcion seleccion
            ArrayList<int[]> padres = seleccionTorneo(poblacionActual);



            //funcion cruce
            ArrayList<int[]> cruce = operadorPMX(padres);



            //funcion mutacion

            for (int[] hijos: cruce) {
                //funcion mutacion
                mutacion(hijos);

            }

            //log2.append("Hijos evaluacion " + contEvaluaciones +":");
            //añadirPoblacionPMX(cruce);


            //funcion reemplazamiento
            reemplazamiento(cruce, poblacionActual, evalucionPoblacion);


            contEvaluaciones+=numindividuosEstacionario;
        }

        log2.append("\n + Tiempo de ejecuccion: " + (System.currentTimeMillis() - tInicial));

        return poblacionActual;
    }

    public void añadirPoblacionOX(ArrayList<int[]> a){
        for(int i = 0; i < a.size(); i++){
            log.append("Individuo: [");
            for(int j = 0; j < a.get(i).length; j++){
                log.append(a.get(i)[j] + " ");
            }
            log.append("]\n");
        }
    }

    public void añadirPoblacionPMX(ArrayList<int[]> a){
        for(int i = 0; i < a.size(); i++){
            log2.append("Individuo: [");
            for(int j = 0; j < a.get(i).length; j++){
                log2.append(a.get(i)[j] + " ");
            }
            log2.append("]\n");
        }
    }

    /**
     * funcion que evalua la poblacion
     * @param poblacionActual poblacion actual a evaluar
     * @return Lista de pares indice coste de la poblacion
     */
    public ArrayList<Pair<Integer,Long>> evalPoblacion(ArrayList<int[]> poblacionActual){
        ArrayList<Pair<Integer,Long>> fitnessIndividuos = new ArrayList<>();
        for(int i = 0; i < poblacionActual.size(); i++){
            Long fitnes = calculoFitnes(poblacionActual.get(i));
            fitnessIndividuos.add(new Pair<Integer,Long>(i,fitnes));
        }

        //ordena de menor a mayor
        fitnessIndividuos.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        return fitnessIndividuos;
    }


    /**
     * Funcion de remplazamiento
     * @param hijos Vector con los hijos a comprobar para reemplazar
     * @param poblacionActual Poblacion actual
     * @param fitnessPoblacion Vector con indices y coste
     */
    public void reemplazamiento(ArrayList<int[]> hijos, ArrayList<int[]> poblacionActual,ArrayList<Pair<Integer,Long>> fitnessPoblacion) {
        ArrayList<int[]> copiaHijos = (ArrayList<int[]>) hijos.clone();
        ArrayList<Pair<Integer,Long>> peores = new ArrayList<>();
        int a1;
        for (int i = 0; i < numindividuosEstacionario; i++) {//Para cada individuo

            ArrayList<Integer> seleccionTorneo = new ArrayList<>();
            for (int j = 0; j < tamTorneoRemplazamiento; j++){
                //Se cogen todos los del torneo
                while(seleccionTorneo.size() != tamTorneoRemplazamiento){
                    a1 = aleatorio.nextInt(poblacionActual.size());
                    boolean esta = false;
                    for (int k = 0; k < seleccionTorneo.size(); k++) {
                        if(a1 == seleccionTorneo.get(k)){
                            esta = true;
                        }
                    }
                    if(!esta){
                        seleccionTorneo.add(a1);
                    }
                }
            }
            //Se guarda el fiteness de cada uno del torneo
            ArrayList<Pair<Integer,Long>> auxiliar = new ArrayList<>();
            for(int j = 0; j < seleccionTorneo.size(); j++){
                for (int k = 0; k < fitnessPoblacion.size(); k++){
                    if(seleccionTorneo.get(j) == fitnessPoblacion.get(k).getKey()){
                        auxiliar.add(fitnessPoblacion.get(k));
                    }
                }
            }
            //Se ordenan y se coge el ultimo
            auxiliar.sort((o1, o2) ->(o1.getValue().compareTo(o2.getValue())));
            peores.add(auxiliar.get(auxiliar.size()-1));

        }

        //Se calcula el fitness de los dos hijos
        long fitnesCopiahijo0 = calculoFitnes(copiaHijos.get(0));
        long fitnesCopiahijo1 = calculoFitnes(copiaHijos.get(1));

        //Si el hijo es mejor qiue el pero se sustituye
        if(fitnesCopiahijo0 < (peores.get(0).getValue())){
            poblacionActual.set(peores.get(0).getKey(), copiaHijos.get(0));
            Pair<Integer,Long> hijo = new Pair(peores.get(0).getKey(),fitnesCopiahijo0);

            //realizamos un cambio
            for(int i = 0; i < fitnessPoblacion.size(); i++){
                if(fitnessPoblacion.get(i).getKey() == peores.get(0).getKey()){
                    fitnessPoblacion.set(i,hijo);
                }
            }


        }
        if(fitnesCopiahijo1 < peores.get(1).getValue() ){
            Pair<Integer,Long> hijo1 = new Pair(peores.get(1).getKey(),fitnesCopiahijo1);
            for(int i = 0; i < fitnessPoblacion.size(); i++){
                if(fitnessPoblacion.get(i).getKey() == peores.get(1).getKey()){
                    fitnessPoblacion.set(i,hijo1);
                }
            }
            poblacionActual.set(peores.get(1).getKey(), copiaHijos.get(1));
        }



    }

    /**
     * Operador PMX
     * @param padres Padres
     * @return Lista de hijos cruzados
     */
    public ArrayList<int[]> operadorPMX(ArrayList<int[]> padres){

        ArrayList<int[]> hijos = new ArrayList<>();
        //Listas de conmutaciones
        ArrayList<Pair<Integer,Integer>> listaP1 = new ArrayList<>();
        ArrayList<Pair<Integer,Integer>> listaP2 = new ArrayList<>();
        //Para cada padre
        for(int i = 0; i < padres.size(); i=i+2) {
            listaP1.clear();
            listaP2.clear();
            int a1 = -1;
            int a2 = -1;
            //Mientras loa aleatorios sean distinto
            while(a2 <= a1) {
                a1 = aleatorio.nextInt(padres.get(i).length);
                a2 = aleatorio.nextInt(padres.get(i+1).length);
            }




            int[] hijo1 = new int[padres.get(i).length];
            int[] hijo2 = new int[padres.get(i+1).length];
            //Se llenan los hijos y listas
            for (int j = a1; j < a2; j++) {
                hijo1[j] = padres.get(i+1)[j];
                hijo2[j] = padres.get(i)[j];

                listaP1.add(new Pair<Integer,Integer>(padres.get(i)[j],padres.get(i+1)[j]));
                listaP2.add(new Pair<Integer,Integer>(padres.get(i+1)[j],padres.get(i)[j]));
            }
            //Mientras que no este lleno
            for(int j = 0; j < hijo1.length; j++){
                //Si llegamos al primer corte empezamos en el segundo
                if(j == a1){
                    j = a2;
                }

                //Primer hijo
                int valor1 = padres.get(i)[j];//Valor inicial a meter
                boolean encontrado = true;//Suponemos que el valor esta en la lista de intercambios
                int cont = 0;//Contador moverse lista
                while(encontrado){//Mientras que esta
                    //Si lo encuentra la correspondencia es el nuevo valor
                    if (valor1 == listaP2.get(cont).getKey()){
                        valor1 = listaP2.get(cont).getValue();
                        cont = 0;//Empezamos a buscar de nuevo esa correspondencia
                    }else{
                        cont++;//Si no examinamos la siguiente
                    }
                    //Si lo recorremos todo es que no esta
                    if(cont == listaP2.size()){
                        encontrado = false;
                    }


                }
                hijo1[j] = valor1;//Se asigna el actual

                //Para hijo 2
                int valor2 = padres.get(i+1)[j];
                boolean encontrado2 = true;
                int cont2 = 0;
                while(encontrado2){

                    if (valor2 == listaP1.get(cont2).getKey()){
                        valor2 = listaP1.get(cont2).getValue();
                        cont2 = 0;
                    }else{
                        cont2++;
                    }

                    if(cont2 == listaP1.size()){
                        encontrado2 = false;
                    }


                }
                hijo2[j] = valor2;
            }
            //Se añaden a la lista con todos lops hijos∫
            hijos.add(hijo1);
            hijos.add(hijo2);


        }

        return hijos;
    }

    /**
     * Operador Ox
     * @param padres padres
     * @return lista de hijos
     */
    public ArrayList<int[]> operadorCruceOX(ArrayList<int[]> padres){
        int a1 = -1;
        int a2 = -1;
        //Se calculan los cortes
        do{
             a1 = aleatorio.nextInt(padres.get(0).length);
             a2 = aleatorio.nextInt(padres.get(0).length);

            if(a1 > a2){
                int aux = a1;
                a1 = a2;
                a2 = aux;
            }

        }while (a1 == a2);

        int[] hijo1 = new int[padres.get(0).length];
        int[] hijo2 = new int[padres.get(1).length];

        //Se rellena los cortes en el hijo contrario
        for(int i = a1; i < a2; i++){
            hijo1[i] = padres.get(0)[i];
            hijo2[i] = padres.get(1)[i];
        }

        boolean llenoH1 = false;
        boolean llenoH2 = false;
        int contadorPosicionHijo1 = a2;
        int contadorPosicionHijo2 = a2;
        int contEleHijo1 = a2-a1;
        int contEleHijo2 = a2-a1;

        //Mientra s no estan llenos
        for (int i = 0; !llenoH1 && !llenoH2 ; i++){

            if(i == hijo1.length){
                i = i % hijo1.length;
            }

            //Se recorre los inteccambios a ver si esta el actual
            boolean estaH1 = false;
            boolean estaH2 = false;
            for(int j = a1; j < a2; j++){
                if (padres.get(1)[i] == padres.get(0)[j]){
                    estaH1 = true;
                }
            }

            for(int j = a1; j < a2; j++){
                if (padres.get(0)[i] == padres.get(1)[j]){
                    estaH2 = true;
                    
                }
            }

            //Si no esta el del primer hijo
            if(!estaH1){
                //Si no esta lleno se añade
                if (!llenoH1) {

                    if (contadorPosicionHijo1 == hijo1.length) {
                        contadorPosicionHijo1 = contadorPosicionHijo1 % hijo1.length;
                    }
                    hijo1[contadorPosicionHijo1] = padres.get(1)[i];
                    contadorPosicionHijo1++;
                    contEleHijo1++;



                    if (contEleHijo1 == hijo1.length) {
                        llenoH1 = true;
                    }


                }
            }

            if(!estaH2){
                if (!llenoH2) {
                    if (contadorPosicionHijo2 == hijo2.length) {
                        contadorPosicionHijo2 = contadorPosicionHijo2 % hijo2.length;
                    }
                    hijo2[contadorPosicionHijo2] = padres.get(0)[i];
                    contadorPosicionHijo2++;
                    contEleHijo2++;



                    if (contEleHijo2 == hijo2.length) {
                        llenoH2 = true;
                    }
                }
            }




        }
        //Se añaden los hijos
        ArrayList<int[]> padresCruzados = new ArrayList<>();
        padresCruzados.add(hijo1);
        padresCruzados.add(hijo2);

        return padresCruzados;
    }

    /**
     * Mutacion
     * @param hijo Hijo a mutar
     */
    public void mutacion(int[] hijo){
            //Para cada gen si se cumple la probabilidad se hace un 2opt
            for(int i = 0; i < hijo.length; i++ ) {
                double valor = aleatorio.nextDouble();
                if(valor>= probMutacion*matrizFlujo.length) {
                    int a1 = aleatorio.nextInt(hijo.length);
                    int aux = hijo[i];
                    hijo[i] = hijo[a1];
                    hijo[a1] = aux;
                }
            }


    }

    /**
     * Seleccion torneo
     * @param poblacionActual poblacion
     * @return
     */
    public ArrayList<int[]> seleccionTorneo(ArrayList<int[]> poblacionActual) {
        //clave->posicion, valor -> fitnes
        ArrayList<Pair<Integer, Long>> candidatos = new ArrayList<>();
        ArrayList<Pair<Integer, Long>> padresSeleccionados = new ArrayList<>();

        int rondas = 0;
        while (rondas < numRondasTorneo) {//CAMBIAR
            for (int i = 0; i < tamTorneoSeleccion; i++) {
                int seleccionado = aleatorio.nextInt(tamPoblacion);

                boolean existe = false;
                for (int j = 0; j < candidatos.size() && !existe; j++) {
                    if (candidatos.get(j).getKey() == seleccionado) {
                        existe = true;
                    }
                }

                if (!existe) {
                    //Añadimos el valor fitnes y la posicion del padre en la matriz
                    long fitnes = calculoFitnes(poblacionActual.get(seleccionado));
                    candidatos.add(new Pair<>(seleccionado, fitnes));
                }

            }

            //seleccionamos al mejor de entre los 3
            rondas++;
            candidatos.sort((Pair<Integer, Long> o1, Pair<Integer, Long> o2) -> o1.getValue().compareTo(o2.getValue()));
            padresSeleccionados.add(candidatos.get(0));

            //vaciamos para la sguiente ejecucion
            candidatos.clear();

        }

       ArrayList<int[]> padres = new ArrayList<>();
       for(int i = 0; i < padresSeleccionados.size(); i++) {
           padres.add(poblacion.get(padresSeleccionados.get(i).getKey()));
       }

       return padres;
    }



    /**
     * Funcion que calcula el coste de la solucion pasada
     *
     * @param padre: Vector con solucion para calculo del coste
     * @return coste calculado
     */
    public long calculoFitnes(int[] padre) {

        long coste = 0;

        for (int i = 0; i < padre.length; i++) {
            for (int j = 0; j < padre.length; j++) {
                if (i != j) {
                    coste = coste + (matrizFlujo[i][j] * matrizDistancia[padre[i]][padre[j]]);
                }
            }
        }
        return coste;
    }

    String getLog(){
        return log.toString();
    }

    String getLog2(){
        return log2.toString();
    }


}

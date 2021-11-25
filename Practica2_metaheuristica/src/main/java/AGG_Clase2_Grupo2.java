import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class AGG_Clase2_Grupo2 {
    private ArrayList<int[]> poblacion;
    private final int maxEvaluaciones;
    private final int probCruceEstacionario;
    private final double probMutacion;
    public final int tamPoblacion;
    public final double probCruceGeneracional;
    public Random aleatorio;
    public final int tamTorneoSeleccion;
    public final int tamTorneoRemplazamiento;
    public final int[][] matrizFlujo;
    public final int[][] matrizDistancia;

    public AGG_Clase2_Grupo2(ArrayList<int[]> _poblacion, int _maxEvaluaciones, int _probCruceEstacionario, double _probMutacion, Random _aleatorio, int _tamTorneoSeleccion, int _tamTorneoReemplazamiento, int[][] _matrizFlujo, int[][] _matrizDistancia, double _probCruceGeneracional) {

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
        this.probCruceGeneracional = _probCruceGeneracional;

    }

    public ArrayList<int[]> operadorPMX(){
        int contEvaluaciones = 0;
        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        ArrayList<Pair<Integer,Long>> evaluacionPoblacion = evalPoblacion(poblacionActual);

        while (contEvaluaciones != 1000){

            Pair<Integer,Long> elite = evaluacionPoblacion.get(0);



            //funcion Seleccion
            ArrayList<int[]> padres = torneoSeleccion(poblacionActual,evaluacionPoblacion);

            //funcion de cruce
            ArrayList<int[]> cruce = operadorPMX(padres);




            //funcion mutacion
            mutacion(cruce);



            evaluacionPoblacion = evalPoblacion(cruce);

            //remplazamiento
            reemplazamiento(cruce,poblacionActual,evaluacionPoblacion,elite);
            poblacionActual.clear();
            poblacionActual = (ArrayList<int[]>) cruce.clone();


            contEvaluaciones++;
        }

        return poblacionActual;
    }

    public ArrayList<int[]> operadorOX2(){
        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        ArrayList<Pair<Integer,Long>> evalucionPoblacion = evalPoblacion(poblacionActual);

        return poblacionActual;
    }


    public ArrayList<int[]> torneoSeleccion( ArrayList<int[]> poblacionActual,ArrayList<Pair<Integer,Long>> evalucionPoblacion) {
        ArrayList<int[]> seleccionados = new ArrayList<>();

        for(int i = 0; i < tamPoblacion; i++){
            if(i == tamPoblacion-1){
                if (evalucionPoblacion.get(i).getValue() <= evalucionPoblacion.get(0).getValue()) {
                    seleccionados.add(poblacionActual.get(i));
                } else {

                    seleccionados.add(poblacionActual.get(0));
                }
            }else {
                //si son iguales se coge el primero
                if (evalucionPoblacion.get(i).getValue() <= evalucionPoblacion.get(i + 1).getValue()) {
                    seleccionados.add(poblacionActual.get(i));
                } else {

                    seleccionados.add(poblacionActual.get(i + 1));
                }
            }
        }

        return seleccionados;
    }



    public ArrayList<int[]> operadorPMX(ArrayList<int[]> padres){

        ArrayList<int[]> hijos = new ArrayList<>();
        //Listas de conmutaciones
        ArrayList<Pair<Integer,Integer>> listaP1 = new ArrayList<>();
        ArrayList<Pair<Integer,Integer>> listaP2 = new ArrayList<>();
        //Para cada padre
        for(int i = 0; i < padres.size(); i=i+2) {
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






    public void mutacion(ArrayList<int[]> cruce){

        for (int i = 0; i <cruce.size(); i++) {
            for (int j = 0; j < cruce.get(i).length; j++) {
                double valor = aleatorio.nextDouble();
                if (valor >= probMutacion * matrizFlujo.length) {
                    int a1 = aleatorio.nextInt(cruce.get(i).length);
                    int aux = cruce.get(i)[j];
                    cruce.get(i)[j] = cruce.get(i)[a1];
                    cruce.get(i)[a1] = aux;
                }
            }

        }
        System.out.println();
    }

    public void reemplazamiento(ArrayList<int[]> cruce, ArrayList<int[]> poblacionActual, ArrayList<Pair<Integer,Long>> evalFitnes  ,Pair<Integer,Long> elite){
        boolean encontrado  = false;
        for (int i = 0; i < evalFitnes.size() && !encontrado; i++){
            if(evalFitnes.get(i).getValue() == elite.getValue()){
                int [] _elite = poblacionActual.get(elite.getKey());
                int cont = 0;
                for (int j = 0; j < cruce.get(i).length; j++) {
                    if(_elite[j] == cruce.get(i)[j]){
                        cont++;
                    }
                }
                //si el elite ya estaba se sale del bucle
                if(cont == _elite.length){
                    encontrado = true;
                }
            }

        }

        if(!encontrado){
            int posPeor = evalFitnes.get(evalFitnes.size()-1).getKey();
            cruce.set(posPeor, poblacionActual.get(elite.getKey()));
            evalFitnes.set(evalFitnes.get(evalFitnes.size()-1).getKey(), elite);
        }



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



    public ArrayList<Pair<Integer,Long>> evalPoblacion(ArrayList<int[]> poblacionActual){
        ArrayList<Pair<Integer,Long>> fitnessIndividuos = new ArrayList<>();
        for(int i = 0; i < poblacionActual.size(); i++){
            Long fitnes = calculoFitnes(poblacionActual.get(i));
            fitnessIndividuos.add(new Pair<>(i,fitnes));
        }

        //ordena de menor a mayor
        fitnessIndividuos.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        return fitnessIndividuos;
    }
}

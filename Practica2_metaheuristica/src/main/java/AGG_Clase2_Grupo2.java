import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class AGG_Clase2_Grupo2 {
    private ArrayList<int[]> poblacion;
    private final int maxEvaluaciones;
    private final double probMutacion;
    public final int tamPoblacion;
    public final double probCruceGeneracional;
    public Random aleatorio;
    public final int tamTorneoSeleccion;
    public final int[][] matrizFlujo;
    public final int[][] matrizDistancia;
    public final int posicionesOX2;
    private StringBuilder log;
    private StringBuilder log2;

    public AGG_Clase2_Grupo2(ArrayList<int[]> _poblacion, int _maxEvaluaciones, double _probMutacion, Random _aleatorio, int _tamTorneoSeleccion,
                             int[][] _matrizFlujo, int[][] _matrizDistancia, double _probCruceGeneracional, int _posicionesOX2 ) {

        this.poblacion = _poblacion;
        this.maxEvaluaciones = _maxEvaluaciones;
        this.probMutacion = _probMutacion;
        this.tamPoblacion =  this.poblacion.size();
        this.aleatorio = _aleatorio;
        this.tamTorneoSeleccion = _tamTorneoSeleccion;
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;
        this.posicionesOX2 = _posicionesOX2;
        this.probCruceGeneracional = _probCruceGeneracional;
        this.log = new StringBuilder();
        this.log2 = new StringBuilder();

    }

    /**
     * Funcion principal algoritmo generacional PMX
     * @return: devuelve poblacion final
     */
    public ArrayList<int[]> operadorPMX(){
        long timeIni = System.currentTimeMillis();
        log.append("Población inicial cruce PMX\n");
        int contEvaluaciones = 0;
        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        guardaPoblaion(poblacionActual, log);
        //Evalucaion inicial de la poblacion
        ArrayList<Pair<Integer,Long>> evaluacionPoblacion = evalPoblacion(poblacionActual);
        contEvaluaciones += tamPoblacion;

        while (contEvaluaciones != maxEvaluaciones){

            //guardamos el elite de la población.
            Pair<Integer,Long> elite = evaluacionPoblacion.get(0);
            int [] _elite = poblacionActual.get(elite.getKey());

            guardaElite(elite, poblacionActual,"PMX", log);


            //funcion Seleccion
            ArrayList<int[]> padres = torneoSeleccion(poblacionActual,evaluacionPoblacion);

            //funcion de cruce
            ArrayList<int[]> cruce = CrucePMX(padres);




            //funcion mutacion
            mutacion(cruce);


            //volvemos a evaluar la población antes de reemplazar
            evaluacionPoblacion = evalPoblacion(cruce);

            //remplazamiento
            reemplazamiento(cruce,poblacionActual,evaluacionPoblacion,elite, _elite);
            poblacionActual.clear();
            poblacionActual = (ArrayList<int[]>) cruce.clone();


            contEvaluaciones+=tamPoblacion;

        }
        long tFinal = System.currentTimeMillis()-timeIni;
        log.append("\n Tiempo Generacional PMX: "+tFinal);
        return poblacionActual;
    }

    /**
     * Función principal que realiza el algoritmo con OX2
     * @return: poblacion final
     */
    public ArrayList<int[]> operadorOX2(){
        long timeIni = System.currentTimeMillis();
        log2.append("Población inicial cruce OX2\n");
        int contEvaluaciones = 0;
        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        guardaPoblaion(poblacionActual, log2);
        //Evalucaion inicial de la poblacion
        ArrayList<Pair<Integer,Long>> evaluacionPoblacion = evalPoblacion(poblacionActual);
        contEvaluaciones += tamPoblacion;

        while (contEvaluaciones != maxEvaluaciones){
            //guardamos el elite de la población.
            Pair<Integer,Long> elite = evaluacionPoblacion.get(0);
            guardaElite(elite, poblacionActual,"OX2", log2);
            int [] _elite = poblacionActual.get(elite.getKey());

            //funcion Seleccion
            ArrayList<int[]> padres = torneoSeleccion(poblacionActual,evaluacionPoblacion);

            //funcion de cruce
            ArrayList<int[]> cruce = CruceOX2(padres);




            //funcion mutacion
            mutacion(cruce);


            //volvemos a evaluar la población antes de reemplazar
            evaluacionPoblacion = evalPoblacion(cruce);

            //remplazamiento
            reemplazamiento(cruce,poblacionActual,evaluacionPoblacion,elite,_elite);
            poblacionActual.clear();
            poblacionActual = (ArrayList<int[]>) cruce.clone();


            contEvaluaciones+=tamPoblacion;

        }
        long tFinal = System.currentTimeMillis()-timeIni;
        log2.append("\n Tiempo Generacional PMX: "+tFinal);
        return poblacionActual;
    }


    /**
     * Operador de cruce OX2 Algoritmo generacional
     * @param padres: poblacion a curzar
     * @return: devuelve la poblacion cruzada
     */
    public  ArrayList<int[]> CruceOX2(ArrayList<int[]> padres){

        ArrayList<int[]> hijos = new ArrayList<>();

        //Realizamos el cruce para cada padre i e i+1
        for (int i = 0; i < padres.size(); i= i+2) {

            ArrayList<Integer> posiciones = new ArrayList<Integer>();

            //Generamos las 3 posiciones con indice distinto antes de aplicar el cruce
            while(posiciones.size() != posicionesOX2){
                int n1 = aleatorio.nextInt(padres.get(0).length);
                boolean esta = false;
                for (int j = 0; j < posiciones.size(); j++) {
                    if(n1 == posiciones.get(j)){
                        esta = true;
                    }
                }
                if(!esta){
                    posiciones.add(n1);
                }
            }
            posiciones.sort(Comparator.naturalOrder());
            ArrayList<Integer> huecos = new ArrayList<>();

            int[] hijos1 = new int[padres.get(i).length];
            int[] hijos2 = new int[padres.get(i+1).length];


            //creacion hijo 1
            for(int j = 0; j < padres.get(i+1).length; j++){
                boolean coincide = false;
                for(int k = 0; k < posiciones.size(); k++){
                    //comprobamos si los valores coinciden, para usarlos o guardar los huecos
                    if(padres.get(i+1)[j] == padres.get(i)[posiciones.get(k)]){
                        coincide = true;

                    }
                }

                if(!coincide){ //si no coincide se va rellenando con los elementos del padre
                    hijos1[j] = padres.get(i+1)[j];
                }else{
                    huecos.add(j);//almacenamos los huecos
                }
            }
            for (int j = 0; j < huecos.size(); j++) {
                hijos1[huecos.get(j)] = padres.get(i)[posiciones.get(j)];
            }
            huecos.clear();//limpiamos los huecos

            //creacion hijo 2

            for(int j = 0; j < padres.get(i).length; j++){
                boolean coincide = false;
                for(int k = 0; k < posiciones.size(); k++){
                    if(padres.get(i)[j] == padres.get(i+1)[posiciones.get(k)]){
                        coincide = true;

                    }
                }
                //
                if(!coincide){ //si no coincide se va rellenando con los elementos del padre
                    hijos2[j] = padres.get(i)[j];
                }else{
                    huecos.add(j);//almacenamos los huecos
                }
            }
            for (int j = 0; j < huecos.size(); j++) {
                hijos2[huecos.get(j)] = padres.get(i+1)[posiciones.get(j)];
            }
            huecos.clear();//limpiamos los huecos

            hijos.add(hijos1);
            hijos.add(hijos2);
        }

        //devolvemoos la población cruzada
        return  hijos;
    }

    /**
     * Torne con k=2, que selecciona al mejor de dos individuos
     * @param poblacionActual: poblacion a utilizar
     * @param evalucionPoblacion: fitnes de cada individuo de la población
     * @return devuelve la poblacion seleccionada
     */
    public ArrayList<int[]> torneoSeleccion( ArrayList<int[]> poblacionActual,ArrayList<Pair<Integer,Long>> evalucionPoblacion) {
        ArrayList<int[]> seleccionados = new ArrayList<>();
        ArrayList<Pair<Integer, Long>>  torneo = new ArrayList<>();
        int n2, n1;

        //Para cada individuo de la población se realiza un torneo aleatorio k=2(sin repetir indice)
        for (int i = 0; i < poblacionActual.size(); i++) {
            do {
                n1 = aleatorio.nextInt(poblacionActual.size());
                n2 = aleatorio.nextInt(poblacionActual.size());

            } while (n1 != n2);

            //Guardamos el fitnes de los dos individuos
            Pair<Integer,Long> fitnes1 = evalucionPoblacion.get(n1);

            Pair<Integer,Long> fitnes2 = evalucionPoblacion.get(n2);

            torneo.add(fitnes1);
            torneo.add(fitnes2);

            //ordenamos los fitnes de los dos individuos
            torneo.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()));

            //nos quedamos con el mejor de los dos.
            seleccionados.add(poblacionActual.get(torneo.get(0).getKey()));

            torneo.clear();

        }
        return seleccionados;
    }


    /**
     * Operador de ccruece PMX Algoritmo Generacional
     * @param padres población a la cual se les realizara el cruce
     * @return devuelve la población cruzada.
     */
    public ArrayList<int[]> CrucePMX(ArrayList<int[]> padres){

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

            //Se añaden a la lista con todos lops hijos
            hijos.add(hijo1);
            hijos.add(hijo2);


        }

        return hijos;
    }


    /**
     * realizamos la mutacion de cada gen del individuo, si se cumple la probabilidad de mutacion.
     * @param cruce población obtenida tras el cruce
     */
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

    }


    /**
     * Función encargada de reemplazar una población por otra.
     * @param cruce: poblacion cruzada y mutada.
     * @param poblacionActual: poblacion anterior.
     * @param evalFitnes: evalucaion de la poblaciuon cruzada y mutada.
     * @param elite: pasamos el elite de la poblacion anterior.
     */
    public void reemplazamiento(ArrayList<int[]> cruce, ArrayList<int[]> poblacionActual, ArrayList<Pair<Integer,Long>> evalFitnes  ,Pair<Integer,Long> elite, int[] eliteIndv){
        boolean encontrado  = false;
        for (int i = 0; i < evalFitnes.size() && !encontrado; i++){
            if(evalFitnes.get(i).getValue() == elite.getValue()){

                int cont = 0;
                for (int j = 0; j < cruce.get(i).length; j++) {
                    if(eliteIndv[j] == cruce.get(i)[j]){
                        cont++;
                    }
                }
                //si el elite ya estaba se sale del bucle
                if(cont == eliteIndv.length){
                    encontrado = true;
                }
            }

        }

        if(!encontrado){
            int posPeor = evalFitnes.get(evalFitnes.size()-1).getKey();
            cruce.set(posPeor, eliteIndv);
            Pair<Integer,Long> nuevoElite = new Pair<>(posPeor, elite.getValue());
            evalFitnes.set(evalFitnes.get(evalFitnes.size()-1).getKey(), nuevoElite);
            evalFitnes.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()));
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


    /**
     * Función encargada de calcular el fitnes de cada individuo
     * @param poblacionActual: poblacion a evaluar
     * @return: devuelve un vector de pares con el indice de los individuos y su fitnes.
     */
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

    public void guardaPoblaion(ArrayList<int[]> poblacionActual, StringBuilder _log) {


        int cont = 0;
        for (int [] individuo: poblacionActual)
        {
            _log.append("Individuo: "+cont+" ");
            cont++;
            for (int i = 0; i < individuo.length; i++) {
            _log.append(individuo[i]+" ");
            }
            _log.append("\n");
        }

    }

    public void guardaElite(Pair<Integer,Long> elite,ArrayList<int[]> poblacionActual, String cruce, StringBuilder _log){
        _log.append("Elite: ");

        int [] _elite = poblacionActual.get(elite.getKey());

        for (int i = 0; i < _elite.length; i++) {
            _log.append(_elite[i]+" ");
        }
        _log.append(" "+elite.getValue()+" \n");
    }

    /**
     * Getter de log
     */
    public String getLog() {
        return log.toString();
    }

    /**
     * Getter de log
     */
    public String getLog2() {
        return log2.toString();
    }
}

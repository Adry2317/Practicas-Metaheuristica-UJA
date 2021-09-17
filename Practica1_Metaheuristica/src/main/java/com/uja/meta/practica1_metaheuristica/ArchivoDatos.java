/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uja.meta.practica1_metaheuristica;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author joseantonio
 */
public class ArchivoDatos {
    
    private int[][] matrizFlujo;
    private int[][] matrizDistancias;
    private String nombreArchivo;
    
    public ArchivoDatos(String rutaArchivo){
        String linea;
        FileReader f = null;
        //Para separar por extension al estar reservado se pone \\ para coger el simbolo reservado
        nombreArchivo = rutaArchivo.split("\\.")[0];
        
        try {
            
            //Inicializamos el fileReader
            f = new FileReader(rutaArchivo);
            
            //Se crea el Buffer de lectura
            BufferedReader buff = new BufferedReader(f);
            
            linea = buff.readLine();
            String[] filasIni = linea.split(" ");
            //Cogemos el numero del fichero pasado
            int numero = Integer.parseInt(filasIni[1]);
            
            //Inicializamos ambas matrices al tamaño anterior 
            matrizDistancias = new int[numero][numero];
            
            matrizFlujo = new int[numero][numero];
            
            //Se lee la linea blanca
            linea = buff.readLine();
            
            for(int i = 0; i < numero; i++){
                
                //Se lee la primera linea
                linea = buff.readLine();
                
                //Se separa la linea por espacios
                String[] fila = linea.split(" ");
                
                int errores = 0;
                
                //Se recorre la linea
                for (int j = 0; j < fila.length; j++) {
                    
                    
                    //Si se encuentra un espacio en blanco mas y da error se
                    //aumenta los errores de forma que se inserte la columna correcta
                    try{
                        matrizFlujo[i][j-errores] = Integer.parseInt(fila[j]);
                    }catch(NumberFormatException e){
                        errores++;
                    }
                    
                }
            }
            
            //Se lee la linea blanca
            linea = buff.readLine();
            
            for(int i = 0; i < numero; i++){
                
                //Se lee la primera linea
                linea = buff.readLine();
                
                //Se separa la linea por espacios
                String[] fila = linea.split(" ");
                
                int errores = 0;
                //Se recorre la linea
                for (int j = 0; j < fila.length; j++) {
                    
                    
                    
                    //Si se encuentra un espacio en blanco mas y da error se
                    //aumenta los errores de forma que se inserte la columna correcta
                    try{
                        matrizDistancias[i][j-errores] = Integer.parseInt(fila[j]);
                    }catch(NumberFormatException e){
                        errores++;
                    }
                    
                }
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        
    }

    public int[][] getMatrizFlujo() {
        return matrizFlujo;
    }

    public int[][] getMatrizDistancias() {
        return matrizDistancias;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }
    
    
    
}

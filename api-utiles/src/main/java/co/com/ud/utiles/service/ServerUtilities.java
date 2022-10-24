/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.utiles.service;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author sierraj
 */
public class ServerUtilities {
    
    public static Integer generarNumero(){
        Random random = new Random(new Date().getTime());
        return random.nextInt(100);
    }
    
    public static String eliminarCerosIzq(String valor){
        return valor.replaceFirst("^0+(?!$)", "");
    }
    
}

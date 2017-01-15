/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Parametros.Parametro;


/**
 *
 * @author Karlo
 */
public class Conexion {
    
    //Metodo main para prueba de consulta
    public static void main(String[] args){
       /* try {
           getBuses(null);
           getBuses(null);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       Parametro p=new Parametro();
       p.setLinea(24);
       p.setSentido(1);
       p.setCodigo_nfc("2551172531");
       print(Long.valueOf("2551172531"));
      // p.setCodigo(2551172531);
       MySQL db=new MySQL();
       //db.insertData(p);
       //db.MySQLConnect();
       print(db.getLineas(p).getLineas().length);
       //double r=MySQL.distanciaCoord(-2.899342,-79.009921,-2.899424,-79.009973);//RadioParada
       //double r=MySQL.distanciaCoord(-2.899300,-79.010951,-2.899248,-79.011011);//RadioBus
       //db.getInfo(p);
       //print(r);// r=0.004340023497561063
       //db.getPosicion(p);
    }
 
   public static void print(Object o){System.out.println(o);}
         
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Datos.Conexion;
import Datos.MySQL;
import Parametros.Parametro;
import Parametros.Values;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author carlo
 */
@Path("datos")
public class DatosResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DatosResource
     */
    public DatosResource() {
    }

    /**
     * Retrieves representation of an instance of Servidor.DatosResource
     * @return an instance of java.lang.String
     */
    @GET
    //@Path("/busp")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Parametro getXml() {
        //TODO return proper representation object 
        Parametro p=new Parametro();
        return p;
    }

    /**
     * PUT method for updating or creating an instance of DatosResource
     * @param p
     * @param content representation for the resource
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void putXml(Parametro p) {
       // print("PUT method: "+p.getNombre());
       // print("PUT method: "+p.getLatituds()[0]);
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Parametro readPositionBus(Parametro p) {
       print(p.getCodRequest());
       print(p.getLinea());
       print(p.getSentido());
       //print(p);
       MySQL db=new MySQL();
       //db.MySQLConnect();
       //db.getLineas();
       //db.getInfo(p);
       //db.getCoordenada(p);
        /*try {
           Conexion.getConexion();
           Conexion.getBuses();
           //getBuses(null);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       switch(p.getCodRequest()){//Codigo de solicitud
           case Values.AUTENTICACION:
               //Usuario usuario=(Usuario) p;
               break;
           case Values.INFO_PARADAS://Entregar informaci[on de linea seleccionada
               p= db.getInfo(p);
               break;
           case Values.PARADAS://Entregar lineas disponibles
               
               break;
           case Values.PUT_CORDENADA:
               db.insertData(p);
               break;
           case Values.INFO_LINEA:
               //p=db.getPosicion(p);
               p=db.getBusProximo(p);
               break;
           case Values.GET_LINEAS:
               p=db.getLineas(p);
               break;
           default:
                p=new Parametro();
                break;
       }   
       return p;
    }
    public void print(Object o){
        System.out.println(o);
    }
    
}


///https://www.adictosaltrabajo.com/tutoriales/rest-swagger/
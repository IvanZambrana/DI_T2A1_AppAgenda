/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package appagenda;

import entidades.Provincia;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ivan
 */
public class AppAgenda {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Map<String,String> emfProperties = new HashMap<String,String>();
        // emfProperties.put("javax.persistence.jdbc.user", "APP");
        // emfProperties.put("javax.persistence.jdbc.password", "App");
        //emfProperties.put("javax.persistence.schemageneration.database.action","create");
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("AppAgendaPU",emfProperties);
        EntityManager em=emf.createEntityManager();
        
        
        em.getTransaction().begin(); //Iniciar una transacción
        /*
        //Crear objetos tipo Provincia para insertar en la tabla  
        Provincia provinciaCadiz = new Provincia();
        provinciaCadiz.setNombre("Cadiz");
        Provincia provinciaSevilla = new Provincia();
        provinciaSevilla.setNombre("Sevilla");
        
        //Inserción en la tabla
        em.persist(provinciaCadiz);
        em.persist(provinciaSevilla);
        */
        
        //em.getTransaction().rollback(); //Cancelar operaciones (pre-commit)
        em.getTransaction().commit(); //Realizar volcado
        
        
        //Cerrar la conexión
        em.close();
        emf.close();
        try{
            DriverManager.getConnection("jdbc:derby:BDAgenda;shutdown=true");
        } catch (SQLException ex){
        }
    }
    
}

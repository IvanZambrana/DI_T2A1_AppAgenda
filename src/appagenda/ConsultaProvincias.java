
package appagenda;

import entidades.Provincia;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Ivan
 */
public class ConsultaProvincias {

    
    public static void main(String[] args) {
        //EntityManagerFactory y EntityManager
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em=emf.createEntityManager();
        
        //------------------FIND ALL-----------------------
       //Objeto tipo Query para realizar consultas predefinidas (findAll)
       Query queryProvincias = em.createNamedQuery("Provincia.findAll");
       
       //Se crea un objeto List que almacena objetos tipo Provincia (findAll)
       List<Provincia> listProvincias = queryProvincias.getResultList();
       
       //Mostrar nombre de todos los objetos de la Lista "listProvincias"con un bucle
       for(Provincia provincia : listProvincias){
            System.out.println(provincia.getNombre());
        }
       
       //------------------FIND BY NOMBRE-------------------
       //Objeto tipo Query para realizar consultas predefinidas (findByNombre)
       Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
       queryProvinciaCadiz.setParameter("nombre", "Cadiz"); //Parámetro
       
       //Se crea un objeto List que almacena el objeto tipo Provincia que cumpla el parámetro(findByNombre)
       List<Provincia> listProvinciaCadiz = queryProvinciaCadiz.getResultList();
       
       //Mostrar nombre de todos los objetos de la Lista "listProvinciaCadiz" con un bucle
       for(Provincia provinciaCadiz:listProvinciaCadiz){
            System.out.println("CONSULTA findByNombre (Parámetro: Cadiz) -> "+provinciaCadiz.getId()+":" + provinciaCadiz.getNombre());
        }
       
       //----------------MÉTODO FIND()-----------------------
       Provincia provinciaId2=em.find(Provincia.class,2);
        if (provinciaId2 != null){
            System.out.print("CONSULTA find() (Parámetro: ID = 2) -> " + provinciaId2.getId() + ":" + provinciaId2.getNombre()+"\n");
        } else {
            System.out.println("No hay ninguna provincia con ID=2");
        }

        //-------------MODIFICACIÓN DE OBJETOS----------------
        em.getTransaction().begin(); //Inicia transacción
        for(Provincia provinciaCadiz : listProvinciaCadiz){
            provinciaCadiz.setCodigo("CA");
            em.merge(provinciaCadiz); //Método merge para realizar cambios
        }
        //em.getTransaction().commit(); //Realiza volcado
        
       
        //----------------ELIMINAR OBJETO---------------------
        Provincia provinciaId20 = em.find(Provincia.class, 20); //Eliminar objeto con id=20
        em.getTransaction().begin();
        if (provinciaId20 != null){
        em.remove(provinciaId20);
        }else{
        System.out.println("No hay ninguna provincia con ID=20");
        }
        em.getTransaction().commit();//Realiza volcado
        
        
        //Cerrar la conexión
        em.close();
        emf.close();
        try{
            DriverManager.getConnection("jdbc:derby:BDAgenda;shutdown=true");
        } catch (SQLException ex){
        }
    }
    
}

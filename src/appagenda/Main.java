/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package appagenda;

import source.AgendaViewController;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ivan
 */
public class Main extends Application {
    
    //EntityManagerFactory y EntityManager
    private EntityManagerFactory emf;
    private EntityManager em;

    
    @Override
    public void start(Stage primaryStage)throws IOException{
        //Referencia al archivo FXML
        StackPane rootMain = new StackPane();
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../source/AgendaView.fxml"));
        Pane rootAgendaView=fxmlLoader.load();
        rootMain.getChildren().add(rootAgendaView);
        
        // Conexión a la BD creando los objetos EntityManager y EntityManagerFactory
        emf=Persistence.createEntityManagerFactory("AppAgendaPU");
        em=emf.createEntityManager();
        
        //Cargar objeto controlador del archivo FXML
        AgendaViewController agendaViewController = (AgendaViewController)fxmlLoader.getController();
        
        //Usamos el método setEntityManager de la clase controladora
        agendaViewController.setEntityManager(em);

        //Llamada al método cargarTodasPersonas creados en el controlador
        agendaViewController.cargarTodasPersonas();
        
        
        Scene scene = new Scene(rootMain,1200,800);
        primaryStage.setTitle("App Agenda");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {super.stop();
        em.close();
        emf.close();
        try{
            DriverManager.getConnection("jdbc:derby:BDAgenda;shutdown=true");
        } catch(SQLException ex) {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}

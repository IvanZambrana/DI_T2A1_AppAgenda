/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package source;

import entidades.Persona;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author Ivan
 */
public class AgendaViewController implements Initializable {

    private EntityManager entityManager;
    @FXML
    private TableView<Persona> tableViewAgenda;
    @FXML
    private TableColumn<Persona,String> columnNombre;
    @FXML
    private TableColumn<Persona,String> columnApellidos;
    @FXML
    private TableColumn<Persona,String> columnEmail;
    @FXML
    private TableColumn<Persona, String> columnProvincia;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private AnchorPane rootAgendaView;
    public void setEntityManager(EntityManager entityManager){
        this.entityManager=entityManager;
    }
    
    //Variable para persona seleccionada en la tabla
    private Persona personaSeleccionada;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        //Asociar columnas a propiedades de la clase entidad
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnProvincia.setCellValueFactory(
            cellData->{
                SimpleStringProperty property=new SimpleStringProperty();
                if (cellData.getValue().getProvincia()!=null){
                    property.setValue(cellData.getValue().getProvincia().getNombre());
                }
                return property;
            });
        
        //Listener para TableView
        tableViewAgenda.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            personaSeleccionada=newValue;
        
            //Rellenar los TextFields con los valores de personaSeleccionada
            if (personaSeleccionada != null){
                textFieldNombre.setText(personaSeleccionada.getNombre());
                textFieldApellidos.setText(personaSeleccionada.getApellidos());
            } else {
                textFieldNombre.setText("");
                textFieldApellidos.setText("");
            }
        });
    }     
    
    //Método para cargar todas las personas
    public void cargarTodasPersonas(){
        //Uso de Query para seleccionar todos los objetos de la tabla Persona
        Query queryPersonaFindAll = entityManager.createNamedQuery("Persona.findAll");
        
        //Crear lista de objetos tipo Persona
        List<Persona> listPersona = queryPersonaFindAll.getResultList();
        
        //Con setItems se rellena el TableView de la vista
        tableViewAgenda.setItems(FXCollections.observableArrayList(listPersona));
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        
        //Comprobamos que haya algún registro en el TableView
        if (personaSeleccionada != null){
            //En caso de que no sea null se asignan los valores recogidos en el TextField
            personaSeleccionada.setNombre(textFieldNombre.getText());
            personaSeleccionada.setApellidos(textFieldApellidos.getText());
            
            //También se actualiza el objeto en la base de datos
            entityManager.getTransaction().begin();
            entityManager.merge(personaSeleccionada);
            entityManager.getTransaction().commit();
            
            //Actualizamos el TableView con los nuevos valores
            int numFilaSeleccionada =
            tableViewAgenda.getSelectionModel().getSelectedIndex();
            tableViewAgenda.getItems().set(numFilaSeleccionada,personaSeleccionada);

            //Cambio de foco
            TablePosition pos = new
            TablePosition(tableViewAgenda,numFilaSeleccionada,null);
            tableViewAgenda.getFocusModel().focus(pos);
            tableViewAgenda.requestFocus();
            
        }
    }

    @FXML
private void onActionButtonNuevo(ActionEvent event){
    try{
        // Cargar la vista de detalle
        FXMLLoader fxmlLoader=new
        FXMLLoader(getClass().getResource("PersonaDetalleView.fxml"));
        Parent rootDetalleView=fxmlLoader.load();
        
        //Método para pasar la vista a PersonaDetalleView
        PersonaDetalleViewController personaDetalleViewController =
        (PersonaDetalleViewController) fxmlLoader.getController();
        personaDetalleViewController.setRootAgendaView(rootAgendaView);

        //Intercambio de datos funcionales con el detalle
        personaDetalleViewController.setTableViewPrevio(tableViewAgenda);
        personaSeleccionada = new Persona();
        personaDetalleViewController.setPersona(entityManager, personaSeleccionada,true);
        
        //Mostrar datos actuales del objeto
        personaDetalleViewController.mostrarDatos();
            
        // Ocultar la vista de la lista
        rootAgendaView.setVisible(false);
        
        //Añadir la vista detalle al StackPane principal para que se muestre
        StackPane rootMain = (StackPane) rootAgendaView.getScene().getRoot();
        rootMain.getChildren().add(rootDetalleView);
        
    } catch (IOException ex){
        Logger.getLogger(AgendaViewController.class.getName()).log(Level.SEVERE,null,ex);
    }
}

    @FXML
    private void onActionButtonEditar(ActionEvent event) {
       try{
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader=new
            FXMLLoader(getClass().getResource("PersonaDetalleView.fxml"));
            Parent rootDetalleView=fxmlLoader.load();

            //Método para pasar la vista a PersonaDetalleView
            PersonaDetalleViewController personaDetalleViewController =
            (PersonaDetalleViewController) fxmlLoader.getController();
            personaDetalleViewController.setRootAgendaView(rootAgendaView);

            //Intercambio de datos funcionales con el detalle
            personaDetalleViewController.setTableViewPrevio(tableViewAgenda);
            personaDetalleViewController.setPersona(entityManager, personaSeleccionada,false);
            
            //Mostrar datos actuales del objeto
            personaDetalleViewController.mostrarDatos();
            
            // Ocultar la vista de la lista
            rootAgendaView.setVisible(false);

            //Añadir la vista detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootAgendaView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);
        
        } catch (IOException ex){
            Logger.getLogger(AgendaViewController.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        //Creación de ventana emergente de confirmación
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText("¿Desea suprimir el siguiente registro?");
        alert.setContentText(personaSeleccionada.getNombre() + " " + personaSeleccionada.getApellidos());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
         // Se procede a borrar el objeto personaSeleccionada
            entityManager.getTransaction().begin();
            entityManager.merge(personaSeleccionada);
            entityManager.remove(personaSeleccionada);
            entityManager.getTransaction().commit();
            tableViewAgenda.getItems().remove(personaSeleccionada);
            tableViewAgenda.getFocusModel().focus(null);
            tableViewAgenda.requestFocus();

        } else {
         // Aquí simplemente de deja seleccionada la misma fila en el TableView
            int numFilaSeleccionada=
            tableViewAgenda.getSelectionModel().getSelectedIndex();
            tableViewAgenda.getItems().set(numFilaSeleccionada,personaSeleccionada);
            TablePosition pos = new TablePosition(tableViewAgenda,
            numFilaSeleccionada,null);
            tableViewAgenda.getFocusModel().focus(pos);
            tableViewAgenda.requestFocus();
        }

    }
}

package org.cristopherpineda.sistema;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.cristopherpineda.controller.AreaController;
import org.cristopherpineda.controller.CargoController;
import org.cristopherpineda.controller.ContactoController;
import org.cristopherpineda.controller.EspecialidadController;
import org.cristopherpineda.controller.MedicoController;
import org.cristopherpineda.controller.MenuPrincipalController;
import org.cristopherpineda.controller.PacienteController;
import org.cristopherpineda.controller.ProgramadorController;
import org.cristopherpineda.controller.TelefonoController;

public class Principal extends Application {
private final String PAQUETE_VISTA = "/org/cristopherpineda/view/";
private Stage escenarioPrincipal; 
private Scene escena;


@Override
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital de Infectologia");
        menuPrincipal();
        escenarioPrincipal.show();
        
    }

    public void menuPrincipal() {  
       try{
        MenuPrincipalController menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml",600,278);
        menuPrincipal.setEscenarioPrincipal(this);
    }catch(Exception e){
            e.printStackTrace();
            }
       
    }

    public void ventanaMedicos(){
        try{
            MedicoController medicoController = (MedicoController) cambiarEscena("MedicoView.fxml",690,423);
            medicoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
     
    
    }
    public Initializable cambiarEscena(String fxml, int ancho, int alto)  throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho ,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    public void ventanaProgramador() {
        try{
            ProgramadorController programadorController = (ProgramadorController) cambiarEscena("ProgramadorView.fxml",690,423);
            programadorController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
      
    }
        
  

    
    }

    public void ventanaPaciente() {
  try{
            PacienteController pacienteController = (PacienteController) cambiarEscena("PacienteView.fxml",793,499);
            pacienteController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
          }
    }  

    public void ventanaTelefono() {
  try{
            TelefonoController telefonoController = (TelefonoController) cambiarEscena("TelefonoView.fxml",690,423);
            telefonoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
          }
    }

    public void ventanaContacto() {
  try{
            ContactoController contactoController = (ContactoController) cambiarEscena("ContactoUrgenciaView.fxml",678,499);
            contactoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
          }
    
    }

    public void ventanaArea() {
  try{
            AreaController areaController = (AreaController) cambiarEscena("AreaView.fxml",679,426);
            areaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
          }
    }
    public void ventanaEspecialidad() {
  try{
            EspecialidadController especialidadController = (EspecialidadController) cambiarEscena("EspecialidadView.fxml",679,426);
            especialidadController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
          }
    }
    public void ventanaCargo() {
  try{
            CargoController cargoController = (CargoController) cambiarEscena("CargoView.fxml",679,426);
            cargoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
          }
    }
    
    
    }

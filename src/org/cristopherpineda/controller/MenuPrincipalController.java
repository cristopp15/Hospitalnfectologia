package org.cristopherpineda.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.cristopherpineda.sistema.Principal;

public class MenuPrincipalController implements Initializable {
 private Principal escenarioPrincipal;
  
 @Override
    public void initialize(URL location, ResourceBundle resources) {
      
      
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMedicos(){
         escenarioPrincipal.ventanaMedicos();
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
      }
      
    public void ventanaPaciente(){
        escenarioPrincipal.ventanaPaciente();
    }
      
//    public void ventanaContacto(){
  //      escenarioPrincipal.ventanaContacto();
    //}
    
    public void ventanaArea(){
        escenarioPrincipal.ventanaArea();
    }
    
    public void ventanaEspecialidad(){
        escenarioPrincipal.ventanaEspecialidad();
    }
    
    public void ventanaCargo(){
        escenarioPrincipal.ventanaCargo();
    }
    
//  public void ventanaTelefono(){
   //   escenarioPrincipal.ventanaTelefono();
  //  }

}

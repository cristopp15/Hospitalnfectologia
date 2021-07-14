package org.cristopherpineda.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.cristopherpineda.bean.Especialidad;
import org.cristopherpineda.db.Conexion;
import org.cristopherpineda.sistema.Principal;

public class EspecialidadController implements Initializable{
      private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaPaciente;
    @FXML private TextField txtNombreEspecialidad;
    @FXML private TableView tblAreas;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombreEspecialidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
     @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                    activarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    tipoDeOperacion = operaciones.GUARDAR;
                    break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
           
              
        }
    }
    
    public void guardar(){
        Especialidad registro = new Especialidad();
        registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
        

       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidad(?)}");
           procedimiento.setString(1, registro.getNombreEspecialidad());
           procedimiento.execute();
           // listaMedico.add(registro);
       }catch(Exception e){
           e.printStackTrace();
       }
        
    }
    
  public void eliminar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                   
                    break;
            case GUARDAR:
            //    guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
           
              
        }
    }
    
    public void desactivarControles(){
      txtNombreEspecialidad.setEditable(false);
    
      
    }
    
    
     public void activarControles(){
      txtNombreEspecialidad.setEditable(true);
      
  
   
     }  
    
    
    public void limpiarControles(){
        txtNombreEspecialidad.setText(" ");
     
      
    }
    
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}

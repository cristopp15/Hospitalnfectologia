package org.cristopherpineda.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.cristopherpineda.bean.Cargo;
import org.cristopherpineda.db.Conexion;
import org.cristopherpineda.sistema.Principal;

public class CargoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cargo> listaCargo;
    @FXML private TextField txtNombreCargo;
    @FXML private TableView tblCargos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombreCargo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
                cargarDatos();
    }
    
    public void cargarDatos(){
        tblCargos.setItems(getCargo());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargo"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));

    }
    
    public ObservableList<Cargo> getCargo(){
        ArrayList<Cargo> lista = new ArrayList<Cargo>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Cargo(resultado.getInt("codigoCargo"),
                        resultado.getString("nombreCargo")));
                        
            
        
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCargo = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblCargos.getSelectionModel().getSelectedItem() != null){
        txtNombreCargo.setText((((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getNombreCargo()));

        }
    };

    
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
                cargarDatos();
                break;
           
              
        }
    }
    
    public void guardar(){
        Cargo registro = new Cargo();
        registro.setNombreCargo(txtNombreCargo.getText());
        

       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargo(?)}");
           procedimiento.setString(1, registro.getNombreCargo());
           procedimiento.execute();
           // listaMedico.add(registro);
       }catch(Exception e){
           e.printStackTrace();
       }
        
    }
    
          public Cargo buscarCargo(int codigoCargo){
      Cargo resultado = null;
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
          procedimiento.setInt(1, codigoCargo);
          ResultSet registro = procedimiento.executeQuery();
          while(registro.next()){
              resultado = new Cargo(registro.getInt("codigoCargo"),
                                     registro.getString("nombreCargo"));
                                          
              
              
          }
      }catch(Exception e){
          e.printStackTrace();
      }
        return resultado;
  }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblCargos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de elminar el registro?","Eliminar Cargo",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                try{
                PreparedStatement procedimiento = Conexion .getInstancia().getConexion().prepareCall("{call sp_EliminarCargo(?)}");
                procedimiento.setInt(1,((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
                procedimiento.execute();
                listaCargo.remove(tblCargos.getSelectionModel().getSelectedIndex());
                limpiarControles();
                }catch(Exception e){
                e.printStackTrace();
                }
                
                      }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        
                        
                        }else{
                          JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                      }
                
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCargos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
               
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                cargarDatos();
                limpiarControles();
                break;
            
            
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
              
                break;
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                // cargarDatos();
                limpiarControles();
                break;
            
            
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCargo(?,?)}");
            Cargo registro = (Cargo)tblCargos.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombreCargo.getText());
            procedimiento.setInt(1,registro.getCodigoCargo());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
      txtNombreCargo.setEditable(false);
    
      
    }
    
    
     public void activarControles(){
      txtNombreCargo.setEditable(true);
      
  
   
     }  
    
    
    public void limpiarControles(){
        txtNombreCargo.setText(" ");
     
      
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

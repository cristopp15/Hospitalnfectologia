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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.cristopherpineda.bean.Medico;
import org.cristopherpineda.bean.Telefono;
import org.cristopherpineda.db.Conexion;
import org.cristopherpineda.sistema.Principal;

public class TelefonoController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico;
    private ObservableList<Telefono> listaTelefono;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private TextField txtTelefonoLaboral;
    @FXML private ComboBox cmbCodigoMedico;
    @FXML private TableView tblMedicoT;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colPersonal;
    @FXML private TableColumn colLaboral;
    @FXML private TableColumn colCodM;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
     @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedico.setItems(getMedicos());   
    }
    
    public void cargarDatos(){
        tblMedicoT.setItems(getTelefono());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Telefono, Integer>("codigoTelefonoMedico"));
        colPersonal.setCellValueFactory(new PropertyValueFactory<Telefono, String>("TelefonoPersonal"));
        colLaboral.setCellValueFactory(new PropertyValueFactory<Telefono, String>("TelefonoTrabajo"));
        colCodM.setCellValueFactory(new PropertyValueFactory<Telefono, Integer>("codigoMedico"));

    }
    
    public ObservableList<Telefono> getTelefono(){
        ArrayList<Telefono> lista = new ArrayList<Telefono>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonoMedico}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Telefono(resultado.getInt("codigoTelefonoMedico"),
                        resultado.getString("TelefonoPersonal"),
                        resultado.getString("TelefonoTrabajo"),
                       resultado.getInt("codigoMedico")));
            
        
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTelefono = FXCollections.observableList(lista);
    }
    
    public ObservableList<Medico> getMedicos(){
        ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicos}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Medico(resultado.getInt("codigoMedico"),
                        resultado.getInt("LicenciaMedica"),
                        resultado.getString("nombres"),
                        resultado.getString("apellidos"),
                        resultado.getString("horaEntrada"),
                        resultado.getString("horaSalida"),
                        resultado.getInt("turnoMaximo"),
                        resultado.getString("sexo") ));
            
        
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedico = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblMedicoT.getSelectionModel().getSelectedItem() != null){
        txtTelefonoPersonal.setText(String.valueOf(((Telefono)tblMedicoT.getSelectionModel().getSelectedItem()).getTelefonoPersonal()));
        txtTelefonoLaboral.setText((((Telefono)tblMedicoT.getSelectionModel().getSelectedItem()).getTelefonoTrabajo()));
        //cmbCodigoMedico.setValue((((Telefono)tblMedicoT.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbCodigoMedico.getSelectionModel().select(buscarMedico(((Telefono)  tblMedicoT.getSelectionModel().getSelectedItem()).getCodigoMedico()));
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
        Telefono registro = new Telefono();
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoLaboral.getText());
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());

       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoMedico(?,?,?)}");
           procedimiento.setString(1, registro.getTelefonoPersonal());
           procedimiento.setString(2, registro.getTelefonoTrabajo());
           procedimiento.setInt(3, registro.getCodigoMedico());
           procedimiento.execute();
           listaTelefono.add(registro);
       }catch(Exception e){
           e.printStackTrace();
       }
        
    }
    
    public Medico buscarMedico(int codigoMedico){
      Medico resultado = null;
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico(?)}");
          procedimiento.setInt(1, codigoMedico);
          ResultSet registro = procedimiento.executeQuery();
          while(registro.next()){
              resultado = new Medico(registro.getInt("codigoMedico"),
                                     registro.getInt("licenciaMedica"),
                                     registro.getString("nombres"),
                                     registro.getString("apellidos"),
                                     registro.getString("horaEntrada"),
                                     registro.getString("horaSalida"),
                                     registro.getInt("turnoMaximo"),
                                     registro.getString("sexo"));
                                          
              
              
          }
      }catch(Exception e){
          e.printStackTrace();
      }
        return resultado;
  }
    
    public Telefono buscarTelefonoMedico(int codigoTelefonoMedico){
      Telefono resultado = null;
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoMedico(?)}");
          procedimiento.setInt(1, codigoTelefonoMedico);
          ResultSet registro = procedimiento.executeQuery();
          while(registro.next()){
              resultado = new Telefono(registro.getInt("codigoTelefonoMedico"),
                                     registro.getString("TelefonoPersonal"),
                                     registro.getString("TelefonoTrabajo"),
                                     registro.getInt("codigoMedico"));
                                          
              
              
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
                if (tblMedicoT.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de elminar el registro?","Eliminar Telefono Medico",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                try{
                PreparedStatement procedimiento = Conexion .getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoMedico(?)}");
                procedimiento.setInt(1,((Telefono)tblMedicoT.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                procedimiento.execute();
                listaTelefono.remove(tblMedicoT.getSelectionModel().getSelectedIndex());
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
                if(tblMedicoT.getSelectionModel().getSelectedItem() != null){
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
              limpiarControles();
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTelefonoMedico(?,?,?,?)}");
            Telefono registro = (Telefono)tblMedicoT.getSelectionModel().getSelectedItem();
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txtTelefonoLaboral.getText());
            registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            procedimiento.setInt(1,registro.getCodigoTelefonoMedico());
            procedimiento.setString(2, registro.getTelefonoPersonal());
            procedimiento.setString(3, registro.getTelefonoTrabajo());
            procedimiento.setInt(4,registro.getCodigoMedico());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
    public void desactivarControles(){
      txtTelefonoPersonal.setEditable(false);
      txtTelefonoLaboral.setEditable(false);
      cmbCodigoMedico.setDisable(true);
      
    }
    
    public void activarControles(){
      txtTelefonoPersonal.setEditable(true);
      txtTelefonoLaboral.setEditable(true);
      cmbCodigoMedico.setDisable(false);
     }  
  
    public void limpiarControles(){
        txtTelefonoPersonal.setText(" ");
        txtTelefonoLaboral.setText(" ");
        cmbCodigoMedico.getSelectionModel().clearSelection();
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

//  public void menuPrincipal(){
  //      escenarioPrincipal.menuPrincipal();
  //  }

}

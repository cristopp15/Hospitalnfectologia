package org.cristopherpineda.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
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
import org.cristopherpineda.bean.Medico;
import org.cristopherpineda.db.Conexion;
import org.cristopherpineda.report.GenerarReporte;
import org.cristopherpineda.sistema.Principal;

public class MedicoController implements Initializable  {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico;
    @FXML private TextField txtLicenciaMedica;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtTurnoMaximo;
    @FXML private TextField txtSexo;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colTurnos;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
                cargarDatos();
    }
    
    public void cargarDatos(){
        tblMedicos.setItems(getMedicos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("codigoMedico"));
        colLicenciaMedica.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("LicenciaMedica"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Medico, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Medico, String>("apellidos"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaSalida"));
        colTurnos.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("turnoMaximo"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Medico, String>("sexo"));
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
        if(tblMedicos.getSelectionModel().getSelectedItem() != null){
        txtLicenciaMedica.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
        txtNombres.setText((((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getNombres()));
        txtApellidos.setText((((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos()));
        txtHoraEntrada.setText((((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada()));
        txtHoraSalida.setText((((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida()));
        txtHoraEntrada.setText((((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada()));
        txtTurnoMaximo.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getTurnoMaximo()));
        txtSexo.setText((((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getSexo()));
        }
    };
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                    activarControles();
                    limpiarControles();
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
        Medico registro = new Medico();
        registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setHoraEntrada(txtHoraEntrada.getText());
        registro.setHoraSalida(txtHoraSalida.getText());
        registro.setSexo(txtSexo.getText());
        
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedico(?,?,?,?,?,?)}");
           procedimiento.setInt(1, registro.getLicenciaMedica());
           procedimiento.setString(2, registro.getNombres());
           procedimiento.setString(3, registro.getApellidos());
           procedimiento.setString(4, registro.getHoraEntrada());
           procedimiento.setString(5, registro.getHoraSalida());
           procedimiento.setString(6, registro.getSexo());
           procedimiento.execute();
           listaMedico.add(registro);
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
                if (tblMedicos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de elminar el registro?","Eliminar Medico",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                try{
                PreparedStatement procedimiento = Conexion .getInstancia().getConexion().prepareCall("{call sp_EliminarMedico(?)}");
                procedimiento.setInt(1,((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                procedimiento.execute();
                listaMedico.remove(tblMedicos.getSelectionModel().getSelectedIndex());
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
                if(tblMedicos.getSelectionModel().getSelectedItem() != null){
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
                cargarDatos();
                limpiarControles();
                break;
            
            
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedico(?,?,?,?,?,?,?)}");
            Medico registro = (Medico)tblMedicos.getSelectionModel().getSelectedItem();
            registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setHoraEntrada(txtHoraEntrada.getText());
            registro.setHoraSalida(txtHoraSalida.getText());
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1,registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getLicenciaMedica());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getHoraEntrada());
            procedimiento.setString(6, registro.getHoraSalida());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
    
         public void generarReporte(){
         switch(tipoDeOperacion){
             case NINGUNO:
                 limpiarControles();
                 imprimirReporte();
                 //tipoDeOperacion = operaciones.ACTUALIZAR;
                 break;
             case ACTUALIZAR:
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
    
     
     public void imprimirReporte(){
         Map parametros = new HashMap();
         parametros.put("codigoMedico",null);
         GenerarReporte.mostrarReporte("ReporteMedicos.jasper","Reporte de Medicos", parametros);
     }
     
     
    public void desactivarControles(){
      txtLicenciaMedica.setEditable(false);
      txtNombres.setEditable(false);
      txtApellidos.setEditable(false);
      txtHoraEntrada.setEditable(false);
      txtHoraSalida.setEditable(false);
      txtTurnoMaximo.setEditable(false);
      txtSexo.setEditable(false);
    }
    
    public void activarControles(){
      txtLicenciaMedica.setEditable(true);
      txtNombres.setEditable(true);
      txtApellidos.setEditable(true);
      txtHoraEntrada.setEditable(true);
      txtHoraSalida.setEditable(true);
      txtTurnoMaximo.setEditable(false);
      txtSexo.setEditable(true);
   
     }  
    
    public void limpiarControles(){
        txtLicenciaMedica.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
        txtTurnoMaximo.setText("");
        txtSexo.setText("");
        tblMedicos.getSelectionModel().clearSelection();
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
    
    public void ventanaTelefono(){
        escenarioPrincipal.ventanaTelefono();
    }
}

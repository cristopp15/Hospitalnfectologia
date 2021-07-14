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
import org.cristopherpineda.bean.ContactoUrgencia;
import org.cristopherpineda.bean.Paciente;
import org.cristopherpineda.db.Conexion;
import org.cristopherpineda.sistema.Principal;

public class ContactoController implements Initializable {
      private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<ContactoUrgencia> listaContactoUrgencia;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNumeroContacto;
    @FXML private ComboBox cmbCodPaciente;
    @FXML private TableView tblContactoUrgencia;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCodP;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
   @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodPaciente.setItems(getPacientes());   
    }
    
    public void cargarDatos(){
        tblContactoUrgencia.setItems(getContactoUrgencia());
        colCodigo.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoContactoUrgencia"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("numeroContacto"));
        colCodP.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoPaciente"));

    }
    
    public ObservableList<ContactoUrgencia> getContactoUrgencia(){
        ArrayList<ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarContactoUrgencia}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"),
                        resultado.getString("nombres"),
                        resultado.getString("apellidos"),
                       resultado.getString("numeroContacto"),
                        resultado.getInt("codigoPaciente")));
            
        
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaContactoUrgencia = FXCollections.observableList(lista);
    }
    
    public ObservableList<Paciente> getPacientes(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPacientes}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                        resultado.getString("DPI"),
                        resultado.getString("nombres"),
                        resultado.getString("apellidos"),
                        resultado.getDate("fechaNacimiento"),
                        resultado.getInt("edad"),
                        resultado.getString("direccion"),
                        resultado.getString("ocupacion"),
                        resultado.getString("sexo") ));
            
        
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
        txtNombres.setText(String.valueOf(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNombres()));
        txtApellidos.setText((((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getApellidos()));
        txtNumeroContacto.setText((((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getnumeroContacto()));
        //cmbCodigoMedico.setValue((((Telefono)tblMedicoT.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbCodPaciente.getSelectionModel().select(buscarContactoUrgencia(((ContactoUrgencia)  tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
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
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setnumeroContacto(txtNumeroContacto.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());

       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencia(?,?,?,?)}");
           procedimiento.setString(1, registro.getNombres());
           procedimiento.setString(2, registro.getApellidos());
           procedimiento.setString(3, registro.getnumeroContacto());
           procedimiento.setInt(4, registro.getCodigoPaciente());
           procedimiento.execute();
           listaContactoUrgencia.add(registro);
       }catch(Exception e){
           e.printStackTrace();
       }
        
    }
    
    
          public Paciente buscarPaciente(int codigoPaciente){
      Paciente resultado = null;
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
          procedimiento.setInt(1, codigoPaciente);
          ResultSet registro = procedimiento.executeQuery();
          while(registro.next()){
              resultado = new Paciente(registro.getInt("codigoPaciente"),
                                     registro.getString("DPI"),
                                     registro.getString("nombres"),
                                     registro.getString("apellidos"),
                                     registro.getDate("fechaNacimiento"),
                                     registro.getInt("edad"),
                                     registro.getString("direccion"),
                                     registro.getString("ocupacion"),
                                     registro.getString("sexo"));
                                          
              
              
          }
      }catch(Exception e){
          e.printStackTrace();
      }
        return resultado;
  }

           public ContactoUrgencia buscarContactoUrgencia(int codigoContactoUrgencia){
      ContactoUrgencia resultado = null;
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarContactoUrgencia(?)}");
          procedimiento.setInt(1, codigoContactoUrgencia);
          ResultSet registro = procedimiento.executeQuery();
          while(registro.next()){
              resultado = new ContactoUrgencia(registro.getInt("codigoContactoUrgencia"),
                                     registro.getString("nombres"),
                                     registro.getString("apellidos"),
                                     registro.getString("numeroContacto"),
                                     registro.getInt("codigoPaciente"));
                                          
              
              
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
                if (tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de elminar el registro?","Eliminar Contacto Urgencia",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                try{
                PreparedStatement procedimiento = Conexion .getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencia(?)}");
                procedimiento.setInt(1,((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
                procedimiento.execute();
                listaContactoUrgencia.remove(tblContactoUrgencia.getSelectionModel().getSelectedIndex());
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
                if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarContactoUrgencia(?,?,?,?)}");
            ContactoUrgencia registro = (ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem();
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setCodigoPaciente(((Paciente)cmbCodPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            procedimiento.setInt(1,registro.getCodigoContactoUrgencia());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4,registro.getnumeroContacto());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
      txtNombres.setEditable(false);
      txtApellidos.setEditable(false);
      txtNumeroContacto.setEditable(false);
      cmbCodPaciente.setDisable(true);
    }
    
    
     public void activarControles(){
      txtNombres.setEditable(true);
      txtApellidos.setEditable(true);
      txtNumeroContacto.setEditable(true);
      cmbCodPaciente.setDisable(false);
     }  
    
    
    public void limpiarControles(){
        txtNombres.setText(" ");
        txtApellidos.setText(" ");
        txtNumeroContacto.setText(" ");
        cmbCodPaciente.getSelectionModel().clearSelection();
        tblContactoUrgencia.getSelectionModel().clearSelection();
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
    
    public void ventanaPaciente(){
        escenarioPrincipal.ventanaPaciente();
    }
}

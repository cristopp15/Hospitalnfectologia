package org.cristopherpineda.controller;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.cristopherpineda.bean.Paciente;
import org.cristopherpineda.db.Conexion;
import org.cristopherpineda.sistema.Principal;


public class PacienteController implements Initializable {
     private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    private DatePicker fecha;
    @FXML private TextField txtDPI;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtFechaDeNacimiento;
    @FXML private TextField txtEdad;
    @FXML private TextField txtOcupacion;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtSexo;
    @FXML private TableView tblPacientes;
    @FXML private GridPane grpFecha;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colFechaDeNacimiento;
    @FXML private TableColumn colEdad;
    @FXML private TableColumn colOcupacion;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
                cargarDatos();
                fecha = new DatePicker(Locale.ENGLISH);
                fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
                fecha.getCalendarView().todayButtonTextProperty().set("Today");
                fecha.getCalendarView().setShowWeeks(false);
                fecha.getStylesheets().add("/org/cristopherpineda/resource/DatePicker.css");
                grpFecha.add(fecha, 0, 0);
    }
    
    public void cargarDatos(){
        tblPacientes.setItems(getPacientes());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Paciente, String>("DPI"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidos"));
        colFechaDeNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, String>("fechaNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
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
        if(tblPacientes.getSelectionModel().getSelectedItem() != null){
        txtDPI.setText((((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDPI()));    
        txtNombres.setText((((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombres()));
        txtApellidos.setText((((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidos()));
        fecha.selectedDateProperty().set((((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaDeNacimiento()));
        //txtFechaDeNacimiento.setText((((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaDeNacimiento()));
        txtEdad.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
        txtDireccion.setText((((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion()));
        txtOcupacion.setText((((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion()));
        txtSexo.setText((((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo()));
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
                break;
           
              
        }
    }
    
    public void guardar(){
        Paciente registro = new Paciente();
        registro.setDPI(txtDPI.getText());
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setFechaDeNacimiento(fecha.getSelectedDate());
//registro.setFechaDeNacimiento(txtFechaDeNacimiento.getText());        
//        registro.setEdad(Integer.parseInt(txtEdad.getText()));
        registro.setOcupacion(txtOcupacion.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setSexo(txtSexo.getText());
        
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPaciente(?,?,?,?,?,?,?)}");
           procedimiento.setString(1, registro.getDPI());
           procedimiento.setString(2, registro.getNombres());
           procedimiento.setString(3, registro.getApellidos());
           procedimiento.setDate(4, new java.sql.Date(registro.getFechaDeNacimiento().getTime()));
           procedimiento.setString(5, registro.getOcupacion());
           procedimiento.setString(6, registro.getDireccion());
           procedimiento.setString(7, registro.getSexo());
           procedimiento.execute();
           listaPaciente.add(registro);
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
                if (tblPacientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de elminar el registro?","Eliminar Paciente",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                try{
                PreparedStatement procedimiento = Conexion .getInstancia().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
                procedimiento.setInt(1,((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                procedimiento.execute();
                listaPaciente.remove(tblPacientes.getSelectionModel().getSelectedIndex());
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
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarPaciente(?,?,?,?,?,?,?,?)}");
            Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
            registro.setDPI(txtDPI.getText());
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setFechaDeNacimiento(fecha.getSelectedDate());
            registro.setDireccion(txtDireccion.getText());
            registro.setOcupacion(txtOcupacion.getText());
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1,registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getDPI());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaDeNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccion());
            procedimiento.setString(7, registro.getOcupacion());
            procedimiento.setString(8, registro.getSexo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
      
    public void desactivarControles(){
      txtDPI.setEditable(false);
      txtNombres.setEditable(false);
      txtApellidos.setEditable(false);
      fecha.setDisable(true);
      txtEdad.setEditable(false);
      txtOcupacion.setEditable(false);
      txtDireccion.setEditable(false);
      txtSexo.setEditable(false);
    }
    
    
     public void activarControles(){
      txtDPI.setEditable(true);
      txtNombres.setEditable(true);
      txtApellidos.setEditable(true);
      fecha.setDisable(false);
      txtEdad.setEditable(false);
      txtOcupacion.setEditable(true);
      txtDireccion.setEditable(true);
      txtSexo.setEditable(true);
   
     }  
    
    
    public void limpiarControles(){
        txtDPI.setText(" ");
        txtNombres.setText(" ");
        txtApellidos.setText(" ");
        fecha.setSelectedDate(null);
        txtEdad.setText(" ");
        txtOcupacion.setText(" ");
        txtDireccion.setText(" ");
        txtSexo.setText(" ");
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
    
     public void ventanaContacto(){
        escenarioPrincipal.ventanaContacto();
    }
}

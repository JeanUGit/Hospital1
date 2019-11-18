
import java.sql.ResultSetMetaData;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Jean carlos
 */
public class FrmCrud extends javax.swing.JFrame {

    /**
     * Creates new form FrmCrud
     */
    boolean encontrado = false; 
    public FrmCrud() {
        initComponents();
        btnActualizar.setVisible(false);
        btnEliminar.setVisible(false);
        this.setLocationRelativeTo(null);
        Func_buscarEps();
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("folder2.png"));


        return retValue;
    }
    
    public void Func_buscarEps(){
        Conexxion conn = new Conexxion();
        Connection conx = conn.Conectar(); 
        try{
            Statement st = conx.createStatement();
            ResultSet rs;
            st.executeQuery("SELECT Descripcion FROM tbleps ");
            rs = st.getResultSet();
            while(rs.next())
            {
                cbxEps.addItem(rs.getString(1));
            }
            
        }catch(Exception e){
            System.out.println("no conectado"+e.getMessage());
        }   
    }
    
    public void Func_Limpiar(){
        encontrado = false;
        txtDocumento.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        cbxEps.setSelectedIndex(0);
        FechaN.setCalendar(null);
        btnActualizar.setVisible(false);
        btnEliminar.setVisible(false);
        txtDocumento.setEnabled(true);
        txtDocumento.setForeground(Color.BLACK);
        
    }
    
    public void Func_Guardar(){
          
               Date fecha = FechaN.getDate();
               DateFormat f = new SimpleDateFormat("YYYY-MM-dd");
               String fecha2 = f.format(fecha);
               //JOptionPane.showMessageDialog(null,"fecha" + fecha2);
               Conexxion conn = new Conexxion();
               Connection conx = conn.Conectar();
               try{
                   PreparedStatement pst = conx.prepareStatement("INSERT INTO tblpacientes(PKDocumento, Nombre, Apellido, FechaNacimiento, Correo, Telefono, FKId_TblEps) VALUES (?,?,?,?,?,?,?)");
                   pst.setString(1,txtDocumento.getText());
                   pst.setString(2,txtNombre.getText());
                   pst.setString(3,txtApellido.getText());
                   pst.setString(4,fecha2);  
                   pst.setString(5,txtCorreo.getText());
                   pst.setString(6,txtTelefono.getText());
                   pst.setInt(7,cbxEps.getSelectedIndex());
                   pst.executeUpdate();
                   Func_Limpiar();
                   JOptionPane.showMessageDialog(null,"Guardado","YEAH!",JOptionPane.INFORMATION_MESSAGE);
               }catch(SQLException e){
                   System.out.println("No guardó"+ e.getMessage());
               }
   }

    public void  Func_Buscar(){
      Conexxion conn = new Conexxion();
      Connection conx = conn.Conectar(); 
      try{
          Statement st = conx.createStatement();
          ResultSet rs;
          st.executeQuery("SELECT * FROM tblpacientes where PKDocumento="+txtDocumento.getText()+" ");
          rs = st.getResultSet();
          if(rs.next())
          {
            txtDocumento.setEnabled(false);
            txtDocumento.setText(rs.getString(1));
            txtNombre.setText(rs.getString(2));
            txtApellido.setText(rs.getString(3));
            FechaN.setDate(rs.getDate(4));
            txtCorreo.setText(rs.getString(5));
            txtTelefono.setText(rs.getString(6));
            cbxEps.setSelectedIndex(rs.getInt(7));
            btnActualizar.setVisible(true);
            btnEliminar.setVisible(true);
            encontrado=true;
          }
          else{
              JOptionPane.showMessageDialog(null,"NO EXISTE ","Mensaje de Error",JOptionPane.ERROR_MESSAGE);
          }
      }catch(Exception e){
          System.out.println("no conectado"+e.getMessage());
      }
  }
    
     public void  Func_ValidarExistencia(){
      Conexxion conn = new Conexxion();
      Connection conx = conn.Conectar(); 
      try{
          Statement st = conx.createStatement();
          ResultSet rs;
          st.executeQuery("SELECT COUNT(PKDocumento) FROM tblpacientes where PKDocumento="+txtDocumento.getText()+" ");
          rs = st.getResultSet();
          if(rs.next())
          {
              int dato = Integer.parseInt( rs.getString(1));
             if(dato >= 1)
             {
                 JOptionPane.showMessageDialog(null,"Numero De Documento ya registrado","",JOptionPane.ERROR_MESSAGE);
                 txtDocumento.setForeground(Color.red);
             }
             else
             {
               Func_Guardar();
               txtDocumento.setForeground(Color.BLACK);
             }
          }
      }catch(Exception e){
          System.out.println("no conectado"+e.getMessage());
      }
  }

    public void Func_Actualizar(){
        if(encontrado)
        {
            Date fecha = FechaN.getDate();
            DateFormat f = new SimpleDateFormat("YYYY-MM-dd");
            String fecha2 = f.format(fecha);
            //JOptionPane.showMessageDialog(null,"Fecha: "+fecha2);
            Conexxion conn = new Conexxion();
            Connection conx = conn.Conectar();
            try {
                 PreparedStatement pst = conx.prepareStatement("UPDATE tblpacientes SET Nombre=?, Apellido=?, FechaNacimiento=?, Correo=?, Telefono=?, FKId_TblEps=? WHERE PKDocumento = ?");
                 pst.setString(1, txtNombre.getText());
                 pst.setString(2, txtApellido.getText());
                 pst.setString(3, fecha2);
                 pst.setString(4, txtCorreo.getText());
                 pst.setString(5, txtTelefono.getText());
                 pst.setInt(6, cbxEps.getSelectedIndex());
                 pst.setString(7, txtDocumento.getText());
                 pst.executeUpdate();
                 JOptionPane.showMessageDialog(null,"Datos Actualizados");
                 Func_Limpiar(); 
            } 
            catch (Exception e) 
            {
                System.out.println(e.getMessage());
            }
        }else{JOptionPane.showMessageDialog(null,"LO siento  pero no puedo actualizar","Head shoot",JOptionPane.WARNING_MESSAGE);}
        
        
       
    }
    
    public void Func_Eliminar(){
        if(encontrado)
        {
             Conexxion conn = new Conexxion();
            Connection conx = conn.Conectar();
            try {
                String dato = JOptionPane.showInputDialog("Ingrese el documento a eliminar");
                Statement st = conx.createStatement();
                if(JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar los datos de esta persona ["+dato+"] ?")==0)
                {
                    st.executeUpdate("delete from tblpacientes where PKDocumento="+dato+"  ");
                    Func_Limpiar();
                    JOptionPane.showMessageDialog(null,"Dato borrado con exito");
                } 
                Func_Limpiar();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
        }else{JOptionPane.showMessageDialog(null,"LO siento  pero no puedo Eliminar","Head shoot",JOptionPane.WARNING_MESSAGE);}
    }
   
    public void Func_mostrar(){
        int posicion = btnMostrar.getX();
        if(posicion == 0)
        {
            Func_CargarDatos();
            panelRegistro.setVisible(false);
            Animacion.Animacion.mover_derecha(-430,0,2, 2, PanelMostarDatos);
            Animacion.Animacion.mover_derecha(0,430,2, 2, btnMostrar);
        }
        else
        {
            Animacion.Animacion.mover_izquierda(0, -430, 2, 2, PanelMostarDatos);
            Animacion.Animacion.mover_izquierda(430, 0, 2, 2, btnMostrar);
            panelRegistro.setVisible(true);
            
        }
        
    }
    
    public void Func_CargarDatos(){
        
        Conexxion conn = new Conexxion();
        Connection conx = conn.Conectar(); 
        DefaultTableModel modelo =  new DefaultTableModel();
        modelo.addColumn("Documento");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Fecha Nacimiento");
        modelo.addColumn("Correo");
        modelo.addColumn("Telefono");
        modelo.addColumn("Eps");
        try{
            Statement st = conx.createStatement();
            ResultSet rs;
            ResultSetMetaData rsm;
            st.executeQuery("SELECT PKDocumento, Nombre , Apellido, FechaNacimiento, Correo, Telefono, TblEps.Descripcion FROM tblpacientes,tbleps WHERE tbleps.Id = tblpacientes.FKId_TblEps");
            rs = st.getResultSet();
                while (rs.next())
                {
                    String [] Fila = new String[7]; 
                    Fila[0] = rs.getString(1);
                    Fila[1] = rs.getString(2);
                    Fila[2] = rs.getString(3);
                    Fila[3] = rs.getString(4);
                    Fila[4] = rs.getString(5);
                    Fila[5] = rs.getString(6);
                    Fila[6] = rs.getString(7);
                    modelo.addRow(Fila);
                    
                }
                tabla.setModel(modelo);

            
        }catch(Exception e){
            System.out.println("no conectado"+e.getMessage());
        }   
    }
    
    
   

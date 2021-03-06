    package Controller.productoDao;

import Model.Conexion;
import Model.Producto;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
public class ProductoDao {

    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

    public List listar() {
        List<Producto>productos=new ArrayList();
        String sql = "select * from producto";
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt(1));
                p.setImagProducto(rs.getBinaryStream(2));
                p.setNomProducto(rs.getString(3));
                p.setPrecio(rs.getInt(4));
                productos.add(p);
            }

        } catch (Exception e) {

        }
        return productos;
    }

    public void listarImagen(int id, HttpServletResponse response) {
        String sql = "select * from producto where id_Producto="+id;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        response.setContentType("image/*");
        try {
            outputStream = response.getOutputStream();
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next());
            {
                inputStream = rs.getBinaryStream(2);
                System.out.println("Encuentra un inaaresultado adicional ");
            }
            
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
           
            int i = 0;

            while ((i = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(i);

            }
            
            System.out.println("Me debe estar manaddo ");
        } catch (SQLException e ) {
            System.err.println("Producto "+ e.getMessage());

        } catch (IOException ex) {
            System.out.println("error arhcio IO " + ex.getMessage());
            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
System.out.print("Ya tiene la imahen en memori a ");
    }
    
  public void agregar(Producto p){
      
        System.out.println("Nombre "+ p.getNomProducto());
        System.out.println("precio"+ p.getPrecio());
        System.out.println("imagen"+ p.getImagProducto());
        
        String sql="insert into producto ( nombre_producto, imagen_producto, precio) values(?,?,?)";
                   
                    
                    
        try {
             con=cn.getConexion();
            ps=con.prepareStatement(sql);
            
            ps.setString(1, p.getNomProducto());
            ps.setBlob(2, p.getImagProducto());
            ps.setInt(3, p.getPrecio());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, e);
            
            
            System.out.println("Error agrgar )"+e.getMessage());
             System.out.println("Error agrgar )"+e.getLocalizedMessage());
             System.out.println("Descripcion )"+e.getStackTrace());
            
       
        }
    }
  //Metodo para consultar y porteriormente agregar al carrito
    public Producto consultarId(int id_producto) {

        Conexion cn = new Conexion();
        String sql = "SELECT * FROM producto WHERE id_producto=" + id_producto;
        Producto pro = new Producto();
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                pro.setId(rs.getInt(1));
                pro.setImagProducto(rs.getBinaryStream(2));
                pro.setNomProducto(rs.getString(3));
                pro.setPrecio(rs.getInt(4));
                
            }
        } catch (SQLException e) {
        }

        return pro;
    }

    public static void main(String[] args) {
        ProductoDao pdao = new ProductoDao();
        pdao.listar();
    }

   
}
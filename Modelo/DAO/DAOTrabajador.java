/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.DAO;
import Vista.Interfaces.MYSQL;
import Vista.Interfaces.SQL2;
import Vista.Interfaces.VARIABLES;
import Controlador.Excepciones.ExcepcionCorreo;
import Controlador.VO.Trabajador;
import Controlador.Conexiones.DataConnectionSQL;
import Controlador.Conexiones.DataConnectionMYSQL;
import Controlador.Archivo;
import com.csvreader.CsvWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
/**
 *
 * @author PROGRAMADOR
 */
public class DAOTrabajador extends Archivo<Trabajador> implements VARIABLES,SQL2<Trabajador>,MYSQL<Trabajador>{
    private static DataConnectionSQL conexionSQL; 
    private static DataConnectionMYSQL conexionMYSQL;
    static ExcepcionCorreo excepciones;
    Trabajador empleado;
    
    public DAOTrabajador()throws Exception{
            conexionSQL = DataConnectionSQL.getInstance();
            conexionMYSQL = DataConnectionMYSQL.getInstance();
    }

    @Override
    public void crear(Trabajador algo) throws SQLException {
        //
        String  rutaDatosPersonal = "C:\\INTRANET\\".concat(Trabajador.NOMBRE_ARCHIVO);//"C:\\prueba Fichero\\archivo.txt";
        File archivo = new File(rutaDatosPersonal);
        if(archivo.exists()){  
            String loadQuery =  "LOAD DATA LOCAL INFILE 'C:/INTRANET/"
                                .concat(Trabajador.NOMBRE_ARCHIVO)
                                .concat("'")
                                .concat(" INTO TABLE trabajador\n")
                                .concat(" FIELDS ENCLOSED BY '\"' TERMINATED BY '&' ESCAPED BY '\"'\n")
                                .concat(" LINES TERMINATED BY '\\r\n';");
            PreparedStatement stmt = conexionMYSQL.getCon().prepareStatement(loadQuery);
            stmt.execute();
            archivo.delete();      
        }else{
            throw new SQLException("EL ".concat(Trabajador.NOMBRE_ARCHIVO).concat(" no existe. Se ha borrado."));
        }
    }

    @Override
    public void eliminar() throws SQLException {
        //
        String DELETE="DELETE FROM `trabajador`";
        PreparedStatement ps;
        ps = conexionMYSQL.getCon().prepareStatement(DELETE);
        ps.executeUpdate();
    }
    /*
        Overload de metodo read en interface
    */
    @Override
    public ArrayList<Trabajador> read(int mes) throws Exception {
        //
        ArrayList<Trabajador> listaTrabajador = new ArrayList<>();
        String sql ="SELECT softland.sw_ccostoper.codiCC AS centroCosto,SUBSTRING(softland.sw_ccostoper.codiCC,0,6) AS centroCostoAbr,softland.cwtcarg.CarCod AS codCargo,softland.cwtcarg.CarNom AS nombreCargo,softland.sw_ccostoper.codiCC AS centroCosto,SUBSTRING(softland.sw_ccostoper.codiCC,0,6) AS centroCostoAbr,softland.cwtccos.DescCC AS unidad,codBancoSuc,codEstudios,appaterno,apmaterno,nombre,Email,rut,direccion,codComuna,codCiudad,telefono1,telefono2,telefono3,fechaNacimient,sexo,estadoCivil,nacionalidad,fechaIngreso,fechaPrimerCon,fechaFiniquito, estado"+mes+" AS estado,softland.sw_personal.ficha,softland.cwtcarg.CarCod AS codCargo,softland.cwtcarg.CarNom AS nombreCargo \n" +
                "FROM softland.sw_personal INNER JOIN softland.sw_estadoper ON softland.sw_estadoper.ficha=softland.sw_personal.ficha\n" +
                "INNER JOIN softland.sw_cargoper ON softland.sw_cargoper.ficha=softland.sw_personal.ficha \n" +
                "INNER JOIN softland.cwtcarg ON softland.cwtcarg.CarCod=softland.sw_cargoper.carCod \n" +
                "INNER JOIN softland.sw_ccostoper on softland.sw_ccostoper.ficha=softland.sw_personal.ficha \n" +
                "INNER JOIN softland.cwtccos on softland.cwtccos.CodiCC=softland.sw_ccostoper.codiCC \n" +
                "WHERE softland.sw_ccostoper.vigHasta LIKE '%9999%' AND softland.sw_cargoper.vigHasta LIKE '%9999%' ORDER BY softland.sw_personal.ficha DESC";//"WHERE softland.sw_cargoper.vigHasta LIKE '%9999%'";
        PreparedStatement ps = conexionSQL.getCon().prepareStatement(sql);

//        ArrayList<Trabajador> trabajadorNuevo = new ArrayList<>(); //Creamos un arraylist para meter los datos extraídos de la tabla

        ResultSet rs = ps.executeQuery();
        conexionSQL.getCon().commit();
        int contador = 0;
        while(rs.next()){
                        Trabajador nuevoTrabajador = new Trabajador(
                        rs.getString("codBancoSuc"),
                        rs.getString("codEstudios"),
                        rs.getString("appaterno"), 
                        rs.getString("apmaterno"),
                        rs.getString("nombre"),                      
                        rs.getString("Email"),
                        rs.getString("rut"),
                        rs.getString("direccion"),
                        rs.getString("codComuna"),                    
                        rs.getString("codCiudad"), 
                        rs.getString("telefono1"), 
                        rs.getString("telefono2"), 
                        rs.getString("telefono3"), 
                        rs.getDate("fechaNacimient"),                    
                        rs.getString("sexo"),                     
                        rs.getString("estadoCivil"),  
                        rs.getString("nacionalidad"),                     
                        rs.getDate("fechaIngreso"),  
                        rs.getDate("fechaPrimerCon"), 
                        rs.getDate("fechaFiniquito"),
                        rs.getString("estado"), 
                        rs.getString("ficha") ,
                        //areaTrabajo
                        ++contador,
                        rs.getString("centroCosto"),
                        rs.getString("centroCostoAbr"),
                        rs.getString("nombreCargo"),
                        rs.getString("codCargo")
                        );
                listaTrabajador.add(nuevoTrabajador);
        }
        HashSet<Trabajador> hs = new HashSet<>();        
        hs.addAll(listaTrabajador);
        listaTrabajador.clear();
        listaTrabajador.addAll(hs);       
        return listaTrabajador;
    }
    /*
    Overload para crear lista P171
    */
    @Override
    public ArrayList<String> readVariableP171(int mes) throws Exception{
        //
        DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");
        LocalDateTime fechaHora = LocalDateTime.now();
        String sql ="SELECT ficha,valor AS P171 FROM softland.sw_variablepersona WHERE codVariable = 'P171' AND mes ="+mes;
        PreparedStatement ps = conexionSQL.getCon().prepareStatement(sql);

        ArrayList<String> ls = new ArrayList<>(); //Creamos un arraylist para meter los datos extraídos de la tabla

        ResultSet rs = ps.executeQuery();
        conexionSQL.getCon().commit();
        ls.add(mes+";"+fechaHora.format(formatoFechaHora)+";");
        while(rs.next()){
            //datos de la tabla
            ls.add(rs.getString("ficha")+"&&"+rs.getString("P171")+";");//nombre es el campo de la base de datos
        }
        return ls;
    }
    /*
    Overload para crear lista P267
    */
    @Override
    public ArrayList<String> readVariableP276(int mes) throws Exception{
        //
        DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");
        LocalDateTime fechaHora = LocalDateTime.now();
         String sql ="SELECT ficha,sum(valor) AS P267 FROM (\n" +
                    "    SELECT ficha,codVariable,mes,CAST(valor AS INT) AS valor FROM softland.sw_variablepersona WHERE codVariable = 'P092' AND mes ="+mes+"  UNION all\n" +
                    "    SELECT ficha,codVariable,mes,CAST(valor AS INT) AS valor FROM softland.sw_variablepersona WHERE codVariable = 'P174' AND mes ="+mes+"  UNION all\n" +
                    "    SELECT ficha,codVariable,mes,CAST(valor AS INT) AS valor FROM softland.sw_variablepersona WHERE codVariable = 'P266' AND mes ="+mes+" \n" +
                    ") sw_variablepersona WHERE codVariable = 'P092' OR codVariable = 'P174' OR codVariable = 'P266' AND mes ="+mes+"  GROUP BY ficha";
            PreparedStatement ps = conexionSQL.getCon().prepareStatement(sql);

            ArrayList<String> ls = new ArrayList<>(); //Creamos un arraylist para meter los datos extraídos de la tabla

            ResultSet rs = ps.executeQuery();
            conexionSQL.getCon().commit();
            ls.add(mes+";"+fechaHora.format(formatoFechaHora)+";");
            while(rs.next()){
                //datos de la tabla
                ls.add(rs.getString("ficha")+"&&"+rs.getString("P267")+";");//nombre es el campo de la base de datos
            }
            return ls;
    }
    
    @Override
    public void agregarDatos(ArrayList<Trabajador> lista, char separador) throws IOException {
       Archivo.crear(Trabajador.NOMBRE_ARCHIVO, "C:\\INTRANET\\");
       //se inicia el archivo
       CsvWriter csvOutput = Archivo.inicializarCsv(separador);
       //
       lista.forEach(trabajador -> {
            try {
                    csvOutput.write(trabajador.getCodSucursal()); 
                    csvOutput.write(trabajador.getCodEstudios());
                    csvOutput.write(trabajador.get$apellidoP()); 
                    csvOutput.write(trabajador.get$apellidoM());
                    csvOutput.write(trabajador.get$nombres());                      
                    csvOutput.write(trabajador.getCorreo());
                    csvOutput.write(trabajador.get$run());
                    csvOutput.write(trabajador.getDireccion());
                    csvOutput.write(trabajador.getCodComuna());                    
                    csvOutput.write(trabajador.getCodCiudad()); 
                    csvOutput.write(trabajador.getFono1()); 
                    csvOutput.write(trabajador.getFono2()); 
                    csvOutput.write(trabajador.getFono3()); 
                    csvOutput.write(trabajador.get$fechaNacimiento().toString());                    
                    csvOutput.write("");//naciminetoHijo       
                    csvOutput.write("");//diasNacimiento
                    csvOutput.write(trabajador.get$sexo());                     
                    csvOutput.write(trabajador.getEstadoCivil());  
                    csvOutput.write(trabajador.getNacionalidad());                     
                    csvOutput.write(trabajador.getFechaIngreso().toString());  
                    csvOutput.write(trabajador.getFechaContrato().toString());  
                    csvOutput.write(trabajador.getFechaFiniquito().toString());  
                    csvOutput.write(trabajador.getEstado());  
                    csvOutput.write(trabajador.getFicha());
                    csvOutput.write(trabajador.getCodigoArea()+"");
                    csvOutput.endRecord();
            } catch (IOException ex) {
                //
                
            }
       });
       csvOutput.close();
    }
    /*
        VARIABLES P171 Y P267
    */
    @Override
    public void agregarDatos(ArrayList<String> lista,boolean isVariable,String nombreArchivo) throws IOException {
        Archivo.crear(nombreArchivo, "C:\\INTRANET\\");
        BufferedWriter bw;
        if(verificarArchivo()) {
                bw = new BufferedWriter(new FileWriter(getArchivo()));
                for(int i=0; i<lista.size();i++){
                    bw.write(lista.get(i));
                    }
            } else {
                bw = new BufferedWriter(new FileWriter(getArchivo()));
                for(int i=0; i<lista.size();i++){
                    bw.write(lista.get(i));
                    }
            }
        bw.close();
    }
    /*
     *
     *
     *
        FUNCIONES PARA INGRESAR TRABAJADARO EN MYSQL
     *
     *
     *
     *
     */
    @Override
    public void backup() throws SQLException {
        //
        PreparedStatement ps;  
        String query="CREATE TABLE IF NOT EXISTS trabajador_tmp LIKE trabajador;";
        ps = conexionMYSQL.getCon().prepareStatement(query); 
        ps.executeUpdate(); 
                //
        String DELETE="DELETE FROM trabajador_tmp";
        ps = conexionMYSQL.getCon().prepareStatement(DELETE); 
        ps.executeUpdate(); 
        
        String insert = "INSERT trabajador_tmp SELECT * FROM trabajador;";
        ps = conexionMYSQL.getCon().prepareStatement(insert); 
        ps.executeUpdate(); 
    }

    @Override
    public void restore() throws SQLException {
        
        String INSERT="INSERT trabajador SELECT * FROM trabajador_tmp;";
        PreparedStatement ps;
        ps = conexionMYSQL.getCon().prepareStatement(INSERT);
        ps.executeUpdate();
    }

    @Override
    public void deleteBackup() throws SQLException {
        
        String INSERT="DELETE FROM trabajador_tmp;";
        PreparedStatement ps;
        ps = conexionMYSQL.getCon().prepareStatement(INSERT);
        ps.executeUpdate();
    }

    @Override
    public void agregarDatos(Trabajador param, String nombreArchivo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

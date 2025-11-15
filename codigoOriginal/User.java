package codigoOriginal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    public Connection conectarBD() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver").newInstance(); 
            String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=123";
            conn = DriverManager.getConnection(url);
        } catch (Exception e) { }
        return conn;
    }
    public String nome="";
    public boolean result = false;
    public boolean verificarUsuario(String login, String senha) {
        String sql = "";
        Connection conn = conectarBD();
        //INSTRUÇÃO SQL
        sql = "select nome from usuarios ";
        sql += "Where login = '" + login + "'";
        sql += " and senha = '" + senha + "';";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                result = true;
                nome = rs.getString("nome");
            }
        } catch (Exception e) { }
        return result;
    }
    /*
 * =========================================================
 * MÉTODO 'main' PARA TESTE RÁPIDO
 * =========================================================
 */
public static void main(String[] args) {

    // 1. Crie uma instância da sua classe
    User testador = new User();

    System.out.println("Iniciando testes práticos...");

    
    boolean teste1 = testador.verificarUsuario("lopes", "123");
    System.out.println("Teste 1 (Sucesso 'lopes'/'123'): " + teste1); 

   
    boolean teste2 = testador.verificarUsuario("lopes", "senhaErrada");
    System.out.println("Teste 2 (Falha 'lopes'/'senhaErrada'): " + teste2); 

    
    System.out.println("---");
    System.out.println("Agora, DESLIGUE o seu PostgreSQL e pressione ENTER para rodar o Teste 3.");
    try {
        System.in.read(); 
    } catch (Exception e) {}

    
    boolean teste3 = testador.verificarUsuario("lopes", "123");
    System.out.println("Teste 3 (Erro - BD Desligado): " + teste3);
    
    }
}
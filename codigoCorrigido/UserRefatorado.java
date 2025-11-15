package codigoCorrigido;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement; // Importado para segurança
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Classe de usuário refatorada para boas práticas de segurança e conexão.
 */
public class UserRefatorado {

    // A URL deve vir de um arquivo de configuração, mas mantida aqui para o exercício.
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=123456";

    /**
     * Conecta ao banco de dados.
     * @return um objeto Connection.
     * @throws SQLException se a conexão falhar.
     */
    public Connection conectarBD() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Verifica as credenciais do usuário de forma segura, prevenindo SQL Injection.
     *
     * @param login O nome de usuário.
     * @param senha A senha do usuário.
     * @return true se o usuário for válido, false caso contrário.
     */
    public boolean verificarUsuario(String login, String senha) {

    String sqlQuery = "select nome from usuarios Where login = ? and senha = ?";

    try { // <-- O try normal (sem o "with-resources" por agora, para ficar mais simples)

        // --- ADICIONE ESTA LINHA ---
        Class.forName("org.postgresql.Driver");
        // ---------------------------

        Connection conn = conectarBD();
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, login);
        pstmt.setString(2, senha);
        ResultSet queryResult = pstmt.executeQuery();

        boolean resultado = queryResult.next();

        // Feche os recursos manualmente (já que tirámos o try-with-resources)
        queryResult.close();
        pstmt.close();
        conn.close();

        return resultado;

    } catch (SQLException | ClassNotFoundException e) { // Adicionado ClassNotFoundException
        System.err.println("Falha ao verificar usuário: " + e.getMessage());
        return false; 
    }
}
   public static void main(String[] args) {

        // 1. Crie uma instância da classe CORRETA
        UserRefatorado testador = new UserRefatorado(); // <-- CORRIGIDO

        System.out.println("Iniciando testes práticos...");

        // (O resto é igual)
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

package database;

import org.h2.jdbcx.JdbcConnectionPool;

public class Connection {

    //JDBC driver name and database URL
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./data/data;AUTO_SERVER=TRUE";

    //database credentials
    private static final String USER = "sa";
    private static final String PASS = "";

    private JdbcConnectionPool jdbcConnectionPool;

    public static final Connection INSTANCE = new Connection();

    private Connection() {
    }

    public void createConnectionPool() {
        jdbcConnectionPool = null;

        System.out.println("Creating a connection pool...");
        jdbcConnectionPool = getConnectionPool();

        System.out.println("Connection pool successfully created...");
    }

    /**
     * Create H2 JdbcConnectionPool
     *
     * @return Return the connection pool
     */
    public JdbcConnectionPool getConnectionPool() {
        JdbcConnectionPool cp = null;

        try {
            //register JDBC driver
            Class.forName(JDBC_DRIVER);

            //create a connection pool
            cp = JdbcConnectionPool.create(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return cp;
    }

    public void disposeConnectionPool() {
        if (jdbcConnectionPool != null) jdbcConnectionPool.dispose();
        System.out.println("Database successfully disconnected...");
    }

    public JdbcConnectionPool getJdbcConnectionPool() {
        return jdbcConnectionPool;
    }

}

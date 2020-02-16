package database;

import java.sql.SQLException;
import java.sql.Statement;

public class Table {

    /**
     * This method will create the root table that contains the list of all main items.
     * The root table will only be created ONCE during the lifespan of the database.
     */
    public void createRootTable() {
        java.sql.Connection conn = null;
        Statement stmt = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "CREATE TABLE IF NOT EXISTS \"Items\" (ENTRY VARCHAR(255))";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            System.out.println("Successfully created rootTable: \"" + "Items" + "\" in database...");

            //clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try
    }

    /**
     * This method will create the table for the specific item from the root table list.
     *
     * @param itemTableName The name for the table of the specific item
     */
    public void createItemTable(String itemTableName) {
        java.sql.Connection conn = null;
        Statement stmt = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "CREATE TABLE IF NOT EXISTS \"" + itemTableName + "\" (ENTRY VARCHAR(255))";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            System.out.println("Successfully created itemTable: \"" + itemTableName + "\" in database...");

            //clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            //check if created table name already exists
            if (e.getErrorCode() == 42101) {
                System.out.println("Table \"" + itemTableName + "\" already exists");
            } else {
                e.printStackTrace();
            }
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try
    }

    /**
     * This method will create the table for the specific entry from the item table list.
     *
     * @param entryTableName The name for the table of the specific entry in the item table
     */
    public void createEntryTable(String entryTableName) {
        java.sql.Connection conn = null;
        Statement stmt = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "CREATE TABLE \"" + entryTableName + "\" (ID bigint auto_increment,"
                    + "\"Order No.\" INT,"
                    + "\"Item Name\" VARCHAR(255),"
                    + "\"Unit Price\" DOUBLE,"
                    + "\"Quantity\" INT,"
                    + "\"Total Amount\" DOUBLE)";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            System.out.println("Successfully created entryTable: \"" + entryTableName + "\" in database...");

            //clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            //check if created table name already exists
            if (e.getErrorCode() == 42101) {
                System.out.println("Table \"" + entryTableName + "\" already exists");
            } else {
                e.printStackTrace();
            }
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try
    }

    public void renameTable(String currentTableName, String newTableName) {

        java.sql.Connection conn = null;
        Statement stmt = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            //ALTER TABLE current_table_name RENAME TO new_table_name
            String sql = "ALTER TABLE \"" + currentTableName + "\" RENAME TO \"" + newTableName + "\"";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            System.out.println("Successfully renamed table from \"" + currentTableName + "\" to \"" + newTableName + "\".");

            //clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try
    }

    public void delete(String tableName) {
        java.sql.Connection conn = null;
        Statement stmt = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "DROP TABLE \"" + tableName + "\"";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            System.out.println("Successfully deleted \"" + tableName + "\" from database...");

            //clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try
    }

    public void rename() {

    }

}

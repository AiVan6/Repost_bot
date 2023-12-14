package org.example;

import java.sql.*;
import java.util.ArrayList;

public class DB {

    private Connection conn;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public void open(){

        try {
            conn = null;

            String url = "jdbc:sqlite:database.db";
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void close() {
        try {
            conn.close();
            statement.close();
            resultSet.close();
//            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable(){
        try {
            statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Groups(" +
                                "id INTEGER NOT NULL UNIQUE);");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showTable(){
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Groups;");
            while (resultSet.next()){
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");

            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertTable(long  id){

        try {
            preparedStatement = null;
            String sql = "INSERT INTO Groups(id) VALUES (?);";
            preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
//            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public boolean checkChat(long id){
        try {
            String sql = "SELECT id FROM Groups WHERE id='"+id+"';";
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            if(resultSet.getLong("id") == id){
                resultSet.close();
                return true;
            }else {
                resultSet.close();
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOnId(long chatId){
        try {
            String sql = "DELETE FROM Groups WHERE id ="+chatId+";";
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Long> getChatList(){
        ArrayList<Long> arrayList = new ArrayList<>();
        try {
            String sql = "SELECT id FROM Groups;";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                arrayList.add(resultSet.getLong("id"));
            }
            return arrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

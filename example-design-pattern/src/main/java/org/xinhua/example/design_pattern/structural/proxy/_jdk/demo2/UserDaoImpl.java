package org.xinhua.example.design_pattern.structural.proxy._jdk.demo2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 0:12
 * @Description: 用户Dao实现类
 * @Version: 1.0
 */
public class UserDaoImpl implements UserDao {

    @Override
    public User get(Long id) {
        User user = null;
        Connection connection = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "select id,name,address,email from user where id = ?;";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setLong(1, id);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                long sid = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String email = resultSet.getString("email");
                user = new User(sid, name, address, email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.release(connection);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "select id,name,address,email from user;";
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                long sid = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String email = resultSet.getString("email");
                list.add(new User(sid, name, address, email));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.release(connection);
        }
        return list;
    }

    @Transaction
    @Override
    public User add(User user) {
        Connection connection = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "insert into user(name,address,email) values (?,?,?);";
            PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getName());
            pst.setString(2, user.getAddress());
            pst.setString(3, user.getEmail());
            pst.execute();
            ResultSet resultSet = pst.getGeneratedKeys();
            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                user.setId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.release(connection);
        }
        return user;
    }

    @Transaction
    @Override
    public User update(User user) {
        Connection connection = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "update user set name = ? , address = ? , email = ? where id = ?;";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, user.getName());
            pst.setString(2, user.getAddress());
            pst.setString(3, user.getEmail());
            pst.setLong(4, user.getId());
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.release(connection);
        }
        return user;
    }

    @Transaction
    @Override
    public void delete(Long id) {
        Connection connection = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "delete from user where id = ?;";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setLong(1, id);
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.release(connection);
        }
    }

}

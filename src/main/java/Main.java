import annotations.Id;
import entities.EntityManager;
import entities.User;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import static orm.MyConnector.createConnection;

public class Main {

    public static void main(String[] args) throws SQLException, IllegalAccessException, NoSuchFieldException {

        Connection connection = createConnection("root", "353569","custom_orm");
        EntityManager<User> entityManager = new EntityManager<User>(connection);
        User user = new User("Pavel", 20);
        user.setUsername("juko");

        entityManager.persist(user);
    }
}

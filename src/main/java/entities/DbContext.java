package entities;

import java.sql.SQLException;

public interface DbContext<E> {

    boolean persist(E entity) throws IllegalAccessException, SQLException, NoSuchFieldException;
    Iterable<E> find(Class<E> table) throws SQLException;

    Iterable<E> find(Class<E> table, String where);

    Iterable<E> findFirst(Class<E> table);

    Iterable<E> findFirst(Class<E> table, String where);

}

package entities;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class EntityManager<E> implements DbContext<E>{
    private Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    private Field getId(Class<?> entity){
        return Arrays.stream(entity.getDeclaredFields()).filter(x-> x.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(UnsupportedOperationException::new);
    }
    @Override
    public boolean persist(E entity) throws IllegalAccessException, SQLException, NoSuchFieldException {
       Field primaryKey = getId(entity.getClass());
       primaryKey.setAccessible(true);
       Object value = primaryKey.get(entity);
        if (value == null || (long) value == 0) {
            return doInsert(entity, primaryKey);
        } else {
            return doUpdate(entity,primaryKey);
        }
    }

    private boolean doUpdate(E entity, Field primaryKey) throws SQLException, NoSuchFieldException, IllegalAccessException {
        String tableName = getTableName(entity.getClass());
        String[] columns = getColumnsWithoutId(entity.getClass()).split(",");
        String [] values = getFieldValues(entity).split(",");

        List<String> statement = new ArrayList<>();
        for (int i = 0; i < columns.length; i++) {
            statement.add(columns[i] + " = " + values[i]);
        }
                String query = String.format("UPDATE %s SET %s", tableName,String.join(",\n", statement) );
        return false;
    }

    private String getName(E entity) throws NoSuchFieldException, IllegalAccessException {
       Class<?> clazz = entity.getClass();
        Field username = clazz.getField("username");
        return username.get(entity).toString();

    }

    private String getTableName(Class<?> aClass) {
        Entity[] annotationsByType = aClass.getAnnotationsByType(Entity.class);
        if (annotationsByType.length == 0){
            throw new UnsupportedOperationException();
        }
        return annotationsByType[0].name();
    }

    private boolean doInsert(E entity, Field primaryKey) throws SQLException, IllegalAccessException, NoSuchFieldException {
        String tableName = getTableName(entity.getClass());
        String getColumns = getColumnsWithoutId(entity.getClass());
        String values = getFieldValues(entity);

        String query = ("INSERT INTO " + tableName  + " (" + getColumns + ") "
                + "VALUES (" + values+")");

         return connection.prepareStatement(query).execute();
    }

    private String getFieldValues(E entity) throws IllegalAccessException {
        Class<?> clazz = entity.getClass();
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .filter(f -> f.isAnnotationPresent(Column.class))
                .collect(Collectors.toList());

List<String> fieldsValues = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object o = field.get(entity);
            if (o instanceof String || o instanceof LocalDate){
                fieldsValues.add("'"+o.toString()+"'");
            }else {
                fieldsValues.add(o.toString());
            }
        }
        return String.join(",", fieldsValues);
    }

    private String getColumnsWithoutId(Class<?> aClass) {
        List<Field> fields = Arrays.stream(aClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .filter(f -> f.isAnnotationPresent(Column.class))
                .collect(Collectors.toList());
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }


       return String.join(",",fieldNames);

    }

    @Override
    public Iterable<E> find(Class<E> table) throws SQLException {
        String tableName = table.getName();
        String query = String.format("SELECT * FROM %s",tableName);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return null;
    }



    @Override
    public Iterable<E> find(Class<E> table, String where) {
        return null;
    }

    @Override
    public Iterable<E> findFirst(Class<E> table) {
        return null;
    }

    @Override
    public Iterable<E> findFirst(Class<E> table, String where) {
        return null;
    }
}

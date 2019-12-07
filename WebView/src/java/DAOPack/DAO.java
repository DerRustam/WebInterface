package DAOPack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DAO<Obj> {

    public abstract ArrayList<Obj> getAll() throws SQLException;
    public abstract Obj convertToEntity(ResultSet rs) throws SQLException;
}

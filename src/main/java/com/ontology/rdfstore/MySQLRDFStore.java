package com.ontology.rdfstore;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.sdb.SDBFactory;
import org.apache.jena.sdb.Store;
import org.apache.jena.sdb.StoreDesc;
import org.apache.jena.sdb.sql.SDBConnection;
import org.apache.jena.sdb.store.DatabaseType;
import org.apache.jena.sdb.store.LayoutType;
import org.apache.jena.sdb.util.StoreUtils;

import java.sql.SQLException;

public class MySQLRDFStore implements IRDFStore {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    private Store store;

    public MySQLRDFStore(String dbUrl, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);
        StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
        SDBConnection sdbConnection = new SDBConnection(dbUrl, username, password);
        store = SDBFactory.connectStore(sdbConnection, storeDesc);
        if (!StoreUtils.isFormatted(store)) {
            store.getTableFormatter().create();
        }
    }

    public void storeRDFModel(Model dataModel) {
        Model model = SDBFactory.connectDefaultModel(store);
        model.add(dataModel);
        model.close();
    }

    public Model getModel() {
        return SDBFactory.connectDefaultModel(store);
    }

    public void clearStore() {
        if (store != null)
            store.getTableFormatter().truncate();
    }

    public void closeStore() {
        if (store != null)
            store.close();
    }
}

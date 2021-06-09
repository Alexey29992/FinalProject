package com.repairagency.database;

public class QueryBases {

    private QueryBases() {
    }

    public static final String CLIENT_REQUEST_BASE =
            "SELECT request.*, status.status_name FROM request " +
                    "JOIN status ON status_id = status.id";

    public static final String MANAGER_REQUEST_BASE = "SELECT request.*, status.status_name, master.login as master_login, client.login as client_login" +
            " FROM request" +
            " JOIN status ON status_id = status.id" +
            " LEFT JOIN user master ON master_id = master.id" +
            " LEFT JOIN user client ON client_id = client.id";

}

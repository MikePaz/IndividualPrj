package models;

public enum Role {
    ADMIN,
    READ,               // View the transacted data
    READ_WRITE,         // View and Edit the transacted data
    Read_WRITE_DELETE   // View, Edit and Delete the transacted data
}

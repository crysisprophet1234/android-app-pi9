package com.example.requirements_manager.db.exceptions;

public class DbException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DbException (Exception ex) {
        super("Unexpected error! " + ex.getMessage());
    }

}

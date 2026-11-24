package com.theojamilly.gcm.exception;

public class ProdutoJaExisteException extends RuntimeException {
    public ProdutoJaExisteException(String message) {
        super(message);
    }
}

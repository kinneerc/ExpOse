package edu.allegheny.schemaexperiment.doubler;

import java.lang.Exception;


public class SchemaDoublerException extends Exception{
	public SchemaDoublerException() { super(); }
	public SchemaDoublerException(String message) { super(message); }
	public SchemaDoublerException(String message, Throwable cause) { super(message, cause); }
	public SchemaDoublerException(Throwable cause) { super(cause); }
}


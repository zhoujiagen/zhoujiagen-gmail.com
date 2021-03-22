package com.spike.rtfsc.jena.javacc;

import java.io.StringReader;

import literal_plain.LiteralsTokenManager;
import literal_plain.Token;
import literal_plain.SimpleCharStream;

/**
 * 
 */
public class LiteralsRunner {

  public static void main(String[] args) {
    StringReader r = new StringReader("hello");
    SimpleCharStream s = new SimpleCharStream(r);
    LiteralsTokenManager ltm = new LiteralsTokenManager(s);
    Token t = ltm.getNextToken();
    System.out.println("Got a '" + t.image + "' token");
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ehsunbehravesh.woodpecker;

import javax.mail.PasswordAuthentication;

/**
 *
 * @author ehsun7b
 */
public class Authenticator extends javax.mail.Authenticator {

  private PasswordAuthentication authentication;

  public Authenticator(String username, String password) {
    authentication = new PasswordAuthentication(username, password);
  }

  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    return authentication;
  }
}
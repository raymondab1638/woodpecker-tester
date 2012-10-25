package com.ehsunbehravesh.woodpecker;

import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Woodpecker {

  //<editor-fold defaultstate="collapsed" desc="CONSTANTS">
  public static final double VERSION = 1.0;
  //</editor-fold>
  //<editor-fold defaultstate="collapsed" desc="FIELDS">
  private static String smtpServer;
  private static String port;
  private static String username;
  private static String password;
  private static String sender;
  private static String receiver;
  private static int intervalSeconds;
  private static int count;
  private static int currentCount;
  private static String messageContent;
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="METHODS">
  private static void showUsage() {
    PrintStream o = System.out;
    o.println("Usage: -s=smtp server\t-o=port number\t-a=sender address\t-r=receiver address\t-u=username\t-p=password\t-i=interval(seconds)\t-c=count");
  }

  private static String concatArgs(String[] args) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      String string = args[i];
      builder.append(" ".concat(string));
    }
    return builder.toString().trim();
  }

  private static HashMap<String, String> parseArgs(String allArgs) throws Exception {
    HashMap<String, String> result = new HashMap<>();
    String[] parts = allArgs.split(" ");
    if (parts.length <= 0) {
      throw new Exception("No argument");
    } else {
      for (int i = 0; i < parts.length; i++) {
        String part = parts[i].trim();
        String[] smallParts = part.split("=");
        if (smallParts.length != 2) {
          throw new Exception("Bad argument name");
        } else {
          result.put(smallParts[0], smallParts[1]);
        }
      }
    }

    return result;
  }

  private static void sendMail(String smtpServer, String port, String username, String password, String sender, String receiver, String subject, String messageContent) throws MessagingException {
    final String u = username;
    final String p = password;

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", smtpServer);
    props.put("mail.smtp.port", port);

    Authenticator authenticator = new Authenticator(u, p);
    Session session;
    session = Session.getDefaultInstance(props, authenticator);

    MimeMessage message = new MimeMessage(session);

    message.setFrom(new InternetAddress(sender));
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
    message.setSubject(subject);
    message.setText(messageContent);

    System.out.println("Start sending at: " + new Date());
    Transport.send(message);
    System.out.println("Sent successfully at: " + new Date());
  }

  private static void sendAllMessages() {
    for (currentCount = 0; currentCount < count; currentCount++) {

      StringBuilder builderContent = new StringBuilder();
      builderContent.append("Sent at: \t").append(new Date()).append("\n");
      builderContent.append("SMTP Server: \t").append(smtpServer).append("\n");
      builderContent.append("Port Number: \t").append(port).append("\n");
      builderContent.append("Sender: \t").append(sender).append("\n");
      builderContent.append("Receiver: \t").append(receiver).append("\n");
      builderContent.append("Interval: \t").append(intervalSeconds).append(" seconds").append("\n");
      builderContent.append("Count: \t").append(currentCount + 1).append("\n");
      builderContent.append("Total: \t").append(count).append("\n");

      messageContent = builderContent.toString();

      String subject = "Test: ".concat(smtpServer).concat(" (").concat((currentCount + 1) + "").concat(")");
      try {
        sendMail(smtpServer, port, username, password, sender, receiver, subject, messageContent);
        Thread.sleep(intervalSeconds * 1000);
      } catch (Exception ex) {
        System.out.println("Error: " + ex.getMessage());
      }
    }
  }
  //</editor-fold>

  public static void main(String[] args) {
    if (args.length != 8 && args.length != 1) {
      showUsage();
    } else if (args.length == 1) {
      if (args[0].trim().equalsIgnoreCase("-version") || (args[0].trim().equalsIgnoreCase("--version") || args[0].trim().equalsIgnoreCase("-v"))) {
        System.out.println("version " + VERSION);
      } else {
        showUsage();
      }
    } else {
      String allArgs = concatArgs(args);
      try {
        HashMap<String, String> arguments = parseArgs(allArgs);

        smtpServer = arguments.get("-s");
        port = arguments.get("-n");
        sender = arguments.get("-a");
        receiver = arguments.get("-r");
        username = arguments.get("-u");
        password = arguments.get("-p");
        intervalSeconds = Integer.parseInt(arguments.get("-i"));
        count = Integer.parseInt(arguments.get("-c"));

        sendAllMessages();
      } catch (Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        showUsage();
      }
    }
  }
}

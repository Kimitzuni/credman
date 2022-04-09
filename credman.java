/*
 * credman
 * =======
 *
 * /credman/ is a credential manager for Linux, BSD, macOS and
 * Windows
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.*;

class credman {

    public static String OSName = System.getProperty("os.name");
    public static String UserHome = System.getProperty("user.home");
    public static String DirSep = "/";
    public static String ConfigFile = "creds.xml";
    public static String ConfigDir = "credman";
    public static String ConfigPath = "";
    public static double version = 0.131;

    public static void main(String[] args) {
//        System.out.println(OSName);

        setupVars(); createDirs(); logo(); menu();
    }

    public static void setupVars() {
        if (OSName.indexOf("Windows") != -1) {
            DirSep = "\\";
            ConfigPath = UserHome + "\\AppData\\Local\\" + ConfigDir;
        } else {
            DirSep = "/";
            ConfigPath = UserHome + "/.config/" + ConfigDir;
        }
    }

    public static void createDirs() {
        File Path = new File(ConfigPath);
		boolean state = Path.mkdir();
    }

    public static void logo() {
        System.out.println(
        " ██████╗██████╗ ███████╗██████╗ ███╗   ███╗ █████╗ ███╗   ██╗\n" +
        "██╔════╝██╔══██╗██╔════╝██╔══██╗████╗ ████║██╔══██╗████╗  ██║\n" +
        "██║     ██████╔╝█████╗  ██║  ██║██╔████╔██║███████║██╔██╗ ██║\n" +
        "██║     ██╔══██╗██╔══╝  ██║  ██║██║╚██╔╝██║██╔══██║██║╚██╗██║\n" +
        "╚██████╗██║  ██║███████╗██████╔╝██║ ╚═╝ ██║██║  ██║██║ ╚████║\n" +
        " ╚═════╝╚═╝  ╚═╝╚══════╝╚═════╝ ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝\n" +
        "v" + version + " running on " + OSName + "\n" +
        "Configuration files are located at " + ConfigPath);
    }

    public static void menu() {
        Scanner readMenuOpt = new Scanner(System.in);
        Scanner getCredential = new Scanner(System.in);

        System.out.println("\n" + 
            "  1) List Available Credentials\n" +
            "  2) List Credential Info\n" + 
            "  3) Credits/License\n" +
            "  4) Exit"
        );

        String menuOpt = readMenuOpt.nextLine();
        switch (menuOpt) {
            case "1": 
                System.out.println("\nAvailable Credentials\n");
                parseXMLData(1, "");
                menu();

            case "2":
                System.out.println("\nGet Info for Which Credential? ");
                String credential = getCredential.nextLine();
                parseXMLData(2, credential);
                menu();
        }

    }

    public static void parseXMLData(int operation, String credential) {
        try {
            File ConfigXML = new File(ConfigPath + DirSep + ConfigFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(ConfigXML);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("entry");
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    switch(operation) {
                        case 1:
                            System.out.println(eElement.getAttribute("name"));

                        case 2:
                            if (eElement.getAttribute("name").toLowerCase().indexOf(credential.toLowerCase()) != -1) {
                                System.out.println("\n" + 
                                "Domain: " + eElement.getElementsByTagName("site").item(0).getTextContent() + "\n" +
                                "Username: " + eElement.getElementsByTagName("username").item(0).getTextContent() + "\n" +
                                "Password: " + eElement.getElementsByTagName("password").item(0).getTextContent());
                            }
                    }

                        
                }
            }
        } catch (Exception e) {
    		System.out.println("Could not open " + ConfigPath + DirSep + ConfigFile);
        }
    }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
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
		Path.mkdir();

        File ConfigXML = new File(ConfigPath + DirSep + ConfigFile);
        if (!ConfigXML.isFile()) {
            try {
                FileWriter ConfigWriter = new FileWriter(ConfigXML);
                ConfigWriter.write(
                    "<?xml version=\"1.0\"?>\n" +
                    "<credentials>\n" +
                    "</credentials>"
                );
                ConfigWriter.close();

            } catch (IOException e) {
                System.out.println("A");
            }
        }
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
        //#region Scanners
        Scanner readMenuOpt = new Scanner(System.in);
        Scanner getCredential = new Scanner(System.in);
        Scanner newCredName = new Scanner(System.in);
        Scanner newCredDomain = new Scanner(System.in);
        Scanner newCredUser = new Scanner(System.in);
        Scanner newCredPasswd = new Scanner(System.in);
        //#endregion

        System.out.println("\n" + 
            "  1) List Available Credentials\n" +
            "  2) List Credential Info\n" + 
            "  3) Add New Credentials\n" +
            "  4) Exit"
        );

        String menuOpt = "";
        menuOpt = readMenuOpt.nextLine();
        switch (menuOpt) {
            case "1": 
                System.out.println("\nAvailable Credentials\n");
                parseXMLData(1, "");
                break;

            case "2":
                System.out.println("\nGet Info for Which Credential? ");
                String credential = getCredential.nextLine();
                parseXMLData(2, credential);
                break;
            
            case "3": 
                System.out.print("Credential Name: ");
                String credname = newCredName.nextLine();

                System.out.print("Domain: ");
                String creddomain = newCredDomain.nextLine();

                System.out.print("Username: ");
                String creduname = newCredUser.nextLine();

                System.out.print("Password: ");
                String credpasswd = newCredPasswd.nextLine();

                modifyXML(1, credname, creddomain, creduname, credpasswd);
                break;
            
            case "4": System.exit(0);
            default: break;
        }
        menu();
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
                        case 1: System.out.println(eElement.getAttribute("name")); break;
                        case 2:
                            if (eElement.getAttribute("name").toLowerCase().indexOf(credential.toLowerCase()) != -1) {
                                System.out.println("\n" + 
                                "Domain: " + eElement.getElementsByTagName("site").item(0).getTextContent() + "\n" +
                                "Username: " + eElement.getElementsByTagName("username").item(0).getTextContent() + "\n" +
                                "Password: " + eElement.getElementsByTagName("password").item(0).getTextContent());
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
    		System.out.println("Could not open " + ConfigPath + DirSep + ConfigFile);
        }
    }

    public static void modifyXML(int operation, String name, String domain, String username, String passwd) {
        System.out.println("Adding '" + name + "' to credential list");

        try {
            File ConfigXML = new File(ConfigPath + DirSep + ConfigFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(ConfigXML);
            

            switch(operation) {
                case 1:
                    Element rootElement = doc.getDocumentElement();
                    Element entry = doc.createElement("entry");

                    Element Domain = doc.createElement("site");
                    Element Username = doc.createElement("username");
                    Element Passwd = doc.createElement("password");
                    rootElement.appendChild(entry);
                    entry.setAttribute("name", name);

                    Domain.appendChild(doc.createTextNode(domain));
                    Username.appendChild(doc.createTextNode(username));
                    Passwd.appendChild(doc.createTextNode(passwd));

                    entry.appendChild(Domain);
                    entry.appendChild(Username);
                    entry.appendChild(Passwd);
                    break;
            }

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(ConfigPath + DirSep + ConfigFile));
            transformer.transform(domSource, streamResult);

            DOMSource source = new DOMSource(doc);
        } catch (Exception e) {
	        System.out.println("An error occured while modifying the XML Data");
        }
    }
}

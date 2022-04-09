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
    public static double version = 0.1;

    public static void main(String[] args) {
//        System.out.println(OSName);

        setupVars();
        createDirs();
        menu();
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

    public static void menu() {
        System.out.println(
            " ██████╗██████╗ ███████╗██████╗ ███╗   ███╗ █████╗ ███╗   ██╗\n" +
            "██╔════╝██╔══██╗██╔════╝██╔══██╗████╗ ████║██╔══██╗████╗  ██║\n" +
            "██║     ██████╔╝█████╗  ██║  ██║██╔████╔██║███████║██╔██╗ ██║\n" +
            "██║     ██╔══██╗██╔══╝  ██║  ██║██║╚██╔╝██║██╔══██║██║╚██╗██║\n" +
            "╚██████╗██║  ██║███████╗██████╔╝██║ ╚═╝ ██║██║  ██║██║ ╚████║\n" +
            " ╚═════╝╚═╝  ╚═╝╚══════╝╚═════╝ ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝\n" +
            "v" + version + " running on " + OSName + "\n" +
            "Configuration files are located at " + ConfigPath);

            getAvailableCreds();        
    }

    public static void getAvailableCreds() {
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
                    System.out.println("\n==> " + eElement.getAttribute("name") + " <==\n" +
                        "Domain: " + eElement.getElementsByTagName("site").item(0).getTextContent() + "\n" +
                        "Username: " + eElement.getElementsByTagName("username").item(0).getTextContent() + "\n" +
                        "Password: " + eElement.getElementsByTagName("password").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
		System.out.println("Could not open " + ConfigPath + DirSep + ConfigFile);
//            e.printStackTrace();
        }
    }
}

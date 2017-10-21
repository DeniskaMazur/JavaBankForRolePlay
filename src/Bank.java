import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.io.*;
import java.util.HashMap;

public class Bank {

    //_______constants______________

    public static final String file = "ClientList";
    public static final String passFile = "ClientPasswordHashes";
    private static final String ClientBaseHashFileName = "Hash";
    private static final String keyfile = "openKey.key";
    private static final String algorithm ="DESede";
    private static final String passKeyfile = "passwordOpenKey.key";
    private static final String passBaseHashFileName = "PassHash";

    //_______bases___________________

    private static HashMap<String, Integer> clientBase;
    private static HashMap<String, Integer> passBase;

    //______configs__________________

    private static final Boolean EXIT_IF_BASE_CHANGED = true;

    public static HashMap<String, Integer> conigBase(String fileName, String keyfile, String hashFile){

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            StringBuilder file = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                file.append(line);
            }
            String hash = enCript(keyfile, hashFile);
            if (!Integer.toString(file.toString().hashCode()).equals(hash) && hash != null){
                System.err.println("Base has been changed!!!");
                if (EXIT_IF_BASE_CHANGED) System.exit(1);
            }
        }catch (IOException e){System.err.println("Base hasn't been checked!!!");}

        File base = new File(fileName);
        FileReader fileReader = null;
        BufferedReader reader;
        HashMap<String, Integer> configed = new HashMap<String, Integer>();

        try {
            fileReader = new FileReader(base);
        }catch (FileNotFoundException e){
            try {
                base.createNewFile();
                fileReader = new FileReader(base);
            }catch (IOException u){
                System.err.println("Problems with access or creating file (ClientList)");
                System.exit(1);
            }
        }
        reader = new BufferedReader(fileReader);

        String line;
        try {

            while (!((line = reader.readLine()) == null)){

                String name = line.substring(0, line.indexOf(' '));

                configed.put(name,
                        Integer.parseInt(line.substring(line.indexOf(' ') + 1, line.length()))
                );

            }

        }catch (IOException e){
            System.err.println("Problems with reading base.");
            System.exit(1);
        }

        return configed;

    }

    public static void Pay(String client, String password,String target, int count){

        clientBase = conigBase(file, keyfile, ClientBaseHashFileName);
        passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);

        if (checkPassword(passBase, client, password)){
            if (clientBase.get(client) >= count){
                clientBase.put(client, clientBase.get(client) - count);
                clientBase.put(target, clientBase.get(target) + count);
            } else System.out.println("Your balance is just " + clientBase.get(client) + ".");
        } else System.out.println("Wrong password.");

        savePasswordsAndClients();

    }

    public static String GetBalance(String client, String password){

        clientBase = conigBase(file, keyfile, ClientBaseHashFileName);
        passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);

        if (checkPassword(passBase, client, password))
            return clientBase.get(client).toString();
        else {
            System.out.println("Wrong password.");
            return null;
        }

    }

    public static Boolean createNewClient(String name, String password){

        clientBase = conigBase(file, keyfile, ClientBaseHashFileName);
        passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);

        for (String key: clientBase.keySet()){
            if (key.equals(name)) return false;
        }
        clientBase.put(name, 0);
        passBase.put(name, password.hashCode());

        savePasswordsAndClients();

        return true;

    }

    public static void View(){

        clientBase = conigBase(file, keyfile, ClientBaseHashFileName);

        for (String key: clientBase.keySet()){

            System.out.println(key);

        }

    }

    public static boolean changePassword(String client, String oldPassword, String newPassword){

        clientBase = conigBase(file, keyfile, ClientBaseHashFileName);
        passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);

        if (checkPassword(passBase, client, oldPassword)){

            passBase.put(client, newPassword.hashCode());
            savePasswordsAndClients();
            return true;

        }

        return false;

    }

    public static void Save(HashMap<String, Integer> map, String file, String keyfile){

        try {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < map.keySet().size(); i++){
                String name = map.keySet().toArray()[i].toString();
                builder.append(name + " " + map.get(name));
            }
            saveInFile(Integer.toString(builder.toString().hashCode()), file, keyfile);
        }catch (Exception e){
            System.err.println("Problems with saving hash");
            System.exit(1);
        }
        finally {
            System.out.println("Hash saved");
        }

    }

    public static void printAll(HashMap<String, Integer> map, String file){

        StringBuilder builder = new StringBuilder();

        for (String name: clientBase.keySet()){

            builder.append(name + " " + map.get(name) + "\n");

        }

        try {

            PrintWriter writer = new PrintWriter(new File(file));
            writer.print(builder.toString());
            writer.flush();

        }catch (IOException e){
            System.err.println("Problems with update.");
            System.exit(1);
        }

    }

    private static void saveInFile(String message, String file, String keyfile){

        FileOutputStream fos;

        try {

            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            SecretKey key = kg.generateKey();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            ObjectOutputStream oos = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream(file), cipher));
            oos.writeObject(message);

            fos = new FileOutputStream(keyfile);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
            DESedeKeySpec keyspec = (DESedeKeySpec) skf.getKeySpec(key, DESedeKeySpec.class);
            fos.write(keyspec.getKey());

            fos.close();
            oos.close();

        }catch (Exception e){
            System.err.println("Problems with cipher file.");
        }

    }

    private static String enCript(String keyfile, String hashFile){

        FileInputStream fis;
        try
        {

            fis = new FileInputStream(keyfile);
            byte[] keyspace = new byte[fis.available()];
            fis.read(keyspace);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
            DESedeKeySpec keyspec = new DESedeKeySpec(keyspace);
            SecretKey key = skf.generateSecret(keyspec);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            ObjectInputStream ois = new ObjectInputStream(new CipherInputStream(new FileInputStream(hashFile), cipher));
            String secret = (String)ois.readObject();
            fis.close();
            ois.close();

            return secret;

        }
        catch (Exception e) {}

        return null;

    }

    private static void savePasswordsAndClients(){

        printAll(clientBase, file);
        Save(clientBase, ClientBaseHashFileName, keyfile);
        printAll(passBase, passFile);
        Save(passBase, passBaseHashFileName, passKeyfile);

    }

    private static Boolean checkPassword(HashMap<String, Integer> base, String name, String password){

        return (base.get(name) == password.hashCode());

    }

}

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.io.*;
import java.util.HashMap;

class Bank {

    //_______constants______________

    private static final String file = "ClientList";
    private static final String passFile = "ClientPasswordHashes";
    private static final String clientBaseHashFileName = "Hash";
    private static final String keyfile = "openKey.key";
    private static final String algorithm ="DESede";
    private static final String passKeyfile = "passwordOpenKey.key";
    private static final String passBaseHashFileName = "PassHash";
    private static final String cardUids = "cardUids";
    private static final String CardUidsHashFileName = "CardUidsHash";
    private static final String uidKeyFile = "uidKeyFile.key";
    private static final String card_prefix = "c";

    //_______bases___________________

    private static HashMap<String, Long> clientBase;
    private static HashMap<String, Long> passBase;
    private static HashMap<String, Long> uidBase;

    //______configs__________________

    private static final Boolean EXIT_IF_BASE_CHANGED = false;

    private static HashMap<String, Long> conigBase(String fileName, String keyfile, String hashFile){

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
        HashMap<String, Long> configed = new HashMap<>();

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
                        Long.parseLong(line.substring(line.indexOf(' ') + 1, line.length()))
                );

            }

        }catch (IOException e){
            System.err.println("Problems with reading base.");
            System.exit(1);
        }

        return configed;

    }

    static boolean Pay(String client, String password,String target, int count, Boolean hash, String user_card){

        clientBase = conigBase(file, keyfile, clientBaseHashFileName);
        passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);
        Boolean act = false;

        if (checkPassword(passBase, client, password, hash, user_card) && isClient(target, "u")){
            if (user_card.equals(card_prefix)) client = getClientWithUid(client);
            if ((clientBase.get(client) >= count) && count > 0){
                clientBase.put(client, clientBase.get(client) - count);
                clientBase.put(target, clientBase.get(target) + count);
                act = true;
            }
        }

        savePasswordsAndClients();

        return act;

    }

    static String GetBalance(String client, String password, Boolean hash, String user_card){

        clientBase = conigBase(file, keyfile, clientBaseHashFileName);
        passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);

        if (checkPassword(passBase, client, password, hash, user_card)){
            if (user_card.equals(card_prefix)) client = getClientWithUid(client);
            return clientBase.get(client).toString();
        }
        else {
            return null;
        }

    }

    static Boolean createNewClient(String name, String uid, String password, Boolean hash){

        clientBase = conigBase(file, keyfile, clientBaseHashFileName);
        passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);
        uidBase = conigBase(cardUids, uidKeyFile, CardUidsHashFileName);

        for (String key: clientBase.keySet()){
            if (key.equals(name)) return false;
        }
        for (String key: uidBase.keySet()){
            if (uidBase.get(key).equals(uid)) return false;
        }
        clientBase.put(name, 0L);
        if (hash){passBase.put(name, Long.parseLong(password));}
        else passBase.put(name, (long)password.hashCode());
        uidBase.put(name, Long.parseLong(uid));

        savePasswordsAndClients();

        return true;

    }

    static String viewAsStr(){

        clientBase = conigBase(file, keyfile, clientBaseHashFileName);
        StringBuilder builder = new StringBuilder();

        for (String key: clientBase.keySet()){

            builder.append(key);
            builder.append("_");

        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();

    }

    static boolean changePassword(String client, String oldPassword, String newPassword, Boolean hash, String user_card){

        clientBase = conigBase(file, keyfile, clientBaseHashFileName);
        passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);

        if (checkPassword(passBase, client, oldPassword, hash, user_card)){

            if (user_card.equals(card_prefix)) client = getClientWithUid(client);
            passBase.put(client, Long.parseLong(newPassword));
            savePasswordsAndClients();
            return true;

        }

        return false;

    }

    static boolean isClient(String client, String user_card){

        clientBase = conigBase(file, keyfile, clientBaseHashFileName);
        if (user_card.equals(card_prefix)){client = getClientWithUid(client);}
        return !(clientBase.get(client) == null);

    }

    private static Boolean checkPassword(HashMap<String, Long> base, String name, String password, Boolean hash, String user_card){

        if (user_card.equals(card_prefix)){name = getClientWithUid(name);}

        if (hash){

            return (Long.toString(base.get(name)).equals(password));

        }else {

            return (base.get(name) == password.hashCode());

        }

    }

    static Boolean checkPasswordNotConf(String client, String password, Boolean hash, String user_card){

        passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);
        return checkPassword(passBase, client, password, hash, user_card);

    }

    private static void Save(HashMap<String, Long> map, String file, String keyfile){

        try {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < map.keySet().size(); i++){
                String name = map.keySet().toArray()[i].toString();
                builder.append(name + " " + map.get(name));
            }
            saveInFile(Long.toString(builder.toString().hashCode()), file, keyfile);
        }catch (Exception e){
            System.err.println("Problems with saving hash");
            System.exit(1);
        }

    }

    private static void printAll(HashMap<String, Long> map, String file){

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
        catch (Exception e) {e.printStackTrace();}

        return null;

    }

    private static void savePasswordsAndClients(){

        conigIfNull();

        printAll(clientBase, file);
        Save(clientBase, clientBaseHashFileName, keyfile);
        printAll(passBase, passFile);
        Save(passBase, passBaseHashFileName, passKeyfile);
        printAll(uidBase, cardUids);
        Save(uidBase, CardUidsHashFileName, uidKeyFile);

    }

    private static String getClientWithUid(String uid){

        uidBase = conigBase(cardUids, uidKeyFile, CardUidsHashFileName);

        for (String key : uidBase.keySet()) {

            if (uidBase.get(key) == Integer.parseInt(uid)) return key;

        }

        return null;

    }

    private static void conigIfNull(){

        if (clientBase == null) clientBase = conigBase(file, keyfile, clientBaseHashFileName);
        if (passBase == null) passBase = conigBase(passFile, passKeyfile, passBaseHashFileName);
        if (uidBase == null) uidBase = conigBase(cardUids, uidKeyFile, CardUidsHashFileName);

    }


}
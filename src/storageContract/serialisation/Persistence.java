package storageContract.serialisation;

import storageContract.cargo.interfaces.Cargo;
import storageContract.logic.BusinessLogicImpl;

import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.math.BigDecimal;
import java.time.Duration;

import static storageContract.controller.CommandListAndMessages.SYMB_FAIL;

public class Persistence {
    public static void serializeJOS(BusinessLogicImpl bl, String filename) {
        if (bl == null || filename == null) {
            throw new IllegalArgumentException("ERROR: Filename or LogicElement was null.");
        }
        //checking for duplicates
        if (new File(filename).exists()) {
            throw new IllegalArgumentException("ERROR: The File you try to use for serialisation(JOS) already exists. Due to Business Policy an overwrite is not allowed.");
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(bl);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iaExc) {
            System.err.println(iaExc.getLocalizedMessage());
        }
    }

    public static BusinessLogicImpl deserializeJOS(String sourceFile) throws FileNotFoundException {

        if (sourceFile == null || sourceFile.isEmpty()) {
            throw new IllegalArgumentException("ERROR: Filename was empty.");
        }

        String path = sourceFile.replace(".txt", "").concat(".txt");
        if (!new File(path).exists()) {
            throw new FileNotFoundException("ERROR: File not found.");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sourceFile))) {
            return (BusinessLogicImpl) ois.readObject();
        } catch (IllegalArgumentException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        //file/Obj not found
        return null;
    }

    public static void serializeJBP(BusinessLogicImpl bl, String filename) {
        if (bl == null || filename == null) {
            throw new IllegalArgumentException(SYMB_FAIL+"ERROR: Filename or LogicElement was null.");
        }
        //checking for duplicates
        if (new File(filename).exists()) {
            throw new IllegalArgumentException(SYMB_FAIL+"The File you try to use for serialisation(JBP) already exists. Due to Business Policy an overwrite is not allowed.");
        }
        String path = filename.trim().replace(".txt", "").replace(".xml", "").concat(".xml");
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)))) {
            PersistenceDelegate pd = encoder.getPersistenceDelegate(Integer.class);
            encoder.setPersistenceDelegate(BigDecimal.class, pd);
            PersistenceDelegate pd2 = encoder.getPersistenceDelegate(String.class);
            encoder.setPersistenceDelegate(Duration.class, pd2);
            encoder.writeObject(bl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BusinessLogicImpl deserializeJBP(String filename) throws FileNotFoundException {
        if (filename == null||filename.isEmpty()) {
            throw new IllegalArgumentException("ERROR: Filename was null or empty.");
        }

        String path = filename.trim().replace(".txt", "").replace(".xml", "").concat(".xml");
        if(!new File(path).exists()){
            throw new FileNotFoundException("ERROR: File not found.");
        }
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)))) {
            return (BusinessLogicImpl) decoder.readObject();
        } catch (FileNotFoundException fnfExc) {
            fnfExc.printStackTrace();
        }
        return null;
    }

    public static void serializeSingleCargo(BusinessLogicImpl bl, int positionToBeSerialized) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serializedSingleCargo.txt"));
                Cargo cargo = bl.getCargoByPosition(positionToBeSerialized);
                if(cargo == null){
                    throw new IllegalArgumentException(SYMB_FAIL + "No Cargo was found at the position specified.");
                }else{
                    oos.writeObject(cargo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (IllegalArgumentException iaExc){
                System.out.println(iaExc.getLocalizedMessage());
            }
    }

    public static Cargo deserializeSingleCargo() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serializedSingleCargo.txt"));
            return (Cargo) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

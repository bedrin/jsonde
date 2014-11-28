package com.jsonde.gui.license;

import com.jsonde.gui.Main;
import com.jsonde.gui.license.codec.binary.Base64;
import com.jsonde.util.file.FileUtils;
import com.jsonde.util.io.IO;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class LicenseManager {

    private byte SALT_SIZE = 8;

    private Random saltGenerator = new Random(System.currentTimeMillis());

    private byte[] getLicenseData(byte[] licenseCode) throws
            InvalidKeyException,
            NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            BadPaddingException {

        byte[] salt = new byte[SALT_SIZE];

        System.arraycopy(licenseCode, 0, salt, 0, SALT_SIZE);

        byte[] encodedData = new byte[licenseCode.length - SALT_SIZE];

        System.arraycopy(licenseCode, SALT_SIZE, encodedData, 0, encodedData.length);

        DESKeySpec key = new DESKeySpec(salt);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generateSecret(key));

        return cipher.doFinal(encodedData);

    }

    private byte[] createLicenseCode(byte[] licenseData) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {

        byte[] salt = new byte[SALT_SIZE];

        saltGenerator.nextBytes(salt);

        DESKeySpec key = new DESKeySpec(salt);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(key));

        byte[] encodedData = cipher.doFinal(licenseData);

        byte[] licenseCode = new byte[SALT_SIZE + encodedData.length];

        System.arraycopy(salt, 0, licenseCode, 0, SALT_SIZE);
        System.arraycopy(encodedData, 0, licenseCode, SALT_SIZE, encodedData.length);

        return licenseCode;

    }

    private static final byte[] license = new byte[]{
            77, 121, 32, 83, 117, 112, 101, 114, 32, 76, 105, 99, 101, 110, 115, 101
    };

    public boolean checkLicense(String licenseCode) {
        byte[] licenseCodeBytes = Base64.decodeBase64(licenseCode);
        byte[] licenseData = new byte[0];
        try {
            licenseData = getLicenseData(licenseCodeBytes);
        } catch (Exception e) {
            Main.getInstance().processException(e);
            return false;
        }
        return Arrays.equals(license, licenseData);
    }

    public void saveLicense(String license) {

        String licenseFileName =
                FileUtils.USER_HOME +
                        FileUtils.FILE_SEPARATOR +
                        ".jsonde" +
                        FileUtils.FILE_SEPARATOR +
                        "jsonde.license";

        File licenseFile = new File(licenseFileName);

        if (!licenseFile.exists()) {
            FileUtils.createFile(licenseFile);
        }

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(licenseFile);
            fileWriter.write(license);
        } catch (IOException e) {
            Main.getInstance().processException(e);
        } finally {
            IO.close(fileWriter);
        }

    }

    public boolean checkLicense() {

        String licenseFileName =
                FileUtils.USER_HOME +
                        FileUtils.FILE_SEPARATOR +
                        ".jsonde" +
                        FileUtils.FILE_SEPARATOR +
                        "jsonde.license";

        File licenseFile = new File(licenseFileName);

        if (licenseFile.exists()) {
            BufferedReader licenseFileReader = null;
            try {
                licenseFileReader = new BufferedReader(new FileReader(licenseFile));
                String licenseCode = licenseFileReader.readLine();
                return checkLicense(licenseCode);
            } catch (FileNotFoundException e) {
                Main.getInstance().processException(e);
                return false;
            } catch (IOException e) {
                Main.getInstance().processException(e);
                return false;
            } finally {
                IO.close(licenseFileReader);
            }
        }

        return false;

    }

    public Date getFirstExecutionDate() {

        String licenseFileName =
                FileUtils.USER_HOME +
                        FileUtils.FILE_SEPARATOR +
                        ".jsonde" +
                        FileUtils.FILE_SEPARATOR +
                        "jsonde.data";

        File firstExecutionDateFile = new File(licenseFileName);

        Date firstExecutionDate = null;

        if (firstExecutionDateFile.exists()) {
            ObjectInputStream firstExecutionDateReader = null;
            try {
                firstExecutionDateReader =
                        new ObjectInputStream(new FileInputStream(firstExecutionDateFile));

                firstExecutionDate = (Date) firstExecutionDateReader.readObject();

            } catch (FileNotFoundException e) {
                Main.getInstance().processException(e);
            } catch (IOException e) {
                Main.getInstance().processException(e);
            } catch (ClassNotFoundException e) {
                Main.getInstance().processException(e);
            } finally {
                IO.close(firstExecutionDateReader);
            }
        } else {
            FileUtils.createFile(firstExecutionDateFile);
        }

        if (null == firstExecutionDate) {

            firstExecutionDate = Calendar.getInstance().getTime();

            ObjectOutputStream firstExecutionDateOutputStream = null;

            try {
                firstExecutionDateOutputStream = new ObjectOutputStream(
                        new FileOutputStream(firstExecutionDateFile)
                );
                firstExecutionDateOutputStream.writeObject(firstExecutionDate);
            } catch (FileNotFoundException e) {
                Main.getInstance().processException(e);
            } catch (IOException e) {
                Main.getInstance().processException(e);
            } finally {
                IO.close(firstExecutionDateOutputStream);
            }

        }

        return firstExecutionDate;

    }

}

package com.jsonde.gui.configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "session")
public class SessionConfiguration {

    @XmlElement(name = "database-file-name")
    private String databaseFileName;

    public String getDatabaseFileName() {
        return databaseFileName;
    }

    public void setDatabaseFileName(String databaseFileName) {
        this.databaseFileName = databaseFileName;
    }

    public static SessionConfiguration loadSessionConfiguration(String fileName) throws SessionConfigurationException {
        return loadSessionConfiguration(new File(fileName));
    }

    public static SessionConfiguration loadSessionConfiguration(File file) throws SessionConfigurationException {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SessionConfiguration.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            SessionConfiguration sessionConfiguration =
                    (SessionConfiguration) unmarshaller.unmarshal(file);

            return sessionConfiguration;
        } catch (JAXBException e) {
            throw new SessionConfigurationException(e);
        }

    }

    public void save(String fileName) throws SessionConfigurationException {
        saveSessionConfiguration(this, fileName);
    }

    public static void saveSessionConfiguration(SessionConfiguration sessionConfiguration, String fileName) throws SessionConfigurationException {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SessionConfiguration.class);

            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.marshal(sessionConfiguration, new File(fileName));
        } catch (JAXBException e) {
            throw new SessionConfigurationException(e);
        }

    }

}

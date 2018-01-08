package org.dikhim.jclicker;

        import java.io.*;
        import java.util.Properties;
        import java.util.logging.Level;
        import java.util.logging.Logger;

/**
 * Configuration Class This Singleton Class is responsible for loading all of
 * the general configuration options for this application
 *
 * @author jkimmell
 */
public class Configuration {

    /**
     * The instance of Configuration that this Class is storing
     */
    private static Configuration instance = null;

    /**
     * FILENAME is the file location of the configuration JSON file
     *
     * @todo Potentially make this configurable
     */
    public static final String FILENAME = "/Users/jkimmell/Projects/NetTail/nettailconfig.json";

    /**
     * LOGGER is an instance of the Logger class so that we can do proper
     * logging
     */
    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    /**
     * Get the Instance of this class There should only ever be one instance of
     * this class and other classes can use this static method to retrieve the
     * instance
     *
     * @return Configuration the stored Instance of this class
     */
    public static Configuration getInstance() {
        if (Configuration.instance == null) {
            Configuration.instance = new Configuration();
        }

        return Configuration.instance;
    }

    /**
     * The loaded JSON Config Object, This should be a key-value pair of options
     */
    private Properties config = null;

    /**
     * How fast should the log rendering refresh?
     */
    private Integer refreshInterval;

    /**
     * What's the maximum length of content to keep in memory?
     */
    private Integer contentLimit;

    /**
     * How should the log files be sorted?
     */
    private String sortAttribute;

    /**
     * What direction should the log files be sorted?
     */
    private String sortDirection;

    /**
     * Constructor This is private so it cannot be instantiated by anyone other
     * than this class Try and load the current Config, if no config was found,
     * try to create a new one
     */
    private Configuration() {
        boolean result = this.loadConfig(FILENAME);

        if (!result) {
            if (!this.createConfig(FILENAME)) {
                System.exit(0); //Catastrophic
            }
        }
    }

    /**
     * Load the Configuration specified at fileName
     *
     * @param fileName
     * @return boolean did this load succeed?
     */
    private boolean loadConfig(String fileName) {
        this.config = new Properties();

        InputStream is ;
        try {
            is = new FileInputStream(fileName);
            this.config.load(is);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return false;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return false;
        }



      /*  this.contentLimit = this.config.getProperty("contentLimit");
        this.refreshInterval = this.config.getInt("refreshInterval");
        this.sortAttribute = this.config.getString("sortAttribute");
        this.sortDirection = this.config.getString("sortDirection");*/

        return true;
    }

    /**
     * Create a brand new config file with some default values
     *
     * @param fileName
     * @return
     */
    private boolean createConfig(String fileName) {
        this.contentLimit = 100000;
        this.refreshInterval = 2000;
        this.sortAttribute = "name";
        this.sortDirection = "ASC";

        return this.saveConfig(fileName);
    }

    /**
     * Save the current Configuration values out to a file to be retrieved in in
     * the future
     *
     * @param fileName
     * @return
     */
    private boolean saveConfig(String fileName) {
        try {
            PrintWriter configFile = new PrintWriter(fileName, "UTF-8");

            /**
             * Create a JSON Object that we can stringify and save
           /*  *//*
            JSONObject json = new JSONObject();
            json.put("contentLimit", this.contentLimit);
            json.put("refreshInterval", this.refreshInterval);
            json.put("sortAttribute", this.sortAttribute);
            json.put("sortDirection", this.sortDirection);

            LOGGER.log(Level.INFO, null, json.toString());

            configFile.write(json.toString());*/

            configFile.close();

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    /**
     * Refresh Interval Getter
     *
     * @return Integer Refresh Interval
     */
    public Integer getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * Refresh Interval Setter
     *
     * @param refreshInterval
     */
    public void setRefreshInterval(Integer refreshInterval) {
        this.refreshInterval = refreshInterval;
        this.saveConfig(FILENAME);
    }

    /**
     * Content Limit Getter
     *
     * @return Integer Content Limit
     */
    public Integer getContentLimit() {
        return contentLimit;
    }

    /**
     * Content Limit Setter
     *
     * @param contentLimit
     */
    public void setContentLimit(Integer contentLimit) {
        this.contentLimit = contentLimit;
        this.saveConfig(FILENAME);
    }

    /**
     * Sort Attribute Getter
     *
     * @return String Sort Attribute
     */
    public String getSortAttribute() {
        return sortAttribute;
    }

    /**
     * Sort Attribute Setter
     *
     * @param sortAttribute
     */
    public void setSortAttribute(String sortAttribute) {
        this.sortAttribute = sortAttribute;
        this.saveConfig(FILENAME);
    }

    /**
     * Sort Direction Getter
     *
     * @return String Sort Direction
     */
    public String getSortDirection() {
        return sortDirection;
    }

    /**
     * Sort Direction Setter
     *
     * @param sortDirection
     */
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
        this.saveConfig(FILENAME);
    }
}
package one.tulasi.framework.dataprovider;

import one.tulasi.framework.util.UtilBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;



/**
 * This class provides methods to read values of keys from *.properties from "ConfigFiles" folder
 */
public class ConfigManager 
{
	private Properties properties = new Properties();	
	private String configFilePath;
	private final Logger log = LogManager.getLogger("ConfigManager");
	private final String fileSeparator = System.getProperty("file.separator");
	

	//reads the key values from *.properties where * is config name provided to the constructor
	public ConfigManager(String configFilePath)
    {
		this.configFilePath = configFilePath;
    }
	
	/**
	 * Returns the value of given property from either sys.properties or app.properties file  
	 * @param key - Config Param value that requires to be returned from Config.properties file
	 * @return - return ConfigValue
	 */
	public String getProperty(String key)
	{
		String value ="";
        if(!key.equals(""))
        {
			loadProperties();
			try
			{
				if(!properties.getProperty(key).trim().isEmpty())
					value = properties.getProperty(key).trim();
			}
			catch(NullPointerException e)
			{
				Assert.fail("Key - '" + key + "' does not exist or not given a value in " + getConfigFilePath() + " file \n" + UtilBase.getStackTrace());
			}
        }
        else
        {
            log.error("key cannot be null.. ");
            Assert.fail("key cannot be null.. ");                      
        }
        return value;
    }

	private void loadProperties()
	{
		FileInputStream fis;
		try 
		{
			fis = new FileInputStream(getConfigFilePath());
			properties.load(fis);
			fis.close();
		} 
		catch (FileNotFoundException e) 
		{
			log.error("Cannot find configuration file - "+getConfigFilePath());
			Assert.fail("Cannot find configuration file - "+getConfigFilePath());
		} 
		catch (IOException e) 
		{
			log.error("Cannot read configuration file - "+" at "+getConfigFilePath());
			Assert.fail("Cannot read configuration file - "+" at "+getConfigFilePath());
		}
	}
	
	
	public String getConfigFilePath()
	{
		return configFilePath;	
	}
}

package one.tulasi.framework.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.io.File;



public class ReportSetup 
{
   private static final String fileSeparator = System.getProperty("file.separator");
   private static final Logger log = LogManager.getLogger("ReportSetup");
   private static boolean isDirCreated = true;
   private static String message;
 
   /**
	* This method setup's reporting environment i.e., creating a root folder and destination folder for storing report information
	*/
	public static void createFolderStructure() {
		createReportsFolder();
		createDataFolder();
		createDownloadsFolder();
     	createMediaFolders(); 	
	}

	/**
   * This method creates 'Reports' directory if it does not exist
   */
	private static void createReportsFolder() {
		File file = new File(getReportsPath());
     	if (!file.exists()) {
     		isDirCreated = file.mkdir();
	   	}
     	if(!isDirCreated) {
     		message = "\n Exception occurred while creating 'reports' directory";
			log.error("Check folder permissions of Project Directory..."+message);
			Assert.fail("Check folder permissions of Project Directory..."+message);
		}
	}

	/**
	 * This method creates "downloads" directory if it does not exist
	 */
	public static void createDownloadsFolder() {
		File file = new File(getDownloadsPath());
		if (!file.exists()) {
			isDirCreated = file.mkdir();
		}
		if(!isDirCreated) {
			message = "\n Exception occurred while creating 'downloads' directory";
			log.error("Check folder permissions of Project Directory..."+message);
			Assert.fail("Check folder permissions of Project Directory..."+message);
		}
	}
	
	/**
   * This method creates 'Latest Reports' directory if it does not exist
   * if directory exists it renames to Results_on_<currentDataTime> folder name and creates 'Latest Reports' directory
   */
	private static void createDataFolder() {
		try {
			File dataFolder = new File(getDataPath());
	      	if(dataFolder.exists()) {
	      		/*Path p = Paths.get(getDataPath());
			    BasicFileAttributes view;
				view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
			    String fCreationTime = view.creationTime().toString();
			    String istTime = UtilBase.convertToISTTime(fCreationTime.split("\\.")[0].replace("T","-"));
	      		String oldFolder = getReportsPath() + fileSeparator + "Results_on_" + istTime.replace(":", "_at_");
	      		File oldResults = new File(oldFolder);
	      		dataFolder.renameTo(oldResults);*/
	      		return;
	      	}
	      	isDirCreated = dataFolder.mkdir();
	      	if(!isDirCreated) {
	      		message = "Exception occurred while creating 'data' directory";
				log.error("Check folder permissions of Project Directory..."+message);
				Assert.fail("Check folder permissions of Project Directory..."+message);
			}
		} catch (Exception e) {
			log.error("Exception occurred while creating 'data' directory or unable to rename current 'data' directory "+e.getCause());
			Assert.fail("Exception occurred while creating 'data' directory or unable to rename current 'data' directory "+e.getCause());
		}
	}
	

	/**
   * This method creates 'screenshots' directory if they does not exist
   */
	private static void createMediaFolders() {
		File imagesFolder = new File(getImagesPath());
		if(!imagesFolder.exists()) {
			isDirCreated = imagesFolder.mkdir();
		} 
     	if(!isDirCreated) {
     		message = "\n Exception occurred while creating 'data/screenshots' directory";
			log.error("Check folder permissions of Project Directory..."+message);
			Assert.fail("Check folder permissions of Project Directory..."+message);
		}
	}
	
	
	/**
	 *@return - This method returns path to the folder where screenshots are stored
	 */
	public static String getImagesPath() {
		return getDataPath()+ fileSeparator + "screenshots";
	}


	/**
	 *@return - This method returns path to the folder where data are stored
	 */
	private static String getDataPath() {
		return getReportsPath() + fileSeparator + "data";
	}
	
	/**
	 *@return - This method returns the path to the root of reports folder
	 */
	public static String getReportsPath() {
		return System.getProperty("user.dir") + fileSeparator + "reports";
	}

	/**
	 * @return This method returns path to downloads folder
	 */
	public static String getDownloadsPath() {
		return getDataPath() + fileSeparator + "downloads";
	}
}
	



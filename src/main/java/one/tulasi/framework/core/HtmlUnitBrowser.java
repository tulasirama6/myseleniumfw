package one.tulasi.framework.core;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import one.tulasi.framework.util.ReportSetup;
import one.tulasi.framework.util.UtilBase;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.*;


/**
 * Created by TulasiRam A on 13 Dec 2016.
 */
public class HtmlUnitBrowser extends HtmlUnitDriver{

    public HtmlUnitBrowser() {
        super(true);
    }

    protected WebClient modifyWebClient(WebClient webClient) {
        ConfirmHandler okHandler = (page, message) -> true;
        webClient.setConfirmHandler(okHandler);
        webClient.addWebWindowListener(new CustomWebWindowListener());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new CustomAjaxController()); //Remove when attaching some files while saving SSP
        return webClient;
    }

    public class CustomAjaxController extends AjaxController {
            public boolean processSynchron(HtmlPage htmlPage, WebRequest webRequest, boolean async) {
                webRequest.getRequestParameters().removeIf(pair -> pair.getName().contains("attachment"));
                return false;
            }

    }

    public class CustomWebWindowListener implements WebWindowListener{

        public void webWindowOpened(WebWindowEvent event) {
        }

        public void webWindowContentChanged(WebWindowEvent event) {
            WebResponse response = event.getWebWindow().getEnclosedPage().getWebResponse();
//            List<NameValuePair> headers = response.getResponseHeaders();
//            for(NameValuePair header: headers){
//                System.out.println(header.getName() + " : " + header.getValue());
//            }
            // Change or add conditions for content-types that you would to like
            // receive like a file.
            if(response.getContentType().equals("text/html")){
                return;
            }
            else if(response.getContentType().equals("text/plain")){
                getFileResponse(response, ReportSetup.getDownloadsPath() + "/testDownload.war");
            }
            else if(response.getContentType().equals("application/vnd.ms-excel")) {
                getFileResponse(response, ReportSetup.getDownloadsPath() + "/testDownload.xls");
            }
            else if(response.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                String fileName = response.getResponseHeaderValue("Content-Disposition").split("\"")[1];
                getFileResponse(response, ReportSetup.getDownloadsPath() + UtilBase.getFileSeparator() + fileName);
            } else {
                return;
            }
        }

        public void webWindowClosed(WebWindowEvent event) {
        }

        public void getFileResponse(WebResponse response, String fileName){
            InputStream inputStream = null;// write the inputStream to a FileOutputStream
            OutputStream outputStream = null;
            try {
                inputStream = response.getContentAsStream();// write the inputStream to a FileOutputStream
                outputStream = new FileOutputStream(new File(fileName));
                int read;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                System.out.println("Done!");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        // outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


}

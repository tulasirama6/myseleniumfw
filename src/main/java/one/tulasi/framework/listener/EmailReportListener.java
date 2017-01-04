package one.tulasi.framework.listener;

import org.testng.*;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class EmailReportListener implements IReporter{

    private static final Logger log = Logger.getLogger(EmailReportListener.class);

    private PrintWriter printWriter;
    private int mRow;
    private Integer testIndex;
    private int methodIndex;
    private Scanner scanner;
    private String outputHtmlFileName = "custom-report.html";


    /**
     * Creates summary of the run in a html file
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outputDirectory) {
        printWriter = createWriter(outputDirectory);
        if(printWriter==null) return;
        startHtml(printWriter);
        generateSuiteSummaryReport(suites);
        generateMethodSummaryReport(suites);
        generateMethodDetailReport(suites);
        endHtml(printWriter);
        printWriter.flush();
        printWriter.close();
    }

    protected PrintWriter createWriter(String outputDirectory) {
        try {
            new File(outputDirectory).mkdirs();
            return new PrintWriter(new BufferedWriter(new FileWriter(new File(outputDirectory, outputHtmlFileName))));
        } catch (IOException e) {
            log.error("Unable to create output file", e);
            return null;
        }
    }

    /**
     * Creates a table showing the highlights of each test method with links to
     * the method details
     */
    protected void generateMethodSummaryReport(List<ISuite> suites) {
        tableStart("methodOverview", "summary");
        methodIndex = 0;
        int testIndex = 1;
        for (ISuite suite : suites) {
            if (suites.size() >= 1) {
                titleRow(suite.getName(), 6);
            }
            Map<String, ISuiteResult> r = suite.getResults();
            for (ISuiteResult r2 : r.values()) {
                ITestContext testContext = r2.getTestContext();
                String testName = testContext.getName();
                this.testIndex = testIndex;
                resultSummary(suite, testContext.getFailedConfigurations(),
                        testName, "failed", " (configuration methods)");
                resultSummary(suite, testContext.getFailedTests(), testName,
                        "failed", "");
                resultSummary(suite, testContext.getSkippedConfigurations(),
                        testName, "skipped", " (configuration methods)");
                resultSummary(suite, testContext.getSkippedTests(), testName,
                        "skipped", "");
                resultSummary(suite, testContext.getPassedTests(), testName,
                        "passed", "");
                testIndex++;
            }
        }
        printWriter.println("</table>");
    }

    public int getFailedCount(List<ISuite> suites)
    {
        int count = 0;
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> r = suite.getResults();
            for (ISuiteResult r2 : r.values()) {
                ITestContext testContext = r2.getTestContext();
                count += testContext.getFailedConfigurations().size();
                count += testContext.getFailedTests().size();
            }
        }
        return count;
    }

    public int getSkippedCount(List<ISuite> suites)
    {
        int count = 0;
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> r = suite.getResults();
            for (ISuiteResult r2 : r.values()) {
                ITestContext testContext = r2.getTestContext();
                count += testContext.getSkippedConfigurations().size();
                count += testContext.getSkippedTests().size();
            }
        }
        return count;
    }

    /** Creates a section showing known results for each method */
    protected void generateMethodDetailReport(List<ISuite> suites) {
        methodIndex = 0;
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> r = suite.getResults();
            for (ISuiteResult r2 : r.values()) {
                ITestContext testContext = r2.getTestContext();
                if (r.values().size() > 0) {
                    printWriter.println("<h1>" + testContext.getName() + "</h1>");
                }
                resultDetail(testContext.getFailedConfigurations());
                resultDetail(testContext.getFailedTests());
                resultDetail(testContext.getSkippedConfigurations());
                resultDetail(testContext.getSkippedTests());
                //resultDetail(testContext.getPassedTests());
            }
        }
    }

    private void resultSummary(ISuite suite, IResultMap tests, String testName, String style, String details) {
        if (tests.getAllResults().size() > 0)
        {
            StringBuilder buff = new StringBuilder();
            String lastClassName = "";
            int mq = 0;
            int cq = 0;
            for (ITestNGMethod method : getMethodSet(tests, suite)) {
                mRow += 1;
                methodIndex += 1;
                ITestClass testClass = method.getTestClass();
                String className = testClass.getName();
                if (mq == 0) {
                    String id = (testIndex == null ? null : "t" + Integer.toString(testIndex));
                    titleRow(testName + " &#8212; " + style + details, 6, id);
                    startResultSummaryTable();
                    testIndex = null;
                }
                if (!className.equalsIgnoreCase(lastClassName)) {
                    if (mq > 0) {
                        cq += 1;
                        printWriter.print("<tr class=\"" + style
                                + (cq % 2 == 0 ? "even" : "odd") + "\">"
                                + "<td");
                        if (mq > 1) {
                            printWriter.print(" rowspan=\"" + mq + "\"");
                        }
                        printWriter.println(">" + lastClassName + "</td>" + buff);
                    }
                    mq = 0;
                    buff.setLength(0);
                    lastClassName = className;
                }

                Set<ITestResult> resultSet = tests.getResults(method);
                long end = Long.MIN_VALUE;
                long start = Long.MAX_VALUE;
                long startMS=0;
                String firstLine="";
                String screenshotLnk="";
                String exceptionWithScreenShot = "";
                String methodParameters = "";
                String absoluteParams = "";
                String methodName = "";
                String lastMethodName = "";
                for (ITestResult testResult : tests.getResults(method)) {
                    methodName = testResult.getMethod().getTestClass().getName() + "." + testResult.getMethod().getMethodName();
                    if(!methodName.equals(lastMethodName)) {
                        if (testResult.getEndMillis() > end) {
                            end = testResult.getEndMillis()/1000;
                        }
                        if (testResult.getStartMillis() < start) {
                            startMS = testResult.getStartMillis();
                            start =startMS/1000;
                        }

                        Throwable exception=testResult.getThrowable();
                        boolean hasThrowable = exception != null;
                        if(hasThrowable){
                            Object[] parameters = testResult.getParameters();
                            boolean hasParameters = parameters != null && parameters.length > 0;
                            if (hasParameters) {
                                for (Object p : parameters) {
                                    methodParameters+= Utils.escapeHtml(toString(p));
                                }
                                absoluteParams = "Method Parameters are: " +methodParameters+"<br/>";
                                methodParameters = "";
                            }

                            String str = Utils.stackTrace(exception, true)[0];
                            scanner = new Scanner(str);
                            firstLine= scanner.nextLine()+"<br/>";

                            List<String> msgs = Reporter.getOutput(testResult);
                            boolean hasReporterOutput = msgs.size() > 0;
                            if(hasReporterOutput){
                                for (String line : msgs)
                                {
                                    screenshotLnk=line;
                                }
                            }
                        }

                        if(screenshotLnk.equalsIgnoreCase("")){
                            screenshotLnk = "N/A";
                        }
                        if(firstLine.equalsIgnoreCase("")){
                            firstLine = "N/A";
                        }
                        exceptionWithScreenShot+=absoluteParams+firstLine+"</td><td>"+screenshotLnk + "</td>";
                        lastMethodName = methodName;
                    }
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startMS);

                mq += 1;
                if (mq > 1) {
                    buff.append("<tr class=\"").append(style).append(cq % 2 == 0 ? "odd" : "even").append("\">");
                }
                String description = method.getDescription();
                String testInstanceName = resultSet
                        .toArray(new ITestResult[] {})[0].getTestName();
                buff.append("<td style=\"text-align:left;padding-right:2em\"><a href=\"#m").append(methodIndex).append("\">").append(qualifiedName(method)).append(" ").append(description != null && description.length() > 0 ? "(\""
                        + description + "\")"
                        : "").append("</a>").append(null == testInstanceName ? "" : "<br>("
                        + testInstanceName + ")").append("</td>").append("<td style=\"text-align:left;padding-right:2em\">").append(exceptionWithScreenShot).append("<td class=\"numi\">").append(timeConversionToMinutes(end - start)).append("</td>").append("</tr>");

            }
            if (mq > 0) {
                cq += 1;
                printWriter.print("<tr class=\"" + style
                        + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
                if (mq > 1) {
                    printWriter.print(" rowspan=\"" + mq + "\"");
                }
                printWriter.println(">" + lastClassName + "</td>" + buff);
            }
        }
    }

    /**
     * Write the first line of the stack trace
     *
     * @param tests
     */
    private void getShortException(IResultMap tests) {

        for (ITestResult result : tests.getAllResults()) {
            methodIndex++;
            Throwable exception = result.getThrowable();
            List<String> msgs = Reporter.getOutput(result);
            boolean hasReporterOutput = msgs.size() > 0;
            boolean hasThrowable = exception != null;
            if (hasThrowable) {
                boolean wantsMinimalOutput = result.getStatus() == ITestResult.SUCCESS;
                if (hasReporterOutput) {
                    printWriter.print("<h3>"
                            + (wantsMinimalOutput ? "Expected Exception"
                            : "Failure") + "</h3>");
                }

                // Getting first line of the stack trace
                String str = Utils.stackTrace(exception, true)[0];
                scanner = new Scanner(str);
                String firstLine = scanner.nextLine();
                printWriter.println(firstLine);
            }
        }
    }

    private String timeConversionToMinutes(long seconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        return prefixZeroToDigit(minutes) + "min " + prefixZeroToDigit((int)seconds) + "secs";
    }

    private String timeConversion(long seconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        int hours = minutes / MINUTES_IN_AN_HOUR;
        minutes -= hours * MINUTES_IN_AN_HOUR;

        return prefixZeroToDigit(hours) + ":" + prefixZeroToDigit(minutes) + ":" + prefixZeroToDigit((int)seconds);
    }

    private String prefixZeroToDigit(int num){
        if(num <=9){
            return "0"+ num;
        }
        else
            return ""+ num;

    }

    /** Starts and defines columns result summary table */
    private void startResultSummaryTable() {
        printWriter.println("<tr><th>Class</th>"
                + "<th>Method</th><th>Exception short details</th><th>Screenshot</th><th>Execution Time</th></tr>");
        mRow = 0;
    }

    private String qualifiedName(ITestNGMethod method) {
        StringBuilder addon = new StringBuilder();
        String[] groups = method.getGroups();
        int length = groups.length;
        if (length > 0 && !"basic".equalsIgnoreCase(groups[0])) {
            addon.append("(");
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    addon.append(", ");
                }
                addon.append(groups[i]);
            }
            addon.append(")");
        }

        return "<b>" + method.getMethodName() + "</b> " + addon;
    }

    private void resultDetail(IResultMap tests) {
        Set<ITestResult> testResults=tests.getAllResults();
        List<ITestResult> testResultsList = new ArrayList<>(testResults);
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
        testResultsList.sort(new TestResultsSorter());
        for (ITestResult result : testResultsList) {
            ITestNGMethod method = result.getMethod();
            methodIndex++;
            String cname = method.getTestClass().getName();
            printWriter.println("<h2 id=\"m" + methodIndex + "\">" + cname + ":"
                    + method.getMethodName() + "</h2>");
            Set<ITestResult> resultSet = tests.getResults(method);
            generateForResult(result, method, resultSet.size());
            printWriter.println("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");

        }
    }


    /**
     * Write all parameters
     *
     * @param tests
     */
    private void getParameters(IResultMap tests) {

        for (ITestResult result : tests.getAllResults()) {
            methodIndex++;
            Object[] parameters = result.getParameters();
            boolean hasParameters = parameters != null && parameters.length > 0;
            if (hasParameters) {

                for (Object p : parameters) {
                    printWriter.println(Utils.escapeHtml(toString(p)) + " | ");
                }
            }

        }
    }

    private void generateForResult(ITestResult ans, ITestNGMethod method,
                                   int resultSetSize) {
        Object[] parameters = ans.getParameters();
        boolean hasParameters = parameters != null && parameters.length > 0;
        if (hasParameters) {
            tableStart("result", null);
            printWriter.print("<tr class=\"param\">");
            for (int x = 1; x <= parameters.length; x++) {
                printWriter.print("<th>Param." + x + "</th>");
            }
            printWriter.println("</tr>");
            printWriter.print("<tr class=\"param stripe\">");
            for (Object p : parameters) {
                printWriter.println("<td>" + Utils.escapeHtml(toString(p))
                        + "</td>");
            }
            printWriter.println("</tr>");
        }
        List<String> msgs = Reporter.getOutput(ans);
        boolean hasReporterOutput = msgs.size() > 0;
        Throwable exception = ans.getThrowable();
        boolean hasThrowable = exception != null;
        if (hasReporterOutput || hasThrowable) {
            if (hasParameters) {
                printWriter.println("</table>");
            }
            printWriter.println("<div>");
            if (hasReporterOutput) {
                if (hasThrowable) {
                    printWriter.println("<h3>Test Messages</h3>");
                }
                for (String line : msgs) {
                    printWriter.println(line + "<br/>");
                }
            }
            if (hasThrowable) {
                boolean wantsMinimalOutput = ans.getStatus() == ITestResult.SUCCESS;
                if (hasReporterOutput) {
                    printWriter.println("<h3>"
                            + (wantsMinimalOutput ? "Expected Exception"
                            : "Failure") + "</h3>");
                }
                generateExceptionReport(exception, method);
            }
            printWriter.println("</div>");
        }

    }

    protected void generateExceptionReport(Throwable exception,
                                           ITestNGMethod method) {
        printWriter.print("<div class=\"stacktrace\">");
        printWriter.print(Utils.stackTrace(exception, true)[0]);
        printWriter.println("</div>");
    }

    /**
     * Since the methods will be sorted chronologically, we want to return the
     * ITestNGMethod from the invoked methods.
     */
    private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite) {

        return getMethodSet(tests, suite, false);

    }


    /**
     * This method returns the total methods including the Data Provider
     */

    private Collection<ITestNGMethod> getMethodSetWithDataProviderCount(IResultMap tests, ISuite suite) {

        return getMethodSet(tests, suite, true);

    }

    private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite, boolean withDataProvider) {
        List<IInvokedMethod> r = Lists.newArrayList();
        List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
        for (IInvokedMethod im : invokedMethods) {
            if (tests.getAllMethods().contains(im.getTestMethod())) {
                r.add(im);
            }
        }
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
        r.sort(new TestSorter());
        List<ITestNGMethod> result = Lists.newArrayList();

        // Add all the invoked methods
        for (IInvokedMethod m : r) {
            for (ITestNGMethod temp : result) {
                if (!temp.equals(m.getTestMethod()))
                    result.add(m.getTestMethod());
            }
        }

        // Add all the methods that weren't invoked (e.g. skipped) that we haven't added yet
        Collection<ITestNGMethod> allMethodsCollection=tests.getAllMethods();
        List<ITestNGMethod> allMethods=new ArrayList<>(allMethodsCollection);
        allMethods.sort(new TestMethodSorter());

        for (ITestNGMethod m : allMethods) {
            if(withDataProvider) {
                result.add(m);
            } else
            if (!result.contains(m)) {
                result.add(m);
            }
        }
        return result;

    }

    @SuppressWarnings("unused")
    public void generateSuiteSummaryReport(List<ISuite> suites) {
        String startTime = "";
        SimpleDateFormat summaryFormat = new SimpleDateFormat("kk:mm:ss");
        Map<String, ISuiteResult> forTimeTests = suites.get(0).getResults();
        for (ISuiteResult r : forTimeTests.values()) {
            if(forTimeTests.size()>0)
                startTime = summaryFormat.format(r.getTestContext().getStartDate());
            break;
        }
        if(!startTime.equals(""))
        {
            printWriter.print("<p> Suite Started at " + startTime + "</p><br/>");
        }
        printWriter.print("<table border='0' style=\"border: none; border-collapse: collapse;\" border='0' cellspacing='0' cellpadding='0'>"
                +"<tr><td border='0' style=\"border: none; border-collapse: collapse;\" border='0' cellspacing='0' cellpadding='0'>");
        tableStart("testOverview", null);
        printWriter.print("<tr>");
        tableColumnStart("Suite Name");
        tableColumnStart("Test Name");
        tableColumnStart("Passed");
        tableColumnStart("Skipped");
        tableColumnStart("Failed");
        tableColumnStart("Time Taken");
        tableColumnStart("Included<br/>Groups");
        printWriter.println("</tr>");

        NumberFormat formatter = new DecimalFormat("#,##0.0");
        int qty_tests = 0;
        int qty_pass_m = 0;
        int qty_pass_s = 0;
        int qty_skip = 0;
        long timeStart = Long.MAX_VALUE;
        int qty_fail = 0;
        long timeEnd = Long.MIN_VALUE;
        testIndex = 1;
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> tests = suite.getResults();
            for (ISuiteResult r : tests.values()) {
                qty_tests += 1;
                ITestContext overview = r.getTestContext();
                if (suites.size() >= 1) {
                    startSummaryRow(suite.getName());
                }
                summaryCell(overview.getName(), true);
                int q = getMethodSetWithDataProviderCount(overview.getPassedTests(), suite).size();
                qty_pass_m += q;
                summaryCell(q, "pass");
                q = getMethodSetWithDataProviderCount(overview.getSkippedTests(), suite).size();
                qty_skip += q;
                summaryCell(q, "skip");
                q = getMethodSetWithDataProviderCount(overview.getFailedTests(), suite).size();
                qty_fail += q;
                summaryCell(q, "fail");
                printWriter.println("</td>");

                timeStart = Math.min(overview.getStartDate().getTime(), timeStart);
                timeEnd = Math.max(overview.getEndDate().getTime(), timeEnd);
                summaryCell(timeConversion((overview.getEndDate().getTime() - overview.getStartDate().getTime()) / 1000), true);
                summaryCell(overview.getIncludedGroups());
                printWriter.println("</tr>");
                testIndex++;
            }
        }
        if (qty_tests > 1) {
            printWriter.println("<tr class=\"total\"><td>Total</td>");
            summaryCell(qty_pass_m, Integer.MAX_VALUE);
            summaryCell(qty_skip, 0);
            summaryCell(qty_fail, 0);
            summaryCell(" ", true);
            summaryCell(" ", true);
            summaryCell(" ", true);
            summaryCell(timeConversionToMinutes(((timeEnd - timeStart) / 1000)), true);
            printWriter.println("<td colspan=\"3\">&nbsp;</td></tr>");
        }
        printWriter.println("</table></td><td border=\"0\" style=\"border: none; border-collapse: collapse;\" border='0' cellspacing='0' cellpadding='0'>");
        printWriter.println("</td></tr></table>");
    }


    private void summaryCell(String[] val) {
        StringBuilder b = new StringBuilder();
        for (String v : val) {
            b.append(v).append(" ");
        }
        summaryCell(b.toString(), true);
    }

    private void summaryCell(String v, boolean isgood) {
        printWriter.print("<td class=\"numi" + (isgood ? "" : "_attn") + "\">" + v
                + "</td>");
    }

    private void summaryCell(int v, String className) {
        printWriter.print("<td class=" + className + ">" + String.valueOf(v) + "</td>");
    }

    private void startSummaryRow(String label) {


        mRow += 1;
        printWriter.print("<tr"
                + (mRow % 2 == 0 ? " class=\"stripe\"" : "")
                + "><td style=\"text-align:left;padding-right:2em\"><b>" + label + "</b></a>" + "</td>");
        // <a href=\"#t" + testIndex + "\ older link
    }

    private void summaryCell(int v, int maxexpected) {
        summaryCell(String.valueOf(v), v <= maxexpected);
    }

    private void tableStart(String cssclass, String id) {
        printWriter.println("<table cellspacing=\"0\" cellpadding=\"0\""
                + (cssclass != null ? " class=\"" + cssclass + "\""
                : " style=\"padding-bottom:2em\"")
                + (id != null ? " id=\"" + id + "\"" : "") + ">");
        mRow = 0;
    }

    private void tableColumnStart(String label) {
        printWriter.print("<th>" + label + "</th>");
    }

    private void titleRow(String label, int cq) {
        titleRow(label, cq, null);
    }

    private void titleRow(String label, int cq, String id) {
        printWriter.print("<tr");
        if (id != null) {
            printWriter.print(" id=\"" + id + "\"");
        }
        printWriter.println("><th colspan=\"" + cq + "\">" + label + "</th></tr>");
        mRow = 0;
    }

    /** Starts HTML stream */
    protected void startHtml(PrintWriter out) {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("<title>Results Email</title>");
        out.println("<style type=\"text/css\">");
        out.println("body {font-family:'Segoe UI','Helvetica','Arial';  font-size: small;background-color: #F5F5F5;}");
        out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show;border-color:black; border:1px solid black}");
        out.println("a {color:inherit}");
        out.println(".pass {background-color:green;color:rgb(178, 255, 178);font-weight:bold}");
        out.println(".fail {  background-color:red;color: rgb(253, 164, 164);font-weight:bold}");
        out.println(".skip {background-color: #FF9D0A;color: rgb(255, 225, 184);font-weight:bold}");
        out.println(".testOverview {margin-bottom:10px;border-collapse:collapse;empty-cells:show;float:left;position:relative;background-color: #A9A9A9; color:#424141;}");
        out.println("td {background-color: #E8E8E8; color:#424141;border: 1px solid rgb(48, 48, 48); text-align:center; padding:.25em .5em}");
        out.println("th {background-color: #E0DDDD; color:rgb(48, 48, 48);border: 1px solid rgb(48, 48, 48); text-align:center;  padding:.25em .5em}");
        out.println(".passedodd td {background-color: #B0F9B0}");
        out.println(".passedeven td {background-color: #A9F1A9}");
        out.println(".skippedodd td {background-color: #F0F0F0; color:#61605E; border: 1px solid #4D4D4C;}");
        out.println(".skippedeven td {background-color: #ECEAE8; color:#61605E;  border: 1px solid #4D4D4C;}");
        out.println(".failedodd td{background-color: #FFEBEB; color:#FF0000;border: 1px solid #B81308;}");
        out.println(".failedeven td {background-color: #FFEBEF; color:#FF0000;border: 1px solid #B81308;}");
        out.println(".detail td{text-align:left;}");
        out.println(".result th {vertical-align:bottom}");
        out.println(".param th {padding-left:1em;padding-right:1em}");
        out.println(".param td {padding-left:.5em;padding-right:2em}");
        out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
        out.println(".total td {font-weight:bold}");
        out.println(".stacktrace {white-space:pre;font-family:monospace}");
        out.println(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
    }

    /** Finishes HTML stream */
    protected void endHtml(PrintWriter out) {
        out.println("</body></html>");
        out.flush();
        out.close();
    }


    // ~ Inner Classes --------------------------------------------------------
    /** Arranges methods by classname and method name */
    private class TestSorter implements Comparator<IInvokedMethod> {
        // ~ Methods
        // -------------------------------------------------------------

        /** Arranges methods by classname and method name */
        @Override
        public int compare(IInvokedMethod o1, IInvokedMethod o2) {
            // Commented out for verifying test execution results
			/*	if (r == 0) {
				r=o1.getTestMethod().compareTo(o2.getTestMethod());
			}*/
            return o1.getTestMethod().getTestClass().getName().compareTo(o2.getTestMethod().getTestClass().getName());
        }
    }

    private class TestMethodSorter implements Comparator<ITestNGMethod> {
        @Override
        public int compare(ITestNGMethod o1, ITestNGMethod o2) {
            int r =o1.getTestClass().getName().compareTo(o2.getTestClass().getName());
            if (r == 0) {
                r=o1.getMethodName().compareTo(o2.getMethodName());
            }
            return r;
        }
    }

    private class TestResultsSorter implements Comparator<ITestResult> {
        @Override
        public int compare(ITestResult o1, ITestResult o2) {
            int result = o1.getTestClass().getName().compareTo(o2.getTestClass().getName());
            if (result == 0) {
                result = o1.getMethod().getMethodName().compareTo(o2.getMethod().getMethodName());
            }
            return result;
        }
    }

    public static String toString(Object obj) {
        String result;
        if (obj != null) {
            if (obj instanceof boolean[]) {
                result = Arrays.toString((boolean[]) obj);
            } else if (obj instanceof byte[]) {
                result = Arrays.toString((byte[]) obj);
            } else if (obj instanceof char[]) {
                result = Arrays.toString((char[]) obj);
            } else if (obj instanceof double[]) {
                result = Arrays.toString((double[]) obj);
            } else if (obj instanceof float[]) {
                result = Arrays.toString((float[]) obj);
            } else if (obj instanceof int[]) {
                result = Arrays.toString((int[]) obj);
            } else if (obj instanceof long[]) {
                result = Arrays.toString((long[]) obj);
            } else if (obj instanceof Object[]) {
                result = Arrays.deepToString((Object[]) obj);
            } else if (obj instanceof short[]) {
                result = Arrays.toString((short[]) obj);
            } else {
                result = obj.toString();
            }
        } else {
            result = "null";
        }
        return result;
    }
}


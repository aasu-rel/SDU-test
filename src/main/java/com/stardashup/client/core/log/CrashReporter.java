package com.stardashup.client.core.log;

import com.stardashup.client.SDUClient;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Generates crash reports for SDU-related exceptions.
 *
 * <p>Crash reports include system information, mod state, and full stack traces.
 * SDU exceptions are caught and reported without crashing the entire game.</p>
 */
public class CrashReporter {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    private final File crashDir;

    public CrashReporter(File crashDir) {
        this.crashDir = crashDir;
        crashDir.mkdirs();
    }

    /**
     * Reports an exception, generating a crash report file.
     *
     * @param throwable the exception that occurred
     * @param context   a description of what was happening when the crash occurred
     */
    public void reportCrash(Throwable throwable, String context) {
        String timestamp = DATE_FORMAT.format(new Date());
        File reportFile = new File(crashDir, "crash-" + timestamp + ".txt");

        StringBuilder report = new StringBuilder();
        report.append("===== StarDashUp Client Crash Report =====\n");
        report.append("Time: ").append(timestamp).append("\n");
        report.append("Context: ").append(context).append("\n");
        report.append("SDU Version: ").append(SDUClient.MOD_VERSION).append("\n");
        report.append("\n");

        // System info
        report.append("--- System Info ---\n");
        report.append("OS: ").append(System.getProperty("os.name")).append(" ")
                .append(System.getProperty("os.version")).append("\n");
        report.append("Java: ").append(System.getProperty("java.version")).append(" (")
                .append(System.getProperty("java.vendor")).append(")\n");
        report.append("JVM: ").append(System.getProperty("java.vm.name")).append("\n");

        Runtime rt = Runtime.getRuntime();
        long usedMB = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
        long maxMB = rt.maxMemory() / (1024 * 1024);
        report.append("Memory: ").append(usedMB).append("MB / ").append(maxMB).append("MB\n");
        report.append("\n");

        // Exception
        report.append("--- Exception ---\n");
        report.append(throwable.getClass().getName()).append(": ").append(throwable.getMessage()).append("\n");

        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        report.append(sw.toString());
        report.append("\n");

        report.append("===== End of Crash Report =====\n");

        // Write to file
        FileOutputStream fos = null;
        OutputStreamWriter writer = null;
        try {
            fos = new FileOutputStream(reportFile);
            writer = new OutputStreamWriter(fos, UTF8);
            writer.write(report.toString());
            SDULogger.staticError("Crash report saved to: " + reportFile.getAbsolutePath());
        } catch (IOException e) {
            SDULogger.staticError("Failed to write crash report!", e);
        } finally {
            try {
                if (writer != null) writer.close();
                if (fos != null) fos.close();
            } catch (IOException ignored) {
            }
        }

        // Also log to console
        SDULogger.staticError("SDU Crash in [" + context + "]: " + throwable.getMessage(), throwable);
    }
}

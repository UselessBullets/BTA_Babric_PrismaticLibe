package useless.prismaticlibe.helper;


import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.core.util.FileUtils;
import useless.prismaticlibe.PrismaticLibe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;


public class SoundHelper {
    private static Hashtable<String, String> fileCache = new Hashtable<String, String>();
    public static final File appDirectory = Minecraft.getAppDir("minecraft-bta");
    public static final File soundDirectory = new File(appDirectory.getAbsolutePath() + "/resources/mod/sound");
    public static final File musicDirectory = new File(appDirectory.getAbsolutePath() + "/resources/mod/music");
    public static final File streamingDirectory = new File(appDirectory.getAbsolutePath() + "/resources/mod/streaming");

    public static void addMusic(String MOD_ID, String soundSource){
        String destination = musicDirectory + ("/" + soundSource).replace("//", "/");
        String source = ("/assets/" + MOD_ID + "/music/" + soundSource).replace("//", "/").trim();
        PrismaticLibe.LOGGER.info("File source: " + source);
        PrismaticLibe.LOGGER.info("File destination: " + destination);

        PrismaticLibe.LOGGER.info(extract(source, destination, soundSource) + " Added to sound directory");
    }
    public static void addSound(String MOD_ID, String soundSource){
        String destination = soundDirectory + ("/" + MOD_ID + "/").replace("//", "/");
        String source = ("/assets/" + MOD_ID + "/sound/" + soundSource).replace("//", "/").trim();
        PrismaticLibe.LOGGER.info("File source: " + source);
        PrismaticLibe.LOGGER.info("File destination: " + destination);

        PrismaticLibe.LOGGER.info(extract(source, destination, soundSource) + " Added to sound directory");
    }
    private static String extract(String jarFilePath, String destination, String soundSource){

        if(jarFilePath == null)
            return null;

        // See if we already have the file
        if(fileCache.contains(jarFilePath))
            return fileCache.get(jarFilePath);

        // Alright, we don't have the file, let's extract it
        try {
            // Read the file we're looking for
            InputStream fileStream = SoundHelper.class.getResourceAsStream(jarFilePath);

            // Was the resource found?
            if(fileStream == null)
                return null;

            File tempFile = new File(new File(destination), soundSource);
            tempFile.getParentFile().mkdirs();
            tempFile.delete();
            Files.createFile(tempFile.toPath());

            // Set this file to be deleted on VM exit
            tempFile.deleteOnExit();

            // Create an output stream to barf to the temp file
            OutputStream out = Files.newOutputStream(tempFile.toPath());

            // Write the file to the temp file
            byte[] buffer = new byte[1024];
            int len = fileStream.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = fileStream.read(buffer);
            }

            // Store this file in the cache list
            fileCache.put(jarFilePath, tempFile.getAbsolutePath());

            // Close the streams
            fileStream.close();
            out.close();

            // Return the path of this sweet new file
            return tempFile.getAbsolutePath();

        } catch (IOException e) {
            PrismaticLibe.LOGGER.warn(e.toString());
            for (StackTraceElement element :e.getStackTrace()){
                PrismaticLibe.LOGGER.debug(element.toString());
            }
            return null;
        }
    }
}



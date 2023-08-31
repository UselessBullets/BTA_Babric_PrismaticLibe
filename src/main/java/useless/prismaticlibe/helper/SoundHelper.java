package useless.prismaticlibe.helper;


import com.b100.utils.FileUtils;
import net.minecraft.client.Minecraft;
import useless.prismaticlibe.PrismaticLibe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;


public class SoundHelper {
    private static Hashtable<String, String> fileCache = new Hashtable<String, String>();
    public static final File appDirectory = Minecraft.getAppDir("minecraft-bta");
    public static final File soundDirectory = new File(appDirectory.getAbsolutePath() + "\\resources\\mod\\sound");
    public static final File musicDirectory = new File(appDirectory.getAbsolutePath() + "\\resources\\mod\\music");
    public static final File streamingDirectory = new File(appDirectory.getAbsolutePath() + "\\resources\\mod\\streaming");
    public SoundHelper(){
    }

    public  void addMusic(String MOD_ID, String soundSource){
        String destination = musicDirectory + ("\\" + soundSource.replace("/", "\\")).replace("\\\\", "\\");
        String source = ("/assets/" + MOD_ID + "/music/" + soundSource).replace("//", "/").trim();
        PrismaticLibe.LOGGER.info("File source: " + source);
        PrismaticLibe.LOGGER.info("File destination: " + destination);

        PrismaticLibe.LOGGER.info(extract(source, destination, soundSource) + " Added to sound directory");
    }
    public  void addSound(String MOD_ID, String soundSource){
        String destination = soundDirectory + ("\\" + MOD_ID + "\\").replace("\\\\", "\\");
        String source = ("/assets/" + MOD_ID + "/sound/" + soundSource).replace("//", "/").trim();
        PrismaticLibe.LOGGER.info("File source: " + source);
        PrismaticLibe.LOGGER.info("File destination: " + destination);

        PrismaticLibe.LOGGER.info(extract(source, destination, soundSource) + " Added to sound directory");
    }
    private  String extract(String jarFilePath, String destination, String soundSource){

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
            PrismaticLibe.LOGGER.info("Temp file: " + tempFile.getAbsolutePath());
            Files.createDirectories(Paths.get(destination));
            tempFile.delete();
            tempFile.createNewFile();

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
            PrismaticLibe.LOGGER.warn(e.getMessage());
            return null;
        }
    }
}



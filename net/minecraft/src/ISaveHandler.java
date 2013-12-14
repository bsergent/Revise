package net.minecraft.src;

import java.io.File;

public interface ISaveHandler
{
    /**
     * Loads and returns the world info
     */
    /**
     * Called to flush all changes to disk, waiting for them to complete.
     */
    void flush();

    /**
     * Gets the file location of the given map
     */
    File getMapFileFromName(String var1);

    /**
     * Returns the name of the directory where world information is saved.
     */
    String getSaveDirectoryName();
}

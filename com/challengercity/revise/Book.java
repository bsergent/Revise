
package com.challengercity.revise;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ben Sergent V/ha1fBit
 */
public class Book {

    private String title;
    private String author;
    private List<String> pages;
    private File file;
    private byte type; // 0=mcbook,1=single,2=multi

    public Book() {
        this.title = "";
        this.author = "";
        this.pages = new LinkedList<String>();
        this.file = null;
        this.type = 0;
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.pages = new LinkedList<String>();
        this.file = null;
        this.type = 0;
    }

    public Book(String title, String author, List<String> pages, File file, byte type) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.file = file;
        this.type = type;
    }
    
    public String getAuthor() {
        return author;
    }

    public File getFile() {
        return file;
    }

    public List<String> getPages() {
        return pages;
    }

    public String getTitle() {
        return title;
    }
    
    public byte getType() {
        return type;
    }
    
}

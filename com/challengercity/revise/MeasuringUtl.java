/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.challengercity.revise;

import java.awt.FontMetrics;
/**
 *
 * @author Ben Sergent V/ha1fBit
 */
public class MeasuringUtl { // TODO Don't measure §e codes
    
    public static double getMcStringWidth(String string) { // Gets the width of a string using the font metrics and rectangles
        double returnValue = 0D;
        java.awt.geom.Rectangle2D bounds = ReviseMain.metrics.getStringBounds(string, null);
        returnValue = bounds.getWidth();
        return returnValue;
    }
    
    public static int getMcCodeLength(String str) { // Gets string length adding one for each new line
        int returnLength = str.length();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                returnLength++;
            } 
        } 
        return returnLength;
    }
    
    public static int getFormattedPageLength(String string) { // Gets string length adding one for each new line after formatting
        string = formatForMc(string); // Only difference
        int returnValue = string.length();

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '\n') {
                returnValue++;
            } 
        } 

        return returnValue;
    }
    
    public static String formatForMc(String page) {
        String returnValue = "";
        //page = chopForMcCodeLength(page);
        page = page.replaceAll(" ", " ");
        
        for (int i = 0; i < 13; i++) { // For all lines
            boolean isLine13 = false;
            if (i == 12) {
                isLine13 = true;
            }
            int breakPoint = getMcCodeLineBreakPoint(page, isLine13);
            if (breakPoint > page.length()) break; 
            returnValue = returnValue + page.substring(0, breakPoint);
            page = page.substring(breakPoint);
        } 

        return returnValue;
    }
    
    public static int getMcCodeLineBreakPoint(String string, boolean isLine13) {
        int returnValue = 0;
        int stringEndIndex = Math.min(string.length(), 266);
        int indexOfLastSpace = -1;
        int doNotCountWidth = 0;

        for (returnValue = 1; returnValue < stringEndIndex; returnValue++) {
            double stringWidth = getMcStringWidth(string.substring(0, returnValue - 1));
            
            char character = string.charAt(returnValue);

            if (character == '§') {
                doNotCountWidth = (int)(doNotCountWidth + 4.0F);
            } 

            if ((!isLine13) && (stringWidth - doNotCountWidth >= 204.0D)) { // If line 1 - 12
                if (indexOfLastSpace == -1)
                break; 
                returnValue = indexOfLastSpace;break;
            } 

            if ((isLine13) && (stringWidth - doNotCountWidth >= 172.0D)) { // If line 13
                if (indexOfLastSpace == -1)
                break; 
                returnValue = indexOfLastSpace;break;
            } 

            if (character == '\n') { // Found newline, stop loop
                break;
            } 

            if (character == ' ') { // Update last space
                indexOfLastSpace = returnValue;
            } 
        } 
        return returnValue;
    }
    
    public static String checkMCRestraints(java.util.ArrayList<String> pages, int curPI) {
        
        String page = pages.get(curPI);
        
        int numOfLines = 1;
        while (page.contains("\n")) {
            numOfLines++;
            page = page.replaceFirst("\n", "");
            if (numOfLines > 19) {
                return "More than 19 lines";
            }
        }
        
        if (pages.size() > 50) {
            return "More than 50 pages";
        }
        
//        A book may have no more than 50 pages.
//        A single page of a book may have no more than 256 characters.
//        A single page of a book may have no more than 13 lines of text.
//        The title of a book can be no more than 16 characters.
        
        return "Error";
    }
    
}

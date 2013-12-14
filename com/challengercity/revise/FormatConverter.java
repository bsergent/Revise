
package com.challengercity.revise;

/**
 *
 * @author Ben Sergent V/ha1fBit
 */
public class FormatConverter {

    public static String convertHtmlToMc(String html) {
        String mc = html.replaceFirst("<html>\n  <head>\n    \n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      ", "");
        mc = mc.replaceFirst("\n    </p>\n  </body>\n</html>\n", "");
        mc = mc.replaceAll("<b>", "§l");
        mc = mc.replaceAll("<u>", "§n");
        mc = mc.replaceAll("<i>", "§o");
        mc = mc.replaceAll("<del>", "§m");
        mc = mc.replaceAll("</b>", "§r");
        mc = mc.replaceAll("</u>", "§r");
        mc = mc.replaceAll("</i>", "§r");
        mc = mc.replaceAll("</del>", "§r");
        while (mc.contains("§r§r")) {
            mc = mc.replaceAll("§r§r", "§r");
        }
        return mc;
    }
    
    public static String convertMcToHtml(String mc) {
        mc = mc.replaceAll("\n","<br/>");
        String html = "<html>\n  <head>\n    \n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      "+mc+"\n    </p>\n  </body>\n</html>";
        html = html.replaceAll("§l", "<b>");
        html = html.replaceAll("§n", "<u>");
        html = html.replaceAll("§o", "<i>");
        html = html.replaceAll("§m", "<del>");
        html = html.replaceAll("§r", "</b></u></i></del>"); // TODO Optimize code to end formatting
        //html = html.replaceAll("§a", "<font color = '#FF0000'>");
        return html;
    }
    
}

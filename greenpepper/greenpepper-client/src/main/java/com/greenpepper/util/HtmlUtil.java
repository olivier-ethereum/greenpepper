package com.greenpepper.util;

/**
 * <p>HtmlUtil class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class HtmlUtil
{    
    /**
     * <p>cleanUpResults.</p>
     *
     * @param resultsToClean a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String cleanUpResults(String resultsToClean)
    {
        if(resultsToClean != null)
        {
            String cleanedString = resultsToClean.replace("</html><html>", "<br/><br/>");
            cleanedString = cleanedString.replace("</HTML><HTML>", "<br/><br/>");
            cleanedString = cleanedString.replace("<html>", "");
            cleanedString = cleanedString.replace("</html>", "");
            cleanedString = cleanedString.replace("<HTML>", "");
            cleanedString = cleanedString.replace("</HTML>", "");
            cleanedString = cleanedString.replace("<body>", "");
            cleanedString = cleanedString.replace("</body>", "");
            cleanedString = cleanedString.replace("<BODY>", "");
            cleanedString = cleanedString.replace("</BODY>", "");
            return cleanedString;
        }
        
        return null;
    }
}

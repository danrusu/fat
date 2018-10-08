package base.xml;

import java.util.List;

/**
 * Accepted tags within config.xml
 *
 */
enum XmlConfigTags{
    
    suite,
    test;
    
    public static boolean isXmlConfigTag(String configTag){
        
       return  List.of(values()).stream()
               .anyMatch(tag -> tag.name().equals(configTag));
    }
}

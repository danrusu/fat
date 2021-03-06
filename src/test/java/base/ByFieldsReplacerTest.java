package test.java.base;

import static main.java.base.pom.ByUtils.byToString;

import org.junit.jupiter.api.Test;

import main.java.base.Assert;
import main.java.projects.mock.pages.ByFieldsReplacerTestData;

/**
 * Unit test for testing StringFieldsReplacerInterface. 
 * 
 * @author dan.rusu
 *
 */
public class ByFieldsReplacerTest{

    ByFieldsReplacerTestData o1 = new ByFieldsReplacerTestData("@text","section");


    @Test
    public void testPublicMemberCssSelector() {	   	

        Assert.isEqual(

                "#id_section",

                byToString(o1.cssSelector));
    }


    @Test
    public void testPrivaeMemberXpathSelector() {     

        Assert.isEqual(

                "//*[@id=id_section]", 

                byToString(o1.getXpathSelector()));	        
    }



    @Test
    public void testPublicMemberIdSelector() {     
        Assert.isEqual(

                "id_section", 

                byToString(o1.idSelector));
    }



    @Test
    public void testPublicMemberClassNameSelector() {     
        Assert.isEqual(

                "class_section", 

                byToString(o1.classNameSelector));
    }



    @Test
    public void testFieldWithNoReplacement_cssSelector() {     
        Assert.isEqual(

                "#counter@tex", 

                byToString(o1.field));
    }

}


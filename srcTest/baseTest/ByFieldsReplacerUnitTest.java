package baseTest;

import static base.pom.ByUtils.byToString;

import org.junit.jupiter.api.Test;

import base.Assert;
import projects.mock.pages.ByFieldsReplacerTestData;

/**
 * Unit test for testing StringFieldsReplacerInterface. 
 * 
 * @author dan.rusu
 *
 */
public class ByFieldsReplacerUnitTest{

    ByFieldsReplacerTestData o1 = new ByFieldsReplacerTestData("@text","section");


    @Test
    public void testPublicMemberCssSelector() {	   	

        Assert.equals(

                "#id_section",

                byToString(o1.cssSelector));
    }


    @Test
    public void testPrivaeMemberXpathSelector() {     

        Assert.equals(

                "//*[@id=id_section]", 

                byToString(o1.getXpathSelector()));	        
    }



    @Test
    public void testPublicMemberIdSelector() {     
        Assert.equals(

                "id_section", 

                byToString(o1.idSelector));
    }



    @Test
    public void testPublicMemberClassNameSelector() {     
        Assert.equals(

                "class_section", 

                byToString(o1.classNameSelector));
    }



    @Test
    public void testFieldWithNoReplacement_cssSelector() {     
        Assert.equals(

                "#counter@tex", 

                byToString(o1.field));
    }

}


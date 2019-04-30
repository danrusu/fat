package main.java.base.result;

public enum ResultStatus {

    STARTED("blue"),

    PASSED("green"), 

    FAILED("red"),

    SKIPPED("blue");	  


    private String htmlColor;


    private ResultStatus(String htmlColor) {

        this.htmlColor = htmlColor;
    }


    public String getHtmlColor() {

        return htmlColor;   
    }


    public boolean isStarted() {

        return this.equals(STARTED);
    }


    public boolean isPassed() {

        return this.equals(PASSED);
    }


    public boolean isFailed() {

        return this.equals(FAILED);
    }


    public boolean isSkiped() {

        return this.equals(SKIPPED);
    }

}

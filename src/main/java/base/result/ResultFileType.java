package main.java.base.result;

/**
 * Available result file types.
 */
public enum ResultFileType{
	HTML,
	XML;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
   
}
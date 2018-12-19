package main.base.xml;

import java.util.List;



public enum DSLValue{
    
    $userDir,
    $logFolder,
    
    $HHmmss,
    $HH,
    $mm,
    $ss,
    
    $ddMMyyyy,
    $yyyy,
    $MM,
    $dd,    
    $M,
    $d,
    
    $tomorrow_dd,
    $tomorrow_MM,
    $tomorrow_yyyy,
    $tomorrow_d,
    $tomorrow_M;

    
    
    public static boolean contains(String value){
        
        return List.of(values()).stream()
                .anyMatch(dslValue -> dslValue.name().equals(value));
        
    } 
}


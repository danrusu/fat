package base.xml;


public enum XmlDynamicValue{
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
		for(XmlDynamicValue d : values()){
			if (d.name().equals(value)){ 
				return true;
			}
		}
		return false;
	} 
}
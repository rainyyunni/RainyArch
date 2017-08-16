package projectbase.utils;

public class FileTypeUtil {
	public static boolean IsWord(String contentType){
		String t=contentType.toLowerCase();
		return t.contains("msword")||t.contains("/doc");
	}
	public static boolean IsExcel(String contentType){
		String t=contentType.toLowerCase();
		return t.contains("msword")||t.contains("/xls");
	}
	public static boolean IsPdf(String contentType){
		String t=contentType.toLowerCase();
		return t.contains("pdf");
	}
	public static boolean IsImage(String contentType){
		String t=contentType.toLowerCase();
		return t.contains("image");
	}
}

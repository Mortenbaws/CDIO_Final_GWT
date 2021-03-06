package cdio.shared;
import com.google.gwt.i18n.client.DateTimeFormat;

public class FieldVerifier {

	public static boolean isValidMaengde(String value) {
		try {
			Double.parseDouble(value);
			if (Double.parseDouble(value) > 0)
				return true;
		} catch (NumberFormatException e) {
			return false;
		}
		return false;
	}

	public static boolean isValidStatus(String value) {
		int status = Integer.parseInt(value);

		if (status <= 2 && status >= 0) {
			return true;
		}

		return false;
	}

	public static boolean isValidName(String name) {
		if (!notSpecialChar(name)) {
			return false;
		}
		if (name.length() == 0) {
			return false;
		}
		if (name.length() <= 20) {
			return true;
		}

		return false;
	}

	public static boolean isValidRolleName(String name) {
		if (!notSpecialChar(name)) {
			return false;
		}
		if (name.length() == 0) {
			return false;
		}
		if (name.length() <= 20 && (name.equals("admin") || name.equals("farmaceut") || name.equals("vaerkfoerer")
				|| name.equals("operatoer"))) {
			return true;
		}

		return false;
	}

	public static boolean isValidRbId(String id) {
		if(id.startsWith("-")){
			return false;
		}
		try {
			Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		if (Integer.parseInt(id) > 0 && Integer.parseInt(id) <= 99999999) {
			return true;
		}
		return false;
	}

	public static boolean isValidIni(String Ini) {
		if (Ini.length() > 1 && Ini.length() < 5 && Ini.matches("[a-zA-Z]+")) {
			return true;
		} else
			return false;
	}

	public static boolean isValidRaavareID(String raavareID) {
		try {
			Integer.parseInt(raavareID);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean notSpecialChar(String name) {
		if (name == null)
			return false;
		for (int i = 0; i < name.length(); i++) {
			char p = name.charAt(i);
			if (p == '#' || p == '<' || p == '>' || p == '"' || p == '&')
				return false;
		}
		return true;
	}

	public static boolean isValidCpr(String cpr) {
		String sub1 = cpr.substring(0,6);
		String sub2 = cpr.substring(6,10);
		try {
			if ( cpr.length() == 10 && isValidRbId(sub2) ){
		
			DateTimeFormat myDateTimeFormat = DateTimeFormat.getFormat("ddMMyy");
			myDateTimeFormat.parseStrict(sub1);
			
				return true;
			}
		} catch (IllegalArgumentException e) {
			return false;
		}
		return false;
		}
		
		

	public static boolean isVaildPassword(String password) {
		if (password.length() > 4 && password.length() < 9) {
			return true;
		} else
			return false;
	}
}

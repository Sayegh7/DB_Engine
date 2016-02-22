import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Reflector {

	public static Reflector r;

	public Reflector() {
		r = this;
	}

	public Object Convert(String strColType, String strColValue) {
		Object x = null;
		if (strColType.equals("Integer")) {
			x = new Integer(strColValue);
		}
		if (strColType.equals("java.lang.String")) {
			x = (strColValue).toString();
		}
		if (strColType.equals("java.lang.Double")) {
			x = Double.valueOf(strColValue);
		}
		if (strColType.equals("java.util.Date")) {
			SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");
			try {
				x = dateformat3.parse(strColValue);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return x;
	}
	public static void main(String[] args) throws IOException {
		Date d = new Date();
		Page p = new Page("Hania", 5);
		for (int i = 0; i < p.page.length; i++) {
			System.out.println(Arrays.toString(p.page[i]));
		}
	}
}

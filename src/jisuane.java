import java.math.BigDecimal;

public class jisuane {

	public static void main(String[] args) {
		jisuan(10);
	}

	public static void jisuan(int n) {
		double e = 1;
		double j = 1;
		for (int i = 0; i < n; i++) {
			j *= (i + 1);
			e += 1 / j;
		}
		BigDecimal sum = new BigDecimal(e);
		BigDecimal e1 = sum.setScale(10, BigDecimal.ROUND_UP);
		System.out.println(e1.toString());
	}
}

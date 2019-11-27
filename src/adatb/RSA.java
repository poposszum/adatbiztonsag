package adatb;

import java.util.ArrayList;
import java.util.List;

public class RSA {
    private static int p = 13;
    private static int q = 17;
    private static int n = p * q;
    private static int fi = (p - 1) * (q - 1);

    public static int euclideanAlgorithm(int a, int b, int d) {
        d = a;
        if (b != 0) {
            return euclideanAlgorithm(b, a % b, d);
        }
        return d;
    }

    public static List<Integer> extendedEuclideanAlgorithm(int a, int b) {
        List<Integer> outcomes = new ArrayList<>();
        int x0 = 1;
        int x1 = 0;
        int y0 = 0;
        int y1 = 1;
        int s = 1;
        int r;
        int q;
        int x;
        int y;
        int d;

        while (b != 0){
            r = a % b;
            q = a / b;

            a = b;
            b = r;
            x = x1;
            y = y1;

            x1 = q * x1 + x0;
            y1 = q * y1 + y0;
            x0 = x;
            y0 = y;

            s = (-1)*s;
        }

        x = s * x0;
        y = (-1) * s * y0;

        d = a;

        outcomes.add(d);
        outcomes.add(x);
        outcomes.add(y);

        return outcomes;
    }

    public static int encrypt(int m, int e){
        int a = quickPower(m, e, n);
        return a % n;
    }

    public static int decrypt(int c,int d){
        int a = kmt(c, d);
        return a % n;
    }

    public static int quickPower(int base, int exp, int mod){
        base = base % mod;
        if (exp == 0){
            return 1;
        }
        if (exp == 1){
            return base % mod;
        } else if( exp %2 == 0){
            return quickPower((base * base) % mod, exp / 2, mod);
        }
        else {
            return base * (quickPower(base, exp - 1, mod)) % mod;
        }
    }

    public static int kmt(int c, int d){
        int c1 = quickPower(c % p,d % (p - 1), p);
        int c2 = quickPower(c % q,d % (q - 1), q);

        List<Integer> result = extendedEuclideanAlgorithm(q, p);
        int x1 = c2 * result.get(2) * p;
        int x2 = c1 * result.get(1) * q;

        return (x1 + x2) % n;
    }

    public static boolean millerTest(int number) {
        int a = number - 1;
        int s = 0;
        int d;

        while(true) {
            if (a % 2 == 0 && a > 2){
                s++;
                a /= 2;
            } else {
                break;
            }
        }

        d = a;

        if(s == 0)
            return false;

        boolean prime = false;

        int alap;

        for(int i=0; i < 8 ; i++){
            while (true) {
                alap = (int)(Math.random() * (number - 2)) + 2;
                if(euclideanAlgorithm(number, alap, number) == 1)
                    break;
            }

            prime = false;

            int exp = d;
            int hv = quickPower(alap, exp, number);
            if (hv == 1 || hv == number - 1) {
                prime = true;
                continue;
            }
            else if (hv == number) {
                break;
            }


            for(int j=1; j<s; j++){
                exp *= 2;
                hv = quickPower(alap, exp, number);
                if(hv == 1){
                    return false;
                }
                else if(hv == number - 1){
                    prime = true;
                    break;
                }
            }
            if (!prime)
                return false;
        }
        return prime;
    }

    public static int randomNumber(){
        while(true){
            int number = (int)(Math.random() * (fi - 2)) + 2;
            if(euclideanAlgorithm(fi, number, n) == 1){
                return number;
            }
        }
    }

    public void solve() {
        StringBuilder stringBuilder = new StringBuilder();

        if(!millerTest(p) || !millerTest(q) || p > q) {
            if (!millerTest(p))
                stringBuilder.append("p is not a prime");
            if(!millerTest(q))
                stringBuilder.append("q is not a prime");
            if(p > q)
                stringBuilder.append("p must be smaller than q");
        } else {

            stringBuilder.append("p = " + p +", q = " + q + "\n");


            int num = randomNumber();
            stringBuilder.append("e = " + num + "\n");

            int m = 2100;
            m = m % n;
           stringBuilder.append("m = " + m + "\n");

            int c = encrypt(m, num);
            stringBuilder.append("c = " + c + "\n");

            List list = extendedEuclideanAlgorithm(fi, num);
            int d = (int) list.get(2);
            d = d % fi;
            while (d < 0) {
                d += fi;
            }
            stringBuilder.append("d = " + d + "\n");

            int v = decrypt(c, d);
            stringBuilder.append("result = " + v + "\n");
        }
        System.out.println(stringBuilder);
    }
}

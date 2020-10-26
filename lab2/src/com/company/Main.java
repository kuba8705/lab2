package com.company;

/* Watek M_prostokatow w ktorym calka obliczana jest metoda prostokatow */

class M_prostokatow extends Thread {
    private double ai, bi, n;

    public M_prostokatow(double _ai, double _bi, double _n) {
        ai = _ai;
        bi = _bi;
        n = _n;
    }

    public void run() {
        double wartosc;
        double h = (bi - ai) / n;

        for (double i = 1; i <= n; i++) {
            wartosc = h * Main.funkcja(ai + i * h);

            Main.dodaj(wartosc);
        }
    }
}

/* Watek M_trapezow w ktorym calka obliczana jest metoda prostokatow */

class M_trapezow extends Thread {
    private double ai, bi, n;

    public M_trapezow(double _ai, double _bi, double _n) {
        ai = _ai;
        bi = _bi;
        n = _n;
    }

    public void run() {

        Main.dodaj(((bi - ai) / n) * (Main.funkcja(ai) / 2 + Main.funkcja(bi) / 2));

        for (int i = 1; i < n; i++) {
            Main.dodaj(Main.funkcja(ai + (i / n) * (bi - ai))*((bi - ai) / n));
        }

    }

}

/* Watek M_Simpsona w ktorym calka obliczana jest metoda simpsona */

class M_Simpsona extends Thread {
    private double ai, bi, n;

    public M_Simpsona(double _ai, double _bi, double _n) {
        ai = _ai;
        bi = _bi;
        n = _n;
    }

    public void run() {

        double o = (bi - ai) / n;
        double h = ((ai + o - ai) / 2.0);

        Main.dodaj(Main.funkcja(ai) * (h / 3.0));
        Main.dodaj(Main.funkcja(bi) * (h / 3.0));

        for (int i = 1; i < n; i++) {
            Main.dodaj(2 * Main.funkcja(ai + i * o) * (h / 3.0));
        }

        for (double i = 0; i < n; i++) {
            Main.dodaj(4 * Main.funkcja((ai + i * o + ai + (i + 1) * o) / 2) * (h / 3.0));
        }

    }
}

public class Main {

    // zmienna suma która jest suma wynikow cząstkowych z wątków

    private static double suma = 0;

    /* Jezeli ktorys z watkow obliczy wynik czastkowy to wywoluje funkcje
     * dodaj ktora dodaje jego wynik do zmiennej suma */

    static public synchronized void dodaj(double liczba) {
        suma += liczba;
    }

    // funkcja ktora zostanie przecalkowana

    static double funkcja(double x) {
        return (-4*x*x);
    }


    public static void main(String[] args) {

        // przedzial <a,b>

        double a = 0, b = 3;

        // n-watkow

        double n = 500;

        /* Odcinek <a,b> dzielimy na 3 rowne czesci. */

        double odcinek = (b - a) / 3.0;

        /* Wywolanie watkow i przekazanie im ich przedzialu calkowania */

        M_prostokatow q1 = new M_prostokatow(a, a + odcinek, n);
        M_trapezow q2 = new M_trapezow(a + odcinek + 0.00001, a + 2 * odcinek, n);
        M_Simpsona q3 = new M_Simpsona(a + 2 * odcinek + 0.00001, b, n);

        // uruchamianie watkow

        q1.start();
        q2.start();
        q3.start();


        try {

            // Czekanie na zakonczenie pracy wszystkich watkow

            q1.join();
            q2.join();
            q3.join();

            // Wypisywanie wyniku

            System.out.println("Wartosc calki funkcji: " + suma);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

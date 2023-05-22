/*
 * Algorithms and Programming II                          February 01, 2022
 * IST 2048
 * Prof. M. Diaz-Maldonado
 *
 * Synopsis:
 * Code shows how to perform some arithmetic operations in Java
 * and to display numeric answers in decimal notation up to six decimal
 * places via output formatting.
 *
 *
 * Sinopsis:
 * El codigo muestra como realizar algunas operaciones aritmeticas en Java
 * y tambien muestra como imprimir respuestas numericas en notacion decimal
 * hasta seis lugares decimales.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 */


class Arithmetics
{
  public static void main (String[] args)
  {
    System.out.println("Arithmetic Operations Examples:");


    // example 1.1-A
    double x = Math.pow(5.0 - 19.0 / 7.0 + Math.pow(2.5, 3.0), 2.0);

    System.out.printf("example 1.1-A: %.6f\n", x);


    // example 1.1-B
    x = 7.0 * 3.1 + Math.sqrt(120.0) / 5.0 - Math.pow( 15.0, (5.0 / 3.0) );
    System.out.printf("example 1.1-B: %.6f\n", x);


    // example 1.2-A
    x = Math.cbrt(8.0 + 80.0 / 2.6) + Math.exp(3.5);
    System.out.printf("example 1.2-A: %.6f\n", x);


    // example 1.3-A
    x =( 23.0 + Math.cbrt(45.0) ) / (16.0 * 0.7) + Math.log10(589006.0);
    System.out.printf("example 1.3-A: %.6f\n", x);


    // example 1.4-D
    x = 1.0 / Math.sqrt(5.0e-2) +
        2.0 * Math.log10( 4.5e-3 / 3.7 + 2.51 / ( 1.8e6 * Math.sqrt(2.5e-1) ) );
    System.out.printf("example 1.4-D: %.6f\n", x);


    // example 1.5-A
    x = Math.sin(0.2 * Math.PI) / Math.cos(Math.PI / 6.0) +
        Math.tan(72.0 * (Math.PI / 180.0) );
    System.out.printf("example 1.5-A: %.6f\n", x);

  }
}

package com.igormeshalkin.util;

public class SalaryStringFormat {

    public static String formatSalary(Double salary) {
        if (salary == 0.0) {
            return "0.00 ₽";
        } else {
            String reverseSalary = new StringBuilder(String.format("%.2f", salary)).reverse().toString();
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("₽" + " ").append(reverseSalary.substring(0, 3));
            reverseSalary = reverseSalary.substring(3);

            int endIndex = Math.min(reverseSalary.length(), 3);
            stringBuilder.append(reverseSalary.substring(0, endIndex));
            reverseSalary = reverseSalary.substring(endIndex);

            while (reverseSalary.length() > 3) {
                stringBuilder.append(" ").append(reverseSalary.substring(0, 3));
                reverseSalary = reverseSalary.substring(3);
            }

            stringBuilder.append(" ").append(reverseSalary);

            return stringBuilder.reverse().toString().trim();
        }
    }

    public static String reformatSalary(String string) {
        return string.substring(0, string.length() - 1).replace(" ", "");
    }
}

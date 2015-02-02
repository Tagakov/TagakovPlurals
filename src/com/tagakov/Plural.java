package com.tagakov;

/**
 * Created by Тагаков on 01.02.2015.
 */

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс собирающий из заданного числа запись этого числа прописью
 */
public class Plural implements CharSequence {
    private enum Scale {
        ONE,
        THOUSAND("тысяча", "тысячи", "тысяч"),
        MILLION("миллион", "миллиона", "миллионов"),
        BILLION("миллиард", "миллиарда", "миллиардов"),
        TRILLION("триллион", "триллиона", "триллионов"),
        QUADRILLION("квадриллион", "квадриллиона", "квадриллионов"),
        QUINTILLION("квинтиллион", "квинтиллиона", "квинтиллионов"),
        SEXTILLION("секстиллион", "секстиллиона", "секстиллионов"),
        SEPTILLION("септиллион", "септиллиона", "септиллионов");

        private String one, few, many;
        private Scale() {
            one = "";
            few = "";
            many = "";
        }
        private Scale(String one, String few, String many) {
            this.few = few;
            this.many = many;
            this.one = one;
        }

        public CharSequence getStringForOne() {
            return one;
        }

        public CharSequence getStringForFew() {
            return few;
        }

        public CharSequence getStringForMany() {
            return many;
        }

        public CharSequence getStringForNumber(Integer number) {
            int numeral = number % 100;
            if (numeral > 19)
                numeral %= 10;
            if (numeral % 10 == 0)
                return getStringForMany();
            if (numeral == 1) {
                return getStringForOne();
            } else if (numeral < 5) {
                return getStringForFew();
            } else {
                return getStringForMany();
            }
        }

        public static Scale getValueForExponent(int exponent) {
            if (exponent < 0)
                return ONE;
            return values()[exponent];
        }
    }

    private static final int FEMALE_NUMERAL_OFFSET = 1000;
    public static final String NEGATIVE_PREFIX = "минус";
    private final StringBuilder numberToTextBuilder = new StringBuilder();
    private String numberAsText;
    private final String initialNumberString;
    private static final Map<Integer, CharSequence> NUMBERS;
    static {
        NUMBERS = new HashMap<Integer, CharSequence>();
        NUMBERS.put(1, "один");
        NUMBERS.put(FEMALE_NUMERAL_OFFSET + 1, "одна");
        NUMBERS.put(2, "два");
        NUMBERS.put(FEMALE_NUMERAL_OFFSET + 2, "две");
        NUMBERS.put(3, "три");
        NUMBERS.put(4, "четыре");
        NUMBERS.put(5, "пять");
        NUMBERS.put(6, "шесть");
        NUMBERS.put(7, "семь");
        NUMBERS.put(8, "восемь");
        NUMBERS.put(9, "девять");
        NUMBERS.put(10, "десять");
        NUMBERS.put(11, "одиннадцать");
        NUMBERS.put(12, "двенадцать");
        NUMBERS.put(13, "тринадцать");
        NUMBERS.put(14, "четырнадцать");
        NUMBERS.put(15, "пятнадцать");
        NUMBERS.put(16, "шестнадцать");
        NUMBERS.put(17, "семнадцать");
        NUMBERS.put(18, "восемнадцать");
        NUMBERS.put(19, "девятнадцать");
        NUMBERS.put(20, "двадцать");
        NUMBERS.put(30, "тридцать");
        NUMBERS.put(40, "сорок");
        NUMBERS.put(50, "пятьдесят");
        NUMBERS.put(60, "шестьдесят");
        NUMBERS.put(70, "семьдесят");
        NUMBERS.put(80, "восемьдесят");
        NUMBERS.put(90, "девяносто");
        NUMBERS.put(100, "сто");
        NUMBERS.put(200, "двести");
        NUMBERS.put(300, "триста");
        NUMBERS.put(400, "четыреста");
        NUMBERS.put(500, "пятьсот");
        NUMBERS.put(600, "шестьсот");
        NUMBERS.put(700, "семьсот");
        NUMBERS.put(800, "восемьсот");
        NUMBERS.put(900, "девятьсот");
    }


    /**
     *
     * @param numberCharSequence число порядком не более септилиона
     */
    public Plural(CharSequence numberCharSequence) {
        //Временный билдер используемый для составления числительного
        StringBuilder sb = new StringBuilder();
        this.initialNumberString = numberCharSequence.toString();

        int startPosition;
        //Смещаемся если число отрицательное
        if (numberCharSequence.charAt(0) == '-') {
            sb.append(NEGATIVE_PREFIX);
            sb.append(' ');
            startPosition = 1;
        } else {
            startPosition = 0;
        }

        //Дополняем количество символов строки до кратного трем
        //Убираем знак минуса, если есть
        String preparedString;
        int numberLength = initialNumberString.length() - startPosition;
        int digits = numberLength % 3;
        if (digits == 1) {
            preparedString = "00" + initialNumberString.substring(startPosition);
        } else if (digits == 2) {
            preparedString = "0" + initialNumberString.substring(startPosition);
        } else {
            preparedString = initialNumberString.substring(startPosition);
        }

        //Преобразуем тройку цифр в числительное, добавляя имя степени тсячи
        numberLength = preparedString.length();
        for (int i = 0; i < numberLength; i += 3) {
            int exponent = (numberLength - i) / 3 - 1;
            Scale currentScale = Scale.getValueForExponent(exponent);
            sb.append(numberToText(preparedString.subSequence(i, i + 3), currentScale));
            sb.append(' ');
        }

        numberAsText = sb.toString().trim();
        //Если билдер пуст или содержит только префикс отрицательного числа
        //это значит что число состояло из одних нолей и знака минуса
        if (numberAsText.isEmpty() || numberAsText.equals(NEGATIVE_PREFIX)) {
            numberAsText = "ноль";
        }
    }

    /**
     * Преобразует число c тремя разрядами или меньше и его позицию в составном числе в числительное
     * @param number Число для преобразования
     * @param scale Степерь тысячи для этого числа, определяющая префикс
     * @return
     */
    private CharSequence numberToText(CharSequence number, Scale scale) {
        // Очищаем вспомогательный билдер
        numberToTextBuilder.delete(0, numberToTextBuilder.length());
        //Парсим число и выделяем его составные части parsedNumber == topDigit + middleDigit + lowDigit
        Integer parsedNumber = Integer.parseInt(number.toString());
        Integer topDigit = parsedNumber / 100 * 100;
        Integer middleDigit = (parsedNumber % 100) / 10 * 10;
        Integer lowDigit = (parsedNumber % 10) ;
        //добавляем сотни
        if (NUMBERS.containsKey(topDigit)) {
            numberToTextBuilder.append(NUMBERS.get(topDigit));
            numberToTextBuilder.append(' ');
        }
        // Если порядок числа это ТЫСЯЧИ и оно оканчивается на 1 или 2 (101, но не 111)
        // То мы должны использовать женский род для написания этих цифр.
        // В русском языке исключение справедливо только для тысячи.
        if (scale == Scale.THOUSAND && lowDigit < 3 && middleDigit != 10) {
            lowDigit += FEMALE_NUMERAL_OFFSET;
        }

        if (NUMBERS.containsKey(middleDigit + lowDigit)) {
            numberToTextBuilder.append(NUMBERS.get(middleDigit + lowDigit));
            numberToTextBuilder.append(' ');
        } else {//если сумма средней и меньшей составных частей не записывается одним словом (11, 12 и т.д.)
            if (NUMBERS.containsKey(middleDigit)) {
                numberToTextBuilder.append(NUMBERS.get(middleDigit));
                numberToTextBuilder.append(' ');
            }
            if (NUMBERS.containsKey(lowDigit)) {
                numberToTextBuilder.append(NUMBERS.get(lowDigit));
                numberToTextBuilder.append(' ');
            }
        }
        if (numberToTextBuilder.length() > 0) { //если переданное число не было нулем, то записываем его порядок тысячи
            numberToTextBuilder.append(scale.getStringForNumber(parsedNumber));
        }
        return numberToTextBuilder;
    }

    public String getNumberAsText() {
        return numberAsText;
    }

    public String getInitialNumberString() {
        return initialNumberString;
    }

    @Override
    public int length() {
        return numberAsText.length();
    }

    @Override
    public char charAt(int index) {
        return numberAsText.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return numberAsText.subSequence(start, end);
    }

    @Override
    public String toString() {
        return numberAsText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plural plural = (Plural) o;

        if (initialNumberString != null ? !initialNumberString.equals(plural.initialNumberString) : plural.initialNumberString != null)
            return false;
        if (numberAsText != null ? !numberAsText.equals(plural.numberAsText) : plural.numberAsText != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = numberAsText != null ? numberAsText.hashCode() : 0;
        result = 31 * result + (initialNumberString != null ? initialNumberString.hashCode() : 0);
        return result;
    }
}
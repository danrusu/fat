package main.utils;

import java.util.stream.IntStream;
import java.util.stream.Stream;



public interface Mod10 {



	public static int getDigitsSum(int number){

		return Stream.of(Integer.toString(number).split(""))
				.mapToInt(x -> Integer.parseInt(x))
				.sum();
	}



	private static int doubleEvenIndexInts(int index, int digit) {
		return (index % 2 == 0) ? 
				getDigitsSum(digit * 2) 
				: digit;
	}



	public static int getMod10CheckSum(String digitsString){

		String[] digits = digitsString.split("");

		return IntStream.range(0, digits.length)
				.map( i -> doubleEvenIndexInts(
						i + digits.length - 1, // reversed index (RTL)
						Integer.parseInt(digits[i])))
				.sum();
	}




	public static int getMod10CheckDigit(String digitsString) {
		return getMod10CheckSum(digitsString) * 9 % 10;
	}



/*	public static void main(String[] args) {
		// System.out.println(getMod10CheckDigit("")); // this does not work
		System.out.println(getMod10CheckDigit("000000000150010000050155"));
		System.out.println(getMod10CheckDigit("1111"));
		System.out.println(getMod10CheckDigit("00112233445566778899"));
		System.out.println(getMod10CheckDigit("000509170000015001"));
	}*/

}

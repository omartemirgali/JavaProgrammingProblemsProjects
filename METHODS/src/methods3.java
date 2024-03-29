import java.util.Scanner;

public class methods3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter an integer: ");
        int number = in.nextInt();
        if (isPalindrome(number)) {
            System.out.print(number + " is a palindrome");
        } else {
            System.out.print(number + " is not a palindrome");
        }
    }
    public static int reverse(int number) {
        int reverse = 0;
        int digit;
        do {
            digit = number % 10;
            reverse = reverse * 10 + digit;
            number /= 10;
        } while (number != 0);
        return reverse;
    }
    public static boolean isPalindrome(int number) {
        return (number == reverse(number));
    }
}
